package com.example.ecotrip_tiketwisatatamannasional.model;

public class OpenTrip {
    private String mountain;
    private String date;
    private String description;
    private String hostName;
    private int hostAvatarRes;

    public OpenTrip(String mountain, String date, String description, String hostName, int hostAvatarRes) {
        this.mountain = mountain;
        this.date = date;
        this.description = description;
        this.hostName = hostName;
        this.hostAvatarRes = hostAvatarRes;
    }

    public String getMountain() { return mountain; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
    public String getHostName() { return hostName; }
    public int getHostAvatarRes() { return hostAvatarRes; }
}