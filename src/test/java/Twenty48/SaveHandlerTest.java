package Twenty48;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SaveHandlerTest {
    private Board board;
    private SaveHandler handler = new SaveHandler();

    @BeforeEach
    public void setUp(){
        board = new Board(5);
    }

    @Test
    public void saveAndLoad() throws IOException{
        handler.saveBoard(board, "test", true);
        Board test = handler.loadBoard("test", true);
        for(int i = 0; i < test.getSize(); i++){
            for(int k = 0; k < test.getSize(); k++){
                
            }
        }
        handler.deleteSave("test", true);


    }
}
