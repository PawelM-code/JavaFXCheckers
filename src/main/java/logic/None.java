package main.java.logic;

import javafx.scene.image.ImageView;

public class None extends Figure {
    public None() {
        super(FigureColor.EMPTY_FIELD);
    }

    @Override
    public ImageView getImageView() {
        return new ImageView();
    }
}
