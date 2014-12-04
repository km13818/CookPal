package cse190.cookpal;

import java.sql.Time;

public class Step {
    private String title;
    private String description;

    private int minutes;
    private int stepNumber;

    public Step(String title, String desc, int hours, int minutes, int stepNum) {
        this.title = title;
        this.description = desc;
        this.hours = hours;
        this.minutes = minutes;
        this.stepNumber = stepNum;
    }

    public String toString() {
        return stepNumber + ": " + title + "\n\ttime: " + hours + " hours " + minutes + "minutes";
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

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    private int hours;

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }


}
