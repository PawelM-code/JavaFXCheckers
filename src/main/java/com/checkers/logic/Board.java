package com.checkers.logic;

import com.checkers.logic.figures.*;
import com.checkers.logic.minimax.Minimax;

import java.util.ArrayList;
import java.util.LinkedList;

public class Board {
    private final UserDialogs userDialogs;
    private Minimax minimax = new Minimax(this);
    private GameMove gameMove = new GameMove();
    private GameValidators gameValidators = new GameValidators();
    private GameNextMoves gameNextMoves = new GameNextMoves();
    private LinkedList<LinkedList<Figure>> boardRow;
    private ArrayList<Move> checkBeatingWhite = new ArrayList<>();
    private ArrayList<Move> checkBeatingBlack = new ArrayList<>();
    private ArrayList<Point> currentWhiteFigures = new ArrayList<>();
    private ArrayList<Point> currentBlackFigures = new ArrayList<>();
    private ArrayList<FigurePoint> savedBoard = new ArrayList<>();
    private Move savedLastMove;

    UserDialogs getUserDialogs() {
        return userDialogs;
    }

    Minimax getMinimax() {
        return minimax;
    }

    public GameMove getGameMove() {
        return gameMove;
    }

    public GameValidators getGameValidators() {
        return gameValidators;
    }

    GameNextMoves getGameNextMoves() {
        return gameNextMoves;
    }

    LinkedList<LinkedList<Figure>> getBoardRow() {
        return boardRow;
    }

    public ArrayList<Move> getCheckBeatingWhite() {
        return checkBeatingWhite;
    }

    public ArrayList<Move> getCheckBeatingBlack() {
        return checkBeatingBlack;
    }

    ArrayList<Point> getCurrentWhiteFigures() {
        return currentWhiteFigures;
    }

    ArrayList<Point> getCurrentBlackFigures() {
        return currentBlackFigures;
    }

    public ArrayList<FigurePoint> getSavedBoard() {
        return savedBoard;
    }

    void setSavedLastMove(Move savedLastMove) {
        this.savedLastMove = savedLastMove;
    }

    Move getSavedLastMove() {
        return savedLastMove;
    }

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
