package main.java.logic.minimax;

import main.java.logic.*;
import main.java.logic.figures.FigureColor;
import main.java.logic.figures.FigurePoint;
import main.java.logic.figures.FigurePointScore;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Minimax {
    private Board board;
    private GameNextMoves gameNextMoves = new GameNextMoves(board);

    public Minimax(Board board) {
        this.board = board;
    }

    public Move minimax() throws Exception {
        board.minimax.getAvailableMovesAndBeating();
        ArrayList<Move> blackFirstSimulateMoveList;
        ArrayList<NodeOne> nodeOne = new ArrayList<>();
        ArrayList<NodeTwo> nodeTwo = new ArrayList<>();
        ArrayList<NodeThree> nodeThree = new ArrayList<>();
        ArrayList<FigurePoint> boardStart = new ArrayList<>(board.saveBoardFigurePoints());

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

        if (board.checkBeatingBlack.size() > 0) {
            createNodeListWhenBeating(nodeOne, boardStart, board.checkBeatingBlack, FigureColor.Group.BLACK, board);
        } else {
            createNodeListWhenOnlyMove(nodeOne, boardStart, gameNextMoves.availableMovesBlack, board);
        }
        for (NodeOne node : nodeOne) {
            board.setBoard(node.getBoardState());
            board.minimax.getAvailableMovesAndBeating();
            ArrayList<NodeOne> temp = new ArrayList<>();
            if (board.checkBeatingWhite.size() > 0) {
                createNodeListWhenBeating(temp, node.getBoardState(), board.checkBeatingWhite, FigureColor.Group.WHITE, board);
            } else {
                createNodeListWhenOnlyMove(temp, node.getBoardState(), gameNextMoves.availableMovesWhite, board);
            }
            nodeTwo.add(new NodeTwo(node.getMove(), temp));
        }
        for (NodeTwo nodeTwo1 : nodeTwo) {
            for (NodeOne nodeOneList : nodeTwo1.getNodeOneList()) {
                board.setBoard(nodeOneList.getBoardState());
                board.minimax.getAvailableMovesAndBeating();
                ArrayList<NodeOne> temp = new ArrayList<>();
                if (board.checkBeatingBlack.size() > 0) {
                    blackFirstSimulateMoveList = new ArrayList<>(board.checkBeatingBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        board.gameMove.simulateBeating(FigureColor.Group.BLACK, move, board);
                        int score = scoringAlgorithm(boardScore, nodeOneList, temp, move, board) + nodeOneList.getScore();
                        temp.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), score));
                        board.setBoard(nodeOneList.getBoardState());
                    }
                } else {
                    blackFirstSimulateMoveList = new ArrayList<>(gameNextMoves.availableMovesBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        board.gameMove.simulateMove(move, board);
                        int score = scoringAlgorithm(boardScore, nodeOneList, temp, move, board);
                        temp.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), score));
                        board.setBoard(nodeOneList.getBoardState());
                    }
                }
                nodeThree.add(new NodeThree(nodeTwo1.getMove(), nodeOneList.getMove(), temp));
            }
        }
        board.setBoard(boardStart);

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
            board.minimax.getAvailableMovesAndBeating();
            if (board.checkBeatingBlack.size() > 0) {
                Random rand = new Random();
                int randInt = rand.nextInt(board.checkBeatingBlack.size());

                return board.checkBeatingBlack.get(randInt);
            } else {
                Random rand = new Random();
                int randInt = rand.nextInt(gameNextMoves.availableMovesBlack.size());

                return gameNextMoves.availableMovesBlack.get(randInt);
            }
        }
    }

    private void createNodeListWhenOnlyMove(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> availableMovesBlackOrWhite, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(availableMovesBlackOrWhite);
        for (Move move : blackFirstSimulateMoveList) {
            board.gameMove.simulateMove(move, board);
            nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            board.setBoard(boardStart);
        }
    }

    private void createNodeListWhenBeating(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> checkBeatingBlackOrWhite, FigureColor.Group blackOrWhite, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(checkBeatingBlackOrWhite);
        for (Move move : blackFirstSimulateMoveList) {
            board.gameMove.simulateBeating(blackOrWhite, move, board);
            if (blackOrWhite.equals(FigureColor.Group.WHITE)) {
                nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), -10));
            } else {
                nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            }
            board.setBoard(boardStart);
        }
    }

    private int scoringAlgorithm(ArrayList<FigurePointScore> boardScore, NodeOne nodeOneList, ArrayList<NodeOne> temp, Move move, Board board) {
        board.saveBoardFigurePoints();

        int countBlackFigure = 0;
        int countWhiteFigure = 0;

        for (FigurePoint figurePoint : board.saveBoard) {
            if (board.gameValidators.isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                countBlackFigure = getCountFigureOnTheBoard(countBlackFigure, figurePoint, board);
            }
            if (board.gameValidators.isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                countWhiteFigure = getCountFigureOnTheBoard(countWhiteFigure, figurePoint, board);
            }

            for (FigurePointScore figurePointScore : boardScore) {
                if (figurePointScore.getRow() == figurePoint.getPoint().getRow() && figurePointScore.getCol() == figurePoint.getPoint().getCol()) {
                    if (board.gameValidators.isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                        countBlackFigure += figurePointScore.getScore();
                        break;
                    }
                    if (board.gameValidators.isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                        countWhiteFigure += figurePointScore.getScore();
                        break;
                    }
                }
            }
        }
        return (countBlackFigure - countWhiteFigure);
    }

    private int getCountFigureOnTheBoard(int countFigure, FigurePoint figurePoint, Board board) {
        if (board.gameValidators.isFigurePawn(figurePoint.getFigure(), board)) {
            countFigure++;
        }
        if (board.gameValidators.isFigureQueen(figurePoint.getFigure(), board)) {
            countFigure += 2;
        }
        return countFigure;
    }

    private void getAvailableMovesAndBeating() {
        gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
        gameNextMoves.getAvailableMove(board);
    }
}
