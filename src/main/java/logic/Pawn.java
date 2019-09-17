package main.java.logic;

import javafx.scene.image.ImageView;

public class Pawn extends Figure {
    public Pawn(FigureColor color) {
        super(color);
    }

    @Override
    public ImageView getImageView() {
        if (getColor().equals(FigureColor.WHITE_PAWN)) {
            ImageView whitePawn = new ImageView("/main/resources/pawn/whitePawn2.png");
            whitePawn.setFitWidth(40);
            whitePawn.setFitHeight(40);
            return whitePawn;
        } else {
            ImageView blackPawn = new ImageView("/main/resources/pawn/blackPawn2.png");
            blackPawn.setFitWidth(40);
            blackPawn.setFitHeight(40);
            return blackPawn;
        }
    }
}
