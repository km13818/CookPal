package cse190.cookpal;

/**
 * Created by timchi on 12/3/14.
 */
public class Ingredients {

    private String name;
    private String quantity;

    public Ingredients(String name, String quantity ) {
        this.name = name;
    }

    public String toString() {
        return quantity + name;
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
