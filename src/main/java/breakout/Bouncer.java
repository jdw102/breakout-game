package breakout;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.lang.reflect.Array;
import java.util.ArrayList;


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

    public boolean checkForPowerUp(PowerUp powerUp, ArrayList<Ball> balls, Group root, int lives, ArrayList<Circle> dispLives, ArrayList<Block> blocks, Game game){
        Bounds intersect = Shape.intersect(this, powerUp).getBoundsInLocal();
        if (!intersect.isEmpty()){
            activatePowerUp(powerUp, balls, root, lives, dispLives, blocks, game);
            return true;
        }
        return false;
    }
    public void activatePowerUp(PowerUp powerUp, ArrayList<Ball> balls, Group root, int lives, ArrayList<Circle> dispLives, ArrayList<Block> blocks, Game game){
        switch (powerUp.getPowerUpType()){
            case "DuplicateBall" -> ((DuplicateBall) powerUp).duplicateBalls(2, balls, root);
            case "ExtraLife" -> ((ExtraLife) powerUp).addLife(lives, dispLives, root, game);
            case "Laser" -> ((Laser) powerUp).activateLaser(blocks, this, root, game);
            case "ScoreMultiplier" -> ((ScoreMultiplier) powerUp).multiplyScore(game);
        }
    }
}
