package Twenty48;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Board {
    private int size;
    private Tile[][] boardMatrix;
    private int emptyTiles;
    private int highestScoreTile = 0;

    public Board(int size){
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

    private void addRandomTile(){
        int x = (int) Math.ceil(Math.random() * size) - 1;
        int y = (int) Math.ceil(Math.random() * size) - 1;
        if(boardMatrix[y][x] == null){
            boardMatrix[y][x] = new Tile();
            emptyTiles--;
        }
        else{
            addRandomTile();
        }
    }
    //Transposes the matrix, ie the board; Converts the columns to rows and vice versa
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

    private Tile[] reverseLine(Tile[] line){
        Collections.reverse(Arrays.asList(line));
        return line;
    }


    //Merges the tiles
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

    public void moveRight(){
        for(Tile[] line : boardMatrix){
            handleMergeLine(line);
        }
    }

    public void moveLeft(){
        for(Tile[] line : boardMatrix){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
    }

    public void moveDown(){
        for(Tile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(line);
        }
        transposeMatrix(boardMatrix);
    }

    public void moveUp(){
        for(Tile[] line : transposeMatrix(boardMatrix)){
            handleMergeLine(reverseLine(line));
            reverseLine(line);
        }
        transposeMatrix(boardMatrix);
    }


    public Tile[][] getBoardMatrix() {
        return boardMatrix;
    }

    //Returs True if the game is won or lost
    public boolean isGameOver(){
        return emptyTiles == 0 || highestScoreTile == 2048;
    }

    public void print(){
        int[][] temp = new int[size][size];
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++){
                if(boardMatrix[k][i] == null){
                    temp[k][i] = 0;
                }
                else{
                    temp[k][i] = boardMatrix[k][i].getValue();
                }
            }
        }
        System.out.println(Arrays.deepToString(temp).replace("], ", "]\n"));
    }


public static void main(String[] args) {
    Board b = new Board(5);
    b.addRandomTile();
    b.addRandomTile();
    b.print();
    System.out.println();
    b.moveUp();
    b.print();
    System.out.println();
    b.moveRight();
    b.print();
    System.out.println();
    b.moveDown();
    b.print();
    System.out.println();
    b.moveLeft();
    b.print();
}
}