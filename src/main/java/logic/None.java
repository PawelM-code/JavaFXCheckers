package main.java.logic;

import javafx.scene.image.ImageView;

public class None extends Figure {
    public None() {
        super(" ");
    }

    @Override
    public ImageView getImageView() {
        return new ImageView();
    }
}
