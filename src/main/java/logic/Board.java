package main.java.logic;

import main.java.logic.figures.*;
import main.java.logic.minimax.Minimax;

import java.util.ArrayList;
import java.util.LinkedList;

public class Board {
    final UserDialogs userDialogs;
    public Minimax minimax = new Minimax(this);
    public GameMove gameMove = new GameMove();
    public GameValidators gameValidators = new GameValidators();
    GameNextMoves gameNextMoves = new GameNextMoves();
    LinkedList<LinkedList<Figure>> boardRow;
    public ArrayList<Move> checkBeatingWhite = new ArrayList<>();
    public ArrayList<Move> checkBeatingBlack = new ArrayList<>();
    ArrayList<Point> currentWhiteFigures = new ArrayList<>();
    ArrayList<Point> currentBlackFigures = new ArrayList<>();
    public ArrayList<FigurePoint> savedBoard = new ArrayList<>();
    Move savedLastMove;

    public Board(UserDialogs userDialogs) {
        this.userDialogs = userDialogs;
        initBoard();
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
        savedBoard.clear();
        for (int row = 1; row < GameMove.SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < GameMove.SIZE_OF_THE_BOARD + 1; col++) {
                savedBoard.add(new FigurePoint(new Point(row, col), getFigure(row, col)));
            }
        return savedBoard;
    }
}
