package org.jmqtt.response;

import java.util.List;

public class ActuatorResponse {
    private String name;
    private String description;
    private String baseUnit;
    private List<Measurement> measurements;
    private List<AvailableTag> availableTags;

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

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<AvailableTag> getAvailableTags() {
        return availableTags;
    }

    public void setAvailableTags(List<AvailableTag> availableTags) {
        this.availableTags = availableTags;
    }

    @Override
    public String toString() {
        return "ActuatorResponse{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", baseUnit='" + baseUnit + '\'' +
                ", measurements=" + measurements +
                ", availableTags=" + availableTags +
                '}';
    }
}
