package Twenty48;

import java.io.IOException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Twenty48Controller {
    private Board board;
    private int tileSize;
    private int boardSize;
    private SaveHandler saveHandler = new SaveHandler();

    EventHandler<KeyEvent> keyPress =  (e -> {
            boolean over = board.move(e.getCode().toString());
            drawGrid();
            if(over){
                gameOver(board.isGameWon());
            }
        });
    
    @FXML
    private Pane boardPane;

    @FXML
    private Button saveButton;

    /**
     * Produces the screen for either starting a new game with the respective sizes 3,4 or 5.
     * Also gives the oppurtunity to load a saved game, or delete it.
     * Alerts the user if anyting goes wrong with the loading of the saved games, but does not crash the application
     */
    public void newGame(){
        try{
            boardPane.getScene().removeEventFilter(KeyEvent.KEY_PRESSED, keyPress);
        } catch (Exception e){}
        saveButton.setVisible(false);
        boardPane.getChildren().clear();
        Label diffText = new Label("Choose size");
        Button small = new Button("Small 3x3");
        small.setOnAction(e -> {
            startGame(new Board(3));
        });
        Button normal = new Button("Normal 4x4");
        normal.setOnAction(e -> {
            startGame(new Board(4));
        });
        Button big = new Button("Big 5x5");
        big.setOnAction(e -> {
            startGame(new Board(5));
        });
        HBox buttonBox = new HBox(small, normal, big);
        VBox diffBox = new VBox(diffText, buttonBox);

        Label loadGameLabel = new Label("Load Game");
        VBox tempVBox = new VBox();
        
        try {
            for(String line : saveHandler.getAllSaves(false)){
                String[] temp = line.split(";");
                Board tempBoard = saveHandler.loadBoard(temp[0], false);
                Button b = new Button(temp[0] + "  Score: " + temp[2] + "  Size: " + temp[1]);
                b.setOnAction(e -> {
                    startGame(tempBoard);
                });
                Button x = new Button("X");
                x.setOnAction(e -> {
                    try {
                        saveHandler.deleteSave(temp[0], false);
                        newGame();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                });
                HBox buttonGroup = new HBox(b,x);
                tempVBox.getChildren().add(buttonGroup);
            }
        } catch (IOException e) {
            alertBox(e);
            e.printStackTrace();
        }
        
        ScrollPane scroll = new ScrollPane(tempVBox);
        VBox loadBox = new VBox(loadGameLabel, scroll);

        VBox allContent = new VBox(diffBox, loadBox);

        boardPane.getChildren().add(allContent);
        
    }

    public void saveGame(){
        if(board == null){
            alertBox(new IllegalArgumentException("There is currently no game to save, Start a new one!"));
            return;
        }
        String prompt = promptBox();
        if(prompt == ""){

        }
        else {
            try{
                saveHandler.saveBoard(board, prompt, false);
            }catch(Exception e){
                e.printStackTrace();
                alertBox(e);
            }
        }
    }
    
    /**
     * Used to alert the user of any failures or missinputs
     * Giving feedback to the user
     * @param e exception
     */
    private void alertBox(Exception e){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private String promptBox(){
        TextInputDialog dialog = new TextInputDialog("Save Game");
        dialog.setHeaderText("Please set a name for your save");
        dialog.setContentText("name must be unique and cant be empty");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        return "";
    }

    /**
     * initiaties the user-controls; i.e the arrows on the keyboard
     * also draws the board
     * @param b
     */
    private void startGame(Board b){
        saveButton.setVisible(true);
        board = b;
        boardSize = (int) boardPane.getPrefHeight();
        tileSize = boardSize/b.getSize();
        boardPane.getChildren().clear();
        boardPane.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyPress);
        drawGrid();
    }

    public void gameOver(boolean won){
        boardPane.getChildren().clear();
        saveButton.setVisible(false);
        Label result = new Label(won ? "WIN" : "LOSS");
        Label scoreText = new Label("SCORE");
        Label score = new Label(board.getScore() + "");

        VBox screen = new VBox(result, scoreText, score);
        boardPane.getChildren().add(screen);
    }

    /**
     * Draws the grid by crosschecking with the boardmatrix in Board.java
     */
    private void drawGrid(){
        StackPane temp;
        boardPane.getChildren().clear();
        for (int y = 0; y < board.getSize(); y++){
            for(int x = 0; x < board.getSize(); x++){
                Tile t = board.getTileValue(x, y);
                Label l = new Label(t == null ? "" : "" + t.getValue());
                temp = new StackPane(l);
                StackPane.setAlignment(l, Pos.CENTER);
                temp.setPrefHeight(tileSize);
                temp.setPrefWidth(tileSize);
                temp.setTranslateX(x*tileSize);
                temp.setTranslateY(y*tileSize);
                temp.setStyle("-fx-border-color: grey; -fx-border-width: 2px; -fx-background-color:#"+ (t == null ? "f4f4f4" : t.getColor()) +";");
                boardPane.getChildren().add(temp);
            }
        }
    }
    /**
     * Starts the game on load of scene
     */
    @FXML
    private void initialize(){
        newGame();
    }
}