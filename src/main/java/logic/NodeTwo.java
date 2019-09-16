package main.java.logic;

import java.util.ArrayList;

public class NodeTwo {
    private Move move;
    private ArrayList<NodeOne> nodeOneList;

    public Move getMove() {
        return move;
    }

    public ArrayList<NodeOne> getNodeOneList() {
        return nodeOneList;
    }

    public NodeTwo(Move move, ArrayList<NodeOne> nodeOneList) {
        this.move = move;
        this.nodeOneList = nodeOneList;
    }
}
