package com.w4kened.RefHelper.models;

public enum AidInteraction {
    CREATING("Creating"),
    REQUESTING("Requesting"),
    MODIFYING("Modifying"),
    ACCEPTED("Accepted");



    private final String label;

    AidInteraction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
