package breakout;

import javafx.scene.shape.Rectangle;



public class Bouncer extends Rectangle {
    public int bouncerHeight;
    public int bouncerWidth;
    public int bouncerXStart;
    public int bouncerYStart;
    public int bouncerSpeed;

    public Bouncer(){
        this(0, 0, 10, 70);
    }
    public Bouncer(int xstart, int ystart, int height, int width){
        bouncerXStart = xstart;
        bouncerYStart = ystart;
        bouncerHeight = height;
        bouncerWidth = width;
        bouncerSpeed = 0;
        this.setX(bouncerXStart);
        this.setY(bouncerYStart);
        this.setHeight(bouncerHeight);
        this.setWidth(bouncerWidth);
    }

    public void moveBouncer(){
        this.setX(this.getX() + bouncerSpeed);
    }
    public void checkBounds(int screenWidth){
        if (this.getX() <= 0) {
            this.setX(0);
        }
        if (this.getX() >= screenWidth - this.getWidth()) {
            this.setX(screenWidth - this.getWidth());
        }
    }

    public void setBouncerSpeed(int speed){
        bouncerSpeed = speed;
    }

    public void resetBouncer(){
        this.setX(bouncerXStart);
        bouncerSpeed = 0;
    }
}
