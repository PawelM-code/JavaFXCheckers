package main.java.logic;

import main.java.logic.figures.Figure;
import main.java.logic.figures.FigureColor;
import main.java.logic.figures.Point;

import java.util.ArrayList;
import java.util.List;

public class GameNextMoves {
    private boolean addStatus = false;
    public ArrayList<Move> availableMovesWhite = new ArrayList<>();
    public ArrayList<Move> availableMovesBlack = new ArrayList<>();


    public GameNextMoves(Board board) {
    }


    public void getAvailableMove(Board board) {
        clearAvailableMoves(board);
        board.getFiguresPoint();
        for (Point currentWhiteFigurePoint : board.currentWhiteFigures) {
            int row = currentWhiteFigurePoint.getRow();
            int col = currentWhiteFigurePoint.getCol();
            if (board.getFigure(row, col).getColor().equals(FigureColor.WHITE_PAWN)) {
                Point pointOne = new Point(row - 1, col - 1);
                Point pointTwo = new Point(row - 1, col + 1);
                addAvailablePawnMove(currentWhiteFigurePoint, pointOne, pointTwo, pointOne.getRow() > 0, availableMovesWhite, board);
            } else {
                addAvailableQueenMove(currentWhiteFigurePoint, availableMovesWhite, board);
            }
        }
        for (Point currentBlackFigurePoint : board.currentBlackFigures) {
            int row = currentBlackFigurePoint.getRow();
            int col = currentBlackFigurePoint.getCol();
            if (board.getFigure(row, col).getColor().equals(FigureColor.BLACK_PAWN)) {
                Point pointOne = new Point(row + 1, col - 1);
                Point pointTwo = new Point(row + 1, col + 1);
                addAvailablePawnMove(currentBlackFigurePoint, pointOne, pointTwo, pointOne.getRow() < 9, availableMovesBlack, board);
            } else {
                addAvailableQueenMove(currentBlackFigurePoint, availableMovesBlack, board);
            }
        }
        clearListOfCurrentFigures(board);
    }

    private void addAvailablePawnMove(Point point, Point pointOne, Point pointTwo, boolean rowBoundary, ArrayList<Move> availableMovesWhiteOrBlack, Board board) {
        if (rowBoundary && pointOne.getCol() > 0 && board.getFigure(pointOne.getRow(), pointOne.getCol()).getColor().equals(FigureColor.EMPTY_FIELD)) {
            availableMovesWhiteOrBlack.add(new Move(point.getRow(), point.getCol(), pointOne.getRow(), pointOne.getCol()));
        }
        if (rowBoundary && pointTwo.getCol() < 9 && board.getFigure(pointTwo.getRow(), pointTwo.getCol()).getColor().equals(FigureColor.EMPTY_FIELD)) {
            availableMovesWhiteOrBlack.add(new Move(point.getRow(), point.getCol(), pointTwo.getRow(), pointTwo.getCol()));
        }
    }

    private void addAvailableQueenMove(Point point, List<Move> availableMovesBlack, Board board) {
        addAvailableQueenMoveUpRight(point, availableMovesBlack, board);
        addAvailableQueenMoveUpLeft(point, availableMovesBlack, board);
        addAvailableQueenMoveDownRight(point, availableMovesBlack, board);
        addAvailableQueenMoveDownLeft(point, availableMovesBlack, board);
    }

    private void addAvailableQueenMoveDownLeft(Point point, List<Move> availableMovesBlack, Board board) {
        int row2;
        int col2;
        row2 = point.getRow();
        col2 = point.getCol();
        while (row2 > 1 && col2 > 1) {
            row2--;
            col2--;
            if (board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveDownRight(Point point, List<Move> availableMovesBlack, Board board) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 > 1 && col2 < GameMove.SIZE_OF_THE_BOARD) {
            row2--;
            col2++;
            if (board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveUpLeft(Point point, List<Move> availableMovesBlack, Board board) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 < GameMove.SIZE_OF_THE_BOARD && col2 > 1) {
            row2++;
            col2--;
            if (board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveUpRight(Point point, List<Move> availableMovesBlack, Board board) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 < GameMove.SIZE_OF_THE_BOARD && col2 < GameMove.SIZE_OF_THE_BOARD) {
            row2++;
            col2++;
            if (board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void clearListOfCurrentFigures(Board board) {
        board.currentBlackFigures.clear();
        board.currentWhiteFigures.clear();
    }

    private void clearAvailableMoves(Board board) {
        availableMovesWhite.clear();
        availableMovesBlack.clear();
    }

    public void checkIfFigureIsBeatingAllBoard(Board board) {
        board.gameNextMoves.clearBeatingList(board);
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                checkPawnIsBeating(i, j, board);
                checkQueenIsBeating(i, j, board);
            }
        }
        removeMovesThatCannotByMade(board);
        board.userDialogs.showBeating(board.checkBeatingBlack, board.checkBeatingWhite, board.gameMove.whiteOrBlackMove);
    }

    private void removeMovesThatCannotByMade(Board board) {
        if (board.saveLastMove != null) {
            board.checkBeatingBlack.removeIf(x -> !(x.getRow1() == board.saveLastMove.getRow2() && x.getCol1() == board.saveLastMove.getCol2()));
            board.checkBeatingWhite.removeIf(x -> !(x.getRow1() == board.saveLastMove.getRow2() && x.getCol1() == board.saveLastMove.getCol2()));
        }
    }

    void clearBeatingList(Board board) {
        board.checkBeatingWhite.clear();
        board.checkBeatingBlack.clear();
    }

    private void checkPawnIsBeating(int row, int col, Board board) {
        if (board.gameValidators.isFigurePawn(board.getFigure(row, col), board)) {
            checkBeatingPawnUpLeft(row, col, board);
            checkBeatingPawnUpRight(row, col, board);
            checkBeatingPawnDownLeft(row, col, board);
            checkBeatingPawnDownRight(row, col, board);
        }
    }

    private void checkQueenIsBeating(int row, int col, Board board) {
        if (board.gameValidators.isFigureQueen(board.getFigure(row, col), board)) {
            checkBeatingQueenUpLeft(row, col, board);
            checkBeatingQueenUpRight(row, col, board);
            checkBeatingQueenDownLeft(row, col, board);
            checkBeatingQueenDownRight(row, col, board);
        }
    }

    private void checkBeatingQueenUpLeft(int row, int col, Board board) {
        Figure figureFrom = board.getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1--;
            col1--;
            int row2 = row1 - 1;
            int col2 = col1 - 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 <= 0, col2 <= 0, board)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2, board);
        }
    }

    private void checkBeatingQueenDownLeft(int row, int col, Board board) {
        Figure figureFrom = board.getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1++;
            col1--;
            int row2 = row1 + 1;
            int col2 = col1 - 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 >= 9, col2 <= 0, board)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2, board);
        }
    }

    private void checkBeatingQueenUpRight(int row, int col, Board board) {
        Figure figureFrom = board.getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1--;
            col1++;
            int row2 = row1 - 1;
            int col2 = col1 + 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 <= 0, col2 >= 9, board)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2, board);
        }
    }

    private void checkBeatingQueenDownRight(int row, int col, Board board) {
        Figure figureFrom = board.getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1++;
            col1++;
            int row2 = row1 + 1;
            int col2 = col1 + 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 >= 9, col2 >= 9, board)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2, board);
        }
    }

    private void addingQueenBeats(int row, int col, Figure figureFrom, int row1, int col1, int row2, int col2, Board board) {
        addQueenBeating(row, col, figureFrom, FigureColor.WHITE_QUEEN, board.gameValidators.isFigureBlack(row1, col1, board), row2, col2, board.checkBeatingWhite, board);
        if (addStatus) {
            addAdditionalPossibilitiesToPositionTheFigureAfterBeating(figureFrom, FigureColor.WHITE_QUEEN, row2, col2, board.checkBeatingWhite, board);
        }
        addQueenBeating(row, col, figureFrom, FigureColor.BLACK_QUEEN, board.gameValidators.isFigureWhite(row1, col1, board), row2, col2, board.checkBeatingBlack, board);
        if (addStatus) {
            addAdditionalPossibilitiesToPositionTheFigureAfterBeating(figureFrom, FigureColor.BLACK_QUEEN, row2, col2, board.checkBeatingBlack, board);

        }
    }

    private void addAdditionalPossibilitiesToPositionTheFigureAfterBeating(Figure figureFrom, FigureColor queenColor,
                                                                           int row2, int col2, ArrayList<Move> checkBeatingWhiteOrBlack, Board board) {
        if (board.gameValidators.areColorsEqual(figureFrom.getColor(), queenColor) && checkBeatingWhiteOrBlack.size() > 0 && board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
            Move lastBeating = checkBeatingWhiteOrBlack.get(checkBeatingWhiteOrBlack.size() - 1);
            checkBeatingWhiteOrBlack.add(new Move(lastBeating.getRow1(), lastBeating.getCol1(), row2, col2));
        }
    }

    private boolean breakVerifyQueenBeating(Figure figureFrom, int row1, int col1, int row2, int col2, boolean rowBoundary, boolean colBoundary, Board board) {
        if (rowBoundary || colBoundary) return true;
        if (checkIfQueenBeatsTwoNextFigures(row1, col1, row2, col2, board)) return true;
        if (checkIfQueenBeatsFigureOfHerColor(figureFrom, FigureColor.WHITE_QUEEN, board.gameValidators.isFigureWhite(row1, col1, board), board))
            return true;
        if (checkIfQueenBeatsFigureOfHerColor(figureFrom, FigureColor.BLACK_QUEEN, board.gameValidators.isFigureBlack(row1, col1, board), board))
            return true;
        return false;
    }

    private void addQueenBeating(int row, int col, Figure figureFrom, FigureColor queenColor,
                                 boolean figureBlackOrWhite, int row2, int col2, ArrayList<Move> checkBeatingWhiteOrBlack, Board board) {
        if (board.gameValidators.areColorsEqual(figureFrom.getColor(), queenColor) && figureBlackOrWhite && board.getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
            checkBeatingWhiteOrBlack.add(new Move(row, col, row2, col2));
            addStatus = true;
        }
    }

    private boolean checkIfQueenBeatsFigureOfHerColor(Figure figureFrom, FigureColor queenColor, boolean figureColor, Board board) {
        return board.gameValidators.areColorsEqual(figureFrom.getColor(), queenColor) && figureColor;
    }

    private boolean checkIfQueenBeatsTwoNextFigures(int row, int col, int row1, int row2, Board board) {
        return !board.getFigure(row, col).getColor().equals(FigureColor.EMPTY_FIELD) && !board.getFigure(row1, row2).getColor().equals(FigureColor.EMPTY_FIELD);
    }

    private void checkBeatingPawnDownRight(int row, int col, Board board) {
        if (row + 2 < 9 && col + 2 < 9) {
            if (board.getFigure(row + 2, col + 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row + 2, col + 2);
                Point beatingFigurePoint = new Point(row + 1, col + 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint, board);
            }
        }
    }

    private void addPawnBeating(int row, int col, Point emptyPoint, Point beatingFigurePoint, Board board) {
        if (board.getFigure(row, col).getColor().equals(FigureColor.BLACK_PAWN)) {
            if (board.getFigure(beatingFigurePoint.getRow(), beatingFigurePoint.getCol()).getColor().isInGroup(FigureColor.Group.WHITE)) {
                board.checkBeatingBlack.add(new Move(row, col, emptyPoint.getRow(), emptyPoint.getCol()));
            }
        }
        if (board.getFigure(row, col).getColor().equals(FigureColor.WHITE_PAWN)) {
            if (board.getFigure(beatingFigurePoint.getRow(), beatingFigurePoint.getCol()).getColor().isInGroup(FigureColor.Group.BLACK)) {
                board.checkBeatingWhite.add(new Move(row, col, emptyPoint.getRow(), emptyPoint.getCol()));
            }
        }
    }

    private void checkBeatingPawnDownLeft(int row, int col, Board board) {
        if (col - 2 > 0 && row + 2 < 9) {
            if (board.getFigure(row + 2, col - 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row + 2, col - 2);
                Point beatingFigurePoint = new Point(row + 1, col - 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint, board);
            }
        }
    }

    private void checkBeatingPawnUpRight(int row, int col, Board board) {
        if (row - 2 > 0 && col + 2 < 9) {
            if (board.getFigure(row - 2, col + 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row - 2, col + 2);
                Point beatingFigurePoint = new Point(row - 1, col + 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint, board);
            }
        }
    }

    private void checkBeatingPawnUpLeft(int row, int col, Board board) {
        if (row - 2 > 0 && col - 2 > 0) {
            if (board.getFigure(row - 2, col - 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row - 2, col - 2);
                Point beatingFigurePoint = new Point(row - 1, col - 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint, board);
            }
        }
    }
}
