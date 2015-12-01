package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class GameActivity extends Activity {

    private GameView gv;
    private MediaPlayer bgmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        gv = new GameView(this);
        setContentView(gv);

        bgmMusic = MediaPlayer.create(GameActivity.this, R.raw.fighting);
        bgmMusic.setLooping(true);
        bgmMusic.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bgmMusic.stop();
        bgmMusic.release();
    }

    protected void onResume() {
        super.onResume();
        //Log.d(TAG, "OnResume");
        gv = new GameView(this);
        setContentView(gv);
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
            float score = gv.getScore();
            data.putExtra("score", score);
            setResult(RESULT_OK, data);
            finish();
            return true;
        } else if (id == R.id.pause) {
            gv.pause();
        } else if (id == R.id.resume) {
            gv.resume();
        }

        return super.onOptionsItemSelected(item);
    }
}
