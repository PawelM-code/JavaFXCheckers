package main.java.logic.figures;

import javafx.scene.image.ImageView;
import main.java.logic.figures.Figure;
import main.java.logic.figures.FigureColor;

public class None extends Figure {
    public None() {
        super(FigureColor.EMPTY_FIELD);
    }

    @Override
    public ImageView getImageView() {
        return new ImageView();
    }
}
