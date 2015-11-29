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

import com.game.junglelaw.circle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is used for rendering the view of game drawing the points in the PlayGround class
 * PlayGround instance is included in this view class, then we need to pass this view class to
 * PlayGround class in its constructor for drawing.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
    private int DEFAULT_PLAYER_RADIUS=50;
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
    //private float player_on_screen_radius=30;
    private int map_height,map_width;
    List<StaticCircle> SCircle;
    List<MovableCircle> MCircle;
    /**-----------**/
    public int getScreen_height(){return Screen_height;}
    public int getScreen_width(){return Screen_width;}
    public int getMap_height(){
        return map_height;
    }
    public int getMap_width(){
        return map_width;
    }
    public GameView(Context context) {
        super(context);
        playground=new PlayGround(this);
        holder=getHolder();
        holder.addCallback(this);
        player=new PlayerCircle(1000,1000,DEFAULT_PLAYER_RADIUS,0);
        Log.d(TAG,"GameView created");
    }
    public void drawSCircleList(List<StaticCircle> list,Canvas canvas){
        Paint p=new Paint();
        for(int i=0;i<list.size();i++){
            StaticCircle sc=list.get(i);
            //if(inScreen(sc)){
            PointF circle_center= RelativeCenterLocation(sc);
            p.setColor(sc.getColor());
            canvas.drawCircle(circle_center.x, circle_center.y,RelativeScreenSize(sc),p);
            //}
        }
    }
    public void drawMCircleList(List<MovableCircle> list,Canvas canvas){
        Paint p=new Paint();
        for(int i=0;i<list.size();i++){
            MovableCircle sc=list.get(i);
            //if(inScreen(sc)){
            PointF circle_center= RelativeCenterLocation(sc);
            p.setColor(sc.getColor());
            canvas.drawCircle(circle_center.x, circle_center.y,RelativeScreenSize(sc),p);
            //}
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p=new Paint();
        p.setColor(Color.BLACK);
        canvas.drawColor(Color.WHITE);
        canvas.drawCircle(center.x, center.y, player.player_on_screen_radius, p);
        player.moveToDirection(getMap_width(),getMap_height());
        SCircle=playground.getManger().ProvideStatic();
        MCircle=playground.getManger().ProvideMovable();
        drawSCircleList(SCircle,canvas);
        drawMCircleList(MCircle,canvas);

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
        map_height=2*Screen_height;
        map_width=2*Screen_width;

        playground.getManger().setSize(map_width,map_height);
        playground.getManger().ProvideMovable().add(player);
        surfaceHolder.unlockCanvasAndPost(canvas);
        playground.setRunState(true);
        playground.start();


    }
    public boolean inScreen(AbstractCircle sc){
        if (Math.abs(sc.x-player.x)<=sc.getRadius()+Screen_width/2+10 & Math.abs(sc.y-player.y)<=sc.getRadius()+Screen_height/2+10)
            return true;
        return false;

    }

    public float RelativeScreenSize(AbstractCircle circle){
        return circle.getRadius()*player.zoom_rate;

    }
    public PointF RelativeCenterLocation(AbstractCircle sc){
        float zoom=player.zoom_rate;
        float x=(sc.x-player.x)*zoom;
        float y=(sc.y-player.y)*zoom;
        return new PointF(x+Screen_width/2,y+Screen_height/2);
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
