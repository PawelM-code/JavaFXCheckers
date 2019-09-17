package main.java.logic;

import main.java.logic.figures.Figure;
import main.java.logic.figures.FigureColor;

import java.util.stream.IntStream;

public class GameValidators {

    GameValidators(Board board) {
    }

    public boolean isTheEndOfGame(Board board) {
        if (isPlayerPresent(FigureColor.Group.WHITE, board) != isPlayerPresent(FigureColor.Group.BLACK, board)) {
            board.userDialogs.showEndInfo();
            return true;
        } else if (!isPossibleMove(board)) {
            board.userDialogs.showEndInfo();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossibleMove(Board board) {
        board.gameNextMoves.getAvailableMove(board);
        board.gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
        if (board.gameMove.whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            return board.checkBeatingWhite.size() != 0 || board.availableMovesWhite.size() != 0;
        } else {
            return board.checkBeatingBlack.size() != 0 || board.availableMovesBlack.size() != 0;
        }
    }

    private boolean isPlayerPresent(FigureColor.Group colors, Board board) {
        return IntStream
                .range(1, 9)
                .anyMatch(row -> IntStream
                        .range(1, 9)
                        .anyMatch(col -> board.getFigure(row, col).getColor().isInGroup(colors)));
    }

    public boolean isFigureQueen(Figure figureFrom, Board board) {
        return board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN) || board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN);
    }

    public boolean isFigurePawn(Figure figureFrom, Board board) {
        return board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN) || board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN);
    }

    public boolean isFigureBlack(int row, int col, Board board) {
        return board.getFigure(row, col).getColor().isInGroup(FigureColor.Group.BLACK);
    }

    public boolean isFigureWhite(int row, int col, Board board) {
        return board.getFigure(row, col).getColor().isInGroup(FigureColor.Group.WHITE);
    }

    boolean areColorsEqual(FigureColor firstColor, FigureColor secondColor) {
        return firstColor.equals(secondColor);
    }
}
