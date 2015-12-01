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

    private boolean isMute;
    private GameView gameView;
    private MediaPlayer bkgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameView = new GameView(this);
        setContentView(gameView);

        bkgMusic = MediaPlayer.create(GameActivity.this, R.raw.fighting);
        bkgMusic.setLooping(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        isMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);

        if (!isMute) {
            bkgMusic.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isMute) {
            bkgMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bkgMusic.release();
    }

    protected void onResume() {
        super.onResume();
        gameView = new GameView(this);
        setContentView(gameView);
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
            float score = gameView.getScore();
            data.putExtra("score", score);
            setResult(RESULT_OK, data);
            finish();
            return true;
        } else if (id == R.id.pause) {
            gameView.pause();
        } else if (id == R.id.resume) {
            gameView.resume();
        }

        return super.onOptionsItemSelected(item);
    }
}
