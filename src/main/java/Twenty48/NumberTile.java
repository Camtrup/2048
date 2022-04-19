package Twenty48;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class NumberTile implements ITile {
    private int index;
    private String[] styles = new String[]{
        "b0a7ab",
        "eed2de",
        "e0afc4",
        "d38baa",
        "c5678f",
        "b74575",
        "94375f",
        "702a48",
        "4d1d31",
        "290f1a",
        "00FF00"
    };
    /**
     * Initialises a given tile
     */
    public NumberTile() {
        double random = Math.random();
        if(random < 0.9){
            this.index = 0;
        }
        else {
            this.index = 1;
        }
       
    }

    public int getValue() {
        return (int) Math.pow(2, index + 1);
    }

    public int getIndex() {
        return index;
    }
    /**
     * Increases the index by 1
     */
    public void increaseValue(){
        this.index++;
    }

    public void setIndex(int index) {
        if(index < 0){
            throw new IllegalArgumentException("Index cannot be less than 0");
        }
        this.index = index;
    }

    public Node getNode(int prefSize) {
        Label l = new Label(getValue() + "");
        StackPane.setAlignment(l, Pos.CENTER);
        StackPane s = new StackPane(l);
        s.setStyle("-fx-border-color: grey; -fx-border-width: 2px;-fx-background-color:#" + styles[index] + ";");
        s.setPrefHeight(prefSize);
        s.setPrefWidth(prefSize);
        return s;
    }
}
