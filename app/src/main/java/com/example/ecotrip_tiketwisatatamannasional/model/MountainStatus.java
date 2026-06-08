package com.example.ecotrip_tiketwisatatamannasional.model;

public class MountainStatus {
    private String name;
    private String level;
    private String weather;
    private String temperature;
    private int statusColor; // e.g., Color.GREEN for Normal, Color.YELLOW for Waspada

    public MountainStatus(String name, String level, String weather, String temperature, int statusColor) {
        this.name = name;
        this.level = level;
        this.weather = weather;
        this.temperature = temperature;
        this.statusColor = statusColor;
    }

    public String getName() { return name; }
    public String getLevel() { return level; }
    public String getWeather() { return weather; }
    public String getTemperature() { return temperature; }
    public int getStatusColor() { return statusColor; }
}