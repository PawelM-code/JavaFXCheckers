package com.checkers.logic;

import com.checkers.logic.figures.Figure;
import com.checkers.logic.figures.FigureColor;

import java.util.stream.IntStream;

public class GameValidators {

    public boolean isTheEndOfGame(Board board) {
        if (isPlayerPresent(FigureColor.Group.WHITE, board) != isPlayerPresent(FigureColor.Group.BLACK, board)) {
            if(isPlayerPresent(FigureColor.Group.WHITE, board)){
                board.getUserDialogs().showEndInfoWhiteWon();
            }else {
                board.getUserDialogs().showEndInfoBlackWon();
            }
            return true;
        } else if (!isPossibleMove(board)) {
            if(board.getCheckBeatingWhite().size() == 0 && board.getGameNextMoves().getAvailableMovesWhite().size() == 0){
                board.getUserDialogs().showEndInfoBlackWon();
            }else{
                board.getUserDialogs().showEndInfoWhiteWon();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossibleMove(Board board) {
        board.getGameNextMoves().getAvailableMove(board);
        board.getGameNextMoves().checkIfFigureIsBeatingAllBoard(board);
        assert board.getGameMove().getWhiteOrBlackMove().peek() != null;
        if (board.getGameMove().getWhiteOrBlackMove().peek().equals(FigureColor.Group.WHITE)) {
            return board.getCheckBeatingWhite().size() != 0 || board.getGameNextMoves().getAvailableMovesWhite().size() != 0;
        } else {
            return board.getCheckBeatingBlack().size() != 0 || board.getGameNextMoves().getAvailableMovesBlack().size() != 0;
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
        return areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN) ||areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN);
    }

    public boolean isFigurePawn(Figure figureFrom, Board board) {
        return areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN) || areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN);
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
