package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;

public abstract class PowerUp extends Rectangle {
    public int powerUpSpeed = 2;
    public int powerUpSize = 40;
    public Group iconRoot;

    public PowerUp(){
        this.setX(0);
        this.setY(0);
        this.setWidth(powerUpSize);
        this.setHeight(powerUpSize);
        this.setFill(Color.WHITE);
        iconRoot = new Group();
    }
    public PowerUp(Block origin){
        this.setX(origin.getX());
        this.setY(origin.getY());
        this.setWidth(powerUpSize);
        this.setHeight(powerUpSize);
        this.setFill(Color.WHITE);
        iconRoot = new Group();
    }
    public void movePowerUp(){
        this.setY(this.getY() + powerUpSpeed);
    }
    public void checkState(int bounds, ArrayList<PowerUp> powerUps, Bouncer bouncer, ArrayList<Ball> balls, Group root, int lives, ArrayList<Circle> dispLives, ArrayList<Block> blocks, Game game){
        if (bouncer.checkForPowerUp(this, balls, root, lives, dispLives, blocks, game) || outOfBounds(bounds)){
            powerUps.remove(this);
            root.getChildren().remove(this);
            iconRoot.getChildren().removeAll(iconRoot.getChildren());
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
