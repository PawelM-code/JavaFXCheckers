package main.java.logic;

import javafx.scene.image.ImageView;

public class Queen extends Figure {
    public Queen (String color) {
        super(color);
    }

    @Override
    public ImageView getImageView() {
        if(getColor().equals("d")){
            ImageView whitePawn = new ImageView("/main/resources/pawn/whiteQueen.png");
            whitePawn.setFitWidth(40);
            whitePawn.setFitHeight(40);
            return whitePawn;
        }else {
            ImageView blackPawn = new ImageView("/main/resources/pawn/blackQueen.png");
            blackPawn.setFitWidth(40);
            blackPawn.setFitHeight(40);
            return blackPawn;
        }
    }
}