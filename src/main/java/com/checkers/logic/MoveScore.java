package com.checkers.logic;

public class MoveScore {
    private Move move;
    private int score;

    public MoveScore(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }
}
