package breakout;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class Game {
    public int lastLevel = 3;
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
    public Bouncer myBouncer;
    public int bouncerPos;
    public int bouncerSpeedMag;
    public boolean gamePaused = false;
    public boolean gameStarted = false;
    public boolean disableInput = false;
    public ArrayList<Circle> displayedLives;
    public Group root;
    public int level;
    public int highScore;
    public Text displayHighScore;
    public Text currentLevel;
    public Text newLevel;
    public ArrayList<Integer> highestScores;
    public Text finalScoreText;
    public ArrayList<PowerUp> powerUps;
    public ArrayList<PowerUp> usedPowerUps;
    public ArrayList<Ball> myBalls;
    public Ball startBall;
    public ArrayList<Ball> lostBalls;

    public Game(int ballSize, int ballSpeed, int bouncerWidth, int bouncerHeight, int bouncerStart, int bouncerSpeed, int height, int width, int lev){
        screenWidth = width;
        screenHeight = height;
        bouncerPos = bouncerStart;
        bouncerSpeedMag = bouncerSpeed;
        lostBalls = new ArrayList<>();
        myBalls = new ArrayList<>();
        startBall = new Ball(ballSize, ballSpeed, screenWidth / 2, bouncerStart - ballSize);
        myBalls.add(startBall);
        myBouncer = new Bouncer(screenWidth / 2 - bouncerWidth / 2, bouncerStart, bouncerHeight, bouncerWidth);
        usedPowerUps = new ArrayList<>();

        startText = setText("PRESS SPACE TO START\n\n DESTROY THE BRICKS\n\n DON'T LOSE THE BALL", screenWidth / 2, screenHeight  * 5 / 8);

        level = lev;
        currentLevel = setText("LEVEL " + level, screenWidth - 70, 30 );
        newLevel = setText("WELCOME TO LEVEL " + level, screenWidth / 2, screenHeight / 2);

        score = 0;
        currentScore = setText( "" + score, screenWidth / 2, 30);

        highScore = getHighScore();
        displayHighScore = setText("HS: " + highScore, 45, 30);

        youLost = setText("GAME OVER", screenWidth / 2, screenHeight / 2);

        youWon = setText("YOU WON!", screenWidth / 2, screenHeight / 2);

        backupBlocks = new ArrayList<>();
        myBlocks = new ArrayList<>();
        powerUps = new ArrayList<>();
        root = new Group(myBouncer, currentScore, startText, currentLevel, displayHighScore);
        root.getChildren().addAll(myBalls);
        readLevel(1);
        lives = 3;
        setLives(lives);
    }
    private void setLives(int num){
        displayedLives = new ArrayList<>();
        int x = screenWidth - 80;
        int y = 40;
        for (int i = 0; i < num; i++){
            Circle life = new Circle(x, y, 3, Color.WHITE);
            displayedLives.add(life);
            x+= 10;
        }
        root.getChildren().addAll(displayedLives);
    }
    private void removeLife(){
        lives--;
        Circle last = displayedLives.get(displayedLives.size() - 1);
        displayedLives.remove(last);
        root.getChildren().remove(last);
        if (lives == 0) {
            enterScore(score);
            resetGame(youLost, 1, 0);
        }
        else {
            startBall.resetBall();
            myBalls.add(startBall);
            root.getChildren().addAll(myBalls);
        }
    }
    private void addLife(){
        lives++;
        Circle last = displayedLives.get(displayedLives.size() - 1);
        Circle life = new Circle(last.getCenterX() + 10, 40, 3, Color.WHITE);
        displayedLives.add(life);
        root.getChildren().add(life);
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

            for (Ball b: myBalls){
                b.checkBounds(screenWidth, bouncerPos);
                b.moveBall();
                if (b.getCenterY() >= bouncerPos - 2 * b.getRadius()){
                    b.checkForBouncerCollision(myBouncer);
                }
                if (b.getCenterY() < myBlocks.get(myBlocks.size() - 1).getY() + blockHeight + 2 * b.getRadius()){
                    b.checkBlocksForCollision(myBlocks, powerUps, root, this);
                }
                if (b.getCenterY() > screenHeight + b.getRadius()){
                    lostBalls.add(b);
                }
            }
            myBalls.removeAll(lostBalls);
            root.getChildren().removeAll(lostBalls);
            lostBalls.removeAll(lostBalls);
            if (myBalls.size() == 0){
                removeLife();
            }

            //the ball is bounced off walls and the lost ball function is called if it falls through the bottom
            //its position is constantly updated by the speed variable

            //the following functions both check if the bouncer and blocks have collided with the ball respectively
            //they only check just before the ball is at a height where it could possibly hit the blocks or paddle
            //this is to prevent the amount of unnecessary loops
            for (PowerUp pu: powerUps){
                pu.movePowerUp();
                pu.checkState(screenHeight, usedPowerUps, myBouncer, myBalls);
            }
            powerUps.removeAll(usedPowerUps);
            root.getChildren().removeAll(usedPowerUps);
            usedPowerUps.removeAll(usedPowerUps);
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
                        Block block = new Block(x, y, blockHeight, blockWidth);
                        block.setFill(Color.color(Math.random() * 0.5 + 0.5, Math.random() * 0.5 + 0.5, Math.random() * 0.4 + 0.2));
                        myBlocks.add(block);
                        target++;
                    }
                    x += blockWidth + 2;
                }
                y+= blockHeight + 2;
                System.out.println(row);
            }
            root.getChildren().addAll(myBlocks);
            sc.close();
            }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        backupBlocks.addAll(myBlocks);
    }
    public void endGame(Text message){

    }

    public void scoredPoint(){
        score++;
        currentScore.setText("" + score);
        if (myBlocks.size() == 0){
            if (level == lastLevel){
                enterScore(score);
                resetGame(youWon, 1, 0);
            }
            else{
                level++;
                jumpToLevel(level);
            }
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
            if (code.isDigitKey()){
                int l = Math.min(lastLevel, Integer.parseInt(code.getChar()));
                jumpToLevel(l);
            }
            else{
                switch (code) {
                    case RIGHT -> myBouncer.setBouncerSpeed(bouncerSpeedMag);
                    case LEFT -> myBouncer.setBouncerSpeed(-bouncerSpeedMag);
                    case SPACE -> {
                        playerStarted();
                    }
                    case L -> {
                        addLife();
                    }
                    case R -> {
                        for (Ball b: myBalls) {b.resetBall();}
                        myBouncer.resetBouncer();
                    }
                }
            }

        }
    }
    public void handleKeyRelease() {
        //releasing the arrow keys will stop the bouncer
        myBouncer.setBouncerSpeed(0);
    }
    public void jumpToLevel(int lev){
        level = lev;
        newLevel.setText("WELCOME TO LEVEL " + level);
        resetGame(newLevel, lev, score);
    }
    public void clearScreen(){
        root.getChildren().removeAll(myBlocks);
        root.getChildren().removeAll(myBalls);
        root.getChildren().remove(startText);
        root.getChildren().remove(myBouncer);
        root.getChildren().removeAll(displayedLives);
        root.getChildren().remove(currentLevel);
        root.getChildren().remove(currentScore);
    }
    public void resetGame(Text message, int l, int s){
        clearScreen();
        root.getChildren().add(message);
        if (finalScoreText != null){
            root.getChildren().add(finalScoreText);
        }
        disableInput = true;
        gameStarted = false;
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> {
            root.getChildren().remove(finalScoreText);
            root.getChildren().remove(message);
            root.getChildren().add(startText);
            disableInput = false;
            root.getChildren().add(myBouncer);

            root.getChildren().add(currentScore);
            myBlocks.removeAll(myBlocks);
            readLevel(l);
            level = l;
            currentLevel.setText("LEVEL " + level);
            root.getChildren().add(currentLevel);
            lives = 3;
            setLives(lives);
            score = s;
            currentScore.setText("" + score);

            myBouncer.resetBouncer();
            highScore = getHighScore();
            displayHighScore.setText("HS: " + highScore);
            youWon.setText("YOU WON!");
            youLost.setText("YOU LOST!");
            finalScoreText = null;
        });
        delay.play();
    }
    public int getHighScore(){
        File file = new File("C:\\Users\\User\\IdeaProjects\\breakout\\src\\main\\resources\\breakout\\highscores.txt");
        highestScores = new ArrayList<>();
        int first = 0;
        try {
            Scanner sc = new Scanner(file);
            first = Integer.parseInt(sc.next());
            highestScores.add(first);
            while(sc.hasNext()){
                highestScores.add(Integer.parseInt(sc.next()));
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return first;
    }
    public void enterScore(int score){
        highestScores.add(score);
        Collections.sort(highestScores, Collections.reverseOrder());
        int pos = highestScores.indexOf(score);
        if (pos == 0){
            finalScoreText = setText("NEW HIGH SCORE!", screenWidth / 2, screenHeight * 2 / 3);
        }
        else if (pos < 10){
            finalScoreText = setText("TOP 10 SCORE!", screenWidth / 2, screenHeight * 2 / 3);
        }
        else{
            finalScoreText = null;
        }
        System.out.println(highestScores.toString());
        File file = new File("C:\\Users\\User\\IdeaProjects\\breakout\\src\\main\\resources\\breakout\\highscores.txt");
        file.delete();
        File newFile = new File("C:\\Users\\User\\IdeaProjects\\breakout\\src\\main\\resources\\breakout\\highscores.txt");
        PrintWriter write = null;
        try {
            write = new PrintWriter(newFile, "UTF-8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < Math.min(10, highestScores.size()); i++){
            write.println(highestScores.get(i));
            System.out.println("test");
        }
        write.close();
    }
}
