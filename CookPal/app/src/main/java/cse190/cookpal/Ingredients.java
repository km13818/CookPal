package cse190.cookpal;

import java.io.Serializable;

/**
 * Created by timchi on 12/3/14.
 */
public class Ingredients implements Serializable{

    private String name;
    private String quantity;

    public Ingredients(String name, String quantity ) {
        this.name = name;
        this.quantity = quantity;
    }

    public String toString() {
        return getQuantity() +" of "+ getIngredientName();
    }

    public void setIngredientName(String name){
        this.name = name;
    }

    public void setQuantity(String qty){
        this.quantity = qty;
    }

    public String getIngredientName(){
        return this.name;
    }

    public String getQuantity(){
        return this.quantity;
    }
}
