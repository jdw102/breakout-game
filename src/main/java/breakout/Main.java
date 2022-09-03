package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Feel free to completely change this code or delete it entirely.
 *
 * @author YOUR NAME HERE
 */
public class Main extends Application {
    // useful names for constant values used
    public static final String TITLE = "Example JavaFX Animation";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int BALL_SIZE = 5;
    public static final int BLOCK_SIZE = 40;
    public static final int BlOCK_AMOUNT = 500 / (BLOCK_SIZE + 10);
    public static final int BOUNCER_HEIGHT = 10;
    public static final int BOUNCER_WIDTH = 50;
    public static final int BOUNCER_SPEED = 16;
    public int BALL_X_SPEED = 2;
    public int BALL_Y_SPEED = -2;
    public static final Paint BALL_COLOR = Color.WHITE;
    // many resources may be in the same shared folder
    // note, leading slash means automatically start in "src/main/resources" folder
    // note, Java always uses forward slash, "/", (even for Windows)
    public static final String RESOURCE_PATH = "/breakout/";
//    public static final String BALL_IMAGE = RESOURCE_PATH + "ball.gif";
    private Circle myBall;
    private ArrayList<Rectangle> myBlocks;
    private Rectangle myBouncer;
    private Rectangle myBlock;
    private Scene myScene;
    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {
//        ImageView ball = new ImageView(new Image(BALL_IMAGE));
//        ball.setFitWidth(SIZE);
//        ball.setFitHeight(SIZE);
//        ball.setX(SIZE / 2 - ball.getBoundsInLocal().getWidth() / 2);
//        ball.setY(SIZE / 2 - ball.getBoundsInLocal().getHeight() / 2);


        Scene scene = setupGame(SIZE, SIZE, Color.DARKBLUE);
        stage.setScene(scene);

        stage.setTitle(TITLE);
        stage.show();
///COPIED FROM EXAMPLE
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }
    public Scene setupGame (int width, int height, Paint background) {
        myBall = new Circle(245, 300, BALL_SIZE);
        myBall.setFill(BALL_COLOR);

        myBouncer = new Rectangle(225, 350, BOUNCER_WIDTH, BOUNCER_HEIGHT);

        myBlocks = new ArrayList<>();

        for (int j = 0; j < 3; j++){
            for (int i = 0; i < BlOCK_AMOUNT; i++){
                Rectangle newBlock = new Rectangle(5 + i * 50, 20 + j * 60 ,BLOCK_SIZE, BLOCK_SIZE);
                myBlocks.add(newBlock);
            }
        }

        Group root = new Group(myBall, myBouncer);
        root.getChildren().addAll(myBlocks);
        myScene = new Scene(root, width + 100, height, background);

        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));

        return myScene;
    }
    public void step (double elapsedTime) {
        if (myBall.getCenterX() >= 495 || myBall.getCenterX() <= 5){
            BALL_X_SPEED = BALL_X_SPEED * -1;
        }
        if (myBall.getCenterY() >= 395 || myBall.getCenterY() <= 5){
            BALL_Y_SPEED = BALL_Y_SPEED * -1;
        }
        myBall.setCenterX(myBall.getCenterX() + BALL_X_SPEED);
        myBall.setCenterY(myBall.getCenterY() + BALL_Y_SPEED);
        if (myBouncer.getX() <= 0) {
            myBouncer.setX(0);
        }
        if (myBouncer.getX() >= 450) {
            myBouncer.setX(450);
        }

        if (isBounced(myBall, myBouncer) == "Vertical") {
            BALL_Y_SPEED = BALL_Y_SPEED * -1;
        }
        if (isBounced(myBall, myBouncer) == "Horizontal") {
            BALL_X_SPEED = BALL_X_SPEED * -1;
        }
        for (int i = 2; i < ((Group) myScene.getRoot()).getChildren().size(); i++){
            Rectangle block = (Rectangle) ((Group) myScene.getRoot()).getChildren().get(i);
            if (isBounced(myBall, block) == "Vertical"){
                ((Group) myScene.getRoot()).getChildren().remove(i);
                BALL_Y_SPEED = BALL_Y_SPEED * -1;
            }
            else if (isBounced(myBall, block) == "Horizontal"){
                ((Group) myScene.getRoot()).getChildren().remove(i);
                BALL_X_SPEED = BALL_X_SPEED * -1;
            }
        }
    }
    private void handleKeyInput(KeyCode code) {
        switch (code) {
            case RIGHT -> myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED);
            case LEFT -> myBouncer.setX(myBouncer.getX() - BOUNCER_SPEED);
        }
    }

    private String isBounced (Circle a, Rectangle b) {
        Bounds intersect = Shape.intersect(a, b).getBoundsInLocal();
        if (! intersect.isEmpty()){
            System.out.println(Shape.intersect(a, b).getBoundsInLocal().getHeight());
            if (intersect.getHeight() < intersect.getWidth()){
                return "Vertical";
            }
            else return "Horizontal";
        }
        else return null;
    }
    /**
     * Start the program.
     */

    public static void main (String[] args) {
        launch(args);
    }
}
