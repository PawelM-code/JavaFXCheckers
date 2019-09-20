package com.checkers.logic.minimax;

import com.checkers.logic.Move;

import java.util.ArrayList;

public class NodeTwo {
    private Move move;
    private ArrayList<NodeOne> nodeOneList;

    public Move getMove() {
        return move;
    }

    ArrayList<NodeOne> getNodeOneList() {
        return nodeOneList;
    }

    NodeTwo(Move move, ArrayList<NodeOne> nodeOneList) {
        this.move = move;
        this.nodeOneList = nodeOneList;
    }
}
