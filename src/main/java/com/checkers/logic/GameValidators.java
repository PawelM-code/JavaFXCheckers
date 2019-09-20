package com.checkers.logic;

import com.checkers.logic.figures.Figure;
import com.checkers.logic.figures.FigureColor;

import java.util.stream.IntStream;

public class GameValidators {

    public boolean isTheEndOfGame(Board board) {
        if (isPlayerPresent(FigureColor.Group.WHITE, board) != isPlayerPresent(FigureColor.Group.BLACK, board)) {
            if(isPlayerPresent(FigureColor.Group.WHITE, board)){
                board.userDialogs.showEndInfoWhiteWon();
            }else {
                board.userDialogs.showEndInfoBlackWon();
            }
            return true;
        } else if (!isPossibleMove(board)) {
            if(board.checkBeatingWhite.size() == 0 && board.gameNextMoves.availableMovesWhite.size() == 0){
                board.userDialogs.showEndInfoBlackWon();
            }else{
                board.userDialogs.showEndInfoWhiteWon();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossibleMove(Board board) {
        board.gameNextMoves.getAvailableMove(board);
        board.gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
        assert board.gameMove.whiteOrBlackMove.peek() != null;
        if (board.gameMove.whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            return board.checkBeatingWhite.size() != 0 || board.gameNextMoves.availableMovesWhite.size() != 0;
        } else {
            return board.checkBeatingBlack.size() != 0 || board.gameNextMoves.availableMovesBlack.size() != 0;
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
