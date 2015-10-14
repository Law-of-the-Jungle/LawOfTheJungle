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
    private Thread playground;
    private float player_x,player_y;
    private SurfaceHolder holder;

    public GameView(Context context) {
        super(context);
        playground=new Thread(new PlayGround(this));
        player_x=50;
        player_y=50;
        holder=getHolder();
        holder.addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            player_x=event.getX();
            player_y=event.getY();
            Log.d("x:y",Float.toString(player_x)+":"+Float.toString(player_y));

        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d("draw","drawing");
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(player_x,player_y,100,p);
        //player_x+=10;
        Log.d("draw","after draw");

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("created","surfaceview created");
        playground.start();


    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        Log.d("changed","changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d("destroyed","destroyed");
        boolean retry = true;
        while (retry) {
            try {
                playground.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }
}
