package breakout;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

import static breakout.Main.BOARD_HEIGHT;
import static breakout.Main.myGame;


public class Ball extends Circle{
    public int ballSize;
    public double ballSpeed;
    public double startAngle = 75;
    public double ballXSpeed;
    public double ballYSpeed;
    public int ballXStart;
    public int ballYStart;
    public static final Paint BALL_COLOR = Color.WHITE;

    public Ball(){
        this(5, 4, 245, 300);
    }
    public Ball(int size, double speed, int startx, int starty){
        ballSize = size;
        ballSpeed = speed;
        ballXSpeed = ballSpeed * Math.cos(Math.toRadians(startAngle));
        ballYSpeed = -ballSpeed * Math.sin(Math.toRadians(startAngle));
        ballXStart = startx;
        ballYStart = starty;
        this.setRadius(size);
        this.setCenterX(ballXStart);
        this.setCenterY(ballYStart);
        this.setFill(BALL_COLOR);
    }

    public void moveBall(){
        this.setCenterX(this.getCenterX() + ballXSpeed);
        this.setCenterY(this.getCenterY() + ballYSpeed);
    }

    public void checkBounds(int width, int bouncerpos){
        if ((this.getCenterX() >= width - ballSize || this.getCenterX() <= ballSize) && this.getCenterY() < bouncerpos){
            //the ball only bounces off side walls if it is above the bouncer so it cannot be trapped between the wall and bouncer
            ballXSpeed *= -1;
        }
        if (this.getCenterY() <= ballSize + BOARD_HEIGHT) {
            ballYSpeed *= -1;
        }
    }
    public void checkBlocksForCollision(ArrayList<Block> blocks, Group root, Game game){
        //every block is looped through to see if it has collided with the ball
        for (int i = 0; i < blocks.size(); i++){
            Rectangle block = blocks.get(i);
            //isBounced checks if the ball has collided and in which direction
            String direction = isBounced(block);
            if (direction != null){
                //the score and display are updated
                //after removing a block the score is checked to see if the user has won, if so the game is reset
                switch (direction){
                    //a horizontal bounce reverses the x velocity
                    case "Horizontal" -> ballXSpeed = ballXSpeed * -1;
                    //a vertical bounce reverses the y velocity
                    case "Vertical" -> ballYSpeed = ballYSpeed * -1;
                }
                //the block is removed from both the root and array instance variable
                root.getChildren().remove(blocks.get(i));
                blocks.remove(i);
                game.scoredPoint();
            }
        }
    }
    public void checkForBouncerCollision(Bouncer b){
        //isBounced determines if the ball and bouncer have collided and in what direction
        String direction = isBounced(b);
        if (direction != null){
            switch (direction){
                case "Vertical" -> {
                    // a vertical collision results in a new velocity being calculated and assigned
                    double center = b.getX() + (b.getWidth() / 2);
                    double difference = this.getCenterX() - center;
                    double incomingAngle = Math.atan(ballYSpeed / ballXSpeed);
                    double newAngle = incomingAngle + 90 - difference;
                    // the new angle is a reflection that varies depending on how far the ball is from the center of the bouncer
                    // the velocities are reassigned with the y velocity reversing direction entirely
                    ballXSpeed = ballSpeed * Math.cos(Math.toRadians(newAngle));
                    ballYSpeed = -1 * ballSpeed * Math.sin(Math.toRadians(newAngle));
                }
                case "Horizontal" -> {
                    // a horizontal collision pushes the ball away from the bouncer to prevent bugs due
                    // to continuous collisions and the x velocity is reversed
                    this.setCenterX(this.getCenterX() + 10 * Math.signum(b.bouncerSpeed));
                    this.setCenterY(this.getCenterY() + b.getHeight());
                    ballXSpeed = ballXSpeed * -1;
                }
            }
        }
    }
    private String isBounced (Shape b) {
        //the intersection of the two shapes is found and if empty null is returned
        //a greater width than height indicates a vertical collision
        //a greater height than with indicates a horizontal collision
        Bounds intersect = Shape.intersect(this, b).getBoundsInLocal();
        if (!intersect.isEmpty()) {
            if (intersect.getHeight() < intersect.getWidth()) {
                return "Vertical";
            } else if (intersect.getHeight() > intersect.getWidth()) {
                return "Horizontal";
            }
        }
        return null;
    }
    public void resetBall(){
        this.setCenterX(ballXStart);
        this.setCenterY(ballYStart);
        ballXSpeed = ballSpeed * Math.cos(Math.toRadians(startAngle));
        ballYSpeed = -ballSpeed * Math.sin(Math.toRadians(startAngle));
    }

}
