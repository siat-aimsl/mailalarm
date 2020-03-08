package org.jmqtt.response;

public class Measurement {
    private String statistic;
    private double value;

    public String getStatistic() {
        return statistic;
    }

    public void setStatistic(String statistic) {
        this.statistic = statistic;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Measurements{" +
                "statistic='" + statistic + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
