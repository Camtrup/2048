package Twenty48;

public class Tile {
    private int value;
    public Tile() {
        this.value = 2;
    }
    public int getValue() {
        return value;
    }
    public void increaseValue(){
        this.value *= 2;
    }
}
