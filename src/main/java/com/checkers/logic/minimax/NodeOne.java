package com.checkers.logic.minimax;

import com.checkers.logic.Move;
import com.checkers.logic.figures.FigurePoint;

import java.util.ArrayList;

public class NodeOne {
    private Move move;
    private ArrayList<FigurePoint> boardState;
    private int score;

    NodeOne(Move move, ArrayList<FigurePoint> boardState, int score) {
        this.move = move;
        this.boardState = boardState;
        this.score = score;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    void setScore(int score) {
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    ArrayList<FigurePoint> getBoardState() {
        return boardState;
    }

    int getScore() {
        return score;
    }
}
