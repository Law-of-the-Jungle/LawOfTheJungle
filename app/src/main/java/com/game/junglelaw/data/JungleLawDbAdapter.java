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

    private SQLiteDatabase db;

    public JungleLawDbAdapter(Context context) {
        JungleLawDbHelper mJungleLawDbHelper = new JungleLawDbHelper(context);
        db = mJungleLawDbHelper.getWritableDatabase();
    }

    /**
     * Returns the top 10 (if have) scores
     * @return
     */
    public Cursor query() {
        String sortOrder = PlayerScores.COLUMN_SCORE + " DESC LIMIT 10";
        return db.query(PlayerScores.TABLE_NAME, null, null, null, null, null, sortOrder);
    }

    public void insert(int score, String gameDifficulty, String createTime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PlayerScores.COLUMN_SCORE, score);
        contentValues.put(PlayerScores.COLUMN_SCORE_CREATE_TIME, createTime);
        contentValues.put(PlayerScores.COLUMN_GAME_DIFFICULTY, gameDifficulty);
        db.insert(PlayerScores.TABLE_NAME, null, contentValues);
    }
}