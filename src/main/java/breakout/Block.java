package breakout;

import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {


    public Block(){
        this(0,0, 20, 40);
    }
    public Block(int x, int y, int height, int width){
        this.setWidth(width);
        this.setHeight(height);
        this.setX(x);
        this.setY(y);
    }
}
