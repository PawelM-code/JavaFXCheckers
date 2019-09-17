package main.java.logic;

import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toList;
import static main.java.gui.BoardGui.FIELD_SIZE;

public class Board {
    private static final int SIZE_OF_THE_BOARD = 8;
    private final UserDialogs userDialogs;
    private LinkedList<LinkedList<Figure>> boardRow;
    private ArrayList<Move> checkBeatingWhite = new ArrayList<>();
    private ArrayList<Move> checkBeatingBlack = new ArrayList<>();
    private ArrayDeque<FigureColor.Group> whiteOrBlackMove = new ArrayDeque<>();
    private ArrayList<Point> currentWhiteFigures = new ArrayList<>();
    private ArrayList<Point> currentBlackFigures = new ArrayList<>();
    private ArrayList<Move> availableMovesWhite = new ArrayList<>();
    private ArrayList<Move> availableMovesBlack = new ArrayList<>();
    private ArrayList<FigurePoint> saveBoard = new ArrayList<>();
    private Minimax minimax;
    private Move saveLastMove;
    private boolean addStatus = false;
    private GridPane grid;

    public Board(GridPane grid, UserDialogs userDialogs) {
        this.grid = grid;
        this.userDialogs = userDialogs;
        initBoard();
    }

    public void displayOnGrid() {
        grid.getChildren().clear();
        for (int row = 0; row < SIZE_OF_THE_BOARD; row++)
            for (int col = 0; col < SIZE_OF_THE_BOARD; col++) {
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
        for (int row = 0; row < SIZE_OF_THE_BOARD; row++)
            initRow(row);
    }

    private void initRow(int row) {
        boardRow.add(row, new LinkedList<>());
        for (int col = 0; col < SIZE_OF_THE_BOARD; col++)
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
        for (int row = 1; row < SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < SIZE_OF_THE_BOARD + 1; col++) {
                if (isFigureWhite(row, col)) {
                    currentWhiteFigures.add(new Point(row, col));
                }
                if (isFigureBlack(row, col)) {
                    currentBlackFigures.add(new Point(row, col));
                }
            }
    }

    public void getAvailableMove() {
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
        while (row2 > 1 && col2 < SIZE_OF_THE_BOARD) {
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
        while (row2 < SIZE_OF_THE_BOARD && col2 > 1) {
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
        while (row2 < SIZE_OF_THE_BOARD && col2 < SIZE_OF_THE_BOARD) {
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

    private ArrayList<FigurePoint> saveBoardFigurePoints() {
        saveBoard.clear();
        for (int row = 1; row < SIZE_OF_THE_BOARD + 1; row++)
            for (int col = 1; col < SIZE_OF_THE_BOARD + 1; col++) {
                saveBoard.add(new FigurePoint(new Point(row, col), getFigure(row, col)));
            }
        return saveBoard;
    }

    private void setBoard(ArrayList<FigurePoint> boardState) {
        initBoard();
        for (FigurePoint figurePoint : boardState) {
            int row = figurePoint.getPoint().getRow();
            int col = figurePoint.getPoint().getCol();
            Figure figure = figurePoint.getFigure();
            setFigure(row, col, figure);
        }
    }

    private void simulateBeating(FigureColor.Group whiteOrBlack, Move move) {
        Move startMove = move;
        ArrayDeque<FigureColor.Group> whiteOrBlackMoveCopy = new ArrayDeque<>(whiteOrBlackMove);
        while (nextFigureColor().equals(whiteOrBlack)) {
            move(startMove);
            checkIfFigureIsBeatingAllBoard();
            if (whiteOrBlack.equals(FigureColor.Group.BLACK)) {
                if (checkBeatingBlack.size() > 0) {
                    startMove = checkBeatingBlack.get(0);
                }
            } else {
                if (checkBeatingWhite.size() > 0) {
                    startMove = checkBeatingWhite.get(0);
                }
            }

        }
        whiteOrBlackMove = whiteOrBlackMoveCopy;
    }

    private void clearAvailableMoves() {
        availableMovesWhite.clear();
        availableMovesBlack.clear();
    }

    private Move minimax() throws Exception {
        getAvailableMovesAndBeating();
        ArrayList<Move> blackFirstSimulateMoveList;
        ArrayList<NodeOne> nodeOne = new ArrayList<>();
        ArrayList<NodeTwo> nodeTwo = new ArrayList<>();
        ArrayList<NodeThree> nodeThree = new ArrayList<>();
        ArrayList<FigurePoint> boardStart = new ArrayList<>(saveBoardFigurePoints());

        ArrayList<FigurePointScore> boardScore = new ArrayList<>();

        boardScore.add(new FigurePointScore(1, 2, 4));
        boardScore.add(new FigurePointScore(1, 4, 4));
        boardScore.add(new FigurePointScore(1, 6, 4));
        boardScore.add(new FigurePointScore(1, 8, 4));
        boardScore.add(new FigurePointScore(2, 1, 4));
        boardScore.add(new FigurePointScore(2, 3, 3));
        boardScore.add(new FigurePointScore(2, 5, 3));
        boardScore.add(new FigurePointScore(2, 7, 3));
        boardScore.add(new FigurePointScore(3, 2, 3));
        boardScore.add(new FigurePointScore(3, 4, 2));
        boardScore.add(new FigurePointScore(3, 6, 2));
        boardScore.add(new FigurePointScore(3, 8, 4));
        boardScore.add(new FigurePointScore(4, 1, 4));
        boardScore.add(new FigurePointScore(4, 3, 2));
        boardScore.add(new FigurePointScore(4, 5, 1));
        boardScore.add(new FigurePointScore(4, 7, 3));
        boardScore.add(new FigurePointScore(5, 2, 3));
        boardScore.add(new FigurePointScore(5, 4, 1));
        boardScore.add(new FigurePointScore(5, 6, 2));
        boardScore.add(new FigurePointScore(5, 8, 4));
        boardScore.add(new FigurePointScore(6, 1, 4));
        boardScore.add(new FigurePointScore(6, 3, 2));
        boardScore.add(new FigurePointScore(6, 5, 2));
        boardScore.add(new FigurePointScore(6, 7, 3));
        boardScore.add(new FigurePointScore(7, 2, 3));
        boardScore.add(new FigurePointScore(7, 4, 3));
        boardScore.add(new FigurePointScore(7, 6, 3));
        boardScore.add(new FigurePointScore(7, 8, 4));
        boardScore.add(new FigurePointScore(8, 1, 4));
        boardScore.add(new FigurePointScore(8, 3, 4));
        boardScore.add(new FigurePointScore(8, 5, 4));
        boardScore.add(new FigurePointScore(8, 7, 4));

        if (checkBeatingBlack.size() > 0) {
            createNodeListWhenOnlyMove(nodeOne, boardStart, checkBeatingBlack, FigureColor.Group.BLACK);
        } else {
            createNodeListWhenBeating(nodeOne, boardStart, availableMovesBlack);
        }
        for (NodeOne node : nodeOne) {
            setBoard(node.getBoardState());
            getAvailableMovesAndBeating();
            ArrayList<NodeOne> temp = new ArrayList<>();
            if (checkBeatingWhite.size() > 0) {
                createNodeListWhenOnlyMove(temp, node.getBoardState(), checkBeatingWhite, FigureColor.Group.WHITE);
            } else {
                createNodeListWhenBeating(temp, node.getBoardState(), availableMovesWhite);
            }
            nodeTwo.add(new NodeTwo(node.getMove(), temp));
        }
        for (NodeTwo nodeTwo1 : nodeTwo) {
            for (NodeOne nodeOneList : nodeTwo1.getNodeOneList()) {
                setBoard(nodeOneList.getBoardState());
                getAvailableMovesAndBeating();
                ArrayList<NodeOne> temp = new ArrayList<>();
                if (checkBeatingBlack.size() > 0) {
                    blackFirstSimulateMoveList = new ArrayList<>(checkBeatingBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        simulateBeating(FigureColor.Group.BLACK, move);
                        scoringAlgorithm(boardScore, nodeOneList, temp, move);
                    }
                } else {
                    blackFirstSimulateMoveList = new ArrayList<>(availableMovesBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        simulateMove(move);
                        scoringAlgorithm(boardScore, nodeOneList, temp, move);
                    }
                }
                nodeThree.add(new NodeThree(nodeTwo1.getMove(), nodeOneList.getMove(), temp));
            }
        }
        setBoard(boardStart);

        try {
            for (NodeThree newNodeThree : nodeThree) {
                NodeOne maxElement = Collections.max(newNodeThree.getMovesThree(), Comparator.comparingInt(NodeOne::getScore));
                int maxScore = maxElement.getScore();

                OptionalInt indexOne = IntStream.range(0, nodeTwo.size())
                        .filter(e -> nodeTwo.get(e).getMove().equals(newNodeThree.getMoveOne())).findFirst();

                OptionalInt indexTwo = IntStream.range(0, nodeTwo.get(indexOne.getAsInt()).getNodeOneList().size())
                        .filter(e -> nodeTwo.get(indexOne.getAsInt()).getNodeOneList().get(e).getMove().equals(newNodeThree.getMoveTwo())).findFirst();

                nodeTwo.get(indexOne.getAsInt()).getNodeOneList().get(indexTwo.getAsInt()).setScore(maxScore);
            }

            for (NodeTwo newNodeTwo : nodeTwo) {
                NodeOne minElement = Collections.min(newNodeTwo.getNodeOneList(), Comparator.comparingInt(NodeOne::getScore));
                int minScore = minElement.getScore();

                OptionalInt indexOne = IntStream.range(0, nodeOne.size())
                        .filter(e -> nodeOne.get(e).getMove().equals(newNodeTwo.getMove())).findFirst();

                nodeOne.get(indexOne.getAsInt()).setScore(minScore);
            }

            NodeOne maxElement = Collections.max(nodeOne, Comparator.comparingInt(NodeOne::getScore));
            int maxScore = maxElement.getScore();

            ArrayList<NodeOne> maxScoreList = (ArrayList<NodeOne>) nodeOne.stream().filter(e -> e.getScore() == maxScore).collect(toList());
            Random rand = new Random();
            int randInt = rand.nextInt(maxScoreList.size());

            return maxScoreList.get(randInt).getMove();
        } catch (NoSuchElementException e) {
            getAvailableMovesAndBeating();
            if (checkBeatingBlack.size() > 0) {
                Random rand = new Random();
                int randInt = rand.nextInt(checkBeatingBlack.size());

                return checkBeatingBlack.get(randInt);
            } else {
                Random rand = new Random();
                int randInt = rand.nextInt(availableMovesBlack.size());

                return availableMovesBlack.get(randInt);
            }
        }
    }

    private void getAvailableMovesAndBeating() {
        checkIfFigureIsBeatingAllBoard();
        getAvailableMove();
    }

    private void createNodeListWhenBeating(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> availableMovesBlack) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(availableMovesBlack);
        for (Move move : blackFirstSimulateMoveList) {
            simulateMove(move);
            nodeOne.add(new NodeOne(move, new ArrayList<>(saveBoardFigurePoints()), 0));
            setBoard(boardStart);
        }
    }

    private void createNodeListWhenOnlyMove(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> checkBeatingBlack, FigureColor.Group black) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(checkBeatingBlack);
        for (Move move : blackFirstSimulateMoveList) {
            simulateBeating(black, move);
            nodeOne.add(new NodeOne(move, new ArrayList<>(saveBoardFigurePoints()), 0));
            setBoard(boardStart);
        }
    }

    private void scoringAlgorithm(ArrayList<FigurePointScore> boardScore, NodeOne nodeOneList, ArrayList<NodeOne> temp, Move move) {
        saveBoardFigurePoints();

        int countBlackFigure = 0;
        int countWhiteFigure = 0;

        for (FigurePoint figurePoint : saveBoard) {
            if (isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                countBlackFigure = getCountFigureOnTheBoard(countBlackFigure, figurePoint);
            }
            if (isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                countWhiteFigure = getCountFigureOnTheBoard(countWhiteFigure, figurePoint);
            }

            for (FigurePointScore figurePointScore : boardScore) {
                if (figurePointScore.getRow() == figurePoint.getPoint().getRow() && figurePointScore.getCol() == figurePoint.getPoint().getCol()) {
                    if (isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                        countBlackFigure += figurePointScore.getScore();
                        break;
                    }
                    if (isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                        countWhiteFigure += figurePointScore.getScore();
                        break;
                    }
                }
            }
        }
        int score = countBlackFigure - countWhiteFigure;

        temp.add(new NodeOne(move, new ArrayList<>(saveBoardFigurePoints()), score));
        setBoard(nodeOneList.getBoardState());
    }

    private int getCountFigureOnTheBoard(int countFigure, FigurePoint figurePoint) {
        if (isFigurePawn(figurePoint.getFigure())) {
            countFigure++;
        }
        if (isFigureQueen(figurePoint.getFigure())) {
            countFigure += 2;
        }
        return countFigure;
    }

    private boolean isPlayerPresent(FigureColor.Group colors) {
        return IntStream
                .range(1, 9)
                .anyMatch(row -> IntStream
                        .range(1, 9)
                        .anyMatch(col -> getFigure(row, col).getColor().isInGroup(colors)));
    }

    public void simulateMove(Move move) {
        checkIfMoveIsOnTheBoardIfTrueTryMove(move);
    }

    public void move(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        checkIfFigureIsBeatingAllBoard();

        if (nextFigureColor().equals(FigureColor.Group.WHITE)) {
            moveFigureIfOfColor(move, figureFrom, checkBeatingWhite, FigureColor.Group.WHITE);
        } else {
            moveFigureIfOfColor(move, figureFrom, checkBeatingBlack, FigureColor.Group.BLACK);
        }
    }

    private FigureColor.Group nextFigureColor() {
        return whiteOrBlackMove.peek();
    }

    private void moveFigureIfOfColor(Move move, Figure figureFrom, ArrayList<Move> checkBeatingWhite, FigureColor.Group whiteOrBlack) {
        if (figureFrom.getColor().isInGroup(whiteOrBlack)) {
            tryMoveAndSetNextColorMove(move, checkBeatingWhite, whiteOrBlack);
        } else {
            userDialogs.showInfoWhenWrongColorStarts(whiteOrBlackMove);
        }
    }

    private boolean areColorsEqual(FigureColor firstColor, FigureColor secondColor) {
        return firstColor.equals(secondColor);
    }

    public void computerMove() throws Exception {
        while (nextFigureColor().equals(FigureColor.Group.BLACK)) {
            move(minimax());
        }
    }

    private void tryMoveAndSetNextColorMove(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group whiteOrBlack) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());

        if (checkBeatingWhiteOrBlack.size() == 0) {
            checkIfMoveIsOnTheBoardIfTrueTryMove(move);
            if (!figureFrom.getColor().equals(getFigure(move.getRow1(), move.getCol1()).getColor())) {
                userDialogs.showMoveColor(whiteOrBlackMove);
                setNextColorMove(whiteOrBlack);
            } else {
                userDialogs.showInfoMoveNotAllowed();
            }
        } else {
            if (isBeatingCorrect(move, checkBeatingWhiteOrBlack)) {
                doBeating(move, checkBeatingWhiteOrBlack, whiteOrBlack, figureFrom);
            } else {
                userDialogs.showInfoBeatingNotAllowed();
            }
        }
    }

    private void setNextColorMove(FigureColor.Group whiteOrBlack) {
        whiteOrBlackMove.poll();
        whiteOrBlackMove.offer(whiteOrBlack);
    }

    private void doBeating(Move move, ArrayList<Move> checkBeatingWhiteOrBlack, FigureColor.Group setWhiteOrBlackInQueue, Figure figureFrom) {
        checkIfMoveIsOnTheBoardIfTrueTryMove(move);
        if (figureFrom.getColor().equals(getFigure(move.getRow1(), move.getCol1()).getColor())) {
            userDialogs.showInfoMoveNotAllowed();
        } else {
            clearBeatingList();
            saveLastMove = move;
            checkIfFigureIsBeatingAllBoard();
            if (checkBeatingWhiteOrBlack.size() > 0) {
                userDialogs.showMoveColorWhenStillBeating(whiteOrBlackMove);
            } else {
                saveLastMove = null;
                userDialogs.showMoveColor(whiteOrBlackMove);
                setNextColorMove(setWhiteOrBlackInQueue);
            }
        }
    }

    public void initWhiteOrBlackMove() {
        if (whiteOrBlackMove.size() == 0) {
            whiteOrBlackMove.offer(FigureColor.Group.WHITE);
            whiteOrBlackMove.offer(FigureColor.Group.BLACK);
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

    public void checkIfFigureIsBeatingAllBoard() {
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

    public void clearBeatingList() {
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

    private void checkIfMoveIsOnTheBoardIfTrueTryMove(Move move) {
        Figure figureTo = getFigure(move.getRow2(), move.getCol2());
        if (areColorsEqual(figureTo.getColor(), FigureColor.EMPTY_FIELD) && move.getRow2() <= SIZE_OF_THE_BOARD && move.getRow2() >= 1 &&
                move.getCol2() <= SIZE_OF_THE_BOARD && move.getCol2() >= 1) {
            checkWhatFigure(move);
        }
    }

    private void checkWhatFigure(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());

        if (isFigurePawn(figureFrom)) {
            movePawn(move);
        }
        if (isFigureQueen(figureFrom)) {
            moveQueen(move);
        }
    }

    private boolean isFigureQueen(Figure figureFrom) {
        return areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN) || areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN);
    }

    private boolean isFigurePawn(Figure figureFrom) {
        return areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN) || areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN);
    }

    private void moveQueen(Move move) {
        int count = 0;
        checkQueenMoveIsDiagonal(move, count);
    }

    private void checkQueenMoveIsDiagonal(Move move, int count) {
        if (Math.abs(move.getRow2() - move.getRow1()) == Math.abs(move.getCol2() - move.getCol1())) {
            count = beatingQueenRightDown(move, count);
            count = beatingQueenLeftDown(move, count);
            count = beatingQueenLeftUp(move, count);
            count = beatingQueenRightUp(move, count);
        }
        if (count == Math.abs(move.getRow2() - move.getRow1())) {
            setFigureToANewField(move);
        }
    }

    private int beatingQueenRightUp(Move move, int count) {
        if (move.getRow2() < move.getRow1() && move.getCol2() > move.getCol1()) {
            Figure figureFrom = getFigure(move.getRow1(), move.getCol1());

            if (areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if (doQueenBeating(isFigureBlack(row1, col1), clearBeatingFigureByQueenRightUp(row1, col1), isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                int row1 = move.getRow1();
                int col1 = move.getCol1();

                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1++;
                    if (doQueenBeating(isFigureWhite(row1, col1), clearBeatingFigureByQueenRightUp(row1, col1), isFigureBlack(row1, col1)))
                        break;
                }
            }
        }
        return count;
    }

    private boolean clearBeatingFigureByQueenRightUp(int row, int col) {
        if (row - 1 > 0 && col + 1 <= 8) {
            if (getFigure(row - 1, col + 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private int beatingQueenLeftUp(Move move, int count) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() < move.getRow1() && move.getCol2() < move.getCol1()) {
            int row1 = move.getRow1();
            int col1 = move.getCol1();

            if (areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if (doQueenBeating(isFigureBlack(row1, col1), clearBeatingFigureByQueenLeftUp(row1, col1), isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1--;
                    col1--;
                    if (doQueenBeating(isFigureWhite(row1, col1), clearBeatingFigureByQueenLeftUp(row1, col1), isFigureBlack(row1, col1)))
                        break;
                }
            }
        }
        return count;
    }

    private boolean clearBeatingFigureByQueenLeftUp(int row, int col) {
        if (row - 1 > 0 && col - 1 > 0) {
            if (getFigure(row - 1, col - 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isFigureBlack(int row, int col) {
        return getFigure(row, col).getColor().isInGroup(FigureColor.Group.BLACK);
    }

    private int beatingQueenLeftDown(Move move, int count) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() > move.getRow1() && move.getCol2() < move.getCol1()) {
            int row = move.getRow1();
            int col = move.getCol1();

            if (areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if (doQueenBeating(isFigureBlack(row, col), clearBeatingFigureByQueenLeftDown(row, col), isFigureWhite(row, col)))
                        break;
                }
            }
            if (areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row != move.getRow2() && col != move.getCol2()) {
                    count++;
                    row++;
                    col--;
                    if (doQueenBeating(isFigureWhite(row, col), clearBeatingFigureByQueenLeftDown(row, col), isFigureBlack(row, col)))
                        break;
                }
            }
        }
        return count;
    }

    private boolean clearBeatingFigureByQueenLeftDown(int row, int col) {
        if (row + 1 <= 8 && col - 1 > 0) {
            if (getFigure(row + 1, col - 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private int beatingQueenRightDown(Move move, int count) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        if (move.getRow2() > move.getRow1() && move.getCol2() > move.getCol1()) {
            int row1 = move.getRow1();
            int col1 = move.getCol1();

            if (areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if (doQueenBeating(isFigureBlack(row1, col1), clearBeatingFigureByQueenRightDown(row1, col1), isFigureWhite(row1, col1)))
                        break;
                }
            }
            if (areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_QUEEN)) {
                while (row1 != move.getRow2() && col1 != move.getCol2()) {
                    count++;
                    row1++;
                    col1++;
                    if (doQueenBeating(isFigureWhite(row1, col1), clearBeatingFigureByQueenRightDown(row1, col1), isFigureBlack(row1, col1)))
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

    private boolean clearBeatingFigureByQueenRightDown(int row, int col) {
        if (row + 1 <= 8 && col + 1 <= 8) {
            if (getFigure(row + 1, col + 1).getColor().equals(FigureColor.EMPTY_FIELD)) {
                boardRow.get(row - 1).set(col - 1, new None());
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isFigureWhite(int row, int col) {
        return getFigure(row, col).getColor().isInGroup(FigureColor.Group.WHITE);
    }

    private void movePawn(Move move) {
        moveBlackPawn(move);
        moveWhitePawn(move);
    }

    private void moveWhitePawn(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        if (areColorsEqual(figureFrom.getColor(), FigureColor.WHITE_PAWN)) {
            if (isPawnMoveDiagonal(move.getRow1() - 1, move.getRow2(), move.getCol1(), move.getCol2())) {
                setFigureToANewField(move);
                changePawnToQueen(move);
            }
            beatingAPawnDownRight(move);
            beatingAPawnDownLeft(move);
            beatingAPawnUpRight(move);
            beatingAPawnUpLeft(move);
        }
    }

    private static boolean isPawnMoveDiagonal(int i, int row2, int col1, int col2) {
        return (i == row2) && (col1 + 1 == col2 || col1 - 1 == col2);
    }

    private void changePawnToQueen(Move move) {
        if (move.getRow2() == 1 && getFigure(move.getRow2(), move.getCol2()).getColor().equals(FigureColor.WHITE_PAWN)) {
            boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, new Queen(FigureColor.WHITE_QUEEN));
        }
        if (move.getRow2() == 8 && getFigure(move.getRow2(), move.getCol2()).getColor().equals(FigureColor.BLACK_PAWN)) {
            boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, new Queen(FigureColor.BLACK_QUEEN));
        }
    }

    private void moveBlackPawn(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        if (areColorsEqual(figureFrom.getColor(), FigureColor.BLACK_PAWN)) {
            if (isPawnMoveDiagonal(move.getRow1() + 1, move.getRow2(), move.getCol1(), move.getCol2())) {
                setFigureToANewField(move);
                changePawnToQueen(move);
            }
            beatingAPawnDownRight(move);
            beatingAPawnDownLeft(move);
            beatingAPawnUpRight(move);
            beatingAPawnUpLeft(move);
        }
    }

    private void beatingAPawnUpLeft(Move move) {
        if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnUpLeftChooseColor(move, FigureColor.Group.WHITE);
        }
        if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnUpLeftChooseColor(move, FigureColor.Group.BLACK);
        }
    }

    private void beatingAPawnUpLeftChooseColor(Move move, FigureColor.Group colors) {
        if ((move.getRow1() - 2 == move.getRow2()) && move.getCol1() - 2 == move.getCol2()) {
            if (getFigure(move.getRow2() + 1, move.getCol2() + 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move);
                boardRow.get(move.getRow2()).set(move.getCol2(), new None());
                changePawnToQueen(move);
            }
        }
    }

    private void beatingAPawnUpRight(Move move) {
        if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnUpRightChooseColor(move, FigureColor.Group.WHITE);
        } else if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnUpRightChooseColor(move, FigureColor.Group.BLACK);
        }
    }

    private void beatingAPawnUpRightChooseColor(Move move, FigureColor.Group colors) {
        if ((move.getRow1() - 2 == move.getRow2()) && move.getCol1() + 2 == move.getCol2()) {
            if (getFigure(move.getRow2() + 1, move.getCol2() - 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move);
                boardRow.get(move.getRow2()).set(move.getCol2() - 2, new None());
                changePawnToQueen(move);
            }
        }
    }

    private void beatingAPawnDownLeft(Move move) {
        if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnDownLeftChooseColor(move, FigureColor.Group.WHITE);
        } else if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnDownLeftChooseColor(move, FigureColor.Group.BLACK);
        }
    }

    private void beatingAPawnDownLeftChooseColor(Move move, FigureColor.Group colors) {
        if (move.getRow1() + 2 == move.getRow2() && move.getCol1() - 2 == move.getCol2()) {
            if (getFigure(move.getRow2() - 1, move.getCol2() + 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move);
                boardRow.get(move.getRow2() - 2).set(move.getCol2(), new None());
                changePawnToQueen(move);
            }
        }
    }

    private void beatingAPawnDownRight(Move move) {
        if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.BLACK_PAWN)) {
            beatingAPawnDownRightChooseColor(move, FigureColor.Group.WHITE);
        } else if (getFigure(move.getRow1(), move.getCol1()).getColor().equals(FigureColor.WHITE_PAWN)) {
            beatingAPawnDownRightChooseColor(move, FigureColor.Group.BLACK);
        }
    }

    private void beatingAPawnDownRightChooseColor(Move move, FigureColor.Group colors) {
        if (move.getRow1() + 2 == move.getRow2() && move.getCol1() + 2 == move.getCol2()) {
            if (getFigure(move.getRow2() - 1, move.getCol2() - 1).getColor().isInGroup(colors)) {
                setFigureToANewField(move);
                boardRow.get(move.getRow2() - 2).set(move.getCol2() - 2, new None());
                changePawnToQueen(move);
            }
        }
    }

    private void setFigureToANewField(Move move) {
        Figure figureFrom = getFigure(move.getRow1(), move.getCol1());
        boardRow.get(move.getRow1() - 1).set(move.getCol1() - 1, new None());
        boardRow.get(move.getRow2() - 1).set(move.getCol2() - 1, figureFrom);
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
}
