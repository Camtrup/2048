package Twenty48;

public class NumberTile implements ITile {
    private int value;
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
    public NumberTile(int value, int index){
        this.value = value;
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public String getStyle(){
        return "-fx-background-color:#" + styles[index];
    }
    /**
     * Multiplies the value of the tile by 2 and increases the index by 1
     */
    public void increaseValue(){
        this.value *= 2;
        this.index++;
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
