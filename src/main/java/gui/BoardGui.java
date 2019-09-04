package main.java.gui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.logic.*;

public class BoardGui extends Application implements NewGame{
    public static final int FIELD_SIZE = 60;
    private static final int SIZE_OF_THE_BOARD = 8;

    public BoardGui() {
    }

    @Override
    public void newGame(Stage stage) {
        /*primaryStage.setOnCloseRequest(e -> {
            e.consume();
            ConfirmBox.closeProgram(primaryStage);
        });*/

        BorderPane borderPane = new BorderPane();

        StackPane stackPaneTop = new StackPane();
        Text titleGame = new Text("CHECKERS");
        titleGame.setFont(Font.font("Verdana", 20));
        BorderPane.setAlignment(titleGame, Pos.CENTER);
        stackPaneTop.getChildren().add(titleGame);

        GridPane grid = new GridPane();
        GridPane.setColumnIndex(grid, SIZE_OF_THE_BOARD);
        GridPane.setRowIndex(grid, SIZE_OF_THE_BOARD);

        UserDialogs userDialogs = new UserDialogs(new Text());

        Button newGame = new Button();
        newGame.setText("New Game");
        newGame.setOnAction(e -> restart(stage));

        GridPane setBottom = new GridPane();
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        setBottom.getColumnConstraints().add(column1);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        setBottom.getColumnConstraints().add(column2);
        GridPane.setColumnIndex(setBottom,2);
        GridPane.setRowIndex(setBottom, 1);
        setBottom.getChildren().addAll(newGame,userDialogs.getTextUserDialogs());
        GridPane.setConstraints(newGame,1,0);
        GridPane.setConstraints(userDialogs.getTextUserDialogs(),0,0);
        GridPane.setHalignment(newGame, HPos.RIGHT);
        GridPane.setHalignment(userDialogs.getTextUserDialogs(), HPos.LEFT);
        GridPane.setMargin(newGame,new Insets(10,10,10,10));
        GridPane.setMargin(userDialogs.getTextUserDialogs(),new Insets(10,10,10,10));

        borderPane.setCenter(grid);
        borderPane.setTop(titleGame);
        borderPane.setBottom(setBottom);

        Board board = new Board(grid, userDialogs);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("/main/java/gui/style.css");

      /*  board.setFigure(8, 3, new Pawn("w"));
        board.setFigure(8, 1, new Pawn("w"));
        board.setFigure(1, 2, new Pawn("b"));
        board.setFigure(1, 4, new Pawn("b"));
        board.setFigure(7, 6, new Pawn("b"));*/

        /*board.setFigure(8, 1, new Pawn("w"));
        board.setFigure(5, 2, new Pawn("b"));*/

        board.setFigure(8, 7, new Pawn("w"));
        board.setFigure(8, 5, new Pawn("w"));
        board.setFigure(8, 3, new Pawn("w"));
        board.setFigure(8, 1, new Pawn("w"));
        board.setFigure(7, 8, new Pawn("w"));
        board.setFigure(7, 6, new Pawn("w"));
        board.setFigure(7, 4, new Pawn("w"));
        board.setFigure(7, 2, new Pawn("w"));
        board.setFigure(6, 7, new Pawn("w"));
        board.setFigure(6, 5, new Pawn("w"));
        board.setFigure(6, 3, new Pawn("w"));
        board.setFigure(6, 1, new Pawn("w"));
        board.setFigure(1, 2, new Pawn("b"));
        board.setFigure(1, 4, new Pawn("b"));
        board.setFigure(1, 6, new Pawn("b"));
        board.setFigure(1, 8, new Pawn("b"));
        board.setFigure(2, 1, new Pawn("b"));
        board.setFigure(2, 3, new Pawn("b"));
        board.setFigure(2, 5, new Pawn("b"));
        board.setFigure(2, 7, new Pawn("b"));
        board.setFigure(3, 2, new Pawn("b"));
        board.setFigure(3, 4, new Pawn("b"));
        board.setFigure(3, 6, new Pawn("b"));
        board.setFigure(3, 8, new Pawn("b"));
        board.initWhiteOrBlackMove();
        userDialogs.showStartGameInfo();
        board.displayOnGrid();

        grid.setOnMousePressed(e -> {
            int oldX = 1 + (int) e.getX() / FIELD_SIZE;
            int oldY = 1 + (int) e.getY() / FIELD_SIZE;
            grid.setOnMouseClicked(event -> {
                int x = 1 + (int) event.getX() / FIELD_SIZE;
                int y = 1 + (int) event.getY() / FIELD_SIZE;
                if (!board.isTheEndOfGame()) {
                    board.move(new Move(oldY, oldX, y, x));
                    board.displayOnGrid();
                }
                if(!board.isTheEndOfGame()){
                    board.computerMove();
                }
                board.isTheEndOfGame();
            });

        });
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.show();
    }
    private void restart(Stage stage) {
        newGame(stage);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        newGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
