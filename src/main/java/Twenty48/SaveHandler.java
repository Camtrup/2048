package Twenty48;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class SaveHandler{
    
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
        s += b.getSize() + ";";
        s += b.getScore() + ";";
        for(ITile[] row : b.getBoardMatrix()){
            for(ITile tile : row){
                s += (tile == null ? "0_0" : tile.getValue() + "_" + tile.getIndex()) + ":";
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
                    int size = Integer.parseInt(temp[2]);
                    ITile[][] tempMatrix = new ITile[size][size];
                    
                    String[] loadRows = temp[4].split("-");
                    String[] tempRow;
                    ITile type = null;
                    int highest = 0;
                    int emptyTiles = 0;
                    
                    for(int i = 0; i < size; i++){
                        tempRow = loadRows[i].split(":");
                        for(int k = 0; k < size; k++){
                            String[] valIndex = tempRow[k].split("_");
                            int val = Integer.parseInt(valIndex[0]);
                            highest = highest < val ? val : highest;
                            type = temp[1].equals(NumberTile.class.getSimpleName()) ? new NumberTile() : new PictureTile();
                            type.setValue(val);
                            type.setIndex(Integer.parseInt(valIndex[1]));
                            if(valIndex[0].equals("0")){
                                type = null;    
                                emptyTiles++;
                            }
                            tempMatrix[i][k] = type;
                        }
                    }
                    Board b = new Board(size, Integer.parseInt(temp[3]), tempMatrix, type);
                    b.setHighestScoreTile(highest);
                    b.setEmptyTiles(emptyTiles);
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
        new FileWriter(getFile(test), false).close();
        for(int i = 0; i < save.length; i++){
            if(save[i].substring(0, save[i].indexOf(";")).equals(name)){
                continue;
            }
            else{
                output.append(save[i] + "\n");
            }
        }
        output.close();
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
