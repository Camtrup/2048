package Twenty48;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SaveHandlerTest {
    private TXTSaveHandler handler = new TXTSaveHandler();
    private Board board;

    @BeforeEach
    public void setUp() throws IOException{
        board = new Board(5, new NumberTile());
        handler.saveBoard(board, "test", true);
    }

    @AfterEach 
    public void deleteSave() throws IOException{
        handler.deleteSave("test", true);
    }

    /**
     * Tests saveBoard for invalid input, such as similar names and blank name
     * @throws IOException
     */    
    @Test
    public void saveThrowsTest() throws IOException{
        assertThrows(IllegalArgumentException.class, () -> {
            handler.saveBoard(board, "test", true);
        }, "Name of save is already in use, try again!");
        
        assertThrows(IllegalArgumentException.class, () -> {
            handler.saveBoard(board, "", true);
        }, "Name must contain characters");
    }
    /**
     * Tests if loading throws when the save doesnt exist
     */
    @Test
    public void loadSaveThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            handler.loadBoard("notTest", true);
        }, "Could not find save!");
    }
    /**
     * Tests if deleting throws when save doesnt exist
     */
    @Test
    public void deleteSaveThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            handler.deleteSave("thisIsNotaTest", true);
        }, "Save was not found! Nothing was deleted");
    }
    /**
     * Tests if loading a saved board returns in the same state
     * @throws IOException
     */
    @Test
    public void saveHandlerTest() throws IOException{
        handler.saveBoard(board, "test2", true);
        Board test = handler.loadBoard("test2", true);
        assertEquals(board.getSize(), test.getSize());
        assertEquals(board.getType(), board.getType());
        assertEquals(board.toString(), test.toString());
        handler.deleteSave("test2", true);
    }
}
