package cse190.cookpal;

import java.util.ArrayList;


public class Recipe {
    public String recipeName;
    public String cookbookType;
    public int fb_id;
    public ArrayList<Step> steps;

    public class Step {
        public String overview;
        public String description;
        public int time;  //time in seconds of step
    }
}
