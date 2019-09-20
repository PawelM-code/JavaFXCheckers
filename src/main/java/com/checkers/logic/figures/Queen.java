package com.checkers.logic.figures;

import javafx.scene.image.ImageView;

public class Queen extends Figure {
    public Queen(FigureColor color) {
        super(color);
    }

    @Override
    public ImageView getImageView() {
        if (getColor().equals(FigureColor.WHITE_QUEEN)) {
            ImageView whitePawn = new ImageView("/pawn/whiteQueen.png");
            whitePawn.setFitWidth(40);
            whitePawn.setFitHeight(40);
            return whitePawn;
        } else {
            ImageView blackPawn = new ImageView("/pawn/blackQueen.png");
            blackPawn.setFitWidth(40);
            blackPawn.setFitHeight(40);
            return blackPawn;
        }
    }
}
