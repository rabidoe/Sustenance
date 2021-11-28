package com.enhanced.sustenance.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.enhanced.sustenance.BootSequence;
import com.enhanced.sustenance.MainActivity;
import com.enhanced.sustenance.ui.pantry_fragment.FoodObject;
import com.enhanced.sustenance.ui.recipe_fragment.RecipeObject;
import com.enhanced.sustenance.ui.shopping_fragment.ShoppingItemObject;
import com.enhanced.sustenance.utils.Enums;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String TAG = "DBHandler";

    private DBHandler instance;

    public enum DATABASES {
        FOOD_ITEMS,
        RECIPES,
        SHOPPING
    }

    public static final String DATABASE_NAME = "SustenanceDB.db";

    private static final String CREATE_FOOD_ITEM_DB = "CREATE TABLE IF NOT EXISTS " + DATABASES.FOOD_ITEMS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QTY TEXT, UNIT TEXT, EXPIRY TEXT, CATEGORY TEXT, PACKAGING TEXT, ISLE TEXT, NOTE TEXT)";
    private static final String CREATE_SHOPPING_DB = "CREATE TABLE IF NOT EXISTS " + DATABASES.SHOPPING + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QTY TEXT, UNIT TEXT, CATEGORY TEXT, ISLE TEXT, CHECKED TEXT)";
    private static final String CREATE_RECIPE_DB = "CREATE TABLE IF NOT EXISTS " + DATABASES.RECIPES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT, DIRECTIONS TEXT, NOTES TEXT, INGREDIENTS TEXT, FAVOURITE TEXT)";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        instance = this;
        db.execSQL(CREATE_FOOD_ITEM_DB);
        db.execSQL(CREATE_SHOPPING_DB);
        db.execSQL(CREATE_RECIPE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASES.FOOD_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASES.SHOPPING);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASES.RECIPES);
        onCreate(db);
    }

    public boolean insertItem(DATABASES database, ContentValues contentValues) {
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.insert(String.valueOf(database), null, contentValues);
        return true;
    }

    public boolean updateItem(DATABASES database, String name, ContentValues contentValues) {
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.update(String.valueOf(database), contentValues, "NAME = ? ", new String[]{name});
        return true;
    }

    public void deleteItemByName(DATABASES database, String name) {
        SQLiteDatabase wdb = this.getWritableDatabase();
        wdb.delete(String.valueOf(database), "NAME = ? ", new String[]{name});
    }

    public ArrayList<String> getItemListByName(DATABASES db) {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase rdb = this.getReadableDatabase();
        Cursor res = rdb.rawQuery("SELECT * FROM " + db, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("NAME")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }


    ///////food items//////////

    public boolean handleFoodItem(FoodObject foodObject) {
        ContentValues contentValues = getFoodItemContentValues(foodObject);
        if (getItemListByName(DATABASES.FOOD_ITEMS).contains(foodObject.getName())) {
            updateItem(DATABASES.FOOD_ITEMS, foodObject.getName(), contentValues);
        } else {
            insertItem(DATABASES.FOOD_ITEMS, contentValues);
        }
        BootSequence.executeFoodObjects();
        return true;
    }

    private ContentValues getFoodItemContentValues(FoodObject foodObject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", foodObject.getName());
        if (foodObject.getQty() != null) {
            contentValues.put("QTY", foodObject.getQty());
        }
        if (foodObject.getUnit() != null) {
            contentValues.put("UNIT", foodObject.getUnit());
        }
        if (foodObject.getExpiry() != null) {
            contentValues.put("EXPIRY", foodObject.getExpiryAsString());
        } else {
            contentValues.put("EXPIRY", "");
        }
        if (foodObject.getCategory() != null) {
            contentValues.put("CATEGORY", foodObject.getCategory().getValue());
        }
        if (foodObject.getPackaging() != null) {
            contentValues.put("PACKAGING", foodObject.getPackaging().getValue());
        }
        if (foodObject.getIsle() != null) {
            contentValues.put("ISLE", foodObject.getIsle());
        }
        if (foodObject.getNote() != null) {
            contentValues.put("NOTE", foodObject.getNote());
        }
        return contentValues;
    }


    public ArrayList<FoodObject> getFoodObjectArrayList() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASES.FOOD_ITEMS, null);

        // on below line we are creating a new array list.
        ArrayList<FoodObject> foodObjects = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                FoodObject obj = new FoodObject(cursor.getString(1));
                obj.setQty(cursor.getString(2));
                obj.setUnit(Enums.UNITS.getEnum(cursor.getString(3)));
                String date = cursor.getString(4);
                if (date != null) {
                    String[] strings = date.split("/");
                    if (strings.length == 3) {
                        obj.setExpiryFromStrings(strings[0], strings[1], strings[2]);
                    }
                }
                obj.setCategory(Enums.CATEGORIES.getEnum(cursor.getString(5)));
                obj.setPackaging(Enums.PACKAGING.getEnum(cursor.getString(6)));
                obj.setIsle(cursor.getString(7));
                obj.setNote(cursor.getString(8));
                foodObjects.add(obj);
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return foodObjects;
    }

    ///////shopping///////

    public boolean handleShoppingItem(ShoppingItemObject shoppingItemObject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", shoppingItemObject.getName());
        contentValues.put("QTY", shoppingItemObject.getQty());
        contentValues.put("UNIT", shoppingItemObject.getUnit().getValue());
        contentValues.put("CATEGORY", shoppingItemObject.getCategory().getValue());
        if (shoppingItemObject.getAisle() != null) {
            contentValues.put("ISLE", shoppingItemObject.getAisle());
        }
        if (shoppingItemObject.isChecked()) {
            contentValues.put("CHECKED", "true");
        } else {
            contentValues.put("CHECKED", "false");
        }

        if (getItemListByName(DATABASES.SHOPPING).contains(shoppingItemObject.getName())) {
            updateItem(DATABASES.SHOPPING, shoppingItemObject.getName(), contentValues);
        } else {
            insertItem(DATABASES.SHOPPING, contentValues);
        }
        BootSequence.executeShoppingListObjects();
        return true;
    }

    public boolean updatedShoppingCheckedState(String name, boolean checked) {
        ShoppingItemObject shoppingItemObject = new ShoppingItemObject(name);
        for (ShoppingItemObject obj : MainActivity.getShoppingItemObjects()) {
            if (obj.getName().equalsIgnoreCase(name)) {
                shoppingItemObject = obj;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", shoppingItemObject.getName());
        contentValues.put("QTY", shoppingItemObject.getQty());
        contentValues.put("UNIT", shoppingItemObject.getUnit().getValue());
        contentValues.put("CATEGORY", shoppingItemObject.getCategory().getValue());
        if (shoppingItemObject.getAisle() != null) {
            contentValues.put("ISLE", shoppingItemObject.getAisle());
        }
        if (checked) {
            contentValues.put("CHECKED", "true");
        } else {
            contentValues.put("CHECKED", "false");
        }
        updateItem(DATABASES.SHOPPING, shoppingItemObject.getName(), contentValues);
        BootSequence.executeShoppingListObjects();
        return true;
    }

    public ArrayList<ShoppingItemObject> getShoppingObjectArrayList() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASES.SHOPPING, null);

        // on below line we are creating a new array list.
        ArrayList<ShoppingItemObject> shoppingItemObjects = new ArrayList<>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                ShoppingItemObject obj = new ShoppingItemObject(cursor.getString(1));
                obj.setQty(cursor.getString(2));
                obj.setUnit(Enums.UNITS.getEnum(cursor.getString(3)));
                obj.setCategory(Enums.CATEGORIES.getEnum(cursor.getString(4)));
                obj.setAisle(cursor.getString(5));
                obj.setChecked(cursor.getString(6) != null && cursor.getString(6).equalsIgnoreCase("true"));

                shoppingItemObjects.add(obj);
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return shoppingItemObjects;
    }


    ///////RECIPES////////
//NAME TEXT, DESCRIPTION TEXT, DIRECTIONS TEXT, NOTES TEXT, INGREDIENTS TEXT

    public boolean handleRecipeItem(RecipeObject recipeObject) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", recipeObject.getName());
        if (recipeObject.getDescription() != null) {
            contentValues.put("DESCRIPTION", recipeObject.getDescription());
        }
        if (recipeObject.getDirections() != null) {
            contentValues.put("DIRECTIONS", recipeObject.getDirections());
        }
        if (recipeObject.getNotes() != null) {
            contentValues.put("NOTES", recipeObject.getNotes());
        }
        if (recipeObject.getIngredients() != null) {
            contentValues.put("INGREDIENTS", recipeObject.getIngredients());
        }
        if (recipeObject.getFavourite() != null) {
            contentValues.put("FAVOURITE", recipeObject.getFavourite());
        }

        if (getItemListByName(DATABASES.RECIPES).contains(recipeObject.getName())) {
            updateItem(DATABASES.RECIPES, recipeObject.getName(), contentValues);
        } else {
            insertItem(DATABASES.RECIPES, contentValues);
        }
        BootSequence.executeRecipeList();
        return true;
    }

    public ArrayList<RecipeObject> getRecipeObjectArrayList() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();
        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASES.RECIPES, null);

        // on below line we are creating a new array list.
        ArrayList<RecipeObject> recipeObjects = new ArrayList<>();
        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                RecipeObject obj = new RecipeObject(cursor.getString(1));
                obj.setDescription(cursor.getString(2));
                obj.setDirections(cursor.getString(3));
                obj.setNotes(cursor.getString(4));
                obj.setIngredients(cursor.getString(5));
                obj.setFavourite(cursor.getString(6));

                recipeObjects.add(obj);
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursor.close();
        return recipeObjects;
    }


}


