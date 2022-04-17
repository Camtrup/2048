package Twenty48;

public interface ITile {
    public String getStyle();
    public int getValue();
    public int getIndex();
    public void increaseValue();
    public void setValue(int value);
    public void setIndex(int index);
}
