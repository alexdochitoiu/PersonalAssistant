package com.infouaic.alexdochitoiu.personalassistant;

/**
 * Created by Alex Dochitoiu on 5/13/2018.
 */

class Command {
    private String description;
    private int image;
    private boolean active;
    private String details;

    Command(String description, int image, boolean active, String details) {
        this.description = description;
        this.image = image;
        this.active = active;
        this.details = details;
    }

    String getDescription() {
        return description;
    }

    int getImage() {
        return image;
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean act) {
        this.active = act;
    }

    String getDetails() { return details; }
}
