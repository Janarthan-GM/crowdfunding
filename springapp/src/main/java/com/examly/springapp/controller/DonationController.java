package com.examly.springapp.controller;

import com.examly.springapp.model.Campaign;
import com.examly.springapp.model.CampaignStatus;
import com.examly.springapp.model.Donation;
import com.examly.springapp.repository.CampaignRepository;
import com.examly.springapp.repository.DonationRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/campaigns/{campaignId}/donations")
public class DonationController {

    private final CampaignRepository campaignRepository;
    private final DonationRepository donationRepository;

    public DonationController(CampaignRepository campaignRepository, DonationRepository donationRepository) {
        this.campaignRepository = campaignRepository;
        this.donationRepository = donationRepository;
    }

    // POST /api/campaigns/{id}/donations
    @PostMapping
    public ResponseEntity<?> donateToCampaign(
            @PathVariable Long campaignId,
            @Valid @RequestBody Donation donation) {

        Optional<Campaign> optionalCampaign = campaignRepository.findById(campaignId);
        if (optionalCampaign.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Campaign not found"));
        }

        Campaign campaign = optionalCampaign.get();

        // Check if campaign is ACTIVE and not expired
        if (campaign.getStatus() != CampaignStatus.ACTIVE || campaign.getDeadline().isBefore(LocalDate.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Campaign is not ACTIVE or has expired"));
        }

        // Save the donation
        donation.setCampaign(campaign);
        Donation saved = donationRepository.save(donation);

        // Create response without campaign info
        Donation response = new Donation();
        response.setId(saved.getId());
        response.setAmount(saved.getAmount());
        response.setDonorName(saved.getDonorName());
        response.setMessage(saved.getMessage());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/campaigns/{id}/donations
    @GetMapping
    public ResponseEntity<List<Donation>> getDonationsByCampaign(@PathVariable Long campaignId) {
        List<Donation> donations = donationRepository.findByCampaignId(campaignId);
        return ResponseEntity.ok(donations);
    }
}
