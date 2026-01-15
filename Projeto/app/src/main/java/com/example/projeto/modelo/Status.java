package com.example.projeto.modelo;

public enum Status {
    NEW,
    PENDING,
    ACCEPTED,
    REJECTED,
    COMPLETED;

    @Override
    public String toString() {
        switch (this) {
            case NEW: return "New";
            case PENDING: return "Pending";
            case ACCEPTED: return "Accepted";
            case REJECTED: return "Rejected";
            case COMPLETED: return "Completed";
            default: return super.toString();
        }
    }
}
