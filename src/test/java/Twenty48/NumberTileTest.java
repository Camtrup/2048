package Twenty48;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.Node;

public class NumberTileTest {

    private NumberTile tile;

    @BeforeEach
    public void setup(){
        tile = new NumberTile();
    }

    @Test
    public void tileThrows(){
        assertThrows(IllegalArgumentException.class, () -> {{
            tile.setIndex(-1);
        }});

        assertThrows(IllegalArgumentException.class, () -> {{
            tile.setIndex(100);
        }});
        
        assertThrows(IllegalArgumentException.class, () -> {{
            while(true){
                tile.increaseValue();
            }
        }});
    }

    @Test
    public void tileTest(){
        tile.setIndex(4);
        assertEquals(tile.getIndex(), 4);
        assertEquals(tile.getValue(), Math.pow(2, 4 + 1));
        
        tile.increaseValue();
        assertEquals(tile.getIndex(), 5);
        assertEquals(tile.getValue(), Math.pow(2, 5 + 1));
    }
    
}
