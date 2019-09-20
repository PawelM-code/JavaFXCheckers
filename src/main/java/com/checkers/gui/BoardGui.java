package com.checkers.gui;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.checkers.logic.Board;
import com.checkers.logic.Move;
import com.checkers.logic.UserDialogs;
import com.checkers.logic.figures.Figure;
import com.checkers.logic.figures.FigureColor;
import com.checkers.logic.figures.Pawn;


public class BoardGui extends Application implements NewGame {
    private static final int FIELD_SIZE = 60;
    private static final int SIZE_OF_THE_BOARD = 8;
    private GridPane grid;

    public BoardGui() {
    }

    private void displayOnGrid(Board board) {
        grid.getChildren().clear();
        for (int row = 0; row < SIZE_OF_THE_BOARD; row++)
            for (int col = 0; col < SIZE_OF_THE_BOARD; col++) {
                createGridColorFields(row, col);

                Figure figure = board.getFigure(row + 1, col + 1);
                ImageView imageView = figure.getImageView();
                GridPane.setHalignment(imageView, HPos.CENTER);
                grid.add(imageView, col, row);
            }
    }

    private void createGridColorFields(int row, int col) {
        Rectangle rectBlack = new Rectangle(FIELD_SIZE, FIELD_SIZE);
        Rectangle rectWhite = new Rectangle(FIELD_SIZE, FIELD_SIZE);
        rectBlack.setFill(Color.color(0.1, 0.1, 0.1));
        rectBlack.setStroke(Color.BLACK);
        rectBlack.setStrokeWidth(1);
        rectWhite.setFill(Color.TRANSPARENT);
        rectWhite.setStroke(Color.BLACK);
        rectWhite.setStrokeWidth(1);

        if ((row + col) % 2 == 0) grid.add(rectWhite, col, row);
        else grid.add(rectBlack, col, row);

        ImageView lightBoard = new ImageView("/board/blackMat.jpg");
        lightBoard.setFitHeight(FIELD_SIZE);
        lightBoard.setFitWidth(FIELD_SIZE);
    }

    @Override
    public void newGame(Stage stage) {
        BorderPane borderPane = new BorderPane();

        StackPane stackPaneTop = new StackPane();
        Text titleGame = new Text("CHECKERS");
        titleGame.setStyle("-fx-font-size: 40");
        BorderPane.setAlignment(titleGame, Pos.CENTER);
        stackPaneTop.getChildren().add(titleGame);
        titleGame.getStyleClass().add("/com/checkers/gui/style.css");

        grid = new GridPane();
        GridPane.setColumnIndex(grid, SIZE_OF_THE_BOARD);
        GridPane.setRowIndex(grid, SIZE_OF_THE_BOARD);
        grid.setMaxSize(480, 480);
        grid.getStylesheets().add("/com/checkers/gui/style.css");
        grid.setStyle("-fx-border-width:3px;-fx-border-color: rgb(10,10,10)");

        StackPane stackPaneCenter = new StackPane();
        stackPaneCenter.setMaxWidth(560);
        stackPaneCenter.setMaxHeight(560);

        Image gridPaneBackground = new Image("/board/plywood3.jpg");
        ImagePattern gridPaneBackgroundImagePattern = new ImagePattern(gridPaneBackground);

        Rectangle rectangle = new Rectangle(560, 560);
        rectangle.setFill(gridPaneBackgroundImagePattern);
        rectangle.setStrokeWidth(1);
        rectangle.setStroke(Color.BLACK);
        rectangle.setArcWidth(30.0);
        rectangle.setArcHeight(30.0);

        stackPaneCenter.getChildren().addAll(rectangle, grid);

        UserDialogs userDialogs = new UserDialogs(new Text());

        Button newGame = new Button();
        newGame.setText("New Game");
        newGame.setOnAction(e -> restart(stage));
        newGame.getStylesheets().add("/com/checkers/gui/style.css");

        GridPane setBottom = new GridPane();
        setBottom.getStyleClass().add("/com/checkers/gui/style.css");

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        setBottom.getColumnConstraints().add(column1);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);
        setBottom.getColumnConstraints().add(column2);
        GridPane.setColumnIndex(setBottom, 2);
        GridPane.setRowIndex(setBottom, 1);
        setBottom.getChildren().addAll(newGame, userDialogs.getTextUserDialogs());
        GridPane.setConstraints(newGame, 1, 0);
        GridPane.setConstraints(userDialogs.getTextUserDialogs(), 0, 0);
        GridPane.setHalignment(newGame, HPos.RIGHT);
        GridPane.setHalignment(userDialogs.getTextUserDialogs(), HPos.LEFT);
        GridPane.setMargin(newGame, new Insets(10, 10, 10, 10));
        GridPane.setMargin(userDialogs.getTextUserDialogs(), new Insets(10, 10, 10, 10));

        StackPane stackPaneLeft = new StackPane();
        stackPaneLeft.setPrefSize(50, 560);
        StackPane stackPaneRight = new StackPane();
        stackPaneRight.setPrefSize(50, 560);

        borderPane.setCenter(stackPaneCenter);
        borderPane.setTop(titleGame);
        borderPane.setBottom(setBottom);
        borderPane.setLeft(stackPaneLeft);
        borderPane.setRight(stackPaneRight);

        Board board = new Board(userDialogs);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("/com/checkers/gui/style.css");
        board.setFigure(8, 5, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(8, 3, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(8, 7, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(8, 5, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(8, 3, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(8, 1, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(7, 8, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(7, 6, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(7, 4, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(7, 2, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(6, 7, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(6, 5, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(6, 3, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(6, 1, new Pawn(FigureColor.WHITE_PAWN));
        board.setFigure(1, 2, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(1, 4, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(1, 6, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(1, 8, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(2, 1, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(2, 3, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(2, 5, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(2, 7, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(3, 2, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(3, 4, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(3, 6, new Pawn(FigureColor.BLACK_PAWN));
        board.setFigure(3, 8, new Pawn(FigureColor.BLACK_PAWN));
        board.gameMove.initWhiteOrBlackMove();
        userDialogs.showStartGameInfo();
        displayOnGrid(board);

        grid.setOnMousePressed(e -> {
            int oldX = 1 + (int) e.getX() / FIELD_SIZE;
            int oldY = 1 + (int) e.getY() / FIELD_SIZE;
            grid.setOnMouseClicked(event -> {
                int x = 1 + (int) event.getX() / FIELD_SIZE;
                int y = 1 + (int) event.getY() / FIELD_SIZE;
                if (!board.gameValidators.isTheEndOfGame(board)) {
                    board.gameMove.move(new Move(oldY, oldX, y, x), board);
                    displayOnGrid(board);
                }
                if (!board.gameValidators.isTheEndOfGame(board)) {
                    try {
                        board.gameMove.computerMove(board);
                        displayOnGrid(board);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                board.gameValidators.isTheEndOfGame(board);
            });

        });
        stage.setTitle("Checkers");
        stage.setScene(scene);
        stage.setMaxHeight(700);
        stage.setMaxWidth(700);
        stage.setMinHeight(640);
        stage.setMinWidth(640);
        stage.show();
    }

    private void restart(Stage stage) {
        newGame(stage);
    }

    @Override
    public void start(Stage primaryStage) {
        newGame(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
