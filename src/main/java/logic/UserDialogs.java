package main.java.logic;

import javafx.scene.text.Text;
import main.java.logic.figures.FigureColor;

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

    public void showEndInfoWhiteWon() {
        System.out.println("End of the game. White Won!");
        textUserDialogs.setText("End of the game. White Won!");
    }

    public void showEndInfoBlackWon() {
        System.out.println("End of the game. Black Won!");
        textUserDialogs.setText("End of the game. Black Won!");
    }

    public void showMoveColor(ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
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

    void showBeating(ArrayList<Move> checkBeatingBlack, ArrayList<Move> checkBeatingWhite, ArrayDeque<FigureColor.Group> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals(FigureColor.Group.WHITE) && checkBeatingWhite.size() > 0) {
            convertBeatingToStringAndShow(checkBeatingWhite);
        } else if (whiteOrBlackMove.peek().equals(FigureColor.Group.BLACK) && checkBeatingBlack.size() > 0) {
            convertBeatingToStringAndShow(checkBeatingBlack);
        }
    }

    private void convertBeatingToStringAndShow(ArrayList<Move> checkBeatingWhite) {
        ArrayList<String> white = new ArrayList<>();
        for (Move move : checkBeatingWhite) {
            white.add("" + move.getRow1() + move.getCol1() + move.getRow2() + move.getCol2());
        }
        System.out.println("Beating: " + white.stream()
                .distinct()
                .collect(Collectors.toList()));
    }

    public void showStartGameInfo() {
        System.out.println("Start Game. [e.g. A1B2] \nMove white.");
        textUserDialogs.setText("Start Game. Move white!");
    }
}
