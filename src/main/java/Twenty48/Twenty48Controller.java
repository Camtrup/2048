package Twenty48;

import java.io.IOException;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private ISaveHandler saveHandler = new TXTSaveHandler();

    
    
    @FXML
    private Label scoreDisplay;
    
    @FXML
    private Pane boardPane;
    
    @FXML
    private Button saveButton;
    
    /**
     * An object of the type KeyEvent that handles the userinput for controlling the game
     * Translates the key-symbol to a string
     * Also checks if the game is over
     */
    EventHandler<KeyEvent> keyPress =  (e -> {
            boolean over = board.move(e.getCode().toString());
            drawGrid();
            scoreDisplay.setText(board.getScore() + "");
            if(over){
                drawGrid();
                gameOver(board.isGameWon());
            }
        });

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
        
        
        Label numText = new Label("Choose size of Number-tiles!");
        numText.setAlignment(Pos.CENTER);
        Button numSmall = new Button("Small 3x3");
        numSmall.setOnAction(e -> {
            startGame(new Board(3, new NumberTile()));
            
        });
        Button numNormal = new Button("Normal 4x4");
        numNormal.setOnAction(e -> {
            startGame(new Board(4, new NumberTile()));
        });
        Button numBig = new Button("Big 5x5");
        numBig.setOnAction(e -> {
            startGame(new Board(5, new NumberTile()));
        });
        HBox numBox = new HBox(numSmall, numNormal, numBig);
        numBox.setSpacing(5);
        VBox numContainer = new VBox(numText, numBox);
        

        Label picText = new Label("Choose size of Picture-tiles!");
        Button picSmall = new Button("Small 3x3");
        picSmall.setOnAction(e -> {
            startGame(new Board(3, new PictureTile()));
        });
        Button picNormal = new Button("Normal 4x4");
        picNormal.setOnAction(e -> {
            startGame(new Board(4, new PictureTile()));
        });
        Button picBig = new Button("Big 5x5");
        picBig.setOnAction(e -> {
            startGame(new Board(5, new PictureTile()));
        });
        HBox picBox = new HBox(picSmall, picNormal, picBig);
        picBox.setSpacing(5);
        VBox picContainer = new VBox(picText, picBox);

        Label loadGameLabel = new Label("Load Game");
        VBox tempVBox = new VBox();
        
        try {
            for(String line : saveHandler.getAllSaves(false)){
                String[] temp = line.split(";");
                Board tempBoard = saveHandler.loadBoard(temp[0], false);
                Button b = new Button("Name: " + temp[0] + "  Type: " + temp[1]);
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

        VBox allContent = new VBox(numContainer, picContainer, loadBox);
        allContent.setSpacing(10);
       // allContent.getStyleableParent().setAlignment(Pos.CENTER);
        //VBox.setMargin(allContent,new Insets(5));

        boardPane.getChildren().add(allContent);
    }

    /**
     * Saves the current game with a prompted name, if the name is already taken or another exception is fired
     * it is logged and alerted to the user
     */
    public void saveGame(){
        if(board == null){
            alertBox(new IllegalArgumentException("There is currently no game to save, Start a new one!"));
            return;
        }
        String prompt = promptBox();
        if (prompt == null) {return;}
        try{
            saveHandler.saveBoard(board, prompt, false);
        }catch(Exception e){
            e.printStackTrace();
            alertBox(e);
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

    /**
     * prompts the user for input on what the name of a given save
     * @return String with the name of the save or a blank string if a result is not present
     */
    private String promptBox(){
        TextInputDialog dialog = new TextInputDialog("Save Game");
        dialog.setHeaderText("Please set a name for your save");
        dialog.setContentText("name must be unique and cant be empty");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            return result.get();
        }
        else{
            return null;
        }
    }

    /**
     * initiaties the user-controls; i.e the arrows on the keyboard
     * also draws the board
     * @param b board to be used in the new game
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

    /**
     * Displays an alertbox that informs the user of the outcome of the game
     * If the player loses they are only given the choice of starting another game
     * If the plasyer wins the player can decide to continue to the next target (which is double of the current)
     * , continue forever or start a new game
     * @param won decides the outcome of the game
     */
    public void gameOver(boolean won){
        String s = won ? "WON" : "LOST";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("You " + s + "!");
        alert.setHeaderText("Your score: " + board.getScore());
        alert.setContentText("Do you want to...");

        ButtonType buttonTypeOne = new ButtonType("Continue to " + (int) Math.pow(2, board.getWinCondition() + 1) + "?");
        ButtonType buttonTypeTwo = new ButtonType("Continue forever?!");
        ButtonType buttonTypeThree = new ButtonType("Start a new game?");
        if(!board.isGameWon()){
            alert.getButtonTypes().setAll(buttonTypeThree);
        }
        else{
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
        }


        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent()){
            newGame();
        }
        else if (result.get() == buttonTypeOne){
            board.setWinCondition(board.getWinCondition() + 1);
        } else if (result.get() == buttonTypeTwo) {
            board.setWinCondition(0);
        } else if(result.get() == buttonTypeThree){
            newGame();
        }
    }

    /**
     * Draws the grid by crosschecking with the boardmatrix in Board.java
     */
    private void drawGrid(){
        Node temp;
        boardPane.getChildren().clear();
        for (int y = 0; y < board.getSize(); y++){
            for(int x = 0; x < board.getSize(); x++){
                ITile t = board.getTile(x, y);
                if(t == null){
                    StackPane pane = new StackPane();
                    pane.setPrefHeight(tileSize);
                    pane.setPrefWidth(tileSize);
                    temp = pane;
                    temp.setStyle("-fx-border-color:grey; -fx-border-width:1px;");
                }
                else{
                    temp = t.getNode(tileSize);
                }
                temp.setTranslateX(x*tileSize);
                temp.setTranslateY(y*tileSize);
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