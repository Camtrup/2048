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
    /**
     * Test the board-constructor for invalid inputs
     */
    @Test
    public void boardThrows(){
        assertThrows(IllegalArgumentException.class, () -> {{
            new Board(-1, new NumberTile());
        }}, "Size must be more than 2");

        assertThrows(IllegalArgumentException.class, () -> {{
            new Board(1, new NumberTile());
        }}, "Size must be more than 2");

        assertThrows(IllegalArgumentException.class, () -> {{
            ITile[][] m = new ITile[5][4];
            new Board(0,m);
        }}, "Board and size does not match");
    }

    /**
     * Tests setter for invalid input
     */
    @Test
    public void setWinConditionThrows(){
        assertThrows(IllegalArgumentException.class, () -> {{
            freshBoard.setWinCondition(-1);
        }});
    }

    /**
     * Tests for a given win when a tile reaches 2048.
     * "P" generates the winning board, and the a move to the left gives a win
     * 
     */
    @Test
    public void testWin(){
        freshBoard.move("P"); //Places two tiles with value 1024 in the top-left corner
        freshBoard.move("LEFT"); //Merges the two tiles to the left, and winning the game
        assertEquals(Math.pow(2, freshBoard.getHighestScoreTile()),2048);
        assertTrue(freshBoard.isGameOver());
        assertTrue(freshBoard.isGameWon());
    }

    /**
     * Tests for a loss
     * creates a losing board and checks if the game is over
     * The cjeck if the game is lost, or not won
     */
    @Test
    public void testLoss(){
        freshBoard.move("L"); //Creates a losing board where it looks like a cheesboard
        assertEquals(freshBoard.getEmptyTiles(), 0);
        assertTrue(freshBoard.isGameOver());
        assertFalse(freshBoard.isGameWon());
    }
    
    /**
     * Tests for moving in all directions by placing a tile in the top left corner
     * and then simulating user-input.
     * Checks by confirming that board is different for each move.
     * Also check if the tile has increased value, either by being in the same place or on the board
     * if another tile has come infront of it. Thereby testing the merging of tiles aswell
     */
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
        assertTrue(b.getTile(0, 0).getIndex() > 0 || b.getHighestScoreTile() > 0);

    }
    
}
