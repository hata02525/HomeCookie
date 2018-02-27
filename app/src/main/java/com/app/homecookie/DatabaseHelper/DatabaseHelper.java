package com.app.homecookie.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.homecookie.Beans.IngredientBean;
import com.app.homecookie.Beans.RecipeDetailBean;
import com.app.homecookie.Beans.StepBean;

import java.util.ArrayList;

/**
 * Created by fluper on 8/5/17.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "ingredientsManager";
    private static final String TABLE_INGREDIENTS = "ingredients";
    private static final String USER_TABLE_INGREDIENTS = "user_ingredients";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_UNITS = "units";
    private static final String KEY_QTY = "qty";

    private static final String USER_KEY_ID = "id";
    private static final String USER_KEY_NAME = "name";
    private static final String USER_KEY_PHOTO = "photo";
    private static final String USER_KEY_UNITS = "units";
    private static final String USER_KEY_QTY = "qty";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_PHOTO + " TEXT," + KEY_UNITS + " TEXT," + KEY_QTY + " TEXT" + ")";

    private static final String CREATE_INGREDIENTS_TABLE_FOR_USER = "CREATE TABLE " + USER_TABLE_INGREDIENTS + "("
            + USER_KEY_ID + " INTEGER PRIMARY KEY," + USER_KEY_NAME + " TEXT,"
            + USER_KEY_PHOTO + " TEXT," + USER_KEY_UNITS + " TEXT," + USER_KEY_QTY + " TEXT" + ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INGREDIENTS_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE_FOR_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_INGREDIENTS);
        onCreate(db);
    }

    public boolean addIngredients(RecipeDetailBean.IngredientsBean ingBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String id = ingBean.getIngredientId();
        String qty = ingBean.getQty();
        String unit = ingBean.getUnit();
        String name = ingBean.getName();
        values.put(KEY_ID, id);
        values.put(KEY_QTY, qty);
        values.put(KEY_UNITS, unit);
        values.put(KEY_NAME, name);
        if (db.insert(TABLE_INGREDIENTS, null, values) == -1) {
            return false;
        }
        db.close(); // Closing database connection
        return true;
    }


    public ArrayList<RecipeDetailBean.IngredientsBean> getIngredients() {
        ArrayList<RecipeDetailBean.IngredientsBean> ingredientList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecipeDetailBean.IngredientsBean ingBean = new RecipeDetailBean.IngredientsBean();
                ingBean.setIngredientId(cursor.getString(0));
                ingBean.setName(cursor.getString(1));
                ingBean.setUnit(cursor.getString(3));
                ingBean.setQty(cursor.getString(4));
                ingBean.setPhoto("");
                // Adding ingredients to list
                ingredientList.add(ingBean);
            } while (cursor.moveToNext());
        }
        return ingredientList;
    }


    public RecipeDetailBean.IngredientsBean getSingleIngredients(String id) {
        RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_INGREDIENTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PHOTO, KEY_UNITS, KEY_QTY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ingredient.setIngredientId(cursor.getString(0));
        ingredient.setName(cursor.getString(1));
        ingredient.setPhoto(cursor.getString(2));
        ingredient.setUnit(cursor.getString(3));
        ingredient.setQty(cursor.getString(4));
        // return contact
        return ingredient;
    }

    public RecipeDetailBean.IngredientsBean getSingleIngredientFromUserDb(String id) {
        RecipeDetailBean.IngredientsBean ingredient = new RecipeDetailBean.IngredientsBean();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(USER_TABLE_INGREDIENTS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PHOTO, KEY_UNITS, KEY_QTY}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ingredient.setIngredientId(cursor.getString(0));
        ingredient.setName(cursor.getString(1));
        ingredient.setPhoto(cursor.getString(2));
        ingredient.setUnit(cursor.getString(3));
        ingredient.setQty(cursor.getString(4));
        // return contact
        return ingredient;
    }

    public int deleteIngredient(RecipeDetailBean.IngredientsBean ingBean) {
        int deletedrow = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        deletedrow = db.delete(TABLE_INGREDIENTS, KEY_ID + " = ?",
                new String[]{ingBean.getIngredientId()});
        db.close();
        return deletedrow;
    }

    public int updateIngredient(RecipeDetailBean.IngredientsBean ingBean) {
        int updateRow = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String qty = ingBean.getQty();
        String unit = ingBean.getUnit();
        String name = ingBean.getName();
        values.put(KEY_QTY, qty);
        values.put(KEY_UNITS, unit);
        values.put(KEY_NAME, name);
        updateRow = db.update(TABLE_INGREDIENTS, values, KEY_ID + " = ?",
                new String[]{ingBean.getIngredientId()});
        db.close();
        return updateRow;
    }

    public boolean addIngredientsToUserDb(RecipeDetailBean.IngredientsBean ingBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String id = ingBean.getIngredientId();
        String qty = ingBean.getQty();
        String unit = ingBean.getUnit();
        String name = ingBean.getName();
        values.put(USER_KEY_ID, id);
        values.put(USER_KEY_QTY, qty);
        values.put(USER_KEY_UNITS, unit);
        values.put(USER_KEY_NAME, name);
        if (db.insert(USER_TABLE_INGREDIENTS, null, values) == -1) {
            updateIngredientInUserDb(ingBean);
        }
        db.close(); // Closing database connection
        return true;
    }


    public int updateIngredientInUserDb(RecipeDetailBean.IngredientsBean ingBean) {
        int updateRow = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String qty = ingBean.getQty();
        String unit = ingBean.getUnit();
        String name = ingBean.getName();
        values.put(KEY_QTY, qty);
        values.put(KEY_UNITS, unit);
        values.put(KEY_NAME, name);
        updateRow = db.update(USER_TABLE_INGREDIENTS, values, KEY_ID + " = ?",
                new String[]{ingBean.getIngredientId()});
        db.close();
        return updateRow;
    }


    public ArrayList<RecipeDetailBean.IngredientsBean> getUserIngredients() {
        ArrayList<RecipeDetailBean.IngredientsBean> ingredientList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + USER_TABLE_INGREDIENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecipeDetailBean.IngredientsBean ingBean = new RecipeDetailBean.IngredientsBean();
                ingBean.setIngredientId(cursor.getString(0));
                ingBean.setName(cursor.getString(1));
                ingBean.setUnit(cursor.getString(3));
                ingBean.setQty(cursor.getString(4));
                ingBean.setPhoto("");
                // Adding ingredients to list
                ingredientList.add(ingBean);
            } while (cursor.moveToNext());
        }
        return ingredientList;
    }

    public int deleteIngredientFromUserDb(RecipeDetailBean.IngredientsBean ingBean) {
        int deletedrow = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        deletedrow = db.delete(USER_TABLE_INGREDIENTS, KEY_ID + " = ?",
                new String[]{ingBean.getIngredientId()});
        db.close();
        return deletedrow;
    }


    public void clearUserDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DELETE FROM " + USER_TABLE_INGREDIENTS);
        db.execSQL("DELETE FROM " + TABLE_INGREDIENTS);
    }


}
