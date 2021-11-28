package com.enhanced.sustenance.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.ui.pantry_fragment.FoodObject;
import com.enhanced.sustenance.ui.recipe_fragment.RecipeObject;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingItemObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static final String TAG = "Utils";

    public static class SortArrayListString {

        public static ArrayList<String> ascending(ArrayList<String> stringArrayList) {
            Collections.sort(stringArrayList);
            return stringArrayList;
        }

        public static ArrayList<String> descending(ArrayList<String> stringArrayList) {
            Collections.sort(stringArrayList, Collections.reverseOrder());
            return stringArrayList;
        }
    }

    public static class SortFoodItemArrayList {

        public static ArrayList<FoodObject> alphabetically(ArrayList<FoodObject> foodObjectArrayList) {
            ArrayList<String> strings = new ArrayList<>();
            for (FoodObject obj : foodObjectArrayList) {
                strings.add(obj.getName());
            }
            SortArrayListString.ascending(strings);
            FoodObject[] foodObjects = new FoodObject[strings.size()];
            for (int i = 0; i < foodObjectArrayList.size(); i++) {
                FoodObject obj = foodObjectArrayList.get(i);
                int index = strings.indexOf(obj.getName());
                foodObjects[index] = obj;
            }
            return new ArrayList<>(Arrays.asList(foodObjects));

        }
    }

    public static class SortShoppingItemArrayList {

        public static ArrayList<ShoppingItemObject> alphabetically(ArrayList<ShoppingItemObject> shoppingObjectArrayList) {
            ArrayList<String> strings = new ArrayList<>();
            for (ShoppingItemObject obj : shoppingObjectArrayList) {
                strings.add(obj.getName());
            }
            SortArrayListString.ascending(strings);
            ShoppingItemObject[] shoppingItemObjects = new ShoppingItemObject[strings.size()];
            for (int i = 0; i < shoppingObjectArrayList.size(); i++) {
                ShoppingItemObject obj = shoppingObjectArrayList.get(i);
                int index = strings.indexOf(obj.getName());
                shoppingItemObjects[index] = obj;
            }
            return new ArrayList<>(Arrays.asList(shoppingItemObjects));

        }
    }

    public static class SortRecipeItemArrayList {

        public static ArrayList<RecipeObject> alphabetically(ArrayList<RecipeObject> recipeObjects) {
            ArrayList<String> strings = new ArrayList<>();
            for (RecipeObject obj : recipeObjects) {
                strings.add(obj.getName());
            }
            SortArrayListString.ascending(strings);
            RecipeObject[] recipeItemObjects = new RecipeObject[strings.size()];
            for (int i = 0; i < recipeObjects.size(); i++) {
                RecipeObject obj = recipeObjects.get(i);
                int index = strings.indexOf(obj.getName());
                recipeItemObjects[index] = obj;
            }
            return new ArrayList<>(Arrays.asList(recipeItemObjects));

        }
    }

    public static boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(editText.getText().toString());
    }

    public static boolean isEmpty(TextView textView) {
        return TextUtils.isEmpty(textView.getText().toString());
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




    ///// recipe utils //////


    public static ArrayList<String[]> getIngredientsList(RecipeObject obj) {
        ArrayList<String[]> list = new ArrayList<>();
        String[] ingredients = obj.getIngredients().split("///");
        for (String ingredient : ingredients) {
            String[] strings = ingredient.split("---");
            list.add(strings);
        }
        return list;
    }

    public static boolean isIngredientAvailable(String name) {
        if (MainActivity.getFoodItemQtyMap().containsKey(name)) {
            return MainActivity.getFoodItemQtyMap().get(name) > 0;
        }
        return false;
    }

    public static int getExpiryScore(String[] strings) {
        if (strings[1] != null && !strings[1].equalsIgnoreCase("") && Integer.parseInt(strings[1]) > 0){
            int daysUntilExpire = daysUntilExpire(strings[0]);
        }
        return 0;
    }

    public static int daysUntilExpire(String name) {
        Date date = new Date();
        long diff = 0;
        for (FoodObject obj : MainActivity.getFoodObjects()) {
            if (obj.getName().equalsIgnoreCase(name)) {
                if (obj.getExpiry() != null) {
                    diff = obj.getExpiry().getTime() - date.getTime();
                    diff = TimeUnit.MILLISECONDS.toDays(diff);
                }
            }
            break;
        }
        return (int) diff;
    }


}

