package main.java.logic;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class Minimax {
    private Board board;

    Minimax(Board board) {
        this.board = board;
    }

    Move minimax() throws Exception {
        board.getAvailableMovesAndBeating();
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
            createNodeListWhenOnlyMove(nodeOne, boardStart, board.checkBeatingBlack, FigureColor.Group.BLACK, board);
        } else {
            createNodeListWhenBeating(nodeOne, boardStart, board.availableMovesBlack, board);
        }
        for (NodeOne node : nodeOne) {
            board.setBoard(node.getBoardState());
            board.getAvailableMovesAndBeating();
            ArrayList<NodeOne> temp = new ArrayList<>();
            if (board.checkBeatingWhite.size() > 0) {
                createNodeListWhenOnlyMove(temp, node.getBoardState(), board.checkBeatingWhite, FigureColor.Group.WHITE, board);
            } else {
                createNodeListWhenBeating(temp, node.getBoardState(), board.availableMovesWhite, board);
            }
            nodeTwo.add(new NodeTwo(node.getMove(), temp));
        }
        for (NodeTwo nodeTwo1 : nodeTwo) {
            for (NodeOne nodeOneList : nodeTwo1.getNodeOneList()) {
                board.setBoard(nodeOneList.getBoardState());
                board.getAvailableMovesAndBeating();
                ArrayList<NodeOne> temp = new ArrayList<>();
                if (board.checkBeatingBlack.size() > 0) {
                    blackFirstSimulateMoveList = new ArrayList<>(board.checkBeatingBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        board.simulateBeating(FigureColor.Group.BLACK, move);
                        scoringAlgorithm(boardScore, nodeOneList, temp, move, board);
                    }
                } else {
                    blackFirstSimulateMoveList = new ArrayList<>(board.availableMovesBlack);
                    for (Move move : blackFirstSimulateMoveList) {
                        board.simulateMove(move);
                        scoringAlgorithm(boardScore, nodeOneList, temp, move, board);
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
            board.getAvailableMovesAndBeating();
            if (board.checkBeatingBlack.size() > 0) {
                Random rand = new Random();
                int randInt = rand.nextInt(board.checkBeatingBlack.size());

                return board.checkBeatingBlack.get(randInt);
            } else {
                Random rand = new Random();
                int randInt = rand.nextInt(board.availableMovesBlack.size());

                return board.availableMovesBlack.get(randInt);
            }
        }
    }

    private void createNodeListWhenBeating(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> availableMovesBlack, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(availableMovesBlack);
        for (Move move : blackFirstSimulateMoveList) {
            board.simulateMove(move);
            nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            board.setBoard(boardStart);
        }
    }

    private void createNodeListWhenOnlyMove(ArrayList<NodeOne> nodeOne, ArrayList<FigurePoint> boardStart, ArrayList<Move> checkBeatingBlack, FigureColor.Group black, Board board) {
        ArrayList<Move> blackFirstSimulateMoveList;
        blackFirstSimulateMoveList = new ArrayList<>(checkBeatingBlack);
        for (Move move : blackFirstSimulateMoveList) {
            board.simulateBeating(black, move);
            nodeOne.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), 0));
            board.setBoard(boardStart);
        }
    }

    private void scoringAlgorithm(ArrayList<FigurePointScore> boardScore, NodeOne nodeOneList, ArrayList<NodeOne> temp, Move move, Board board) {
        board.saveBoardFigurePoints();

        int countBlackFigure = 0;
        int countWhiteFigure = 0;

        for (FigurePoint figurePoint : board.saveBoard) {
            if (board.isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                countBlackFigure = getCountFigureOnTheBoard(countBlackFigure, figurePoint, board);
            }
            if (board.isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                countWhiteFigure = getCountFigureOnTheBoard(countWhiteFigure, figurePoint, board);
            }

            for (FigurePointScore figurePointScore : boardScore) {
                if (figurePointScore.getRow() == figurePoint.getPoint().getRow() && figurePointScore.getCol() == figurePoint.getPoint().getCol()) {
                    if (board.isFigureBlack(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                        countBlackFigure += figurePointScore.getScore();
                        break;
                    }
                    if (board.isFigureWhite(figurePoint.getPoint().getRow(), figurePoint.getPoint().getCol())) {
                        countWhiteFigure += figurePointScore.getScore();
                        break;
                    }
                }
            }
        }
        int score = countBlackFigure - countWhiteFigure;

        temp.add(new NodeOne(move, new ArrayList<>(board.saveBoardFigurePoints()), score));
        board.setBoard(nodeOneList.getBoardState());
    }

    private int getCountFigureOnTheBoard(int countFigure, FigurePoint figurePoint, Board board) {
        if (board.isFigurePawn(figurePoint.getFigure())) {
            countFigure++;
        }
        if (board.isFigureQueen(figurePoint.getFigure())) {
            countFigure += 2;
        }
        return countFigure;
    }
}