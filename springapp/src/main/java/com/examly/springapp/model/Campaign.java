package com.examly.springapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    @Column(length = 1000)
    private String description;

    @NotNull(message = "Goal amount is required")
    @DecimalMin(value = "50.0", message = "Goal amount must be at least 50")
    private BigDecimal goalAmount;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Creator name is required")
    private String creatorName;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be a future date")
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getGoalAmount() {
        return goalAmount;
    }

    public void setGoalAmount(BigDecimal goalAmount) {
        this.goalAmount = goalAmount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }
}
