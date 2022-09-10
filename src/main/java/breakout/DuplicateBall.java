package breakout;

import java.util.ArrayList;

public class DuplicateBall extends PowerUp {


    public DuplicateBall(){
        super();
    }
    public DuplicateBall(Block origin){
        super(origin);
    }
    public void duplicateBalls(int amount, ArrayList<Ball> balls){
        for (Ball b: balls){
            Ball newBall = new Ball(b.getRadius(), b.ballSpeed, b.getCenterX(), b.getCenterY());
            balls.add(newBall);
        }
    }
}
