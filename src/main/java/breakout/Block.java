package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Block extends Rectangle {
    private double scoreMult2Chance = 0.1;
    private double scoreMult3Chance = 0.05;
    private double duplicateBallChance = 0.2;
    private double laserChance = 0.15;
    private double extraLifeChance = 0.05;


    public Block(){
        this(0,0, 20, 40);
    }
    public Block(int x, int y, int height, int width){
        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
    }

    public void destroyBlock(Game game, Group root, ArrayList<PowerUp> powerUps, ArrayList<Block> blocks, ArrayList<Ball> balls){
        root.getChildren().remove(this);
        blocks.remove(this);
        generatePowerUp(this, powerUps, root, balls);
        game.scoredPoint();
    }
    public void generatePowerUp(Block b, ArrayList<PowerUp> powerUps, Group root, ArrayList<Ball> balls){
        PowerUp pu = new ExtraLife(b);
        boolean generated = false;
        double rand = Math.random();
        double rs1 = extraLifeChance;
        double rs2 = laserChance + rs1;
        double rs3 = scoreMult2Chance + rs2;
        double rs4 = scoreMult3Chance + rs3;
        double rs5 = duplicateBallChance + rs4;

        if (rand <= rs5){
            generated = true;
        }
        if (rand <= rs1){
            pu = new ExtraLife(b);
        }
        else if (rand > rs1 && rand <= rs2){
            pu = new Laser(b);
        }
        else if (rand > rs2 && rand <= rs3 ){
            pu = new ScoreMultiplier(b, 2);
        }
        else if (rand > rs3 && rand <= rs4){
            pu = new ScoreMultiplier(b, 3);
        }
        else if (rand > rs4 && rand <= rs5){
            pu = new DuplicateBall(b);
        }
        if (generated){
            int ind = root.getChildren().indexOf(balls.get(0));
            powerUps.add(pu);
            root.getChildren().add(ind, pu);
            root.getChildren().addAll(pu.iconRoot);
        }
    }
}
