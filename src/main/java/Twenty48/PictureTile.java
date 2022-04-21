package Twenty48;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PictureTile implements ITile {

    private int index;
    private String[] styles = new String[]{
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    };

    public PictureTile() {
        double random = Math.random();
        if(random < 0.9){
            this.index = 0;
        }
        else {
            this.index = 1;
        }
    }

    public Node getNode(int prefSize) {
        String image = System.getProperty("user.dir") + "/src/main/resources/Pictures/" + styles[index] + ".jpg";
        ImageView img = new ImageView(new Image(new File(image).toURI().toString()));
        img.setFitHeight(prefSize);
        img.setFitWidth(prefSize);
        img.prefHeight(prefSize);
        img.prefWidth(prefSize);
        return img;
    }



    public int getValue() {
        return (int) Math.pow(2, index + 1);
    }

    public int getIndex() {
        return index;
    }

    public void increaseValue() {
        if(index == styles.length - 1){
            throw new IllegalArgumentException("Maximum value reached");
        }
        index++;
    }

    public void setIndex(int index) {
        if(index < 0){
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        if (index >= styles.length){
            throw new IllegalArgumentException("Index cannot be more than" + (styles.length - 1));
        }
        this.index = index;
    }
    
}
