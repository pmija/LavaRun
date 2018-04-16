package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Modz on 3/10/2018.
 */

public class Player extends GameObject{
    private Bitmap spritesheet;
    public static int score;
    private boolean left = false;
    private boolean right = false;
    private boolean move = false;
    private boolean playing;
    private boolean gameover = false;
    public static boolean hasPowerup;
    public static Powerup currPowerup;
    private long ptime;
    private long powerupElapsed;
    private boolean hasShield;
    private int powerUpTime;
    private int trueScore;
    private int tempspeed;

    private Animation animation = new Animation();
    private long startTime;
    private ArrayList<Integer> pos = new ArrayList<>();
    public static int spritePos = 1;

    public Player(Bitmap res, int w, int h, int numFrames) {

        x = 220;
        y = GamePanel.HEIGHT-(GamePanel.HEIGHT/6);
        score = 0;
        height = h;
        width = w;

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for (int i = 0; i < image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }

        pos.add(50);
        pos.add(220);
        pos.add(360);

        animation.setFrames(image);
        animation.setDelay(100);
        startTime = System.nanoTime();

    }

    public void update()
    {
        long elapsed = (System.nanoTime()-startTime)/1000000;
        if(elapsed>100)
        {

            if (hasPowerup && currPowerup.getType() == 1) {
                tempspeed++;
                score = score / 2;
            }
            else {
                tempspeed = score;
                score++;
                tempspeed++;
            }

            if (hasPowerup && currPowerup.getType() == 2) {
                trueScore = trueScore + 3;
            }
            else {
                trueScore++;
            }

            startTime = System.nanoTime();
        }

        if (hasPowerup && currPowerup.getType() != 0) {

            ptime = System.nanoTime();
            powerupElapsed = (System.nanoTime() - ptime) / 1000000000;

            if(elapsed>100) {
                powerUpTime++;
                ptime = System.nanoTime();
            }

            if (powerUpTime == 50) {

                score = tempspeed;
                hasPowerup = false;
                powerUpTime = 0;

            }

            Log.d("time", "hasPowerup: " + powerUpTime);

        }

        animation.update();

        x = pos.get(spritePos);
    }

    public void hasPowerup(boolean x, Powerup powerup) {

        hasPowerup = x;
        currPowerup = powerup;

        if (currPowerup.getType() == 0) {

            hasShield = true;

        }

    }

    public boolean getGameover() {
        return gameover;
    }

    public void setGameover(boolean gameover) {
        this.gameover = gameover;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }
    public int getScore(){return score;}
    public int getTrueScore() {return trueScore;}
    public boolean getPlaying(){return playing;}
    public void setPlaying(boolean b){playing = b;}
    public void resetDYA(){dx = 0;}
    public boolean hasShield(){return hasShield;}
    public void setHasShield(boolean x){hasShield = x;}
    public void resetScore(){score = 0; tempspeed = 0;}
    public void resetTrueScore(){trueScore = 0;}
    public void removePowerup(){currPowerup = null;}
    public void removeHasShield(){hasShield = false;}
    public void removeHasPowerup(){hasPowerup = false;}
}