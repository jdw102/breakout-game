package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class PowerUp extends Rectangle {
    public int powerUpSpeed = 4;
    public int powerUpSize = 40;

    public PowerUp(){
        this.setX(0);
        this.setY(0);
        this.setWidth(powerUpSize);
        this.setHeight(powerUpSize);
        this.setFill(Color.WHITE);
    }
    public PowerUp(Block origin){
        this.setX(origin.getX());
        this.setY(origin.getY());
        this.setWidth(powerUpSize);
        this.setHeight(powerUpSize);
        this.setFill(Color.WHITE);
    }
    public void movePowerUp(){
        this.setY(this.getY() + powerUpSpeed);
    }
    public void checkState(int bounds, ArrayList<PowerUp> usedPowerUps, Bouncer bouncer, ArrayList<Ball> balls){
        if (bouncer.checkForPowerUp(this, balls) || outOfBounds(bounds)){
            usedPowerUps.add(this);
        }

    }
    private boolean outOfBounds(int bounds){
        if (this.getY() >= bounds + this.getHeight()){
            return true;
        }
        else return false;
    }

    public String getPowerUpType(){
        return this.getClass().getSimpleName();
    }
}
