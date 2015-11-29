package com.game.junglelaw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.game.junglelaw.data.JungleLawContract.PlayerScores;

/**
 * Created by apple on 11/28/15.
 */
public class JungleLawDbAdapter {

    private static final String LOG_TAG = JungleLawDbAdapter.class.getSimpleName();

    private JungleLawDbHelper mJungleLawDbHelper;

    public JungleLawDbAdapter(Context context) {
        mJungleLawDbHelper = new JungleLawDbHelper(context);
    }

    public Cursor query() {
        SQLiteDatabase db = mJungleLawDbHelper.getReadableDatabase();
        String sortOrder = PlayerScores.COLUMN_SCORE + " DESC";
        return db.query(PlayerScores.TABLE_NAME, null, null, null, null, null, sortOrder);
    }

    public void insert(int score) {
        SQLiteDatabase db = mJungleLawDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlayerScores.COLUMN_SCORE, score);
        db.insert(PlayerScores.TABLE_NAME, null, contentValues);
    }
}