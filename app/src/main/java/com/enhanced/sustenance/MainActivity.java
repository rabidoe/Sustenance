package com.enhanced.sustenance;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.enhanced.sustenance.databases.DBHandler;
import com.enhanced.sustenance.databinding.ActivityMainBinding;
import com.enhanced.sustenance.ui.pantry_fragment.FoodObject;
import com.enhanced.sustenance.ui.recipe_fragment.RecipeObject;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingItemObject;
import com.enhanced.sustenance.utils.MyHandler;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;

    private static DBHandler DBHandler;
    private static DBHandler recipeDBHandler;
    private static DBHandler shoppingDBHandler;
    private static final MyHandler mHandler = new MyHandler();

    private static ArrayList<FoodObject> foodObjects = new ArrayList<>();
    private static ArrayList<ShoppingItemObject> shoppingItemObjects = new ArrayList<>();
    private static ArrayList<RecipeObject> recipeObjects = new ArrayList<>();
    private static ArrayList<RecipeObject> favRecipeObjects = new ArrayList<>();
    private static TreeMap<String, Integer> foodItemExpiryMap = new TreeMap<>();
    private static TreeMap<String, Integer> foodItemQtyMap = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHandler = new DBHandler(this);
        recipeDBHandler = new DBHandler(this);
        shoppingDBHandler = new DBHandler(this);

        com.enhanced.sustenance.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recipe, R.id.nav_pantry, R.id.nav_shopping)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        BootSequence.executeFoodObjects();
        BootSequence.executeShoppingListObjects();
        BootSequence.executeRecipeList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }


    public static DBHandler getDBHandler() {
        return DBHandler;
    }

    public static android.os.Handler getHandler() {
        return mHandler.getWorkerThread();
    }

    public static Handler getUIhandler() {
        return mHandler.getUiHandler();
    }

    public static ArrayList<FoodObject> getFoodObjects() {
        return foodObjects;
    }

    public static void setFoodObjects(ArrayList<FoodObject> foodObjects) {
        MainActivity.foodObjects = foodObjects;
    }

    public static ArrayList<ShoppingItemObject> getShoppingItemObjects() {
        return shoppingItemObjects;
    }

    public static void setShoppingItemObjects(ArrayList<ShoppingItemObject> shoppingItemObjects) {
        MainActivity.shoppingItemObjects = shoppingItemObjects;
    }

    public static ArrayList<RecipeObject> getRecipeObjects() {
        return recipeObjects;
    }

    public static void setRecipeObjects(ArrayList<RecipeObject> recipeObjects) {
        MainActivity.recipeObjects = recipeObjects;
    }

    public static ArrayList<RecipeObject> getFavRecipeObjects() {
        return favRecipeObjects;
    }

    public static void setFavRecipeObjects(ArrayList<RecipeObject> favRecipeObjects) {
        MainActivity.favRecipeObjects = favRecipeObjects;
    }

    public static TreeMap<String, Integer> getFoodItemExpiryMap() {
        return foodItemExpiryMap;
    }

    public static void setFoodItemExpiryMap(TreeMap<String, Integer> foodItemExpiryMap) {
        MainActivity.foodItemExpiryMap = foodItemExpiryMap;
    }

    public static TreeMap<String, Integer> getFoodItemQtyMap() {
        return foodItemQtyMap;
    }

    public static void setFoodItemQtyMap(TreeMap<String, Integer> foodItemQtyMap) {
        MainActivity.foodItemQtyMap = foodItemQtyMap;
    }
}