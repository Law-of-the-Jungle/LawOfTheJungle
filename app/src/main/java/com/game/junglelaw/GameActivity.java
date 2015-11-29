package com.game.junglelaw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/**
 * Created by apple on 11/28/15.
 */
public class GameActivity extends Activity {

    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GameView gv = new GameView(this);
        setContentView(gv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "OnResume");
        GameView gv = new GameView(this);
        setContentView(gv);
    }

    @Override
    protected void onPause() {
    /*On pause is called when home button is call*/
        super.onPause();
        Log.d(LOG_TAG, "OnPause");
    }

    @Override
    protected void onStop() {
    /*Called after onPause*/
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }
}
