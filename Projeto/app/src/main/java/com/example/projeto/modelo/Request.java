package com.example.projeto.modelo;

public class Request {

    private int id;
    private int customerId;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private int currentTechnicianId;
    private String canceledAt;
    private int canceledBy;
    private String createdAt;
    private String updatedAt;

    public Request(int id, int customerId, String title, String description, Priority priority,
                   Status status, int currentTechnicianId, String canceledAt, int canceledBy,
                   String createdAt, String updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.currentTechnicianId = currentTechnicianId;
        this.canceledAt = canceledAt;
        this.canceledBy = canceledBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public int getCurrentTechnicianId() { return currentTechnicianId; }
    public void setCurrentTechnicianId(int currentTechnicianId) { this.currentTechnicianId = currentTechnicianId; }

    public String getCanceledAt() { return canceledAt; }

    public int getCanceledBy() { return canceledBy; }

    public String getCreatedAt() { return createdAt; }

    public String getUpdatedAt() { return updatedAt; }
}
