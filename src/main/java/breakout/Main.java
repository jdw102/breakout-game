package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


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
    public int BlOCK_AMOUNT = 500 / (BLOCK_SIZE + 10);
    public int BLOCK_ROWS = 1;
    public static final int BOUNCER_HEIGHT = 10;
    public static final int BOUNCER_WIDTH = 70;
    public int BOUNCER_SPEED = 0;
    public int BALL_X_SPEED = 2;
    public int BALL_Y_SPEED = -2;
    public int BALL_X_START = 245;
    public int BALL_Y_START = 300;
    public boolean disableInput = false;
    public static final Paint BALL_COLOR = Color.WHITE;
    // many resources may be in the same shared folder
    // note, leading slash means automatically start in "src/main/resources" folder
    // note, Java always uses forward slash, "/", (even for Windows)
    public static final String RESOURCE_PATH = "/breakout/";
//    public static final String BALL_IMAGE = RESOURCE_PATH + "ball.gif";
    private Circle myBall;
    private ArrayList<Rectangle> myBlocks;
    private ArrayList<Rectangle> backupBlocks = new ArrayList<>();
    private Rectangle myBouncer;
    private Scene myScene;
    private Group root;
    private int lives = 3;
    private Text livesLeft = new Text("LIVES: " + lives);
    private int score = 0;
    private Text currentScore = new Text("SCORE: " + score);
    private Text youWon = new Text("YOU WON! :D");
    private Text youLost = new Text("GAME OVER");
    public boolean gameStarted;
    public Text startText = new Text("PRESS SPACE TO START!");
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
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY, animation)));
        animation.play();
    }
    public Scene setupGame (int width, int height, Paint background) {
        gameStarted = false;
        myBall = new Circle(BALL_X_START, BALL_Y_START, BALL_SIZE);
        myBall.setFill(BALL_COLOR);

        myBouncer = new Rectangle(225, 350, BOUNCER_WIDTH, BOUNCER_HEIGHT);

        myBlocks = new ArrayList<>();

        livesLeft.setX(0);
        livesLeft.setY(385);
        livesLeft.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        livesLeft.setFill(Color.WHITE);

        currentScore.setX(150);
        currentScore.setY(385);
        currentScore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        currentScore.setFill(Color.WHITE);

        youLost.setX((SIZE - 50)/ 2);
        youLost.setY(SIZE / 2);
        youLost.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        youLost.setFill(Color.WHITE);

        youWon.setX((SIZE - 50) / 2);
        youWon.setY(SIZE / 2);
        youWon.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        youWon.setFill(Color.WHITE);

        startText.setX((SIZE - 150)/ 2);
        startText.setY(SIZE * 2 / 3);
        startText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        startText.setFill(Color.WHITE);

        for (int j = 0; j < BLOCK_ROWS; j++){
            for (int i = 0; i < BlOCK_AMOUNT; i++){
                Rectangle newBlock = new Rectangle(5 + i * 50, 20 + j * 60 ,BLOCK_SIZE, BLOCK_SIZE);
                myBlocks.add(newBlock);
            }
        }
        backupBlocks.addAll(myBlocks);

        root = new Group(myBall, myBouncer, livesLeft, currentScore, startText);
        root.getChildren().addAll(myBlocks);
        myScene = new Scene(root, width + 100, height, background);

        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));
        return myScene;
    }
    public void step (double elapsedTime, Timeline animation) {
        if (gameStarted){
            if (myBall.getCenterX() >= 495 || myBall.getCenterX() <= 5){
                BALL_X_SPEED = BALL_X_SPEED * -1;
            }
            if (myBall.getCenterY() <= 5) {
                BALL_Y_SPEED = BALL_Y_SPEED * -1;
            }
            if (myBall.getCenterY() >= 405){
                lostBall(animation);
            }
            myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED);
            myBall.setCenterX(myBall.getCenterX() + BALL_X_SPEED);
            myBall.setCenterY(myBall.getCenterY() + BALL_Y_SPEED);
            if (myBouncer.getX() <= 0) {
                myBouncer.setX(0);
            }
            if (myBouncer.getX() >= myScene.getWidth() - BOUNCER_WIDTH) {
                myBouncer.setX(myScene.getWidth() - BOUNCER_WIDTH);
            }

            if (isBounced(myBall, myBouncer) == "Vertical") {
                myBall.setCenterY(myBall.getCenterY() + BALL_Y_SPEED * -1);
                BALL_Y_SPEED = BALL_Y_SPEED * -1;
            }
            if (isBounced(myBall, myBouncer) == "Horizontal") {
                myBall.setCenterX(myBall.getCenterX() + BOUNCER_SPEED);
                myBall.setCenterY(myBall.getCenterY() + BOUNCER_HEIGHT);
                BALL_X_SPEED = BALL_X_SPEED * -1;
            }
            if (isBounced(myBall, myBouncer) == "Diagonal"){
                myBall.setCenterX(myBall.getCenterX() + -1 * BALL_X_SPEED);
                myBall.setCenterY(myBall.getCenterY() + -1 * BALL_Y_SPEED);
                BALL_Y_SPEED = BALL_Y_SPEED * -1;
                BALL_X_SPEED = BALL_X_SPEED * -1;
            }
            for (int i = 0; i < myBlocks.size(); i++){
                Rectangle block = myBlocks.get(i);
                String direction = isBounced(myBall, block);
                if (direction != null){
                    score++;
                    currentScore.setText("SCORE: " + score);
                    root.getChildren().remove(myBlocks.get(i));
                    myBlocks.remove(i);
                    switch (direction){
                        case "Horizontal" ->BALL_X_SPEED = BALL_X_SPEED * -1;
                        case "Vertical" -> BALL_Y_SPEED = BALL_Y_SPEED * -1;
                        case "Diagonal" -> {BALL_X_SPEED = BALL_X_SPEED * -1; BALL_Y_SPEED = BALL_Y_SPEED * -1;}
                    }
                }
            }
            if (score == BlOCK_AMOUNT * BLOCK_ROWS){
                restartGame(animation, youWon);

            }
        }

    }
    private void handleKeyInput(KeyCode code) {
        if (!disableInput){
            switch (code) {
                case RIGHT -> BOUNCER_SPEED = 6;
                case LEFT -> BOUNCER_SPEED = -6;
                case SPACE -> {gameStarted = !gameStarted; root.getChildren().remove(startText);}
            }
        }
    }
    private void handleKeyRelease(KeyCode code) {
        switch (code) {
            case RIGHT -> BOUNCER_SPEED = 0;
            case LEFT -> BOUNCER_SPEED = 0;
        }
    }
    private String isBounced (Circle a, Rectangle b) {
        Bounds intersect = Shape.intersect(a, b).getBoundsInLocal();
        if (! intersect.isEmpty()){
            if (intersect.getHeight() < intersect.getWidth()){
                return "Vertical";
            }
            else if (intersect.getHeight() > intersect.getWidth()){
                return "Horizontal";
            }
            else return "Diagonal";
        };
        return null;
    }
    private void lostBall(Timeline animation){
        lives--;
        livesLeft.setText("LIVES: " + lives);
        if (lives == 0){
            restartGame(animation, youLost);
        }
        else{
            animation.setDelay(Duration.seconds(1));
            animation.playFromStart();
            myBall.setCenterX(245);
            myBall.setCenterY(300);
            BALL_Y_SPEED = -2;
        }

    }
    /**
     * Start the program.
     */
    public void restartGame(Timeline animation, Text message){
        gameStarted = false;
        disableInput = true;
        root.getChildren().remove(myBouncer);
        root.getChildren().remove(myBall);
        root.getChildren().remove(livesLeft);
        root.getChildren().remove(currentScore);
        root.getChildren().removeAll(myBlocks);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        root.getChildren().add(message);
        delay.setOnFinished(e ->
            {
                root.getChildren().remove(message);
                root.getChildren().add(startText);
                disableInput = false;
                root.getChildren().add(myBouncer);
                root.getChildren().add(myBall);
                root.getChildren().add(currentScore);
                root.getChildren().add(livesLeft);
                myBlocks.removeAll(myBlocks);
                myBlocks.addAll(backupBlocks);
                root.getChildren().addAll(myBlocks);
                lives = 3;
                score = 0;
                livesLeft.setText("LIVES: " + lives);
                currentScore.setText("SCORE: " + score);
                myBall.setCenterX(BALL_X_START);
                myBall.setCenterY(BALL_Y_START);
                BALL_Y_SPEED = -1 * BALL_Y_SPEED;
            });
        delay.play();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
