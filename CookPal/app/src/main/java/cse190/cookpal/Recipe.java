package cse190.cookpal;

import java.util.ArrayList;

public class Recipe {

    private String recipeName;
    private ArrayList<Step> stepList;
    private ArrayList<Ingredients> ingredientList;

    //TODO: hold recipe image

    public Recipe(String name, ArrayList<Step> stepList, ArrayList<Ingredients> ingredientList) {
        this.recipeName = name;
        this.stepList = stepList;
        this.ingredientList = ingredientList;
    }

    // TODO: temporary dummy Recipe until actual data is passed from Recipe Intent --> remove this
    public Recipe(String name) {
        this.recipeName = name;
        this.stepList = dummyStepList();
        this.ingredientList = dummyIngredientsList();
    }

    public ArrayList<Step> getStepList() {
        return stepList;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    public void setIngredientList(ArrayList<Ingredients> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public ArrayList<Ingredients> getIngredientList() {
        return this.ingredientList;
    }


    // TODO: temporary step population until actual data is passed from Recipe Intent --> remove this
    public ArrayList<Step> dummyStepList() {
        ArrayList<Step> stepList = new ArrayList<Step>();
        Step currStep;

        for(int i = 0; i < 10; i++) {
            currStep = new Step("Chop the onions " + i,
                    "Take your knife. And chop the onions. Don't cry or you're banished " + i,
                    0, i, i);

            stepList.add(currStep);
        }

        return stepList;
    }

    public ArrayList<Ingredients> dummyIngredientsList(){
        ArrayList<Ingredients> ingredientList = new ArrayList<Ingredients>();
        Ingredients ingredient;

        for(int i = 0; i < 10; i++) {
            ingredient = new Ingredients("10 lb", "broccoli");
            ingredientList.add(ingredient);
        }

        return ingredientList;
    }
}
