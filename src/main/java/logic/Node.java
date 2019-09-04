package main.java.logic;

import java.util.ArrayList;
import java.util.Map;

public class Node {
    private ArrayList<FigurePoint> saveBoard;
    private Map<Move, ArrayList<Move>> treeMoves;

    public ArrayList<FigurePoint> getSaveBoard() {
        return saveBoard;
    }

    public Map<Move, ArrayList<Move>> getTreeMoves() {
        return treeMoves;
    }

    public Node(ArrayList<FigurePoint> saveBoard, Map<Move, ArrayList<Move>> treeMoves) {
        this.saveBoard = saveBoard;
        this.treeMoves = treeMoves;
    }
}
