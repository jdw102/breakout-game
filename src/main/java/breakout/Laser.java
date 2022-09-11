package breakout;

import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static breakout.Main.BOARD_HEIGHT;

public class Laser extends PowerUp {
    public ArrayList<Circle> icon;
    public Laser(){
        super();
    }
    public Laser(Block b){
        super(b);
        this.setFill(Color.SLATEGRAY);
        icon = createIcon(b);
        iconRoot.getChildren().addAll(icon);
    }
    public void activateLaser(ArrayList<Block> blocks, Bouncer bouncer, Group root, Game game){
        PauseTransition delay = new PauseTransition(Duration.seconds(0.25));
        ArrayList<Rectangle> laser = createLaser(bouncer);
        root.getChildren().addAll(laser);
        for (int i = 0; i < blocks.size(); i++){
            Block b = blocks.get(i);
            if (laserHit(b, laser.get(0))){
                disintegrateBlock(b, game, root, blocks);
            }
        }
        delay.setOnFinished(e ->{
            root.getChildren().removeAll(laser);
        });
        delay.play();
    }

    private ArrayList<Rectangle> createLaser(Bouncer bouncer){
        ArrayList<Rectangle> laser = new ArrayList<>();
        double originWidth = bouncer.getWidth() / 3;
        double originX = bouncer.getX() + bouncer.getWidth() / 2 -  originWidth / 2;
        double originHeight = 10;
        double originY = bouncer.getY() - originHeight - 10;
        double laserHeight = originY - 50;
        double laserWidth = bouncer.getWidth() / 5;
        double laserX = bouncer.getX() + bouncer.getWidth() / 2 -  laserWidth / 2;
        double laserY = 50;
        double innerWidth = laserWidth / 2;
        double innerX = bouncer.getX() + bouncer.getWidth() / 2 -  innerWidth / 2;
        Rectangle laserBeam = new Rectangle(laserX, laserY, laserWidth, laserHeight);
        Rectangle laserInner = new Rectangle(innerX, laserY, innerWidth, laserHeight);
        Rectangle laserOrigin = new Rectangle(originX, originY, originWidth, originHeight);
        laserOrigin.setFill(Color.GRAY);
        laserBeam.setFill(Color.RED);
        laserInner.setFill(Color.ORANGERED);
        laser.add(laserBeam);
        laser.add(laserInner);
        laser.add(laserOrigin);
        return laser;
    }
    private boolean laserHit(Block b, Rectangle beam){
        Bounds intersect = Shape.intersect(beam, b).getBoundsInLocal();
        if (!intersect.isEmpty()) return true;
        else return false;
    }
    private void disintegrateBlock(Block b, Game game, Group root, ArrayList<Block> blocks){
        blocks.remove(b);
        root.getChildren().remove(b);
        game.scoredPoint();
    }
    private ArrayList<Circle> createIcon(Block b){
        ArrayList<Circle> i = new ArrayList<>();
        double x = b.getX() + b.getWidth() / 2;
        double y = b.getY() + b.getHeight();
        Circle outer = new Circle(x, y, 10, Color.DARKGRAY);
        Circle inner1 = new Circle(x, y, 6, Color.RED);
        Circle inner2 = new Circle(x, y, 4, Color.ORANGERED);
        i.add(outer);
        i.add(inner1);
        i.add(inner2);
        return i;
    }
    public void movePowerUp() {
        super.movePowerUp();
        for (Circle c: icon){
            c.setCenterY(c.getCenterY() + super.powerUpSpeed);
        }
    }
}
