package com.gspb.collateraltracker.model;

import java.time.LocalDateTime;

public class AuditLogEntry {
    private String oldValue;
    private String newValue;
    private LocalDateTime timeChanged;

    public AuditLogEntry() {
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public LocalDateTime getTimeChanged() {
        return timeChanged;
    }

    public void setTimeChanged(LocalDateTime timeChanged) {
        this.timeChanged = timeChanged;
    }
}
