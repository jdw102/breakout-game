package breakout;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ScoreMultiplier extends PowerUp{
    public int multiplier;
    public Text icon;

    public ScoreMultiplier(){
        super();
    }
    public ScoreMultiplier(Block b, int amount){
        super(b);
        multiplier = amount;
        icon = createIcon();
        super.iconRoot.getChildren().add(icon);
        switch (amount){
            case 2 -> this.setFill(Color.SILVER);
            case 3 -> this.setFill(Color.GOLD);
        }
    }
    private Text createIcon(){
        Text label =  new Text("x" + multiplier);
        label.setX(this.getX() + this.getWidth() / 4);
        label.setY(this.getY() + this.getHeight()     * 2 /3);
        label.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        label.setFill(Color.WHITE);
        return label;
    }
    public void multiplyScore(Game game){
        if (game.scoreMultiplier < game.scoreMultiplier * multiplier){
            game.root.getChildren().remove(icon);
            PauseTransition delay = new PauseTransition(Duration.seconds(10));
            game.scoreMultiplier *= multiplier;
            delay.setOnFinished(e ->{
                game.scoreMultiplier = game.level;
            });
            delay.play();
        }
        else{
            game.scoredPoint();
        }
    }

    @Override
    public void movePowerUp() {
        super.movePowerUp();
        icon.setY(icon.getY() + super.powerUpSpeed);
    }
}
