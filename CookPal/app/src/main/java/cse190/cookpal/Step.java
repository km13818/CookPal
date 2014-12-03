package cse190.cookpal;

import java.sql.Time;

public class Step {
    private String title;
    private String description;
    private Time timeTakes;
    private int stepNumber;

    public Step(String title, String desc, Time time, int stepNum) {
        this.title = title;
        this.description = desc;
        this.timeTakes = time;
        this.stepNumber = stepNum;
    }

    public String toString() {
        return stepNumber + ": " + title + "\n\ttime: " + timeTakes.getTime();
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Time getTimeTakes() {
        return timeTakes;
    }

    public void setTimeTakes(Time timeTakes) {
        this.timeTakes = timeTakes;
    }
}
