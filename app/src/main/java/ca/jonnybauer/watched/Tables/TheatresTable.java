package ca.jonnybauer.watched.Tables;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Theatre;
import ca.jonnybauer.watched.Models.Theatre;

/**
 * This class represents the theatres table, and will be used to manipulate it.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class TheatresTable {

    // Instance to be used to access the database
    public static TheatresTable instance = null;

    // Constructor
    private TheatresTable() {}

    // Get Instance Method

    public static TheatresTable getInstance(){
        if(instance == null) {
            return new TheatresTable();
        } else {
            return instance;
        }
    }

    // Table Contents
    private static final String TABLE_NAME = "theatres";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_WEBSITE = "website";
    private static final String COLUMN_FAVOURITE = "favourite";

    // Create Table Query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_ADDRESS + " TEXT,"
            + COLUMN_PHONE + " TEXT,"
            + COLUMN_WEBSITE + " TEXT,"
            + COLUMN_FAVOURITE + " INTEGER,";

    // Create Record Query
    public void addTheatre(Theatre theatre, DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, theatre.getName());
        values.put(COLUMN_ADDRESS, theatre.getAddress());
        values.put(COLUMN_PHONE, theatre.getPhone());
        values.put(COLUMN_WEBSITE, theatre.getWebsite());
        values.put(COLUMN_FAVOURITE, theatre.getFavourite());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get Record Query
    public Theatre getTheatre(DBHelper dbHelper, int theatreID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] args = { theatreID + "" };

        Cursor cursor = db.query(TABLE_NAME, null, selection, args, null, null, null);

        cursor.moveToNext();
        Theatre theatre = new Theatre(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_WEBSITE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE)));
        cursor.close();
        db.close();
        return theatre;

    }

    // Get All Records Query
    public ArrayList<Theatre> getAllTheatres(DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Theatre> theatres = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY  ?", null);
        while(cursor.moveToNext()) {
            Theatre theatre = new Theatre(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_WEBSITE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FAVOURITE)));
            theatres.add(theatre);
        }
        cursor.close();
        db.close();
        return theatres;
    }

    // Update Record Query
    public void updateTheatre(Theatre theatre, DBHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, theatre.getName());
        values.put(COLUMN_ADDRESS, theatre.getAddress());
        values.put(COLUMN_PHONE, theatre.getPhone());
        values.put(COLUMN_WEBSITE, theatre.getWebsite());
        values.put(COLUMN_FAVOURITE, theatre.getFavourite());
        db.insert(TABLE_NAME, null, values);
        String selection = COLUMN_ID + " LIKE ?";
        String[] args = { theatre.getId() + "" };
        db.update(TABLE_NAME, values, selection, args);
        db.close();
    }

    // Delete Record Query
    public void deleteTheatre(DBHelper dbHelper, int theatreID){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = COLUMN_ID + "LIKE ?";
        String[] args = { theatreID + "" };
        db.delete(TABLE_NAME, selection, args);
        db.close();
    }
    



}
