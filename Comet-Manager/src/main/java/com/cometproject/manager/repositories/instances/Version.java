package com.cometproject.manager.repositories.instances;

import org.springframework.data.annotation.Id;

public class Version {

    @Id
    private String id;

    private int versionNumber;

    private String type;
    private String version;
    private String notes;

    public Version(int versionNumber, String type, String version, String notes) {
        this.versionNumber = versionNumber;
        this.type = type;
        this.version = version;
        this.notes = notes;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

