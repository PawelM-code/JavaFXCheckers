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
/*
    public  String getNextMove() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter your move: ");
            String input = scanner.nextLine();
            if ("ABCDEFGH".contains(input.substring(0, 1).toUpperCase()) &&
                    "12345678".contains(input.substring(1, 2)) &&
                    "ABCDEFGH".contains(input.substring(2, 3).toUpperCase()) &&
                    "12345678".contains(input.substring(3, 4)) &&
                    input.length() == 4)
                return input;
            System.out.println("Wrong move try again");
        }
    }*/

    public  void showMoveColor(ArrayDeque<String> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals("white")) {
            System.out.println("Move black");
            textUserDialogs.setText("Move black");
        } else {
            System.out.println("Move white");
            textUserDialogs.setText("Move white");
        }
    }

     void showMoveColorWhenStillBeating(ArrayDeque<String> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals("white")) {
            System.out.println("Move white");
            textUserDialogs.setText("Move white");
        } else {
            System.out.println("Move black");
            textUserDialogs.setText("Move black");
        }
    }

     void showInfoWhenWrongColorStarts(ArrayDeque<String> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals("white")) {
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

     void showBeating(ArrayList<String> checkBeatingBlack, ArrayList<String> checkBeatingWhite, ArrayDeque<String> whiteOrBlackMove) {
        if (whiteOrBlackMove.peek().equals("white") && checkBeatingWhite.size() > 0) {
            System.out.println("Beating: " + checkBeatingWhite.stream()
                    .distinct()
                    .collect(Collectors.toList()));
        } else if (whiteOrBlackMove.peek().equals("black") && checkBeatingBlack.size() > 0) {
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
