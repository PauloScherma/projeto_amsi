package com.example.projeto.modelo;

public enum Status {
    NEW,
    IN_PROGRESS,
    CANCELED,
    COMPLETED;

    @Override
    public String toString() {
        switch (this) {
            case NEW: return "New";
            case IN_PROGRESS: return "In Progress";
            case CANCELED: return "Canceled";
            case COMPLETED: return "Completed";
            default: return super.toString();
        }
    }
}
