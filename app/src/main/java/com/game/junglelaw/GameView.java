package com.game.junglelaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This class is used for rendering the view of game drawing the points in the PlayGround class
 * PlayGround instance is included in this view class, then we need to pass this view class to
 * PlayGround class in its constructor for drawing.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private PlayGround playground;
    private float player_x,player_y;
    private SurfaceHolder holder;
    private String TAG="GameView";

    public GameView(Context context) {
        super(context);
        playground=new PlayGround(this);
        player_x=50;
        player_y=50;
        holder=getHolder();
        holder.addCallback(this);
        Log.d(TAG,"GameView created");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            player_x=event.getX();
            player_y=event.getY();
            Log.d(TAG,Float.toString(player_x)+":"+Float.toString(player_y));

        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(player_x,player_y,100,p);


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"Surfaceview created");
        playground.setRunState(true);
        playground.start();


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d(TAG,"Surfaceview changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"Surfaceview destroyed");
        boolean retry = true;
        while (retry) {
            try {
                playground.setRunState(false);
                playground.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }
}
