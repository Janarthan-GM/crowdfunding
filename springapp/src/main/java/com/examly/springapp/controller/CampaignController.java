package com.examly.springapp.controller;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.repository.CampaignRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignRepository campaignRepository;

    public CampaignController(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    // Create a new campaign
    @PostMapping
    public ResponseEntity<?> createCampaign(@Valid @RequestBody Campaign campaign) {
        campaign.setStatus(CampaignStatus.ACTIVE);
        Campaign saved = campaignRepository.save(campaign);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Get all campaigns, with optional category filter
    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns(@RequestParam(required = false) String category) {
        List<Campaign> campaigns = (category == null || category.isEmpty()) ?
                campaignRepository.findAll() :
                campaignRepository.findByCategory(category);
        return ResponseEntity.ok(campaigns);
    }

    // Get campaign by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getCampaignById(@PathVariable Long id) {
        Optional<Campaign> campaign = campaignRepository.findById(id);
        if (campaign.isPresent()) {
            return ResponseEntity.ok(campaign.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaign not found");
        }
    }
}
