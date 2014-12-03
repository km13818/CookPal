package cse190.cookpal;

import java.sql.Time;
import java.util.ArrayList;

public class Recipe {

    private String recipeName;
    private ArrayList<Step> stepList;

    //TODO: hold recipe image

    public Recipe(String name, ArrayList<Step> stepList) {
        this.recipeName = name;
        this.stepList = stepList;
    }

    // TODO: temporary dummy Recipe until actual data is passed from Recipe Intent --> remove this
    public Recipe(String name) {
        this.recipeName = name;
        this.stepList = dummyStepList();
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    // TODO: temporary step population until actual data is passed from Recipe Intent --> remove this
    public ArrayList<Step> dummyStepList() {
        ArrayList<Step> stepList = new ArrayList<Step>();
        Step currStep;

        for(int i = 0; i < 10; i++) {
            currStep = new Step("Chop the onions " + i,
                    "Take your knife. And chop the onions. Don't cry or you're banished " + i,
                    new Time(1000 * i), i);

            stepList.add(currStep);
        }

        return stepList;
    }
}
