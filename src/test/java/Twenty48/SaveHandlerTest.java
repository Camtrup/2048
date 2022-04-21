package Twenty48;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SaveHandlerTest {
    private SaveHandler handler = new SaveHandler();
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

    
    @Test
    public void saveThrowsTest() throws IOException{
        assertThrows(IllegalArgumentException.class, () -> {
            handler.saveBoard(board, "test", true);
        }, "Name of save is already in use, try again!");
        
        assertThrows(IllegalArgumentException.class, () -> {
            handler.saveBoard(board, "", true);
        }, "Name must contain characters");
    }

    @Test
    public void loadSaveThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            handler.loadBoard("notTest", true);
        }, "Could not find save!");
    }

    @Test
    public void deleteSaveThrows(){
        assertThrows(IllegalArgumentException.class, () -> {
            handler.deleteSave("thisIsNotaTest", true);
        }, "Save was not found! Nothing was deleted");
    }

    @Test
    public void saveHandlerTest() throws IOException{
        handler.saveBoard(board, "test2", true);
        Board test = handler.loadBoard("test2", true);
        assertEquals(board.toString(), test.toString());
        handler.deleteSave("test2", true);
    }
}
