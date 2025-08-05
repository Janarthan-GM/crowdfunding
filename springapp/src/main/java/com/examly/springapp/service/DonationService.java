package com.examly.springapp.service;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.model.Donation;
import com.examly.springapp.repository.CampaignRepository;
import com.examly.springapp.repository.DonationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;
    private final CampaignService campaignService;

    @Autowired
    public DonationService(DonationRepository donationRepository,
                           CampaignRepository campaignRepository,
                           CampaignService campaignService) {
        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
        this.campaignService = campaignService;
    }

    public Donation makeDonation(Long campaignId, Donation donation) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));

        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            throw new ValidationException("Cannot donate. Campaign is not ACTIVE.");
        }

        // Update the campaign’s current amount
        BigDecimal updatedAmount = campaign.getCurrentAmount().add(donation.getAmount());
        campaign.setCurrentAmount(updatedAmount);

        // Save donation
        donation.setCampaign(campaign);
        Donation savedDonation = donationRepository.save(donation);

        // Update status if needed
        campaignService.updateCampaignStatusBasedOnFunding(campaign);
        return savedDonation;
    }

    public List<Donation> getDonationsForCampaign(Long campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("Campaign with ID " + campaignId + " not found");
        }
        return donationRepository.findByCampaignId(campaignId);
    }
}
