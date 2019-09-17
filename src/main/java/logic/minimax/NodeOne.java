package main.java.logic.minimax;

import main.java.logic.Move;
import main.java.logic.figures.FigurePoint;

import java.util.ArrayList;

public class NodeOne {
    private Move move;
    private ArrayList<FigurePoint> boardState;
    private int score;

    public NodeOne(Move move, ArrayList<FigurePoint> boardState, int score) {
        this.move = move;
        this.boardState = boardState;
        this.score = score;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public void setBoardState(ArrayList<FigurePoint> boardState) {
        this.boardState = boardState;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public ArrayList<FigurePoint> getBoardState() {
        return boardState;
    }

    public int getScore() {
        return score;
    }
}
