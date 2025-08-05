package com.examly.springapp.service;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.model.Donation;
import com.examly.springapp.repository.CampaignRepository;
import com.examly.springapp.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService {

    private final DonationRepository donationRepository;
    private final CampaignRepository campaignRepository;

    public DonationService(DonationRepository donationRepository, CampaignRepository campaignRepository) {
        this.donationRepository = donationRepository;
        this.campaignRepository = campaignRepository;
    }

    public Optional<Donation> donateToCampaign(Long campaignId, Donation donation) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);

        if (optionalCampaign.isEmpty()) {
            return Optional.empty();
        }

        Campaign campaign = optionalCampaign.get();

        // Only allow donation if campaign is ACTIVE and not expired
        if (campaign.getStatus() != CampaignStatus.ACTIVE || campaign.getDeadline().isBefore(LocalDate.now())) {
            return Optional.empty();
        }

        // Set campaign to donation
        donation.setCampaign(campaign);
        Donation savedDonation = donationRepository.save(donation);

        // Auto-update campaign status if goal reached
        BigDecimal total = donationRepository.findByCampaignId(campaignId)
                .stream()
                .map(Donation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(campaign.getGoalAmount()) >= 0) {
            campaign.setStatus(CampaignStatus.COMPLETED);
            campaignRepository.save(campaign);
        }

        return Optional.of(savedDonation);
    }

    public List<Donation> getDonationsByCampaign(Long campaignId) {
        return donationRepository.findByCampaignId(campaignId);
    }
}
!