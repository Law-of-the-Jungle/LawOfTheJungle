package com.game.junglelaw.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.game.junglelaw.data.JungleLawContract.PlayerScores;

/**
 * Created by apple on 11/28/15.
 */
public class JungleLawDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = JungleLawDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "jungle_law.db";
    private static final int DATABASE_VERSION = 1;

    public JungleLawDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_EXPENSE_LOG_TABLE = "CREATE TABLE " + PlayerScores.TABLE_NAME + " (" +
                PlayerScores._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PlayerScores.COLUMN_SCORE + " INT NOT NULL, " +
                " UNIQUE (" + PlayerScores._ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_EXPENSE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlayerScores.TABLE_NAME);
        onCreate(db);
    }
}
