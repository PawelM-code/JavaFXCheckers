package main.java.logic;

import java.util.ArrayList;

public class GameMoves {
    static final int SIZE_OF_THE_BOARD = 8;


    public GameMoves(Board board) {
    }

    public void move(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        board.checkIfFigureIsBeatingAllBoard();

        if (board.nextFigureColor().equals(FigureColor.Group.WHITE)) {
            moveFigureIfOfColor(move, figureFrom, board.checkBeatingWhite, FigureColor.Group.WHITE, board);
        } else {
            moveFigureIfOfColor(move, figureFrom, board.checkBeatingBlack, FigureColor.Group.BLACK, board);
        }
    }

    private void moveFigureIfOfColor(Move move, Figure figureFrom, ArrayList<Move> checkBeatingWhite, FigureColor.Group whiteOrBlack, Board board) {
        if (figureFrom.getColor().isInGroup(whiteOrBlack)) {
            tryMoveAndSetNextColorMove(move, checkBeatingWhite, whiteOrBlack, board);
        } else {
            board.userDialogs.showInfoWhenWrongColorStarts(board.whiteOrBlackMove);
        }
    }

    private void tryMoveAndSetNextColorMove(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group whiteOrBlack, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());

        if (checkBeatingWhiteOrBlack.size() == 0) {
            board.checkIfMoveIsOnTheBoardIfTrueTryMove(move);
            if (!figureFrom.getColor().equals(board.getFigure(move.getRow1(), move.getCol1()).getColor())) {
                board.userDialogs.showMoveColor(board.whiteOrBlackMove);
                setNextColorMove(whiteOrBlack, board);
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

    private void setNextColorMove(FigureColor.Group whiteOrBlack, Board board) {
        board.whiteOrBlackMove.poll();
        board.whiteOrBlackMove.offer(whiteOrBlack);
    }

    private void doBeating(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group setWhiteOrBlackInQueue, Figure figureFrom, Board board) {
        board.checkIfMoveIsOnTheBoardIfTrueTryMove(move);
        if (figureFrom.getColor().equals(board.getFigure(move.getRow1(), move.getCol1()).getColor())) {
            board.userDialogs.showInfoMoveNotAllowed();
        } else {
            board.clearBeatingList();
            board.saveLastMove = move;
            board.checkIfFigureIsBeatingAllBoard();
            if (checkBeatingWhiteOrBlack.size() > 0) {
                board.userDialogs.showMoveColorWhenStillBeating(board.whiteOrBlackMove);
            } else {
                board.saveLastMove = null;
                board.userDialogs.showMoveColor(board.whiteOrBlackMove);
                setNextColorMove(setWhiteOrBlackInQueue, board);
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

    void moveQueen(Move move, Board board) {
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

            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if (doQueenBeating(board.isFigureBlack(row1, col1), clearBeatingFigureByQueenRightUp(row1, col1, board), board.isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if (doQueenBeating(board.isFigureWhite(row1, col1), clearBeatingFigureByQueenRightUp(row1, col1, board), board.isFigureBlack(row1, col1)))
                        break;
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

            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if (doQueenBeating(board.isFigureBlack(row1, col1), clearBeatingFigureByQueenLeftUp(row1, col1, board), board.isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if (doQueenBeating(board.isFigureWhite(row1, col1), clearBeatingFigureByQueenLeftUp(row1, col1, board), board.isFigureBlack(row1, col1)))
                        break;
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

            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if (doQueenBeating(board.isFigureBlack(row, col), clearBeatingFigureByQueenLeftDown(row, col, board), board.isFigureWhite(row, col)))
                        break;
                }
            }
            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if (doQueenBeating(board.isFigureWhite(row, col), clearBeatingFigureByQueenLeftDown(row, col, board), board.isFigureBlack(row, col)))
                        break;
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

            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if (doQueenBeating(board.isFigureBlack(row1, col1), clearBeatingFigureByQueenRightDown(row1, col1, board), board.isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (board.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if (doQueenBeating(board.isFigureWhite(row1, col1), clearBeatingFigureByQueenRightDown(row1, col1, board), board.isFigureBlack(row1, col1)))
                        break;
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

    void movePawn(Move move, Board board) {
        moveBlackPawn(move, board);
        moveWhitePawn(move, board);
    }

    private void moveWhitePawn(Move move, Board board) {
        Figure figureFrom = board.getFigure(move.getRow1(), move.getCol1());
        if (board.areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN)) {
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
        if (board.areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN)) {
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
}