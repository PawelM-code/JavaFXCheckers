package com.checkers.logic.minimax;

import com.checkers.logic.Move;

import java.util.ArrayList;

class NodeThree {
    private Move moveOne;
    private Move moveTwo;
    private ArrayList<NodeOne> movesThree;

    Move getMoveOne() {
        return moveOne;
    }

    Move getMoveTwo() {
        return moveTwo;
    }

    ArrayList<NodeOne> getMovesThree() {
        return movesThree;
    }

    NodeThree(Move moveOne, Move moveTwo, ArrayList<NodeOne> movesThree) {
        this.moveOne = moveOne;
        this.moveTwo = moveTwo;
        this.movesThree = movesThree;
    }
}
