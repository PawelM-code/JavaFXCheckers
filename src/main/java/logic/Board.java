package main.java.logic;

import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.logic.figures.*;
import main.java.logic.minimax.Minimax;

import java.util.*;

import static main.java.gui.BoardGui.FIELD_SIZE;

public class Board {
    final UserDialogs userDialogs;
    public Minimax minimax = new Minimax(this);
    public GameMove gameMove = new GameMove(this);
    public GameValidators gameValidators = new GameValidators(this);
    GameNextMoves gameNextMoves = new GameNextMoves(this);
    LinkedList<LinkedList<Figure>> boardRow;
    public ArrayList<Move> checkBeatingWhite = new ArrayList<>();
    public ArrayList<Move> checkBeatingBlack = new ArrayList<>();
    ArrayList<Point> currentWhiteFigures = new ArrayList<>();
    ArrayList<Point> currentBlackFigures = new ArrayList<>();
    public ArrayList<FigurePoint> saveBoard = new ArrayList<>();
    Move saveLastMove;
    private GridPane grid;

    public Board(GridPane grid, UserDialogs userDialogs) {
        this.grid = grid;
        this.userDialogs = userDialogs;
        initBoard();
    }

    public void displayOnGrid() {
        grid.getChildren().clear();
        for (int row = 0; row < GameMove.SIZE_OF_THE_BOARD; row++)
            for (int col = 0; col < GameMove.SIZE_OF_THE_BOARD; col++) {
                createGridColorFields(row, col);

                Figure figure = getFigure(row + 1, col + 1);
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

        ImageView lightBoard = new ImageView("/main/resources/board/blackMat.jpg");
        lightBoard.setFitHeight(FIELD_SIZE);
        lightBoard.setFitWidth(FIELD_SIZE);
    }

    private void initBoard() {
        this.boardRow = new LinkedList<>();
        for (int row = 0; row < GameMove.SIZE_OF_THE_BOARD; row++)
            initRow(row);
    }

    private void initRow(int row) {
        boardRow.add(row, new LinkedList<>());
        for (int col = 0; col < GameMove.SIZE_OF_THE_BOARD; col++)
            boardRow.get(row).add(col, new None());
    }

    public Figure getFigure(int row, int col) {
        return boardRow.get(row - 1).get(col - 1);
    }

    public void setFigure(int row, int col, Figure figure) {
        boardRow.get(row - 1).add(col - 1, figure);
        boardRow.get(row - 1).remove(col);
    }

    void getFiguresPoint() {
        for (int row = 1; row < GameMove.SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < GameMove.SIZE_OF_THE_BOARD + 1; col++) {
                if (gameValidators.isFigureWhite(row, col, this)) {
                    currentWhiteFigures.add(new Point(row, col));
                }
                if (gameValidators.isFigureBlack(row, col, this)) {
                    currentBlackFigures.add(new Point(row, col));
                }
            }
    }

    public void setBoard(ArrayList<FigurePoint> boardState) {
        initBoard();
        for (FigurePoint figurePoint : boardState) {
            int row = figurePoint.getPoint().getRow();
            int col = figurePoint.getPoint().getCol();
            Figure figure = figurePoint.getFigure();
            setFigure(row, col, figure);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int row = 1; row < 9; row++) {
            builder.append("  ");
            for (int col = 1; col < 9; col++) {
                builder.append("|---");
            }

            builder.append("|");
            builder.append("\n");
            builder.append(row).append(FigureColor.EMPTY_FIELD);

            for (int col = 1; col < 9; col++) {
                builder.append("|");
                builder.append(FigureColor.EMPTY_FIELD);
                builder.append(getFigure(row, col).getColor());
                builder.append(FigureColor.EMPTY_FIELD);
            }

            builder.append("|");
            builder.append("\n");
        }
        builder.append("  ");
        for (int col = 1; col < 9; col++) {
            builder.append("|---");
        }
        builder.append("|");
        builder.append("  ");
        builder.append("\n");
        builder.append("  ");

        for (char ch = 'A'; ch < 'I'; ch++) {
            builder.append(FigureColor.EMPTY_FIELD);
            builder.append(FigureColor.EMPTY_FIELD);
            builder.append(ch);
            builder.append(FigureColor.EMPTY_FIELD);
        }

        return builder.toString();
    }

    public ArrayList<FigurePoint> saveBoardFigurePoints() {
        saveBoard.clear();
        for (int row = 1; row < GameMove.SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < GameMove.SIZE_OF_THE_BOARD + 1; col++) {
                saveBoard.add(new FigurePoint(new Point(row, col), getFigure(row, col)));
            }
        return saveBoard;
    }
}
