package Twenty48;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {
    private Board freshBoard;


    @BeforeEach
    public void setUp(){
        freshBoard = new Board(5, new NumberTile());
    }

    @Test
    public void boardThrows(){
        assertThrows(IllegalArgumentException.class, () -> {{
            new Board(-1, new NumberTile());
        }}, "Size must be more than 0");
        
        assertThrows(IllegalArgumentException.class, () -> {{
            ITile[][] m = new ITile[5][4];
            new Board(0,m);
        }}, "Board and size does not match");
    }

    @Test
    public void setWinConditionThrows(){
        assertThrows(IllegalArgumentException.class, () -> {{
            freshBoard.setWinCondition(-1);
        }});
    }

    @Test
    public void testWin(){
        freshBoard.move("P"); //Places two tiles with value 1024 in the top-left corner
        freshBoard.move("LEFT"); //Merges the two tiles to the left, and winning the game
        assertEquals(Math.pow(2, freshBoard.getHighestScoreTile()),2048);
        assertTrue(freshBoard.isGameOver());
        assertTrue(freshBoard.isGameWon());
    }

    @Test
    public void testLoss(){
        freshBoard.move("L"); //Creates a losing board where it looks like a cheesboard
        assertEquals(freshBoard.getEmptyTiles(), 0);
        assertTrue(freshBoard.isGameOver());
        assertFalse(freshBoard.isGameWon());
    }
    
    @Test
    public void testMove(){
        ITile[][] m = new ITile[4][4];
        m[0][0] = new NumberTile();
        Board b = new Board(0, m);
        String prev = b.toString();
        for(String s : new String[]{"DOWN", "RIGHT", "UP", "LEFT"}){
            b.move(s);
            assertFalse(b.toString().equals(prev)); //Checks if the previous board is different to the moved one, i.e it has moved
            prev = b.toString();
        }

    }
    
}
