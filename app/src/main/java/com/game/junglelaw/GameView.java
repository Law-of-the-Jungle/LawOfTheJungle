package com.game.junglelaw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.nfc.Tag;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.game.junglelaw.Circle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used for rendering the view of game drawing the points in the PlayGround class
 * PlayGround instance is included in this view class, then we need to pass this view class to
 * PlayGround class in its constructor for drawing.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private PlayGround playground;

    private int Screen_height,Screen_width;
    private PointF center;

    private SurfaceHolder holder;
    private String TAG="GameView";


    //we need to get the relative location of points to the playerCircle
    //then check if these circle could appear on map or not
    //Draw the circle with location of screen

    /* Testing Data */
    public PlayerCircle player;//User information
    private int map_height,map_width;
    List<StaticCricle> SCircle;


    /**-----------**/


    public GameView(Context context) {
        super(context);
        playground=new PlayGround(this);

        holder=getHolder();
        holder.addCallback(this);

        player=new PlayerCircle(1000,1000,100,0);


        Log.d(TAG,"GameView created");
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Random rand= new Random(255);
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(center.x,center.y,100,p);
        player.moveToDirection();
        for(int i=0;i<SCircle.size();i++){
            StaticCricle sc=SCircle.get(i);
            PointF circle_center= RelativeCenterLocation(sc);
            p.setColor(Color.argb(255, rand.nextInt(), rand.nextInt(), rand.nextInt()));
            canvas.drawCircle(circle_center.x, circle_center.y, sc.getRadius(),p);
        }


    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d(TAG,"Surfaceview created");
        Canvas canvas= surfaceHolder.lockCanvas();
        Screen_width=canvas.getWidth();
        Screen_height=canvas.getHeight();
        center=new PointF(Screen_width/2,Screen_height/2);//Get center of the canvas


        Log.d(TAG,"height:width="+Integer.toString(Screen_width)+":"+Integer.toString(Screen_height));
        //define map size
        map_height=6*Screen_height;
        map_width=6*Screen_width;

        //give circles for testing,every 500 pix adding one static circle
        SCircle= new ArrayList<StaticCricle>();
        for(int i=0;i<map_width/200;i++)
            for(int j=0;j<map_height/200;j++)
                SCircle.add(new StaticCricle(i*200,j*200,10,0));
        surfaceHolder.unlockCanvasAndPost(canvas);
        playground.setRunState(true);
        playground.start();


    }


    public PointF RelativeCenterLocation(StaticCricle sc){
        PointF origin=new PointF(player.x-Screen_width,player.y-Screen_height);
        float x=sc.x-origin.x,y=sc.y-origin.y;
        return new PointF(x,y);


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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            float player_x=event.getX();
            float player_y=event.getY();
            //Log.d(TAG,"before="+Float.toString(player.x)+":"+Float.toString(player.y));
            //Log.d(TAG,"on="+Float.toString(player_x)+":"+Float.toString(player_y));
            player.setNewDirection(new PointF(event.getX(), event.getY()),center);

            //Log.d(TAG,"after="+Float.toString(player.x)+":"+Float.toString(player.y));

        }
        return super.onTouchEvent(event);
    }
}
