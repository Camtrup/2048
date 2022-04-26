package Twenty48;

import java.io.IOException;

public interface ISaveHandler {
    public void saveBoard(Board b, String name, boolean test) throws IOException;
    public Board loadBoard(String name, boolean test) throws IOException;
    public void deleteSave(String name, boolean test) throws IOException;
    public String[] getAllSaves(boolean test) throws IOException;
    
}
