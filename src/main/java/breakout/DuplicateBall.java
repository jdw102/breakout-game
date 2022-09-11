package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class DuplicateBall extends PowerUp {
    public ArrayList<Circle> icon;

    public DuplicateBall(){
        super();
    }
    public DuplicateBall(Block b){
        super(b);
        icon = createIcon(b);
        iconRoot.getChildren().addAll(icon);
        this.setFill(Color.SLATEGRAY);
    }
    public void duplicateBalls(int amount, ArrayList<Ball> balls, Group root){
        ArrayList<Ball> newBalls = new ArrayList<>();
        for (Ball b: balls){
            Ball newBall = new Ball(b.getRadius(), b.ballSpeed, b.getCenterX(), b.getCenterY());
            newBalls.add(newBall);
        }
        balls.addAll(newBalls);
        root.getChildren().addAll(newBalls);
    }
    public ArrayList<Circle> createIcon(Block b){
        ArrayList<Circle> i = new ArrayList<>();
        double disp = b.getWidth() / 8;
        Circle c1out = new Circle(b.getX() + b.getWidth() / 2 + disp, b.getY() + b.getHeight() +disp, 6);
        Circle c2out = new Circle(b.getX() + b.getWidth() / 2 - disp, b.getY() + b.getHeight() - disp, 6);
        Circle c1in = new Circle(b.getX() + b.getWidth() / 2 + disp, b.getY() + b.getHeight() +disp, 4);
        c1in.setFill(Color.WHITE);
        Circle c2in = new Circle(b.getX() + b.getWidth() / 2 - disp, b.getY() + b.getHeight() - disp, 4);
        c2in.setFill(Color.WHITE);
        i.add(c1out);
        i.add(c2out);
        i.add(c1in);
        i.add(c2in);
        return i;
    }
    public void movePowerUp() {
        super.movePowerUp();
        for (Circle c: icon){
            c.setCenterY(c.getCenterY() + super.powerUpSpeed);
        }

    }
}
