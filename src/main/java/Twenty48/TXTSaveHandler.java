package Twenty48;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class TXTSaveHandler implements ISaveHandler{

    //Used to get the instance of tile, good for scalability
    private Map<String, ITile> tileMap = new HashMap<String, ITile>(){{
        this.put("NumberTile", new NumberTile());
        this.put("PictureTile", new PictureTile());
    }};
    
    /**
     * Saves a board with a given name
     * @param name of the save
     * @param b board
     * @param test decides if it saves in the test file or not
     */
    public void saveBoard(Board b, String name, boolean test) throws IOException{
        for(String i : getAllSaves(test)){
            if(i.substring(0, name.length()).equals(name)){
                throw new IllegalArgumentException("Name of save is already in use, try again!");
            }
        }
        if(name.isBlank()){
            throw new IllegalArgumentException("Name must contain characters");
        }
        Writer output = new BufferedWriter(new FileWriter(getFile(test), true));
        String s = name + ";";
        s += b.getType() + ";";
        s += b.getScore() + ";";

        for(ITile[] row : b.getBoardMatrix()){
            for(ITile tile : row){
                s += (tile == null ? "0" : tile.getIndex() + 1) + ":";
            }
            s += "-";
        }
        s += "\n";
        output.append(s);
        output.close();
    }

    /**
     * Loads a board with a given name
     * @param name name of save
     * @param test decides if one loads from the test-file or not
     */
    public Board loadBoard(String name, boolean test) throws IOException{
        for(String value : getAllSaves(test)){
            if(value.substring(0, name.length()).equals(name)){
                String[] temp = value.split(";");
                
                try {
                    
                    ITile type = tileMap.get(temp[1]);
                    
                    String[] loadRows = temp[3].split("-");
                    String[] tempRow;
                    
                    ITile[][] tempMatrix = new ITile[loadRows.length][loadRows.length];
                    for(int i = 0; i < loadRows.length; i++){
                        tempRow = loadRows[i].split(":");
                        for(int k = 0; k < loadRows.length; k++){
                            int tempIndex = Integer.parseInt(tempRow[k]);
                            if(tempIndex == 0){    
                                tempMatrix[i][k] = null;
                            }
                            else{
                                tempIndex--;
                                type = (ITile) type.getClass().getConstructors()[0].newInstance();
                                type.setIndex(tempIndex);
                                tempMatrix[i][k] = type;
                            }
                        }
                    }
                    Board b = new Board(Integer.parseInt(temp[2]), tempMatrix);
                    return b;
                }catch(Exception e){
                    e.printStackTrace();
                    deleteSave(name, test);
                    throw new IllegalStateException("Save is corrupt, will be deleted");
                }
            }
        }
        throw new IllegalArgumentException("Could not find save");    
    }

    /**
     * retrieves all saves in raw format
     * @param test decides if it retrieves from test-file or not
     */
    public String[] getAllSaves(boolean test) throws IOException{
        try (BufferedReader reader = new BufferedReader(new FileReader(getFile(test)))) {
            return reader.lines().toArray(String[]::new);
        }
    }

    /**
     * Deletes save with a given name
     * @param name given name in file
     * @param test decides if in test-file or not
     */
    public void deleteSave(String name, boolean test) throws IOException{
        Writer output = new BufferedWriter(new FileWriter(getFile(test), true));
        String[] save = getAllSaves(test);
        int count = 0;
        new FileWriter(getFile(test), false).close();
        for(int i = 0; i < save.length; i++){
            if(save[i].substring(0, save[i].indexOf(";")).equals(name)){
                continue;
            }
            else{
                count++;
                output.append(save[i] + "\n");
            }
        }
        output.close();
        if(count == save.length){
            throw new IllegalArgumentException("Save was not found! Nothing was deleted");
        }
    }
    /**
     * Delivers the path to the wanted file for reading or writing
     * @param test decides if in test-file or not
     * @return filepath in a string
     */
    private String getFile(Boolean test){
        return System.getProperty("user.dir") + "/src/main/resources/Twenty48/Data/" + (test ? "test" : "data") + ".txt";
    }
}
