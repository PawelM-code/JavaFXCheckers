package main.java.logic;

import java.util.ArrayList;
import java.util.Map;

public class NodeScore {
    private ArrayList<FigurePoint> saveBoard;
    private Map<Move, ArrayList<MoveScore>> treeMoves;

    public NodeScore(ArrayList<FigurePoint> saveBoard, Map<Move, ArrayList<MoveScore>> treeMoves) {
        this.saveBoard = saveBoard;
        this.treeMoves = treeMoves;
    }

    public ArrayList<FigurePoint> getSaveBoard() {
        return saveBoard;
    }

    public Map<Move, ArrayList<MoveScore>> getTreeMoves() {
        return treeMoves;
    }
}
