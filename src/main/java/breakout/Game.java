package breakout;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


public class Game {
    public int screenWidth;
    public int screenHeight;
    public Text livesLeft;
    public Text currentScore;
    public int lives;
    public int score;
    public Text startText;
    public Text youWon;
    public Text youLost;
    public ArrayList<Block> myBlocks;
    public ArrayList<Block> backupBlocks;
    public int blockHeight = 20;
    public int blockWidth = 40;
    public Ball myBall;
    public Bouncer myBouncer;
    public int bouncerPos;
    public int bouncerSpeedMag;
    public boolean gamePaused = false;
    public boolean gameStarted = false;
    public boolean disableInput = false;
    public Group root;
    public int targetScore;
    public int level;
    public Text currentLevel;
    public Text newLevel;

    public Game(int ballSize, int ballSpeed, int bouncerWidth, int bouncerHeight, int bouncerStart, int bouncerSpeed, int height, int width, int lev){
        screenWidth = width;
        screenHeight = height;
        bouncerPos = bouncerStart;
        bouncerSpeedMag = bouncerSpeed;
        myBall = new Ball(ballSize, ballSpeed, screenWidth / 2, screenHeight * 3/4);
        myBouncer = new Bouncer(screenWidth / 2 - bouncerWidth / 2, bouncerStart, bouncerHeight, bouncerWidth);

        startText = setText("PRESS SPACE TO START", screenWidth / 2, screenHeight * 2 / 3);

        level = lev;
        currentLevel = setText("LEVEL " + level, screenWidth * 3 / 4, 35 );
        newLevel = setText("WELCOME TO LEVEL " + level, screenWidth / 2, screenHeight / 2);

        lives = 3;
        livesLeft = setText("LIVES: " + lives, 50, 35);

        score = 0;
        currentScore = setText( "" + score, screenWidth / 2, 35);

        youLost = setText("GAME OVER", screenWidth / 2, screenHeight / 2);

        youWon = setText("YOU WON!", screenWidth / 2, screenHeight / 2);

        backupBlocks = new ArrayList<>();
        myBlocks = new ArrayList<>();
        readLevel(1);
        root = new Group(myBall, myBouncer, livesLeft, currentScore, startText, currentLevel);
        root.getChildren().addAll(myBlocks);
    }
    private Text setText(String s, int x, int y){
        Text t = new Text(s);
        t.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        t.setX(x - t.getLayoutBounds().getWidth() / 2);
        t.setY(y);
        t.setFill(Color.WHITE);
        return t;
    }
    public void step (double elapsedTime) {
        //the step function will only run if the player has started the game or unpaused the game, indicated by the gameStart bool
        if (gameStarted){
            //the bouncer is kept in bounds
            myBouncer.checkBounds(screenWidth);
            myBouncer.moveBouncer();
            myBall.checkBounds(screenWidth, bouncerPos);
            myBall.moveBall();
            //the ball is bounced off walls and the lost ball function is called if it falls through the bottom
            //its position is constantly updated by the speed variable

            //the following functions both check if the bouncer and blocks have collided with the ball respectively
            //they only check just before the ball is at a height where it could possibly hit the blocks or paddle
            //this is to prevent the amount of unnecessary loops
            if (myBall.getCenterY() >= bouncerPos - 2 * myBall.getRadius()){
                myBall.checkForBouncerCollision(myBouncer);
            }
            if (myBall.getCenterY() < myBlocks.get(myBlocks.size() - 1).getY() + blockHeight + 2 * myBall.getRadius()){
                myBall.checkBlocksForCollision(myBlocks, root, this);
            }
            if (myBall.getCenterY() > screenHeight){
                lostBall();
            }
        }
    }

    private void readLevel(int level) {
        File file = new File("C:\\Users\\User\\IdeaProjects\\breakout\\src\\main\\resources\\breakout\\level" + level + ".txt");
        int target = 0;
        int y = 60;
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String row = sc.next();
                int x = 2;
                for (char i: row.toCharArray()){
                    if (i == '1'){
                        System.out.println("test");
                        Block block = new Block(x, y, blockHeight, blockWidth);
                        block.setFill(Color.color(Math.random(), Math.random(), Math.random()));
                        myBlocks.add(block);
                        target++;
                    }
                    x += blockWidth + 2;
                }
                y+= blockHeight + 2;
                System.out.println(row);
            }
            sc.close();
            }
        catch (FileNotFoundException e){
            e.printStackTrace();
            endLevel(youWon, true);
        }
        backupBlocks.addAll(myBlocks);
        targetScore = target;
    }
    public void endLevel(Text message, boolean endGame){
        //the end game function pauses the step function and disables user input
        gameStarted = false;
        disableInput = true;
        //it then removes every node from the scene
        root.getChildren().remove(myBouncer);
        root.getChildren().remove(myBall);
        root.getChildren().remove(livesLeft);
        root.getChildren().remove(currentScore);
        root.getChildren().removeAll(myBlocks);
        root.getChildren().remove(currentLevel);
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
//                myBlocks.addAll(backupBlocks);
//                root.getChildren().addAll(myBlocks);
                readLevel(endGame? 1: level);
                root.getChildren().addAll(myBlocks);
                level = endGame ? 1: level;
                currentLevel.setText("LEVEL " + level);
                lives = 3;
                score = endGame ? 0: score;
                livesLeft.setText("LIVES: " + lives);
                currentScore.setText("" + score);
                myBall.resetBall();
                myBouncer.resetBouncer();
        });
        delay.play();
    }
    public void lostBall() {
        myBall.resetBall();
        //losing a myBall reduces numver of lives by 1 and updates display test
        lives--;
        livesLeft.setText("LIVES: " + lives);
        //if the user runs out of lives the end game function is called
        //otherwise the animation will delay so no user input is taken
        //and the myBall is reset to original position and velocity
        if (lives == 0) {
            endLevel(youLost, true);
        }
        else {
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.play();
        }
    }
    public void scoredPoint(){
        score++;
        currentScore.setText("" + score);
        if (score == targetScore){
            level++;
            newLevel.setText("WELCOME TO LEVEL " + level);
            endLevel(newLevel, false);
        }
    }
    private void playerStarted(){
        gameStarted = !gameStarted;
        root.getChildren().remove(startText);
    }
    private void pause(){
        gamePaused = !gamePaused;
    }
    public void handleKeyInput(KeyCode code) {
        //the disableInput bool is only active when the game is displaying the winning or losing message
        if (!disableInput){
            //holding the right or left key changes the velocity of the bouncer
            //pressing space will start and stop the game as well as remove the starting text;
            switch (code) {
                case RIGHT -> myBouncer.setBouncerSpeed(bouncerSpeedMag);
                case LEFT -> myBouncer.setBouncerSpeed(-bouncerSpeedMag);
                case SPACE -> {
                    playerStarted();
                }
                case L -> {
                    level++;
                    newLevel.setText("WELCOME TO LEVEL " + level);
                    root.getChildren().remove(startText);
                    endLevel(newLevel, false);
                }
            }
        }
    }
    public void handleKeyRelease() {
        //releasing the arrow keys will stop the bouncer
        myBouncer.setBouncerSpeed(0);
    }
}
