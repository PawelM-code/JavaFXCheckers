package main.java.logic;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class Figure {
    private String color;

    public Figure(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }

    public abstract ImageView getImageView();
}
