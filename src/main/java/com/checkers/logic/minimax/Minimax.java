package com.checkers.logic.minimax;

import com.checkers.logic.Board;
import com.checkers.logic.GameNextMoves;
import com.checkers.logic.Move;
import com.checkers.logic.figures.FigureColor;
import com.checkers.logic.figures.FigurePoint;
import com.checkers.logic.figures.FigurePointScore;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Minimax {
    private Board board;
    private GameNextMoves gameNextMoves = new GameNextMoves();

    public Minimax(Board board) {
        this.board = board;
    }

    public Move minimax() {
        getAvailableMovesAndBeating();
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

        if (board.getCheckBeatingBlack().size() > 0) {
            createNodeListWhenBeating(nodeOne, boardStart, board.getCheckBeatingBlack(), FigureColor.Group.BLACK, board);
        } else {
            createNodeListWhenOnlyMove(nodeOne, boardStart, gameNextMoves.getAvailableMovesBlack(), board);
        }
        for (NodeOne node : nodeOne) {
            board.setBoard(node.getBoardState());
            getAvailableMovesAndBeating();
            ArrayList<NodeOne> temp = new ArrayList<>();
            if (board.getCheckBeatingWhite().size() > 0) {
                createNodeListWhenBeating(temp, node.getBoardState(), board.getCheckBeatingWhite(), FigureColor.Group.WHITE, board);
            } else {
                createNodeListWhenOnlyMove(temp, node.getBoardState(), gameNextMoves.getAvailableMovesWhite(), board);
            }
            nodeTwo.add(new NodeTwo(node.getMove(), temp));
        }
        for (NodeTwo nodeTwo1 : nodeTwo) {
            for (NodeOne nodeOneList : nodeTwo1.getNodeOneList()) {
                board.setBoard(nodeOneList.getBoardState());
                getAvailableMovesAndBeating();
                ArrayList<NodeOne> temp = new ArrayList<>();
                if (board.getCheckBeatingBlack().size() > 0) {
                    blackFirstSimulateMoveList = new ArrayList<>(board.getCheckBeatingBlack());
                    for (Move move : blackFirstSimulateMoveList) {
                        board.getGameMove().simulateBeating(FigureColor.Group.BLACK, move, board);
                        int score = scoringAlgorithm(boardScore, board) + nodeOneList.getScore();
                        temp.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), score));
                        board.setBoard(nodeOneList.getBoardState());
                    }
                } else {
                    blackFirstSimulateMoveList = new ArrayList<>(gameNextMoves.getAvailableMovesBlack());
                    for (Move move : blackFirstSimulateMoveList) {
                        board.getGameMove().simulateMove(move, board);
                        int score = scoringAlgorithm(boardScore, board);
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
            getAvailableMovesAndBeating();
            if (board.getCheckBeatingBlack().size() > 0) {
                Random rand = new Random();
                int randInt = rand.nextInt(board.getCheckBeatingBlack().size());

                return board.getCheckBeatingBlack().get(randInt);
            } else {
                Random rand = new Random();
                int randInt = rand.nextInt(gameNextMoves.getAvailableMovesBlack().size());

                return gameNextMoves.getAvailableMovesBlack().get(randInt);
            }
        }
    }

    private void createNodeListWhenOnlyMove(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> availableMovesBlackOrWhite, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(availableMovesBlackOrWhite);
        for (Move move : blackFirstSimulateMoveList) {
            board.getGameMove().simulateMove(move, board);
            nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            board.setBoard(boardStart);
        }
    }

    private void createNodeListWhenBeating(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> checkBeatingBlackOrWhite, FigureColor.Group blackOrWhite, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(checkBeatingBlackOrWhite);
        for (Move move : blackFirstSimulateMoveList) {
            board.getGameMove().simulateBeating(blackOrWhite, move, board);
            if (blackOrWhite.equals(FigureColor.Group.WHITE)) {
                nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), -10));
            } else {
                nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            }
            board.setBoard(boardStart);
        }
    }

    private int scoringAlgorithm(ArrayList<FigurePointScore> boardScore, Board board) {
        board.saveBoardFigurePoints();

        int countBlackFigure = 0;
        int countWhiteFigure = 0;

        for (FigurePoint figurePoint : board.getSavedBoard()) {
            if (board.getGameValidators().isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                countBlackFigure = getCountFigureOnTheBoard(countBlackFigure, figurePoint, board);
            }
            if (board.getGameValidators().isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                countWhiteFigure = getCountFigureOnTheBoard(countWhiteFigure, figurePoint, board);
            }

            for (FigurePointScore figurePointScore : boardScore) {
                if (figurePointScore.getRow() == figurePoint.getPoint().getRow() && figurePointScore.getCol() == figurePoint.getPoint().getCol()) {
                    if (board.getGameValidators().isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                        countBlackFigure += figurePointScore.getScore();
                        break;
                    }
                    if (board.getGameValidators().isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol(), board)) {
                        countWhiteFigure += figurePointScore.getScore();
                        break;
                    }
                }
            }
        }
        return (countBlackFigure - countWhiteFigure);
    }

    private int getCountFigureOnTheBoard(int countFigure, FigurePoint figurePoint, Board board) {
        if (board.getGameValidators().isFigurePawn(figurePoint.getFigure(), board)) {
            countFigure ++;
        }
        if (board.getGameValidators().isFigureQueen(figurePoint.getFigure(), board)) {
            countFigure += 2;
        }
        return countFigure;
    }

    private void getAvailableMovesAndBeating() {
        gameNextMoves.checkIfFigureIsBeatingAllBoard(board);
        gameNextMoves.getAvailableMove(board);
    }
}
