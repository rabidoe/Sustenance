package com.enhanced.sustenance;

import android.os.AsyncTask;

import com.enhanced.sustenance.ui.pantry_fragment.FoodObject;
import com.enhanced.sustenance.ui.pantry_fragment.PantryFragment;
import com.enhanced.sustenance.ui.recipe_fragment.RecipeMainFragment;
import com.enhanced.sustenance.ui.recipe_fragment.RecipeObject;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingItemObject;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingMainFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class BootSequence {
    private static final String TAG = "BootSequence";

    public static void executeFoodObjects() {
        new FoodItemListLoader().execute();
    }

    public static void executeShoppingListObjects() {
        new ShoppingListLoader().execute();
    }

    public static void executeRecipeList() {
        new RecipeListLoader().execute();
    }


    private static class FoodItemListLoader extends AsyncTask<String, String, ArrayList<FoodObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<FoodObject> doInBackground(String... strings) {
            ArrayList<FoodObject> foodObjects = MainActivity.getDBHandler().getFoodObjectArrayList();
            MainActivity.setFoodObjects(foodObjects);

            //set the expiry map
            TreeMap<String, Integer> foodItemExpiryMap = new TreeMap<>();
            Date date = new Date();
            for (FoodObject object : foodObjects) {
                long diff;
                if (object.getExpiry() != null) {
                    diff = object.getExpiry().getTime() - date.getTime();
                    diff = TimeUnit.MILLISECONDS.toDays(diff);
                } else {
                    diff = 1000;
                }
                foodItemExpiryMap.put(object.getName(), (int) diff);
            }
            MainActivity.setFoodItemExpiryMap(foodItemExpiryMap);

            //set the qty map
            TreeMap<String, Integer> foodItemQtyMap = new TreeMap<>();
            for (FoodObject object : foodObjects) {
                int qty = 0;
                if (object.getQty() != null && !object.getQty().equalsIgnoreCase("")) {
                    qty = Integer.parseInt(object.getQty());
                }
                foodItemQtyMap.put(object.getName(), qty);
            }
            MainActivity.setFoodItemQtyMap(foodItemQtyMap);

            return foodObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodObject> foodObjects) {
            PantryFragment.notifyAdaptors();
            RecipeMainFragment.notifyAdaptors();
            super.onPostExecute(foodObjects);

        }
    }

    private static class ShoppingListLoader extends AsyncTask<String, String, ArrayList<ShoppingItemObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<ShoppingItemObject> doInBackground(String... strings) {
            ArrayList<ShoppingItemObject> shoppingItemObjects = MainActivity.getDBHandler().getShoppingObjectArrayList();
            MainActivity.setShoppingItemObjects(shoppingItemObjects);
            return shoppingItemObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<ShoppingItemObject> shoppingItemObjects) {
            ShoppingMainFragment.notifyAdaptors();
            super.onPostExecute(shoppingItemObjects);
        }

    }

    private static class RecipeListLoader extends AsyncTask<String, String, ArrayList<RecipeObject>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<RecipeObject> doInBackground(String... strings) {
            ArrayList<RecipeObject> recipeObjects = MainActivity.getDBHandler().getRecipeObjectArrayList();
            ArrayList<RecipeObject> favObjects = new ArrayList<>();
            MainActivity.setRecipeObjects(recipeObjects);
            for (RecipeObject obj : recipeObjects) {
                if (obj.getFavourite().equalsIgnoreCase("true")) {
                    favObjects.add(obj);
                }
            }
            MainActivity.setFavRecipeObjects(favObjects);
            return recipeObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<RecipeObject> recipeObjects) {
            PantryFragment.notifyAdaptors();
            RecipeMainFragment.notifyAdaptors();
            super.onPostExecute(recipeObjects);
        }

    }

}
