package ca.jonnybauer.watched.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ca.jonnybauer.watched.Tables.ConfigTable;
//import ca.jonnybauer.watched.Tables.TheatresTable;
import ca.jonnybauer.watched.Tables.WatchListTable;

/**
 * This class is used to interact the the database within the application.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "watched";

    // Constructor
    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    /**
     * onCreate method launched when the database is created for the first time.
     */
    public void onCreate(SQLiteDatabase db) {

        // TODO: Create tables for the first time
        db.execSQL(WatchListTable.CREATE_TABLE);
//        db.execSQL(TheatresTable.CREATE_TABLE);
        db.execSQL(ConfigTable.CREATE_TABLE);
    }

    @Override
    /**
     * onUpgrade method launched when the database is upgraded.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
