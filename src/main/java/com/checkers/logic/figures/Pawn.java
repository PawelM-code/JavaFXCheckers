package com.checkers.logic.figures;

import javafx.scene.image.ImageView;

public class Pawn extends Figure {
    public Pawn(FigureColor color) {
        super(color);
    }

    @Override
    public ImageView getImageView() {
        if (getColor().equals(FigureColor.WHITE_PAWN)) {
            ImageView whitePawn = new ImageView("/pawn/whitePawn.png");
            whitePawn.setFitWidth(40);
            whitePawn.setFitHeight(40);
            return whitePawn;
        } else {
            ImageView blackPawn = new ImageView("/pawn/blackPawn.png");
            blackPawn.setFitWidth(40);
            blackPawn.setFitHeight(40);
            return blackPawn;
        }
    }
}
