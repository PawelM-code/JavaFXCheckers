package main.java.logic.figures;

public class FigurePointScore {
    private int row;
    private int col;
    private int score;

    public FigurePointScore(int row, int col, int score) {
        this.row = row;
        this.col = col;
        this.score = score;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getScore() {
        return score;
    }
}
