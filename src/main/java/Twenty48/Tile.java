package Twenty48;

public class Tile implements ITile {
    private int value;
    private int index;
    private String[] colors = new String[]{
        "#fbf6f8",
        "eed2de",
        "e0afc4",
        "d38baa",
        "c5678f",
        "b74575",
        "94375f",
        "702a48",
        "4d1d31",
        "290f1a"
    };
    /**
     * Initizlises a given tile
     */
    public Tile() {
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

    public Tile(int value, int index){
        this.value = value;
        this.index = index;
    }
    /**
     * 
     * @return the value of the tile
     */
    public int getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public String getColor(){
        return colors[index];
    }
    /**
     * Multiplies the value of the tile by 2
     */
    public void increaseValue(){
        this.value *= 2;
        this.index++;
    }
}
