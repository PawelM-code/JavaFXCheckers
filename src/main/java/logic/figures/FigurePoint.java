package main.java.logic.figures;

public class FigurePoint {
    private Point point;
    private Figure figure;

    public Point getPoint() {
        return point;
    }

    public Figure getFigure() {
        return figure;
    }

    public FigurePoint(Point point, Figure figure) {
        this.point = point;
        this.figure = figure;
    }
}
