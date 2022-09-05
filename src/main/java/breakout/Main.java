package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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



/**
 * Feel free to completely change this code or delete it entirely.
 *
 * @author JERRY WORTHY
 *
 * Credit to Robert C Duvall for use of this starting code template and also the basic structure and logic of
 * the handleKeyInput function and the basic setup and start functions for the javafx program. All of which can be
 * found in his example_animation.java
 */
public class Main extends Application {

    // many resources may be in the same shared folder
    // note, leading slash means automatically start in "src/main/resources" folder
    // note, Java always uses forward slash, "/", (even for Windows)
    // Basic animation and scene information initialized
    public static final String RESOURCE_PATH = "/breakout/";
    public static final String TITLE = "Example JavaFX Animation";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


    //blocks and backup blocks initialized
    private ArrayList<Rectangle> myBlocks;
    private ArrayList<Rectangle> backupBlocks = new ArrayList<>();
    public static final int BLOCK_SIZE = 40;
    public int BlOCK_AMOUNT_PER_ROW = 500 / (BLOCK_SIZE + 10) - 2;
    public int BLOCK_ROWS = 4;

    //Bouncer initialized
    private Rectangle myBouncer;
    public static final int BOUNCER_HEIGHT = 10;
    public static final int BOUNCER_WIDTH = 70;
    public static final int BOUNCER_X_START = (SIZE + 100) / 2 - (BOUNCER_WIDTH / 2);
    public static final int BOUNCER_Y_START = SIZE - 50;
    public int BOUNCER_SPEED = 0;

    //Ball initialized
    private Circle myBall;
    public static final int BALL_SIZE = 5;
    public static final double BALL_SPEED = 4;
    public static double startAngle = 75;
    public double BALL_X_SPEED = BALL_SPEED * Math.cos(Math.toRadians(startAngle));
    public double BALL_Y_SPEED = -BALL_SPEED * Math.sin(Math.toRadians(startAngle));
    public int BALL_X_START = 245;
    public int BALL_Y_START = 300;
    public static final Paint BALL_COLOR = Color.WHITE;

    //Text, lives, and score initialized
    private int lives = 3;
    private Text livesLeft = new Text("LIVES: " + lives);
    private int score = 0;
    private Text currentScore = new Text("SCORE: " + score);
    private Text youWon = new Text("YOU WON! :D");
    private Text youLost = new Text("GAME OVER");
    public Text startText = new Text("PRESS SPACE TO START!");

    //user control booleans initialized
    public boolean gameStarted = false;
    public boolean disableInput = false;

    //root, scene, and animation initialized
    private Group root;
    private Scene myScene;
    private Timeline animation;
    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {
        //scene is created and displayed
        Scene scene = setupGame(SIZE, SIZE, Color.DARKBLUE);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();

        //the animation is created, step function is assigned to it, and the animation is played
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }
    public Scene setupGame (int width, int height, Paint background) {
        //game started is initially false so that player can start by pressing space
        gameStarted = false;

        //ball specifications
        myBall = new Circle(BALL_X_START, BALL_Y_START, BALL_SIZE);
        myBall.setFill(BALL_COLOR);

        //bouncer specifications
        myBouncer = new Rectangle(BOUNCER_X_START, BOUNCER_Y_START, BOUNCER_WIDTH, BOUNCER_HEIGHT);

        //the following creates the text for the win or lose messages, starting text, lives, and score
        livesLeft.setX(0);
        livesLeft.setY(385);
        livesLeft.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        livesLeft.setFill(Color.WHITE);

        currentScore.setX(150);
        currentScore.setY(385);
        currentScore.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        currentScore.setFill(Color.WHITE);

        youLost.setX((SIZE + 100 - startText.getLayoutBounds().getWidth())/ 2);
        youLost.setY(SIZE / 2);
        youLost.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        youLost.setFill(Color.WHITE);

        youWon.setX((SIZE + 100 - startText.getLayoutBounds().getWidth()) / 2);
        youWon.setY(SIZE / 2);
        youWon.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        youWon.setFill(Color.WHITE);

        startText.setX((SIZE - startText.getLayoutBounds().getWidth())/ 2);
        startText.setY(SIZE * 2 / 3);
        startText.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        startText.setFill(Color.WHITE);

        //blocks are created using this function
        createBlocks();

        //the root has all necessary nodes added and the scene is created
        root = new Group(myBall, myBouncer, livesLeft, currentScore, startText);
        root.getChildren().addAll(myBlocks);
        myScene = new Scene(root, width + 100, height, background);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        myScene.setOnKeyReleased(e -> handleKeyRelease());
        return myScene;
    }
    public void step (double elapsedTime) {
        //the step function will only run if the player has started the game or unpaused the game, indicated by the gameStart bool
        if (gameStarted){
            //the bouncer is kept in bounds
            if (myBouncer.getX() <= 0) {
                myBouncer.setX(0);
            }
            if (myBouncer.getX() >= myScene.getWidth() - BOUNCER_WIDTH) {
                myBouncer.setX(myScene.getWidth() - BOUNCER_WIDTH);
            }
            myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED);

            //the ball is bounced off walls and the lost ball function is called if it falls through the bottom
            //its position is constantly updated by the speed variable
            if ((myBall.getCenterX() >= SIZE + 100 - BALL_SIZE || myBall.getCenterX() <= BALL_SIZE) && myBall.getCenterY() < BOUNCER_Y_START){
                //the ball only bounces off side walls if it is above the bouncer so it cannot be trapped between the wall and bouncer
                BALL_X_SPEED = BALL_X_SPEED * -1;
            }
            if (myBall.getCenterY() <= BALL_SIZE) {
                BALL_Y_SPEED = BALL_Y_SPEED * -1;
            }
            if (myBall.getCenterY() >= SIZE + BALL_SIZE){
                lostBall();
            }
            myBall.setCenterX(myBall.getCenterX() + BALL_X_SPEED);
            myBall.setCenterY(myBall.getCenterY() + BALL_Y_SPEED);

            //the following functions both check if the bouncer and blocks have collided with the ball respectively
            //they only check just before the ball is at a height where it could possibly hit the blocks or paddle
            //this is to prevent the amount of unnecessary loops
            if (myBall.getCenterY() >= BOUNCER_Y_START - 2 * BALL_SIZE){
                checkForBouncerCollision();
            }
            if (myBall.getCenterY() < myBlocks.get(myBlocks.size() - 1).getY() + BLOCK_SIZE + 2 * BALL_SIZE){
                checkBlocksForCollision();
            }
        }

    }
    private void createBlocks(){
        myBlocks = new ArrayList<>();
        //using a staggered for loop create placement of blocks and add blocks to root
        boolean rowstagger = true;
        for (int j = 0; j < BLOCK_ROWS; j++){
            rowstagger = !rowstagger;
            int rowamount = rowstagger ? 30: 20;
            for (int i = 0; i < BlOCK_AMOUNT_PER_ROW; i++){
                Rectangle newBlock = new Rectangle(rowamount + i * 60, 20 + j * 60 ,BLOCK_SIZE, BLOCK_SIZE);
                myBlocks.add(newBlock);
            }
        }
        backupBlocks.addAll(myBlocks);
        ///copy blocks to backup variable for game reset
    }
    private void checkBlocksForCollision(){
        //every block is looped through to see if it has collided with the ball
        for (int i = 0; i < myBlocks.size(); i++){
            Rectangle block = myBlocks.get(i);
            //isBounced checks if the ball has collided and in which direction
            String direction = isBounced(myBall, block);
            if (direction != null){
                //the score and display are updated
                score++;
                currentScore.setText("SCORE: " + score);
                //after removing a block the score is checked to see if the user has won, if so the game is reset
                switch (direction){
                    //a horizontal bounce reverses the x velocity
                    case "Horizontal" ->BALL_X_SPEED = BALL_X_SPEED * -1;
                    //a vertical bounce reverses the y velocity
                    case "Vertical" -> BALL_Y_SPEED = BALL_Y_SPEED * -1;
                }
                //the block is removed from both the root and array instance variable
                root.getChildren().remove(myBlocks.get(i));
                myBlocks.remove(i);
            }
        }
        if (score == BlOCK_AMOUNT_PER_ROW * BLOCK_ROWS){
            restartGame(youWon);
        }
    }
    private void checkForBouncerCollision(){
        //isBounced determines if the ball and bouncer have collided and in what direction
        String direction = isBounced(myBall, myBouncer);
        if (direction != null){
            switch (direction){
                case "Vertical" -> {
                    // a vertical collision results in a new velocity being calculated and assigned
                    double center = myBouncer.getX() + (myBouncer.getWidth() / 2);
                    double difference = myBall.getCenterX() - center;
                    double incomingAngle = Math.atan(BALL_Y_SPEED / BALL_X_SPEED);
                    double newAngle = incomingAngle + 90 - difference;
                    // the new angle is a reflection that varies depending on how far the ball is from the center of the bouncer
                    // the velocities are reassigned with the y velocity reversing direction entirely
                    BALL_X_SPEED = BALL_SPEED * Math.cos(Math.toRadians(newAngle));
                    BALL_Y_SPEED = -1 * BALL_SPEED * Math.sin(Math.toRadians(newAngle));
                }
                case "Horizontal" -> {
                    // a horizontal collision pushes the ball away from the bouncer to prevent bugs due
                    // to continuous collisions and the x velocity is reversed
                    myBall.setCenterX(myBall.getCenterX() + 10 * Math.signum(BOUNCER_SPEED));
                    myBall.setCenterY(myBall.getCenterY() + BOUNCER_HEIGHT);
                    BALL_X_SPEED = BALL_X_SPEED * -1;
                }
            }
        }

    }
    private void handleKeyInput(KeyCode code) {
        //the disableInput bool is only active when the game is displaying the winning or losing message
        if (!disableInput){
            //holding the right or left key changes the velocity of the bouncer
            //pressing space will start and stop the game as well as remove the starting text;
            switch (code) {
                case RIGHT -> BOUNCER_SPEED = 6;
                case LEFT -> BOUNCER_SPEED = -6;
                case SPACE -> {
                    gameStarted = !gameStarted;
                    root.getChildren().remove(startText);
                }
            }
        }
    }
    private void handleKeyRelease() {
        //releasing the arrow keys will stop the bouncer
        BOUNCER_SPEED = 0;
    }
    private String isBounced (Circle a, Rectangle b) {
        //the intersect of the two shapes is found and if empty null is returned
        //a greater width than height indicates a vertical collision
        //a greater height than with indicates a horizontal collision
        Bounds intersect = Shape.intersect(a, b).getBoundsInLocal();
        if (! intersect.isEmpty()){
            if (intersect.getHeight() < intersect.getWidth()){
                return "Vertical";
            }
            else if (intersect.getHeight() > intersect.getWidth()){
                return "Horizontal";
            }
        };
        return null;
    }
    private void lostBall(){
        //losing a ball reduces numver of lives by 1 and updates display test
        lives--;
        livesLeft.setText("LIVES: " + lives);
        //if the user runs out of lives the end game function is called
        //otherwise the animation will delay so no user input is taken
        //and the ball is reset to original position and velocity
        if (lives == 0){
            restartGame(youLost);
        }
        else{
            animation.setDelay(Duration.seconds(1));
            animation.playFromStart();
            myBall.setCenterX(BALL_X_START);
            myBall.setCenterY(BALL_Y_START);
            BALL_X_SPEED = BALL_SPEED * Math.cos(Math.toRadians(startAngle));
            BALL_Y_SPEED = -BALL_SPEED * Math.sin(Math.toRadians(startAngle));
        }

    }
    /**
     * Start the program.
     */
    public void restartGame(Text message){
        //the end game function pauses the step function and disables user input
        gameStarted = false;
        disableInput = true;
        //it then removes every node from the scene
        root.getChildren().remove(myBouncer);
        root.getChildren().remove(myBall);
        root.getChildren().remove(livesLeft);
        root.getChildren().remove(currentScore);
        root.getChildren().removeAll(myBlocks);
        //a pause transition is created to show the message and on finish it performs a cleanup
        //to reset the game to starting form
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
                myBouncer.setX(BOUNCER_X_START);
                BALL_X_SPEED = BALL_SPEED * Math.cos(Math.toRadians(startAngle));
                BALL_Y_SPEED = -BALL_SPEED * Math.sin(Math.toRadians(startAngle));
            });
        delay.play();
    }

    public static void main (String[] args) {
        launch(args);
    }
}
