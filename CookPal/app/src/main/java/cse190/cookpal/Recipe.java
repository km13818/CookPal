package cse190.cookpal;

import java.sql.Time;
import java.util.ArrayList;

public class Recipe {
    private String recipeName;
    private ArrayList<Steps> stepList;

    public ArrayList<Steps> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<Steps> stepList) {
        this.stepList = stepList;
    }

    class Steps {
        private String title;
        private String description;
        private Time timeTakes;
        private int stepNumber;

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
}
