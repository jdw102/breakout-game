package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;


import static breakout.Game.*;


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
    public static final int SCREEN_HEIGHT = 600;
    public static final int SCREEN_WIDTH = 506;
    public static final int BOARD_HEIGHT = 50;
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;


    public int BALL_SIZE = 5;
    public int BALL_SPEED = 4;
    public int BOUNCER_WIDTH = 70;
    public int BOUNCER_HEIGHT = 10;
    public int BOUNCER_OFFSET = 0;
    public int BOUNCER_SPEED = 6;


    //root, scene, and animation initialized
    private Scene myScene;
    private Timeline animation;
    public static Game myGame;
    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {
        //scene is created and displayed
        Scene scene = setupGame(SCREEN_WIDTH, SCREEN_HEIGHT, Color.color(0, 0.2, 0.4));
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();

        //the animation is created, step function is assigned to it, and the animation is played
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> myGame.step(SECOND_DELAY)));
        animation.play();
    }
    public Scene setupGame (int width, int height, Paint background) {
        //game started is initially false so that player can start by pressing space
        myGame = new Game(BALL_SIZE, BALL_SPEED, BOUNCER_WIDTH, BOUNCER_HEIGHT, SCREEN_HEIGHT - BOARD_HEIGHT - BOUNCER_OFFSET, BOUNCER_SPEED, SCREEN_HEIGHT, SCREEN_WIDTH, 1);
        //the root has all necessary nodes added and the scene is created
        Rectangle infoBoard = new Rectangle(0, 0, SCREEN_WIDTH, BOARD_HEIGHT);
        infoBoard.setFill(Color.BLACK);
        myGame.root.getChildren().add(0, infoBoard);
        myScene = new Scene(myGame.root, width, height, background);
        myScene.setOnKeyPressed(e -> myGame.handleKeyInput(e.getCode()));
        myScene.setOnKeyReleased(e -> myGame.handleKeyRelease());
        return myScene;
    }


    /**
     * Start the program.
     */


    public static void main (String[] args) {
        launch(args);
    }
}
