package cse190.cookpal;

public class Step {
    private String title;
    private String description;
    private int hours;
    private int minutes;
    private int stepNumber;

    public Step(String title, String desc, int hours, int minutes, int stepNum) {
        this.title = title;
        this.description = desc;
        this.hours = hours;
        this.minutes = minutes;
        this.stepNumber = stepNum;
    }

    @Override
    public String toString() {
        return getStepNumber() + ": " + getTitle() + "\nTime Takes: "
                + getHours() + "hr " + getMinutes() + "m";
    }

    public String toStringDescription() {
        return "STEP: "+stepNumber  + getDescription();
    }

    public String intToString(int num){
        //TODO how to carry over the 1
        return String.valueOf(num);
    }

    public String getTime(int hours, int minutes){
        if(hours == 0 && minutes == 0) return "Time: n/a";
        else return "Time: "+intToString(hours)+" : "+intToString(minutes);
    }

    public String getTime() {
        return getTime(getHours(), getMinutes());
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

    public int getHours() { return minutes; }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setHours(int hours) { this.hours = hours; }
}
