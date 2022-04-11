package Twenty48;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.scene.input.KeyEvent;

public class Board {
    private int size;
    private Tile[][] boardMatrix;
    private int emptyTiles;
    private int highestScoreTile = 0;
    private int score;

    /**
     * Initializes the board by creating a matrix with size as dimensions
     * Also adds two tiles on the board to start the game
     * @param size of the board
     */
    public Board(int size){
        this.score = 0;
        this.size = size;
        emptyTiles = size*size;
        boardMatrix = new Tile[size][size];
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                boardMatrix[i][k] = null;
            }
        }
        addRandomTile();
        addRandomTile();
    }
    /**
     * Constructor for loading of boards from file
     * @param size size of the board
     * @param score current score
     * @param matrix the board
     */
    public Board(int size, int score, Tile[][] matrix){
        this.size = size;
        this.boardMatrix = matrix;
        this.score = score;
    }

    /**
     * Adds a random tile, used after each move by the player, to keep progresssing
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
            boardMatrix[r[0]][r[1]] = new Tile();
            emptyTiles--;
        }
    }

    private boolean canTilesMerge(){
        for (int x = 1; x < size; x++){
            for(int y = 1; y < size; y++){
                if(boardMatrix[y][x].getValue() == boardMatrix[y-1][x].getValue() || boardMatrix[y][x].getValue() == boardMatrix[y][x-1].getValue()){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Transposes the matrix, ie the board; Converts the columns to rows and vice versa
     * @param matrix i.e the board
     * @return the transposed matrix
     */
    private Tile[][] transposeMatrix(Tile[][] matrix){
        for (int i = 0; i < size; i++) {
            for (int k = i + 1; k < size; k++) {
                Tile temp = matrix[i][k];
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
    private Tile[] reverseLine(Tile[] line){
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
    private Tile[] handleMergeLine(Tile[] line){
        ArrayList<String> moved = new ArrayList<String>();
        for(int k = size - 2; k >= 0;k--){
            for(int i = k; i < size-1; i++){
                if(line[i] != null){
                    if(line[i+1] == null){
                        line[i+1] = line[i];
                        line[i] = null;
                    }
                    else if(line[i+1].getValue() == line[i].getValue() && !moved.contains("" + i + 1)){
                        line[i] = null;
                        line[i+1].increaseValue();
                        emptyTiles++;
                        highestScoreTile = (line[i+1].getValue() > highestScoreTile ? line[i+1].getValue() : highestScoreTile);
                        score += line[i+1].getValue();
                        moved.add("" + i+1);
                        break;
                    }
                    else{
                        moved.add("" + i +1);
                    }
                }
            }
        }
        return line;
    }

    /**
     * Moves and merges the tiles in the rightward direction
     */
    private void moveRight(){
        for(Tile[] line : boardMatrix){
            handleMergeLine(line);
        }
    }
    /**
     * Moves and merges the tiles in the leftward direction
     */
    private void moveLeft(){
        for(Tile[] line : boardMatrix){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
    }

    /**
     * Moves and merges the tiles in the downward direction
     */
    private void moveDown(){
        for(Tile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(line);
        }
        transposeMatrix(boardMatrix);
    }

    /**
     * Moves and merges the tiles in the upward direction
     */
    private void moveUp(){
        for(Tile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
        transposeMatrix(boardMatrix);
    }
    /**
     * Handles the moves by the user
     * @param direction a character wich defines the direction of the "move"
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
            default:
                break;
        }
        if(!temp.equals(this.toString())){
            addRandomTile();
        }
        return isGameOver();
    }

    /**
     * 
     * @return the boards matrix consisting of tiles
     */
    public Tile[][] getBoardMatrix() {
        return boardMatrix;
    }

    public int getSize() {
        return size;
    }

    public int getScore() {
        return score;
    }
    public Tile getTileValue(int x, int y){
        return boardMatrix[y][x];
    }
    public int getEmptyTiles() {
        return emptyTiles;
    }

    /**
     * Checks if the game won
     * @return a boolean where true means the game is won
     */
    public boolean isGameWon(){
        return highestScoreTile == 2048;
    }
    @Override
    public String toString() {
        String temp = "";
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                if(boardMatrix[k][i] == null){
                    temp += 0;
                }
                else{
                    temp += boardMatrix[k][i].getValue();
                }
            }
        }
        return temp;
    }

}