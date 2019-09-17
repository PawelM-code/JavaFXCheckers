package main.java.logic;

import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;
import java.util.stream.IntStream;

import static main.java.gui.BoardGui.FIELD_SIZE;

public class Board {
    final UserDialogs userDialogs;
    Minimax minimax = new Minimax(this);
    public GameMoves gameMoves= new GameMoves(this);
    LinkedList<LinkedList<Figure>> boardRow;
    ArrayList<Move> checkBeatingWhite = new ArrayList<>();
    ArrayList<Move> checkBeatingBlack = new ArrayList<>();
    ArrayDeque<FigureColor.Group> whiteOrBlackMove = new ArrayDeque<>();
    private ArrayList<Point> currentWhiteFigures = new ArrayList<>();
    private ArrayList<Point> currentBlackFigures = new ArrayList<>();
    ArrayList<Move> availableMovesWhite = new ArrayList<>();
    ArrayList<Move> availableMovesBlack = new ArrayList<>();
    ArrayList<FigurePoint> saveBoard = new ArrayList<>();
    Move saveLastMove;
    private boolean addStatus = false;
    private GridPane grid;

    public Board(GridPane grid, UserDialogs userDialogs) {
        this.grid = grid;
        this.userDialogs = userDialogs;
        initBoard();
    }

    public void displayOnGrid() {
        grid.getChildren().clear();
        for (int row = 0; row < GameMoves.SIZE_OF_THE_BOARD; row++)
            for (int col = 0; col < GameMoves.SIZE_OF_THE_BOARD; col++) {
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
        for (int row = 0; row < GameMoves.SIZE_OF_THE_BOARD; row++)
            initRow(row);
    }

    private void initRow(int row) {
        boardRow.add(row, new LinkedList<>());
        for (int col = 0; col < GameMoves.SIZE_OF_THE_BOARD; col++)
            boardRow.get(row).add(col, new None());
    }

    public Figure getFigure(int row, int col) {
        return boardRow.get(row - 1).get(col - 1);
    }

    public void setFigure(int row, int col, Figure figure) {
        boardRow.get(row - 1).add(col - 1, figure);
        boardRow.get(row - 1).remove(col);
    }

    public boolean isTheEndOfGame() {
        if (isPlayerPresent(FigureColor.Group.WHITE) != isPlayerPresent(FigureColor.Group.BLACK)) {
            userDialogs.showEndInfo();
            return true;
        } else if (!isPossibleMove()) {
            userDialogs.showEndInfo();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPossibleMove() {
        getAvailableMove();
        checkIfFigureIsBeatingAllBoard();
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            return checkBeatingWhite.size() != 0 || availableMovesWhite.size() != 0;
        } else {
            return checkBeatingBlack.size() != 0 || availableMovesBlack.size() != 0;
        }
    }

    private void getFiguresPoint() {
        for (int row = 1; row < GameMoves.SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < GameMoves.SIZE_OF_THE_BOARD + 1; col++) {
                if (isFigureWhite(row, col)) {
                    currentWhiteFigures.add(new Point(row, col));
                }
                if (isFigureBlack(row, col)) {
                    currentBlackFigures.add(new Point(row, col));
                }
            }
    }

    private void getAvailableMove() {
        clearAvailableMoves();
        getFiguresPoint();
        for (Point currentWhiteFigurePoint : currentWhiteFigures) {
            int row = currentWhiteFigurePoint.getRow();
            int col = currentWhiteFigurePoint.getCol();
            if (getFigure(row, col).getColor().equals(FigureColor.WHITE_PAWN)) {
                Point pointOne = new Point(row - 1, col - 1);
                Point pointTwo = new Point(row - 1, col + 1);
                addAvailablePawnMove(currentWhiteFigurePoint, pointOne, pointTwo, pointOne.getRow() > 0, availableMovesWhite);
            } else {
                addAvailableQueenMove(currentWhiteFigurePoint, availableMovesWhite);
            }
        }
        for (Point currentBlackFigurePoint : currentBlackFigures) {
            int row = currentBlackFigurePoint.getRow();
            int col = currentBlackFigurePoint.getCol();
            if (getFigure(row, col).getColor().equals(FigureColor.BLACK_PAWN)) {
                Point pointOne = new Point(row + 1, col - 1);
                Point pointTwo = new Point(row + 1, col + 1);
                addAvailablePawnMove(currentBlackFigurePoint, pointOne, pointTwo, pointOne.getRow() < 9, availableMovesBlack);
            } else {
                addAvailableQueenMove(currentBlackFigurePoint, availableMovesBlack);
            }
        }
        clearListOfCurrentFigures();
    }

    private void addAvailablePawnMove(Point point, Point pointOne, Point pointTwo, boolean rowBoundary, ArrayList<Move> availableMovesWhiteOrBlack) {
        if (rowBoundary && pointOne.getCol() > 0 && getFigure(pointOne.getRow(), pointOne.getCol()).getColor().equals(FigureColor.EMPTY_FIELD)) {
            availableMovesWhiteOrBlack.add(new Move(point.getRow(), point.getCol(), pointOne.getRow(), pointOne.getCol()));
        }
        if (rowBoundary && pointTwo.getCol() < 9 && getFigure(pointTwo.getRow(), pointTwo.getCol()).getColor().equals(FigureColor.EMPTY_FIELD)) {
            availableMovesWhiteOrBlack.add(new Move(point.getRow(), point.getCol(), pointTwo.getRow(), pointTwo.getCol()));
        }
    }

    private void addAvailableQueenMove(Point point, List<Move> availableMovesBlack) {
        addAvailableQueenMoveUpRight(point, availableMovesBlack);
        addAvailableQueenMoveUpLeft(point, availableMovesBlack);
        addAvailableQueenMoveDownRight(point, availableMovesBlack);
        addAvailableQueenMoveDownLeft(point, availableMovesBlack);
    }

    private void addAvailableQueenMoveDownLeft(Point point, List<Move> availableMovesBlack) {
        int row2;
        int col2;
        row2 = point.getRow();
        col2 = point.getCol();
        while (row2 > 1 && col2 > 1) {
            row2--;
            col2--;
            if (getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveDownRight(Point point, List<Move> availableMovesBlack) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 > 1 && col2 < GameMoves.SIZE_OF_THE_BOARD) {
            row2--;
            col2++;
            if (getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveUpLeft(Point point, List<Move> availableMovesBlack) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 < GameMoves.SIZE_OF_THE_BOARD && col2 > 1) {
            row2++;
            col2--;
            if (getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void addAvailableQueenMoveUpRight(Point point, List<Move> availableMovesBlack) {
        int row2 = point.getRow();
        int col2 = point.getCol();
        while (row2 < GameMoves.SIZE_OF_THE_BOARD && col2 < GameMoves.SIZE_OF_THE_BOARD) {
            row2++;
            col2++;
            if (getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                availableMovesBlack.add(new Move(point.getRow(), point.getCol(), row2, col2));
            }
        }
    }

    private void clearListOfCurrentFigures() {
        currentBlackFigures.clear();
        currentWhiteFigures.clear();
    }

    void setBoard(ArrayList<FigurePoint> boardState) {
        initBoard();
        for (FigurePoint figurePoint : boardState) {
            int row = figurePoint.getPoint().getRow();
            int col = figurePoint.getPoint().getCol();
            Figure figure = figurePoint.getFigure();
            setFigure(row, col, figure);
        }
    }

    private void clearAvailableMoves() {
        availableMovesWhite.clear();
        availableMovesBlack.clear();
    }

    void getAvailableMovesAndBeating() {
        checkIfFigureIsBeatingAllBoard();
        getAvailableMove();
    }

    private boolean isPlayerPresent(FigureColor.Group colors) {
        return IntStream
                .range(1, 9)
                .anyMatch(row -> IntStream
                        .range(1, 9)
                        .anyMatch(col -> getFigure(row, col).getColor().isInGroup(colors)));
    }

    FigureColor.Group nextFigureColor() {
        return whiteOrBlackMove.peek();
    }

    boolean areColorsEqual(FigureColor firstColor, FigureColor secondColor) {
        return firstColor.equals(secondColor);
    }

    public void initWhiteOrBlackMove() {
        if (whiteOrBlackMove.size() == 0) {
            whiteOrBlackMove.offer(FigureColor.Group.WHITE);
            whiteOrBlackMove.offer(FigureColor.Group.BLACK);
        }
    }

    void checkIfFigureIsBeatingAllBoard() {
        clearBeatingList();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                checkPawnIsBeating(i, j);
                checkQueenIsBeating(i, j);
            }
        }
        removeMovesThatCannotByMade();
        userDialogs.showBeating(checkBeatingBlack, checkBeatingWhite, whiteOrBlackMove);
    }

    private void removeMovesThatCannotByMade() {
        if (saveLastMove != null) {
            checkBeatingBlack.removeIf(x -> !(x.getRow1() == saveLastMove.getRow2() && x.getCol1() == saveLastMove.getCol2()));
            checkBeatingWhite.removeIf(x -> !(x.getRow1() == saveLastMove.getRow2() && x.getCol1() == saveLastMove.getCol2()));
        }
    }

    void clearBeatingList() {
        checkBeatingWhite.clear();
        checkBeatingBlack.clear();
    }

    private void checkPawnIsBeating(int row, int col) {
        if (isFigurePawn(getFigure(row, col))) {
            checkBeatingPawnUpLeft(row, col);
            checkBeatingPawnUpRight(row, col);
            checkBeatingPawnDownLeft(row, col);
            checkBeatingPawnDownRight(row, col);
        }
    }

    private void checkQueenIsBeating(int row, int col) {
        if (isFigureQueen(getFigure(row, col))) {
            checkBeatingQueenUpLeft(row, col);
            checkBeatingQueenUpRight(row, col);
            checkBeatingQueenDownLeft(row, col);
            checkBeatingQueenDownRight(row, col);
        }
    }

    private void checkBeatingQueenUpLeft(int row, int col) {
        Figure figureFrom = getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1--;
            col1--;
            int row2 = row1 - 1;
            int col2 = col1 - 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 <= 0, col2 <= 0)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2);
        }
    }

    private void checkBeatingQueenDownLeft(int row, int col) {
        Figure figureFrom = getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1++;
            col1--;
            int row2 = row1 + 1;
            int col2 = col1 - 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 >= 9, col2 <= 0)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2);
        }
    }

    private void checkBeatingQueenUpRight(int row, int col) {
        Figure figureFrom = getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1--;
            col1++;
            int row2 = row1 - 1;
            int col2 = col1 + 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 <= 0, col2 >= 9)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2);
        }
    }

    private void checkBeatingQueenDownRight(int row, int col) {
        Figure figureFrom = getFigure(row, col);
        int row1 = row;
        int col1 = col;
        addStatus = false;

        while (true) {
            row1++;
            col1++;
            int row2 = row1 + 1;
            int col2 = col1 + 1;

            if (breakVerifyQueenBeating(figureFrom, row1, col1, row2, col2, row2 >= 9, col2 >= 9)) break;
            addingQueenBeats(row, col, figureFrom, row1, col1, row2, col2);
        }
    }

    private void addingQueenBeats(int row, int col, Figure figureFrom, int row1, int col1, int row2, int col2) {
        addQueenBeating(row, col, figureFrom, FigureColor.WHITE_QUEEN, isFigureBlack(row1, col1), row2, col2, checkBeatingWhite);
        if (addStatus) {
            addAdditionalPossibilitiesToPositionTheFigureAfterBeating(figureFrom, FigureColor.WHITE_QUEEN, row2, col2, checkBeatingWhite);
        }
        addQueenBeating(row, col, figureFrom, FigureColor.BLACK_QUEEN, isFigureWhite(row1, col1), row2, col2, checkBeatingBlack);
        if (addStatus) {
            addAdditionalPossibilitiesToPositionTheFigureAfterBeating(figureFrom, FigureColor.BLACK_QUEEN, row2, col2, checkBeatingBlack);

        }
    }

    private void addAdditionalPossibilitiesToPositionTheFigureAfterBeating(Figure figureFrom, FigureColor queenColor,
                                                                           int row2, int col2, ArrayList<Move> checkBeatingWhiteOrBlack) {
        if (areColorsEqual(figureFrom.getColor(), queenColor) && checkBeatingWhiteOrBlack.size() > 0 && getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
            Move lastBeating = checkBeatingWhiteOrBlack.get(checkBeatingWhiteOrBlack.size() - 1);
            checkBeatingWhiteOrBlack.add(new Move(lastBeating.getRow1(), lastBeating.getCol1(), row2, col2));
        }
    }

    private boolean breakVerifyQueenBeating(Figure figureFrom, int row1, int col1, int row2, int col2, boolean rowBoundary, boolean colBoundary) {
        if (rowBoundary || colBoundary) return true;
        if (checkIfQueenBeatsTwoNextFigures(row1, col1, row2, col2)) return true;
        if (checkIfQueenBeatsFigureOfHerColor(figureFrom, FigureColor.WHITE_QUEEN, isFigureWhite(row1, col1)))
            return true;
        if (checkIfQueenBeatsFigureOfHerColor(figureFrom, FigureColor.BLACK_QUEEN, isFigureBlack(row1, col1)))
            return true;
        return false;
    }

    private void addQueenBeating(int row, int col, Figure figureFrom, FigureColor queenColor,
                                 boolean figureBlackOrWhite, int row2, int col2, ArrayList<Move> checkBeatingWhiteOrBlack) {
        if (areColorsEqual(figureFrom.getColor(), queenColor) && figureBlackOrWhite && getFigure(row2, col2).getColor().equals(FigureColor.EMPTY_FIELD)) {
            checkBeatingWhiteOrBlack.add(new Move(row, col, row2, col2));
            addStatus = true;
        }
    }

    private boolean checkIfQueenBeatsFigureOfHerColor(Figure figureFrom, FigureColor queenColor, boolean figureColor) {
        return areColorsEqual(figureFrom.getColor(), queenColor) && figureColor;
    }

    private boolean checkIfQueenBeatsTwoNextFigures(int row, int col, int row1, int row2) {
        return !getFigure(row, col).getColor().equals(FigureColor.EMPTY_FIELD) && !getFigure(row1, row2).getColor().equals(FigureColor.EMPTY_FIELD);
    }

    private void checkBeatingPawnDownRight(int row, int col) {
        if (row + 2 < 9 && col + 2 < 9) {
            if (getFigure(row + 2, col + 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row + 2, col + 2);
                Point beatingFigurePoint = new Point(row + 1, col + 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint);
            }
        }
    }

    private void addPawnBeating(int row, int col, Point emptyPoint, Point beatingFigurePoint) {
        if (getFigure(row, col).getColor().equals(FigureColor.BLACK_PAWN)) {
            if (getFigure(beatingFigurePoint.getRow(), beatingFigurePoint.getCol()).getColor().isInGroup(FigureColor.Group.WHITE)) {
                checkBeatingBlack.add(new Move(row, col, emptyPoint.getRow(), emptyPoint.getCol()));
            }
        }
        if (getFigure(row, col).getColor().equals(FigureColor.WHITE_PAWN)) {
            if (getFigure(beatingFigurePoint.getRow(), beatingFigurePoint.getCol()).getColor().isInGroup(FigureColor.Group.BLACK)) {
                checkBeatingWhite.add(new Move(row, col, emptyPoint.getRow(), emptyPoint.getCol()));
            }
        }
    }

    private void checkBeatingPawnDownLeft(int row, int col) {
        if (col - 2 > 0 && row + 2 < 9) {
            if (getFigure(row + 2, col - 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row + 2, col - 2);
                Point beatingFigurePoint = new Point(row + 1, col - 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint);
            }
        }
    }

    private void checkBeatingPawnUpRight(int row, int col) {
        if (row - 2 > 0 && col + 2 < 9) {
            if (getFigure(row - 2, col + 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row - 2, col + 2);
                Point beatingFigurePoint = new Point(row - 1, col + 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint);
            }
        }
    }

    private void checkBeatingPawnUpLeft(int row, int col) {
        if (row - 2 > 0 && col - 2 > 0) {
            if (getFigure(row - 2, col - 2).getColor().equals(FigureColor.EMPTY_FIELD)) {
                Point emptyPoint = new Point(row - 2, col - 2);
                Point beatingFigurePoint = new Point(row - 1, col - 1);
                addPawnBeating(row, col, emptyPoint, beatingFigurePoint);
            }
        }
    }

    void checkWhatFigure(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());

        if (isFigurePawn(figureFrom)) {
            gameMoves.movePawn(move, this);
        }
        if (isFigureQueen(figureFrom)) {
            gameMoves.moveQueen(move, this);
        }
    }

    boolean isFigureQueen(Figure figureFrom) {
        return areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN) || areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN);
    }

    boolean isFigurePawn(Figure figureFrom) {
        return areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN) || areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN);
    }

    boolean isFigureBlack(int row, int col) {
        return getFigure(row, col).getColor().isInGroup(FigureColor.Group.BLACK);
    }

    boolean isFigureWhite(int row, int col) {
        return getFigure(row, col).getColor().isInGroup(FigureColor.Group.WHITE);
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

    ArrayList<FigurePoint> saveBoardFigurePoints() {
        saveBoard.clear();
        for (int row = 1; row < GameMoves.SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < GameMoves.SIZE_OF_THE_BOARD + 1; col++) {
                saveBoard.add(new FigurePoint(new Point(row, col), getFigure(row, col)));
            }
        return saveBoard;
    }
}
