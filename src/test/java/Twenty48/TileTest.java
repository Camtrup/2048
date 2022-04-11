package Twenty48;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TileTest {
    private Tile tile;

    @BeforeEach
    public void setUp(){
        tile = new Tile();
    }
    @Test
    public void TestValue(){
        assertEquals(tile.getValue(), 2);
        tile.increaseValue();
        assertEquals(tile.getValue(), 2*2);
    }
}
