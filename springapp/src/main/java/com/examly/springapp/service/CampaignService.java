package com.examly.springapp.service;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    public Campaign createCampaign(Campaign campaign) {
        validateCampaign(campaign);
        campaign.setStatus(CampaignStatus.ACTIVE);
        campaign.setCurrentAmount(BigDecimal.ZERO);
        return campaignRepository.save(campaign);
    }

    public Campaign getCampaignById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found with id: " + id));
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        Campaign existing = getCampaignById(id);
        validateCampaign(updatedCampaign);

        existing.setTitle(updatedCampaign.getTitle());
        existing.setDescription(updatedCampaign.getDescription());
        existing.setGoalAmount(updatedCampaign.getGoalAmount());
        existing.setDeadline(updatedCampaign.getDeadline());
        existing.setCategory(updatedCampaign.getCategory());
        existing.setCreatorName(updatedCampaign.getCreatorName());

        return campaignRepository.save(existing);
    }

    public void deleteCampaign(Long id) {
        Campaign campaign = getCampaignById(id);
        campaignRepository.delete(campaign);
    }

    public void updateCampaignStatusIfNeeded(Campaign campaign) {
        boolean deadlinePassed = campaign.getDeadline().isBefore(LocalDate.now());
        boolean goalReached = campaign.getCurrentAmount().compareTo(campaign.getGoalAmount()) >= 0;

        if (goalReached) {
            campaign.setStatus(CampaignStatus.COMPLETED);
        } else if (deadlinePassed && !goalReached) {
            campaign.setStatus(CampaignStatus.EXPIRED);
        }

        campaignRepository.save(campaign);
    }

    private void validateCampaign(Campaign campaign) {
        if (campaign.getTitle() == null || campaign.getTitle().length() < 3) {
            throw new ValidationException("Title must be at least 3 characters long");
        }

        if (campaign.getDescription() == null || campaign.getDescription().length() < 10) {
            throw new ValidationException("Description must be at least 10 characters long");
        }

        if (campaign.getGoalAmount() == null || campaign.getGoalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Goal amount must be greater than zero");
        }

        if (campaign.getDeadline() == null || campaign.getDeadline().isBefore(LocalDate.now())) {
            throw new ValidationException("Deadline must be a future date");
        }

        if (campaign.getCategory() == null || campaign.getCategory().isBlank()) {
            throw new ValidationException("Category must not be empty");
        }

        if (campaign.getCreatorName() == null || campaign.getCreatorName().isBlank()) {
            throw new ValidationException("Creator name must not be empty");
        }
    }
}
