package main.java.logic;

import javafx.scene.image.ImageView;

public class Queen extends Figure {
    public Queen(FigureColor color) {
        super(color);
    }

    @Override
    public ImageView getImageView() {
        if (getColor().equals(FigureColor.WHITE_QUEEN)) {
            ImageView whitePawn = new ImageView("/main/resources/pawn/whiteQueen2.png");
            whitePawn.setFitWidth(40);
            whitePawn.setFitHeight(40);
            return whitePawn;
        } else {
            ImageView blackPawn = new ImageView("/main/resources/pawn/blackQueen2.png");
            blackPawn.setFitWidth(40);
            blackPawn.setFitHeight(40);
            return blackPawn;
        }
    }
}
