package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class GameActivity extends Activity {

    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    private boolean mIsMute;
    private GameView mGameView;
    private MediaPlayer mGameBackgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        mGameView = new GameView(this);
        setContentView(mGameView);

        mGameBackgroundMusic = MediaPlayer.create(GameActivity.this, R.raw.fighting);
        mGameBackgroundMusic.setLooping(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        mIsMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);

        if (!mIsMute) {
            mGameBackgroundMusic.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mIsMute) {
            mGameBackgroundMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameBackgroundMusic.release();
    }

    protected void onResume() {
        super.onResume();
        mGameView = new GameView(this);
        setContentView(mGameView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.end) {
            Intent data = new Intent();
            float score = mGameView.getScore();
            data.putExtra("score", score);
            setResult(RESULT_OK, data);
            finish();
            return true;
        } else if (id == R.id.pause) {
            mGameView.pause();
        } else if (id == R.id.resume) {
            mGameView.resume();
        }

        return super.onOptionsItemSelected(item);
    }
}
