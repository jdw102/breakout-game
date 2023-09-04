package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ExtraLife extends PowerUp{
    public Text icon;
    public ExtraLife(){
        super();
    }
    public ExtraLife(Block b){
        super(b);
        icon = createIcon();
        iconRoot.getChildren().add(icon);
        this.setFill(Color.FORESTGREEN);
    }

    public static void addLife(int lives, ArrayList<Circle> dispLives, Group root, Game game){
        if (lives < 8){
            game.incrementLives();
            Circle last = dispLives.get(dispLives.size() - 1);
            Circle life = new Circle(last.getCenterX() + 10, 40, 3, Color.WHITE);
            dispLives.add(life);
            root.getChildren().add(life);
        }
        else{
            game.scoredPoint();
        }
    }
    private Text createIcon(){
        Text label =  new Text("+1");
        label.setX(this.getX() + this.getWidth() / 4);
        label.setY(this.getY() + this.getHeight()     * 2 /3);
        label.setFont(Font.font("comic-sans", FontWeight.BOLD, FontPosture.REGULAR, 20));
        label.setFill(Color.WHITE);
        return label;
    }
    @Override
    public void movePowerUp() {
        super.movePowerUp();
        icon.setY(icon.getY() + super.powerUpSpeed);
    }
}
