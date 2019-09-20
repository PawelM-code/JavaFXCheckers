package com.checkers.logic.figures;

import javafx.scene.image.ImageView;

public abstract class Figure {
    private FigureColor color;

    public Figure(FigureColor color) {
        this.color = color;
    }

    public FigureColor getColor() {
        return color;
    }

    public abstract ImageView getImageView();
}
