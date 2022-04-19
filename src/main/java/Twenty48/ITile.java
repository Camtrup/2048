package Twenty48;

import javafx.scene.Node;

public interface ITile {
    public int getValue();
    public int getIndex();
    public void setIndex(int index);
    public void increaseValue();
    public Node getNode(int prefSize);
}
