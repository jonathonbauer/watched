package ca.jonnybauer.watched.Tables;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Config;

/**
 * This class represents the config table, and will be used to manipulate it.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class ConfigTable {

    // Instance to be used to access the database
    public static ConfigTable instance = null;

    // Constructor
    private ConfigTable() {}

    // Get Instance Method

    public static ConfigTable getInstance(){
        if(instance == null) {
            return new ConfigTable();
        } else {
            return instance;
        }
    }

    // Table Contents
    private static final String TABLE_NAME = "config";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WATCH_LIST_STYLE = "watch_list_style";
    private static final String COLUMN_SAVE_LOCATION = "save_location";

    // Create Table Query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_WATCH_LIST_STYLE + " INTEGER,"
            + COLUMN_SAVE_LOCATION + " INTEGER,";

    // Delete Table Query
    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    // Create Record Query
    public void addConfig(Config config, DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WATCH_LIST_STYLE, config.getWatchListStyle());
        values.put(COLUMN_SAVE_LOCATION, config.getSaveLocation());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get Record Query
    public Config getConfig(DBHelper dbHelper, int configID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] args = { configID + "" };

        Cursor cursor = db.query(TABLE_NAME, null, selection, args, null, null, null);

        cursor.moveToNext();
        Config config = new Config(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_WATCH_LIST_STYLE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_SAVE_LOCATION)));
        cursor.close();
        db.close();
        return config;

    }

    // Get All Records Query
    public ArrayList<Config> getAllConfigs(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Config> configs = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY  ?", null);
        while(cursor.moveToNext()) {
            Config config = new Config(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_WATCH_LIST_STYLE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_SAVE_LOCATION)));
            configs.add(config);
        }
        cursor.close();
        db.close();
        return configs;
    }

    // Update Record Query
    public void updateConfig(Config config, DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WATCH_LIST_STYLE, config.getWatchListStyle());
        values.put(COLUMN_SAVE_LOCATION, config.getSaveLocation());
        db.insert(TABLE_NAME, null, values);
        String selection = COLUMN_ID + " LIKE ?";
        String[] args = { config.getId() + "" };
        db.update(TABLE_NAME, values, selection, args);
        db.close();
    }

    // Delete Record Query
    public void deleteConfig(DBHelper dbHelper, int configID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = COLUMN_ID + "LIKE ?";
        String[] args = { configID + "" };
        db.delete(TABLE_NAME, selection, args);
        db.close();
    }
    



}
