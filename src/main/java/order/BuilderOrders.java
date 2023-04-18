package order;

import java.util.ArrayList;
import java.util.List;

public class BuilderOrders {
    private List<String> ingredients = new ArrayList<>(0);
    public BuilderOrders(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public BuilderOrders() {
    }
    public void addIngredients(String ingredient){
        ingredients.add(ingredient);
    }
    public List<String> getIngredients() {
        return ingredients;
    }
}
