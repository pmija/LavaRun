package com.example.modz.lavarun;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Patrick on 3/23/2018.
 */

public class Powerup extends GameObject {

    private int score;
    private int speed;
    private Bitmap sprite;
    private Animation animation = new Animation() ;
    private ArrayList<Integer> pos = new ArrayList<>();
    private ArrayList<Bitmap> images; // represent image based on powerup
    private int type;

    public Powerup(ArrayList<Bitmap> images, int x, int y, int w, int h, int score, int numFrames) {

        super.x = x;
        super.y = y;
        width = w;
        height = h;
        this.score = score;
        //this.speed = 7 + (int)(score/30);

        this.images = images;

        Random rand = new Random();
        int random = rand.nextInt(3);
        type = random;

        /* "type" is the type of power up:
         *
         * 0 is SHIELD
         * 1 is SLOW
         * 2 is 2x SCORE
         *
         * */

        sprite = images.get(random);
        Bitmap[] image = new Bitmap[numFrames];

        for(int i = 0; i<image.length;i++) {
            image[i] = Bitmap.createBitmap(sprite, 0, 0, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);

        pos.add(10);
        pos.add(180);
        pos.add(320);

        this.x = pos.get(x);

    }

    public void update() {
        speed = 7 + (int) (Player.score/30);
        y+=speed;
        animation.update();

    }

    public void draw(Canvas canvas) {

        try{
            canvas.drawBitmap(animation.getImage(), x, y,null);
        }catch(Exception e){}

    }

    public int getWidth() {
        //offset slightly for more realistic collision detection
        return width-10;

    }

    public int getY() {
        return y;
    }

    public int getType() { return type; }

}
