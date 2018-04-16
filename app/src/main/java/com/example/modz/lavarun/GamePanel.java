package com.example.modz.lavarun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Modz on 3/9/2018.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {
    private final static String TAG = "GAMEPANEL";
    private GestureDetectorCompat gestureDetectorCompat;

    public static final int WIDTH = 480;
    public static final int HEIGHT = 856;
    public static int MOVESPEED = 7;
    private long boulderStartTime;
    private long boulderElapsedTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Boulder> boulders;
    private ArrayList<Boulder> boulders2;
    private ArrayList<Powerup> powerups;
    private ArrayList<Bitmap> powerupImages;
    private ArrayList<BreakableBoulder> breakable;
    private Random random=new Random();
    private Paint paint;
    private ReturnMainMenu rmmButton;
    private PlayAgain paButton;
    private ArrayList<Bitmap> boostIcons;
    private BoostContainer boostContainer;
    private Activity game;
    private int boulderY;
    private int boulder11;
    private int boulder12;
    private int boulder21;
    private int boulder22;

    private float scaleFactorX;
    private float scaleFactorY;

    public GamePanel(Context context, Activity activity){
        super(context);

        //add the callback to the surfaceHolder to interrupt the events
        getHolder().addCallback(this);
        this.gestureDetectorCompat = new GestureDetectorCompat(context,this);
        thread = new MainThread(getHolder(),this);

        game = activity;

        //make gamePanel focus so it can handle the evnts
        setFocusable(true);

    }
    //pre defined methods ng surfaceHolder na kelangan i-override
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.background2));
        player=new Player(BitmapFactory.decodeResource(getResources(),R.drawable.sprite),85,104,2);
        rmmButton=new ReturnMainMenu(BitmapFactory.decodeResource(getResources(), R.drawable.main_menu), WIDTH/3, HEIGHT/2,200,81);
        paButton = new PlayAgain(BitmapFactory.decodeResource(getResources(), R.drawable.play_again), WIDTH/3, HEIGHT/3,200,81);
        boulders=new ArrayList<Boulder>();
        boulders2=new ArrayList<Boulder>();
        breakable=new ArrayList<BreakableBoulder>();
        powerups=new ArrayList<Powerup>();
        boulderStartTime=System.nanoTime();
        powerupImages=new ArrayList<Bitmap>();
        boostIcons=new ArrayList<Bitmap>();

        // images for powerups
        powerupImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.shield));
        powerupImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.slow));
        powerupImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.x2score));

        boostIcons.add(BitmapFactory.decodeResource(getResources(), R.drawable.unlockables));
        boostIcons.add(BitmapFactory.decodeResource(getResources(), R.drawable.unlockable1));
        boostIcons.add(BitmapFactory.decodeResource(getResources(), R.drawable.unlockable2));
        boostIcons.add(BitmapFactory.decodeResource(getResources(), R.drawable.unlockable3));

        boostContainer = new BoostContainer(boostIcons);

        //safely start the gameloop

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter =0;
        //will try to stop the thread
        while (retry && counter < 1000){
            counter++;
            try {
                thread.setRunning(false);
                thread.join();

                retry = false;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void update(){

        if(player.getPlaying()) {
            bg.update();
            player.update();

            if (boulders.size() == 0) {

                boulder11 = random.nextInt(3);
                boulders.add(new Boulder(BitmapFactory.decodeResource(getResources(), R.drawable.rock),
                        boulder11, -100, 140, 152, player.getScore(), 1));

                boulder12 = random.nextInt(3);
                boulders.add(new Boulder(BitmapFactory.decodeResource(getResources(), R.drawable.rock),
                        boulder12, -100, 140, 152, player.getScore(), 1));

                boulder21 = random.nextInt(3);
                boulders2.add(new Boulder(BitmapFactory.decodeResource(getResources(), R.drawable.rock),
                        boulder21, -600, 140, 152, player.getScore(), 1));

                boulder22 = random.nextInt(3);
                boulders2.add(new Boulder(BitmapFactory.decodeResource(getResources(), R.drawable.rock),
                        boulder22, -600, 140, 152, player.getScore(), 1));

                boulderY = boulders.get(0).getY()-230;

                int chance = random.nextInt(100);
                int xpos1; //position of first breakable on line 1 of obstacles
                int xpos2; //position of second breakable on line 1 of obstacles
                int xpos3; //position of first breakable on line 2 of obstacles
                int xpos4; //position of second breakable on line 2 of obstacles

                Log.d(TAG, "update: " + chance);

                // powerup spawn 15%

                if (chance <= 14 && powerups.size() >= 0) {

                    powerups.add(new Powerup(powerupImages, random.nextInt(3), boulderY, 140, 152, player.getScore(), 1));

                }

                else if (chance <= 99 && chance >= 60) { // breakable 1

                    if (boulder12 == boulder11) {

                        if (boulder12 == 2) {

                            xpos1 = 1;
                            xpos2 = 0;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos2, -100, 140, 152, player.getScore(), 1));

                        }

                        else if (boulder12 == 0) {

                            xpos1 = 1;
                            xpos2 = 2;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos2, -100, 140, 152, player.getScore(), 1));

                        }

                        else {

                            xpos1 = 0;
                            xpos2 = 2;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos2, -100, 140, 152, player.getScore(), 1));

                        }

                    }

                    else {

                        if ((boulder12 == 1 && boulder11 == 2) || (boulder11 == 1 && boulder12 == 2)) {

                            xpos1 = 0;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));

                        }

                        else if ((boulder12 == 0 && boulder11 == 1) || (boulder11 == 0 && boulder12 == 1)) {

                            xpos1 = 2;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));

                        }

                        else {

                            xpos1 = 1;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos1, -100, 140, 152, player.getScore(), 1));

                        }

                    }

                }

                else if (chance <= 59 && chance >= 30) {

                    if (boulder22 == boulder21) {

                        if (boulder22 == 2) {

                            xpos3 = 1;
                            xpos4 = 0;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos4, -600, 140, 152, player.getScore(), 1));

                        }

                        else if (boulder22 == 0) {

                            xpos3 = 1;
                            xpos4 = 2;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos4, -600, 140, 152, player.getScore(), 1));

                        }

                        else {

                            xpos3 = 0;
                            xpos4 = 2;

                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos4, -600, 140, 152, player.getScore(), 1));

                        }

                    }

                    else {

                        if ((boulder22 == 1 && boulder21 == 2) || (boulder21 == 1 && boulder22 == 2)) {

                            xpos3 = 0;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));

                        }

                        else if ((boulder22 == 0 && boulder21 == 1) || (boulder21 == 0 && boulder22 == 1)) {

                            xpos3 = 2;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));

                        }

                        else {

                            xpos3 = 1;
                            breakable.add(new BreakableBoulder(BitmapFactory.decodeResource(getResources(), R.drawable.twig), xpos3, -600, 140, 152, player.getScore(), 1));

                        }

                    }

                }

            }

        }

        //loop the boulders and checks for collision
        for(int i = 0; i<boulders.size();i++) {

            boulders.get(i).update();

            if (collision(boulders.get(i), player)) {

                if (player.hasShield() == false) {
                    boulders2.clear();
                    boulders.clear();
                    breakable.clear();
                    powerups.clear();
                    player.removeHasPowerup();
                    player.removeHasShield();
                    player.removePowerup();
                    player.setGameover(true);
                    player.setPlaying(false);

                    break;
                }

                else if (player.hasShield()) {

                    boulders.remove(boulders.get(i));
                    player.removeHasShield();

                }

            }
            //remove boulder if it is way off the screen
            if (boulders.get(i).getY() > GamePanel.HEIGHT + 152) {
                boulders.remove(i);
                break;
            }
        }

        for(int z=0; z<boulders2.size();z++) {

            boulders2.get(z).update();

            if (collision(boulders2.get(z), player)) {

                if (player.hasShield() == false) {

                    boulders2.clear();
                    boulders.clear();
                    powerups.clear();
                    breakable.clear();
                    player.removeHasPowerup();
                    player.removeHasShield();
                    player.removePowerup();
                    player.setGameover(true);
                    player.setPlaying(false);

                    break;
                }

                else if (player.hasShield()) {

                    boulders2.remove(boulders2.get(z));
                    player.removeHasShield();

                }

            }

            //remove boulder if it is way off the screen
            if (boulders2.get(z).getY() > GamePanel.HEIGHT + 152) {
                boulders2.remove(z);
                break;
            }

        }

        for(int c=0; c<powerups.size(); c++) {

            powerups.get(c).update();

            if (collision(powerups.get(c), player)) {

                player.hasPowerup(true, powerups.get(c));
                powerups.clear();
                break;

            }

            if (powerups.get(c).getY() > GamePanel.HEIGHT + 152) {
                powerups.remove(c);
                break;
            }

        }

        for(int n=0; n<breakable.size();n++) {

            breakable.get(n).update();

            if (collision(breakable.get(n), player)) {

                if (player.hasShield() == false) {

                    boulders2.clear();
                    boulders.clear();
                    powerups.clear();
                    breakable.clear();
                    player.removeHasPowerup();
                    player.removeHasShield();
                    player.removePowerup();
                    player.setGameover(true);
                    player.setPlaying(false);

                    break;
                }

                else if (player.hasShield()) {

                    breakable.remove(breakable.get(n));
                    player.removeHasShield();

                }

            }

            //remove boulder if it is way off the screen
            if (breakable.get(n).getY() > GamePanel.HEIGHT + 152) {
                breakable.remove(n);
                break;
            }

        }

    }

    public boolean collision(GameObject a, GameObject b) {
        if(Rect.intersects(a.getRectangle(),b.getRectangle())) {
            return true;
        }
        return false;
    }

    public  void drawText(Canvas canvas, String  text){
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.DEFAULT_BOLD);

        canvas.drawText(text,WIDTH/2,HEIGHT/5,paint);

    }

    @Override
    public void draw(Canvas canvas) {
        int score = player.getTrueScore();
        //getwidth kinukuha length ng phone ng user
        scaleFactorX = getWidth()/(WIDTH*1.f);
        scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX,scaleFactorY);
            super.draw(canvas);
            bg.draw(canvas);
            player.draw(canvas);

            for (Powerup p : powerups) {
                p.draw(canvas);
            }
            for (BreakableBoulder bb : breakable) {
                bb.draw(canvas);
            }
            for (Boulder b : boulders) {
                b.draw(canvas);
            }
            for (Boulder b : boulders2) {
                b.draw(canvas);
            }

            boostContainer.draw(canvas);

            if (player.getGameover()) {
                rmmButton.draw(canvas);
                paButton.draw(canvas);
            }

            drawText(canvas, String.valueOf(score));

            canvas.restoreToCount(savedState);

        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        if(motionEvent1.getX()> motionEvent.getX()){
                if (Player.spritePos != 2) {
                    Player.spritePos++;
                }
        }

        else if(motionEvent1.getX()<motionEvent.getX()){
            //left swipe
            if (Player.spritePos != 0) {
                Player.spritePos--;
            }
        }

        return true;
    }

    public boolean onTouchEvent(MotionEvent event){
        this.gestureDetectorCompat.onTouchEvent(event);
        if(event.getAction()==MotionEvent.ACTION_DOWN){

            float px = event.getX() / scaleFactorX;
            float py = event.getY() / scaleFactorY;

            if (!player.getPlaying() && player.getGameover()) {

                // play again
                if((px>=WIDTH/3)&&(py>=HEIGHT/3)&&(px<=WIDTH/3+200) && (py<=HEIGHT+81)){
                    player.resetScore();
                    player.resetTrueScore();
                    player.setGameover(false);
                    player.setPlaying(true);
                }

                // back to main menu
                if((px>=WIDTH/3)&&(py>=HEIGHT/2)&&(px<=WIDTH/2+200) && (py<=HEIGHT+81)){
                    player.resetScore();
                    player.resetTrueScore();
                    game.finish();
                }

            }

            else if(!player.getPlaying()) {
                player.resetScore();
                player.resetTrueScore();
                player.setGameover(false);
                player.setPlaying(true);
            }

            else if (player.getPlaying()) {

                for (int i = 0; i < breakable.size(); i++) {

                    if ((px >= breakable.get(i).getX() && px <= breakable.get(i).getX() + 140) && (py >= breakable.get(i).getY() && py <= breakable.get(i).getY() + 152)) {

                        breakable.remove(breakable.get(i));

                    }

                }

            }

            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
