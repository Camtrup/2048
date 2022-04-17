package Twenty48;

import java.io.File;

public class PictureTile implements ITile {

    private int index;
    private int value;
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
            this.value = 2;
            this.index = 0;
        }
        else {
            this.value = 4;
            this.index = 1;
        }
       
    }

    /**
     * intialises a given tile, used for loading from save
     * @param value value of the tile
     * @param index index in the styles-array
     */
    public PictureTile(int value, int index){
        setIndex(index);
        this.value = value;
    }

    public String getStyle() {
        String image = System.getProperty("user.dir") + "/src/main/resources/Pictures/" + styles[index] + ".jpg";
        return "-fx-background-image: url('" + image + "');-fx-background-position: center center;-fx-background-repeat: stretch;";
    }



    public int getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public void increaseValue() {
        value *= 2;
        index++;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setIndex(int index) {
        if(index < 0){
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        this.index = index;
    }
    
}
