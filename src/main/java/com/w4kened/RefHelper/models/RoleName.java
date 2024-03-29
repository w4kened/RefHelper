package com.w4kened.RefHelper.models;

public enum RoleName {
    ROLE_VOLUNTEER("ROLE_VOLUNTEER"),
    ROLE_REFUGEE("ROLE_REFUGEE");

    private final String label;
    RoleName(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
