package main.java.logic;

import main.java.logic.figures.Figure;
import main.java.logic.figures.FigureColor;
import main.java.logic.figures.None;
import main.java.logic.figures.Queen;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class GameMove {
    static final int SIZE_OF_THE_BOARD = 8;
    ArrayDeque<FigureColor.Group> whiteOrBlackMove = new ArrayDeque<>();

    GameMove(Board board) {
    }

    public void move(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        board.gameNextMoves.checkIfFigureIsBeatingAllBoard(board);

        if (board.gameMove.nextFigureColor().equals(FigureColor.Group.WHITE)) {
            moveFigureIfOfColor(move, figureFrom, board.checkBeatingWhite, FigureColor.Group.WHITE, board);
        } else {
            moveFigureIfOfColor(move, figureFrom, board.checkBeatingBlack, FigureColor.Group.BLACK, board);
        }
    }

    private void moveFigureIfOfColor(Move move, Figure figureFrom, ArrayList<Move> checkBeatingWhite, FigureColor.Group whiteOrBlack, Board board) {
        if (figureFrom.getColor().isInGroup(whiteOrBlack)) {
            tryMoveAndSetNextColorMove(move, checkBeatingWhite, whiteOrBlack, board);
        } else {
            board.userDialogs.showInfoWhenWrongColorStarts(whiteOrBlackMove);
        }
    }

    private void checkIfMoveIsOnTheBoardIfTrueTryMove(Move move, Board board) {
        Figure figureTo = board.getFigure(move.getRow2(), move.getCol2());
        if (board.gameValidators.areColorsEqual(figureTo.getColor(), FigureColor.EMPTY_FIELD) && move.getRow2() <= SIZE_OF_THE_BOARD && move.getRow2() >= 1 &&
                move.getCol2() <= SIZE_OF_THE_BOARD && move.getCol2() >= 1) {
            board.gameMove.checkWhatFigure(move, board);
        }
    }

    private void checkWhatFigure(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());

        if (board.gameValidators.isFigurePawn(figureFrom, board)) {
            movePawn(move, board);
        }
        if (board.gameValidators.isFigureQueen(figureFrom, board)) {
            moveQueen(move, board);
        }
    }

    private void tryMoveAndSetNextColorMove(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group whiteOrBlack, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());

        if (checkBeatingWhiteOrBlack.size() == 0) {
            checkIfMoveIsOnTheBoardIfTrueTryMove(move, board);
            if (!figureFrom.getColor().equals(board.getFigure(move.getRow1(), move.getCol1()).getColor())) {
                board.userDialogs.showMoveColor(whiteOrBlackMove);
                setNextColorMove(whiteOrBlack);
            } else {
                board.userDialogs.showInfoMoveNotAllowed();
            }
        } else {
            if (isBeatingCorrect(move, checkBeatingWhiteOrBlack)) {
                doBeating(move, checkBeatingWhiteOrBlack, whiteOrBlack, figureFrom, board);
            } else {
                board.userDialogs.showInfoBeatingNotAllowed();
            }
        }
    }

    private void setNextColorMove(FigureColor.Group whiteOrBlack) {
        whiteOrBlackMove.poll();
        whiteOrBlackMove.offer(whiteOrBlack);
    }

    private void doBeating(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group setWhiteOrBlackInQueue, Figure figureFrom, Board board) {
        checkIfMoveIsOnTheBoardIfTrueTryMove(move, board);
        if (figureFrom.getColor().equals(board.getFigure(move.getRow1(), move.getCol1()).getColor())) {
            board.userDialogs.showInfoMoveNotAllowed();
        } else {
            board.gameNextMoves.clearBeatingList(board);
            board.saveLastMove = move;
            board.gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
            if (checkBeatingWhiteOrBlack.size() > 0) {
                board.userDialogs.showMoveColorWhenStillBeating(whiteOrBlackMove);
            } else {
                board.saveLastMove = null;
                board.userDialogs.showMoveColor(whiteOrBlackMove);
                setNextColorMove(setWhiteOrBlackInQueue);
            }
        }
    }

    private boolean isBeatingCorrect(Move move, ArrayList<Move> checkBeating) {
        boolean beating = false;
        if (checkBeating.size() > 0) {
            for (Move beatingMove : checkBeating) {
                if (move.getRow1() == beatingMove.getRow1() && move.getCol1() == beatingMove.getCol1() &&
                        move.getRow2() == beatingMove.getRow2() && move.getCol2() == beatingMove.getCol2()) {
                    beating = true;
                    break;
                } else {
                    beating = false;
                }
            }
        }
        return beating;
    }

    private void moveQueen(Move move, Board board) {
        int count = 0;
        checkQueenMoveIsDiagonal(move, count, board);
    }

    private void checkQueenMoveIsDiagonal(Move move, int count, Board board) {
        if (Math.abs(move.getRow2() - move.getRow1()) == Math.abs(move.getCol2() - move.getCol1())) {
            count = beatingQueenRightDown(move, count, board);
            count = beatingQueenLeftDown(move, count, board);
            count = beatingQueenLeftUp(move, count, board);
            count = beatingQueenRightUp(move, count, board);
        }
        if (count == Math.abs(move.getRow2() - move.getRow1())) {
            setFigureToANewField(move, board);
        }
    }

    private int beatingQueenRightUp(Move move, int count, Board board) {
        if (move.getRow2() < move.getRow1() && move.getCol2() > move.getCol1()) {
            Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());

            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        if(clearBeatingFigureByQueenRightUp(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        break;
                    }
                }
            }
            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        if(clearBeatingFigureByQueenRightUp(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        break;
                    }
                }
            }
        }
        return count;
    }

    private int beatingQueenLeftUp(Move move, int count, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() < move.getRow1() && move.getCol2() < move.getCol1()) {
            int row1 = move.getRow1();
            int col1 = move.getCol1();

            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        if(clearBeatingFigureByQueenLeftUp(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        break;
                    }
                }
            }
            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        if(clearBeatingFigureByQueenLeftUp(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        break;
                    }
                }
            }
        }
        return count;
    }

    private boolean clearBeatingFigureByQueenRightUp(int row, int col, Board board) {
        if (row - 1 > 0 && col + 1 <= 8) {
            if (board.getFigure(row - 1, col + 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                board.boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean clearBeatingFigureByQueenLeftUp(int row, int col, Board board) {
        if (row - 1 > 0 && col - 1 > 0) {
            if (board.getFigure(row - 1, col - 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                board.boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private int beatingQueenLeftDown(Move move, int count, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() > move.getRow1() && move.getCol2() < move.getCol1()) {
            int row = move.getRow1();
            int col = move.getCol1();

            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if(board.gameValidators.isFigureBlack(row, col, board)){
                        if(clearBeatingFigureByQueenLeftDown(row, col, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureWhite(row, col, board)){
                        break;
                    }
                }
            }
            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if(board.gameValidators.isFigureWhite(row, col, board)){
                        if(clearBeatingFigureByQueenLeftDown(row, col, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureBlack(row, col, board)){
                        break;
                    }
                }
            }
        }
        return count;
    }

    private int beatingQueenRightDown(Move move, int count, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() > move.getRow1() && move.getCol2() > move.getCol1()) {
            int row1 = move.getRow1();
            int col1 = move.getCol1();

            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        if(clearBeatingFigureByQueenRightDown(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        break;
                    }
                }
            }
            if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if(board.gameValidators.isFigureWhite(row1, col1, board)){
                        if(clearBeatingFigureByQueenRightDown(row1, col1, board)){
                            break;
                        }
                    }
                    if(board.gameValidators.isFigureBlack(row1, col1, board)){
                        break;
                    }
                }
            }
        }
        return count;
    }

    private boolean doQueenBeating(boolean figureBlackOrWhite, boolean clearBeatingFigureByQueen, boolean figureWhiteOrBlack) {
        if (figureBlackOrWhite) {
            if (clearBeatingFigureByQueen) return true;
        }
        return figureWhiteOrBlack;
    }

    private boolean clearBeatingFigureByQueenLeftDown(int row, int col, Board board) {
        if (row + 1 <= 8 && col - 1 > 0) {
            if (board.getFigure(row + 1, col - 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                board.boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean clearBeatingFigureByQueenRightDown(int row, int col, Board board) {
        if (row + 1 <= 8 && col + 1 <= 8) {
            if (board.getFigure(row + 1, col + 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                board.boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private void movePawn(Move move, Board board) {
        moveBlackPawn(move, board);
        moveWhitePawn(move, board);
    }

    private void moveWhitePawn(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN)) {
            if (isPawnMoveDiagonal(move.getRow1() - 1, move.getRow2(), move.getCol1(), move.getCol2())) {
                setFigureToANewField(move, board);
                changePawnToQueen(move, board);
            }
            beatingAPawnDownRight(move, board);
            beatingAPawnDownLeft(move, board);
            beatingAPawnUpRight(move, board);
            beatingAPawnUpLeft(move, board);
        }
    }

    private void moveBlackPawn(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (board.gameValidators.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN)) {
            if (isPawnMoveDiagonal(move.getRow1() + 1, move.getRow2(), move.getCol1(), move.getCol2())) {
                setFigureToANewField(move, board);
                changePawnToQueen(move, board);
            }
            beatingAPawnDownRight(move, board);
            beatingAPawnDownLeft(move, board);
            beatingAPawnUpRight(move, board);
            beatingAPawnUpLeft(move, board);
        }
    }

    private static boolean isPawnMoveDiagonal(int i, int row2, int col1, int col2) {
        return (i == row2) && (col1 + 1 == col2 || col1 - 1 == col2);
    }

    private void beatingAPawnUpLeft(Move move, Board board) {
        if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnUpLeftChooseColor(move, FigureColor.Group.WHITE, board);
        }
        if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnUpLeftChooseColor(move, FigureColor.Group.BLACK, board);
        }
    }

    private void beatingAPawnUpRight(Move move, Board board) {
        if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnUpRightChooseColor(move, FigureColor.Group.WHITE, board);
        } else if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnUpRightChooseColor(move, FigureColor.Group.BLACK, board);
        }
    }

    private void beatingAPawnUpLeftChooseColor(Move move, FigureColor.Group colors, Board board) {
        if ((move.getRow1() - 2 == move.getRow2()) && move.getCol1() - 2 == move.getCol2()) {
            if (board.getFigure(move.getRow2() + 1, move.getCol2() + 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move, board);
                board.boardRow.get(move.getRow2()).set(move.getCol2(), new None());
                changePawnToQueen(move, board);
            }
        }
    }

    private void beatingAPawnUpRightChooseColor(Move move, FigureColor.Group colors, Board board) {
        if ((move.getRow1() - 2 == move.getRow2()) && move.getCol1() + 2 == move.getCol2()) {
            if (board.getFigure(move.getRow2() + 1, move.getCol2() - 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move, board);
                board.boardRow.get(move.getRow2()).set(move.getCol2() - 2, new None());
                changePawnToQueen(move, board);
            }
        }
    }

    private void beatingAPawnDownLeft(Move move, Board board) {
        if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnDownLeftChooseColor(move, FigureColor.Group.WHITE, board);
        } else if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnDownLeftChooseColor(move, FigureColor.Group.BLACK, board);
        }
    }

    private void beatingAPawnDownLeftChooseColor(Move move, FigureColor.Group colors, Board board) {
        if (move.getRow1() + 2 == move.getRow2() && move.getCol1() - 2 == move.getCol2()) {
            if (board.getFigure(move.getRow2() - 1, move.getCol2() + 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move, board);
                board.boardRow.get(move.getRow2() - 2).set(move.getCol2(), new None());
                changePawnToQueen(move, board);
            }
        }
    }

    private void beatingAPawnDownRight(Move move, Board board) {
        if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnDownRightChooseColor(move, FigureColor.Group.WHITE, board);
        } else if (board.getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnDownRightChooseColor(move, FigureColor.Group.BLACK, board);
        }
    }

    private void beatingAPawnDownRightChooseColor(Move move, FigureColor.Group colors, Board board) {
        if (move.getRow1() + 2 == move.getRow2() && move.getCol1() + 2 == move.getCol2()) {
            if (board.getFigure(move.getRow2() - 1, move.getCol2() - 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move, board);
                board.boardRow.get(move.getRow2() - 2).set(move.getCol2() - 2, new None());
                changePawnToQueen(move, board);
            }
        }
    }

    private void setFigureToANewField(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        board.boardRow.get(move.getRow1() - 1).set(move.getCol1() - 1, new None());
        board.boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, figureFrom);
    }

    private void changePawnToQueen(Move move, Board board) {
        if (move.getRow2() == 1 && board.getFigure(move.getRow2(), move.getCol2()).getColor().equals(FigureColor.WHITE_PAWN)) {
            board.boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, new Queen(FigureColor.WHITE_QUEEN));
        }
        if (move.getRow2() == 8 && board.getFigure(move.getRow2(), move.getCol2()).getColor().equals(FigureColor.BLACK_PAWN)) {
            board.boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, new Queen(FigureColor.BLACK_QUEEN));
        }
    }

    public void simulateMove(Move move, Board board) {
        checkIfMoveIsOnTheBoardIfTrueTryMove(move, board);
    }

    public void simulateBeating(FigureColor.Group whiteOrBlack, Move move, Board board) {
        Move startMove = move;
        ArrayDeque<FigureColor.Group> whiteOrBlackMoveCopy = new ArrayDeque<>(whiteOrBlackMove);
        while (board.gameMove.nextFigureColor().equals(whiteOrBlack)) {
            move(startMove, board);
            board.gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
            if (whiteOrBlack.equals(FigureColor.Group.BLACK)) {
                if (board.checkBeatingBlack.size() > 0) {
                    startMove = board.checkBeatingBlack.get(0);
                }
            } else {
                if (board.checkBeatingWhite.size() > 0) {
                    startMove = board.checkBeatingWhite.get(0);
                }
            }

        }
        whiteOrBlackMove = whiteOrBlackMoveCopy;
    }

    public void computerMove(Board board) throws Exception {
        while (board.gameMove.nextFigureColor().equals(FigureColor.Group.BLACK)) {
            move(board.minimax.minimax(), board);
        }
    }

    private FigureColor.Group nextFigureColor() {
        return whiteOrBlackMove.peek();
    }

    public void initWhiteOrBlackMove() {
        if (whiteOrBlackMove.size() == 0) {
            whiteOrBlackMove.offer(FigureColor.Group.WHITE);
            whiteOrBlackMove.offer(FigureColor.Group.BLACK);
        }
    }
}