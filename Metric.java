package app; 

import java.util.ArrayList;

public class Metric {
    public String metricName;
    public double percDifference;
    public double avgPercChange;
    public ArrayList<Double> values;

    public Metric() {
        metricName = "";
        percDifference = 0.0;
        avgPercChange = 0.0;
        values = null;
    }

    public Metric(String metricName, double percDifference, double avgPercChange, ArrayList<Double> values) {
        this.metricName = metricName;
        this.percDifference = percDifference;
        this.avgPercChange = avgPercChange;
        this.values = values;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    public void setPercDifference(double percDifference) {
        this.percDifference = percDifference;
    }

    public void setAvgPercChange(double avgPercChange) {
        this.avgPercChange = avgPercChange;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }
    

    public String getMetricName() {
        return metricName;
    }

    public double getPercDifference() {
        return percDifference;
    }

    public double getAvgPercChange() {
        return avgPercChange;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

}
