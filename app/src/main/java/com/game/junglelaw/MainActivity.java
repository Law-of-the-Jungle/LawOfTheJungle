package com.game.junglelaw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/***
 * This the main activity for welcome and setting etc
 */
public class MainActivity extends Activity {

    private String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        GameView gv=new GameView(this);
        setContentView(gv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume");
        GameView gv=new GameView(this);
        setContentView(gv);
    }

    @Override
    protected void onPause() {
        /*On pause is called when home button is call*/
        super.onPause();
        Log.d(TAG,"OnPause");
    }

    @Override
    protected void onStop() {
        /*Called after onPause*/
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
