package com.example.shotakomiyama.project;

import java.lang.Thread;
import java.util.Random;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Paint;
import android.media.MediaPlayer;


/**
 * Created by shotakomiyama on 2017/06/13.
 */

public class gameView extends SurfaceView
       implements SurfaceHolder.Callback, Runnable{
    //systems
    private SurfaceHolder holder; //
    private Thread thread;        //

    //imgs
    private Bitmap chara_img;
    private Bitmap block_img_top;
    private Bitmap block_img_bottom;
    private Bitmap back_img;

    //values
    private boolean atari_flag;
    private int count;
    private int x;
    private int y;
    private int y_jump;
    private int block_x;
    private int block_y_top;
    private int block_y_bottom;
    private int block_v;
    private int dHeight;
    private MediaPlayer player;

    private final static int
            GAME = 0,
            GAMEOVER = 1;

    private int scene = GAME;
    /* constructor */
    public gameView(Context context){
        super(context);

        playSound();

        //forcus on to get events
        setFocusable(true);

        Paint paint = new Paint();

        holder=getHolder();
        holder.addCallback(this);
        holder.setFixedSize(getWidth(), getHeight());

        //set Resources
        Resources resources = this.getResources();
        block_img_top = BitmapFactory.decodeResource(resources, R.drawable.con_copy);
        block_img_bottom = BitmapFactory.decodeResource(resources, R.drawable.con_copy_2);
        chara_img = BitmapFactory.decodeResource(resources, R.drawable.floppy);
        back_img = BitmapFactory.decodeResource(resources, R.drawable.backg);

        count =0;
        x=200;y=0;

        atari_flag = false;
        run();
        invalidate();
    }

    //random number


    public void drawBack(Bitmap back_img, Canvas canvas){
        int bwidth = back_img.getWidth();
        int bheight = back_img.getHeight();
        Rect rctBack = new Rect(0, 0, bwidth, bheight);
        Rect dstBack = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(back_img, rctBack, dstBack, null);
    }

    /* to make surface */
    public void surfaceCreated(SurfaceHolder holder){
        //to start Thread
        thread=new Thread(this);
        thread.start();
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h){
    }

    //toDeleate Surface
    public void surfaceDestroyed(SurfaceHolder holder){
        thread=null;
    }

    //process of thread
    public void run(){
        Canvas canvas;
        Paint paint = new Paint();
        y_jump = 300;
        x=1;y=1;
        Random rnd = new Random();
        block_v = 20;
        block_x = getWidth();
        int random = rnd.nextInt(10);
        int random_bottom = 10 -random -3;

        dHeight = getHeight()/10;
        int count_t=count;
        while(thread!=null){
            if(count_t != count){
                count_t = count;
                random = rnd.nextInt(10);
                random_bottom = 10 - random -3;
            }

            canvas=holder.lockCanvas();
            canvas.drawBitmap(back_img,0,0,null);
            //block_x = getWidth();
            //block_y = getHeight() - (((getHeight()/ block_img.getHeight())/10)/2) * magnificant_height;
            drawBack(back_img, canvas);

            Rect chara_src = new Rect(0,0,chara_img.getWidth(),chara_img.getHeight());
            Rect chara_dst = new Rect(x,y,
                                     (chara_img.getWidth()/3) +x,
                                     (chara_img.getHeight()/3)+y);
            canvas.drawBitmap(chara_img,chara_src,chara_dst ,null);
            //holder.unlockCanvasAndPost(canvas);

            //canvas.drawBitmap(block_img,block_x,block_y,null);
            block_y_top = dHeight * random;
            block_y_bottom = getHeight()-dHeight * random_bottom;

            Rect src = new Rect(0, 0, block_img_top.getWidth(), block_img_top.getHeight());
            Rect dst_top = new Rect(block_x, 0, (block_img_top.getWidth() / 2) + block_x, (block_img_top.getHeight() + block_y_top));
            canvas.drawBitmap(block_img_top, src, dst_top, null);

            Rect dst_bottom = new Rect(block_x, block_y_bottom, block_img_bottom.getWidth()/2 + block_x, getHeight());
            canvas.drawBitmap(block_img_bottom,src,dst_bottom,null);
            bird_motion(chara_dst,dst_top,dst_bottom);
            block_motion(block_x);

                //bird_motion(chara_dst,dst);




            //Therefore GameOver
            if(atari_flag){
                stopSound();
                scene = GAMEOVER;
                surfaceDestroyed(holder);
                paint.setColor(Color.BLACK);
                paint.setTextSize(50);
                canvas.drawText("GAME OVER SCORE:" + count_t,getWidth()/2,getHeight()/2,paint);
            }
            holder.unlockCanvasAndPost(canvas);
            //interval
            try{
                Thread.sleep(13);
            }catch (Exception e){
            }
        }//end loop of while
    }//end run()


    private void block_motion(int present_block_x){
        block_x -= block_v;
        if(block_x == 0){
            block_x = getWidth();
            count++;
        }
    }

    private void bird_motion(Rect chara_dst,Rect dst_top, Rect dst_bottom){
        y += 30;

        if(Rect.intersects(chara_dst, dst_top)){
            atari_flag = true;
        }

        if(Rect.intersects(chara_dst, dst_bottom))
        {
            atari_flag = true;
        }

        if(y > getHeight()){
            atari_flag = true;
        }
    }

    public void playSound(){
        try{
            stopSound();
            player = MediaPlayer.create(getContext(), R.raw.nyan_cat_theme);
            player.seekTo(0);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.start();
                }
            });
        }catch(Exception e){
        }
    }

    public void stopSound(){
        try{
            if(player==null) return;

            player.stop();
            player.setOnCompletionListener(null);
            player.release();
            player=null;
        }catch(Exception e){

        }
    }

    public void onCompletion(MediaPlayer mediaPlayer){
        stopSound();
    }


    public boolean onTouchEvent(MotionEvent event){
        //kinds of touch
        int action = event.getAction();
        //touch events
        if(scene == GAME){
            switch(action&MotionEvent.ACTION_MASK){
                 case MotionEvent.ACTION_DOWN:
                     if(y != 0) y -=  y_jump;
                        break;
            }
        }
        if(scene == GAMEOVER){
            switch(action&MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    count = 0;
                    run();
                    break;
            }
        }
        return true;
    }
}//end class
