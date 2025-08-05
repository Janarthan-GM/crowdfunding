package com.examly.springapp.service;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.repository.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign createCampaign(Campaign campaign) {
        campaign.setStatus(CampaignStatus.ACTIVE);
        return campaignRepository.save(campaign);
    }

    public List<Campaign> getAllCampaigns(String category) {
        if (category == null || category.isEmpty()) {
            return campaignRepository.findAll();
        } else {
            return campaignRepository.findByCategory(category);
        }
    }

    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepository.findById(id);
    }
}
