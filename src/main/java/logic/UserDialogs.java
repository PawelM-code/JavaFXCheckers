package main.java.logic;

import javafx.scene.text.Text;
import org.junit.Ignore;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserDialogs {
    private static Text textUserDialogs = null;

    public UserDialogs(Text textUserDialogs) {
        UserDialogs.textUserDialogs = textUserDialogs;
    }

    public Text getTextUserDialogs() {
        return textUserDialogs;
    }

    public  void showEndInfo() {
        System.out.println("End of the game.");
        textUserDialogs.setText("End of the game.");
    }

    public  void showMoveColor(ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            System.out.println("Move black");
            textUserDialogs.setText("Move black");
        } else {
            System.out.println("Move white");
            textUserDialogs.setText("Move white");
        }
    }

     void showMoveColorWhenStillBeating(ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            System.out.println("Move white");
            textUserDialogs.setText("Move white");
        } else {
            System.out.println("Move black");
            textUserDialogs.setText("Move black");
        }
    }

     void showInfoWhenWrongColorStarts(ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE)) {
            System.out.println("Move not allowed, try again WHITE.");
            textUserDialogs.setText("Move not allowed, try again WHITE.");
        } else {
            System.out.println("Move not allowed, try again BLACK.");
            textUserDialogs.setText("Move not allowed, try again BLACK.");
        }
    }

     void showInfoMoveNotAllowed() {
        System.out.println("Move not allowed, try again.");
        textUserDialogs.setText("Move not allowed, try again.");
    }

     void showInfoBeatingNotAllowed() {
        System.out.println("Beating not allowed, try again.");
        textUserDialogs.setText("Beating not allowed, try again.");
    }

     void showBeating(ArrayList<String> checkBeatingBlack, ArrayList<String> checkBeatingWhite, ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE) && checkBeatingWhite.size() > 0) {
            System.out.println("Beating: " + checkBeatingWhite.stream()
                    .distinct()
                    .collect(Collectors.toList()));
        } else if (whiteOrBlackMove.peek().equals(FigureColor.Group.BLACK) && checkBeatingBlack.size() > 0) {
            System.out.println("Beating: " + checkBeatingBlack.stream()
                    .distinct()
                    .collect(Collectors.toList()));
        }
    }

    public  void showStartGameInfo() {
        System.out.println("Start Game. [e.g. A1B2] \nMove white.");
        textUserDialogs.setText("Start Game. Move white!");
    }
}
