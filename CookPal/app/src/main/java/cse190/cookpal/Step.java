package cse190.cookpal;


import java.io.Serializable;

public class Step implements Serializable{
    private String title;
    private String instructionTitle;
    private String description;
    private int hours;
    private int minutes;
    private int stepNumber;

    public Step(String title, String instrTitle, String desc, int hours, int minutes, int stepNum) {
        this.title = title;
        this.instructionTitle = instrTitle;
        this.description = desc;
        this.hours = hours;
        this.minutes = minutes;
        this.stepNumber = stepNum;
    }

    @Override
    public String toString() {
        return getStepNumber() + ": " + getTitle() + "\n" + getTime();
    }


    public String toStringDescription() {

        return "STEP: "+stepNumber  + " " + getInstructionTitle() + " " +  getDescription() + " hrs: " + hours + " minutes: " + minutes;
    }

    public String intToString(int num){
        //TODO how to carry over the 1
        return String.valueOf(num);
    }

    public String getTime(int hours, int minutes){
        if(hours == 0 && minutes == 0) {
            return "Time: n/a";
        }
        else return "Time: " + intToString(hours) + "hr " + intToString(minutes) + "m";
    }

    public String getTime() {
        return getTime(getHours(), getMinutes());

    }

    public long getTimeInMilliseconds() {
        return (getHours()*3600000) + (getMinutes()*60000);
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

    public String getInstructionTitle() { return instructionTitle; }

    public void setInstructionTitle(String instrTitle) { this.instructionTitle = instrTitle; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHours() { return hours; }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) { this.hours = hours; }
}
