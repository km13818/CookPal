package cse190.cookpal;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Serializable {



    private String recipeName;
    private ArrayList<Step> stepList;
    private ArrayList<Ingredients> ingredientList;

    //TODO: hold recipe image

    public Recipe(String name, ArrayList<Step> stepList, ArrayList<Ingredients> ingredientList) {
        this.recipeName = name;
        this.stepList = stepList;
        this.ingredientList = ingredientList;
    }

    // Note: dummy Recipe for testing purposes
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

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }


    // Note: dummy step population for testing purposes

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

/*    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };*/
}
