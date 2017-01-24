package com.example.kunal.smarteventsuggestion;

/**
 * Created by Kunal on 21-01-2017.
 */

public class Event {
    private String name;
    private String description;
    private String startTime;
    private String location;
    private String id;
    public Event(){}
    public Event(String name, String location, String id, String description, String startTime) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.location = location;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
