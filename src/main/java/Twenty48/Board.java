package Twenty48;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board {
    private int size;
    private ITile[][] boardMatrix;
    private int emptyTiles;
    private int highestScoreTile = 0;
    private int score;
    private int winCondition = 10; //The index of which the user wins
    private ITile type;

    /**
     * Initializes the board by creating a matrix with size as dimensions
     * Also adds two tiles on the board to start the game
     * @param size of the board
     * @param type of tyle, used to add new random tile
     */
    public Board(int size, ITile type){
        this.type = type;
        this.score = 0;
        this.size = size;
        emptyTiles = size*size;
        boardMatrix = new ITile[size][size];
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                boardMatrix[i][k] = null;
            }
        }
        addRandomTile();
        addRandomTile();
    }
    /**
     * Constructor for loading boards from file
     * @param score current score
     * @param matrix the board
     */
    public Board(int score, ITile[][] matrix){
        this.size = matrix.length;
        this.score = score;
        this.emptyTiles = 0;
        for(ITile[] row : matrix){
            if(row.length != size){
                throw new IllegalArgumentException("Board and size does not match");
            }
            for(ITile tile : row){
                if(tile == null){
                    emptyTiles++;
                }
                else {
                    type = tile;
                    highestScoreTile = tile.getIndex() > highestScoreTile ? tile.getIndex() : highestScoreTile;
                }
            }
        }
        this.boardMatrix = matrix;
    }

    /**
     * Adds a random tile, used after each move by the player, to keep progresssing
     * Iterates the board and adds empty coordinates to a list, 
     * then picks one of these at random to add a new Tile
     */
    private void addRandomTile(){
        ArrayList<int[]> temp = new ArrayList<int[]>();
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                if(boardMatrix[k][i] == null){
                    temp.add(new int[]{k,i});
                }
            }
        }
        if(!temp.isEmpty()){
            int[] r = temp.get((int) Math.floor(Math.random() * temp.size()));
            try {
                boardMatrix[r[0]][r[1]] = (ITile) type.getClass().getConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e) {
                e.printStackTrace();
            }
            emptyTiles--;
            
        }
        else {
            throw new IllegalArgumentException("Board is full, unable to add another tile");
        }
    }

    /**
     * Is fired when the emptyTiles is 0, i.e the board is full of tiles.
     * checks if any tiles can merge, if not the game is lost
     * @return true if any tiles can merge, false if not
     */
    private boolean canTilesMerge(){
        for (int x = 0; x < size; x++){
            for(int y = 0; y < size; y++){
                if(x + 1 < size){
                    if(boardMatrix[y][x].getValue() == boardMatrix[y][x+1].getValue()){
                        return true;
                    }
                }
                if(y + 1 < size){
                    if(boardMatrix[y][x].getValue() == boardMatrix[y+1][x].getValue()){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Transposes the matrix, ie the board; Converts the columns to rows and vice versa
     * @param matrix the board
     * @return the transposed matrix
     */
    private ITile[][] transposeMatrix(ITile[][] matrix){
        for (int i = 0; i < size; i++) {
            for (int k = i + 1; k < size; k++) {
                ITile temp = matrix[i][k];
                matrix[i][k] = matrix[k][i];
                matrix[k][i] = temp;
            }
        }
        return matrix;
    }
    /**
     * Reversea a given line
     * @param line in the matrix
     * @return the reversed line of tiles
     */
    private ITile[] reverseLine(ITile[] line){
        Collections.reverse(Arrays.asList(line));
        return line;
    }
    public boolean isGameOver(){
        if(emptyTiles == 0){
            return !canTilesMerge();
        }
        return isGameWon();
    }
    /**
     * Merges the tiles by pulling them to the edge. two tiles merge if they are the same value
     * @param line of tiles; a line in the matrix
     * @return A line in the matrix where the tiles are merged from left to right
     */
    private void handleMergeLine(ITile[] line){
        ArrayList<String> moved = new ArrayList<String>();
        for(int k = size - 2; k >= 0;k--){
            for(int i = k; i < size-1; i++){
                if(line[i] != null){
                    if(line[i+1] == null){
                        line[i+1] = line[i];
                        line[i] = null;
                    }
                    else if(line[i+1].getValue() == line[i].getValue() && !moved.contains("" + i)){
                        line[i] = null;
                        line[i+1].increaseValue();
                        emptyTiles++;
                        highestScoreTile = (line[i+1].getIndex() > highestScoreTile ? line[i+1].getIndex() : highestScoreTile);
                        score += line[i+1].getValue();
                        moved.add("" + i);
                        break;
                    }
                    else{
                        moved.add("" + i);
                    }
                }
            }
        }
    }

    /**
     * Moves and merges the tiles in the rightward direction
     */
    private void moveRight(){
        for(ITile[] line : boardMatrix){
            handleMergeLine(line);
        }
    }
    /**
     * Moves and merges the tiles in the leftward direction
     */
    private void moveLeft(){
        for(ITile[] line : boardMatrix){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
    }

    /**
     * Moves and merges the tiles in the downward direction
     */
    private void moveDown(){
        for(ITile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(line);
        }
        transposeMatrix(boardMatrix);
    }

    /**
     * Moves and merges the tiles in the upward direction
     */
    private void moveUp(){
        for(ITile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
        transposeMatrix(boardMatrix);
    }
    /**
     * Handles the moves by the user
     * Also handles random-tile-adding if the board has changed after a move
     * Includes a cheatCode and lossCode for fun (and testing-purposes)
     * @param direction a character wich defines the direction of the "move"
     * @return a boolean which notifies the controller if the game is over or not
     */
    public boolean move(String direction){
        String temp = this.toString();
        switch(direction){
            case "DOWN", "S":
                moveDown();
                break;
            case "UP", "W":
                moveUp();
                break;
            case "LEFT", "A":
                moveLeft();
                break;
            case "RIGHT", "D":
                moveRight();
                break;
            case "P":
                cheadCode();
                break;
            case "L":
                lossCode();
                break;
            default:
                throw new IllegalArgumentException("Not a valid input");
        }
        if(!temp.equals(this.toString())){
            addRandomTile();
        }
        return isGameOver();
    }

    /**
     * Used to generate a winning board by placing two tiles of value 1024 in the top-right corner
     */
    private void cheadCode(){
        ITile[][] tempMatrix = new ITile[size][size];
        ITile tempTile1 = new NumberTile();
        ITile tempTile2 = new NumberTile();
        tempTile2.setIndex(9);
        tempTile1.setIndex(9);
        tempMatrix[0][0] = tempTile1;
        tempMatrix[0][1] = tempTile2;
        boardMatrix = tempMatrix;
    }

    /**
     * Used to generate a losing board
     */
    private void lossCode(){
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                try {
                    ITile tempTile = (ITile) type.getClass().getConstructors()[0].newInstance();
                    if(i % 2 == 0){
                        tempTile.setIndex(k % 2 == 0 ? 2 : 3);
                    }
                    else{
                        tempTile.setIndex(k % 2 == 0 ? 3 : 2);
                    }
                    boardMatrix[i][k] = tempTile;
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        boardMatrix[0][0] = null;
        emptyTiles = 1;
    }

    public ITile[][] getBoardMatrix() {
        return boardMatrix;
    }

    public int getSize() {
        return size;
    }

    public int getScore() {
        return score;
    }
    public ITile getTileValue(int x, int y){
        return boardMatrix[y][x];
    }
    public int getEmptyTiles() {
        return emptyTiles;
    }
    public String getType() {
        return type.getClass().getSimpleName();
    }

    /**
     * Checks if the game won
     * @return a boolean where true means the game is won
     */
    public boolean isGameWon(){
        return highestScoreTile == winCondition;
    }
    public int getWinCondition() {
        return winCondition;
    }
    /**
     * Returns the highest indexed tile. 
     * Is +1 because of 0-indexing
     * @return highest index
     */
    public int getHighestScoreTile() {
        return highestScoreTile + 1;
    }
    public void setWinCondition(int winCondition) {
        this.winCondition = winCondition;
    }

    @Override
    public String toString() {
        String temp = "";
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                if(boardMatrix[i][k] == null){
                    temp += 0 + " ";
                }
                else{
                    temp += boardMatrix[i][k].getValue() + " ";
                }
            }
            temp += "\n";
        }
        return temp;
    }
}