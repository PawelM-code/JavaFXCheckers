package test;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import main.java.logic.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationTestSuite {
    private Figure figure1 = new Pawn("b"); //black
    private Figure figure2 = new Pawn("w"); //white
    private Figure figure3 = new Queen("d"); //white
    private Figure figure4 = new Queen("e"); //black
    private Board board;

    @Before
    public void setup(){
        UserDialogs mockUserDialogs = mock(UserDialogs.class);
        GridPane mockGrid = mock(GridPane.class);
        ObservableList<Node> mockObservableList = mock(ObservableList.class);
        when(mockGrid.getChildren()).thenReturn(mockObservableList);
        board = new Board(mockGrid, mockUserDialogs);
    }

    private void testMove(Move move) {
        board.move(move);
    }

    @Test
    public void testGetFigureColor() {
        Assert.assertEquals(figure1.getColor(), "b");
        Assert.assertEquals(figure2.getColor(), "w");
        Assert.assertEquals(figure3.getColor(), "d");
        Assert.assertEquals(figure4.getColor(), "e");
    }

    @Test
    public void testGetFigure() {
        board.initWhiteOrBlackMove();
        board.setFigure(1, 2, figure1);

        String result1 = board.getFigure(1, 2).getColor();

        Assert.assertEquals(result1, "b");
    }

    @Test
    public void testMovePawn() {
        board.initWhiteOrBlackMove();
        Move move1 = new Move(8, 1, 7, 2);
        Move move2 = new Move(1, 1, 2, 2);

        board.setFigure(8, 1, figure2);
        board.setFigure(1, 1, figure1);

        testMove(move1);
        testMove(move2);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(1, 1).getColor();
        String result4 = board.getFigure(2, 2).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, "w");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, "b");
    }

    @Test
    public void testMoveQueen() {
        Move move1 = new Move(8, 1, 1, 8);
        Move move2 = new Move(1, 1, 8, 8);
        Move move3 = new Move(8, 8, 8, 1);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure3);
        board.setFigure(1, 1, figure4);

        testMove(move1);
        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(1, 8).getColor();

        testMove(move2);
        String result3 = board.getFigure(1, 1).getColor();
        String result4 = board.getFigure(8, 8).getColor();

        testMove(move3);
        String result5 = board.getFigure(1, 1).getColor();
        String result6 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, "d");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, "e");
        Assert.assertEquals(result5, " ");
        Assert.assertEquals(result6, "e");
    }

    @Test
    public void testChangePawnToQueen() {
        Move move1 = new Move(2, 3, 1, 4);
        Move move2 = new Move(7, 2, 8, 1);

        board.initWhiteOrBlackMove();
        board.setFigure(2, 3, figure2);
        board.setFigure(7, 2, figure1);

        testMove(move1);
        testMove(move2);

        String result1 = board.getFigure(2, 3).getColor();
        String result2 = board.getFigure(1, 4).getColor();
        String result3 = board.getFigure(7, 2).getColor();
        String result4 = board.getFigure(8, 1).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, "d");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, "e");
    }

    @Test
    public void testSimpleBeatingPawn() {
        Move move1 = new Move(8, 1, 6, 3);
        Move move2 = new Move(1, 3, 3, 1);
        Move move3 = new Move(8, 7, 6, 5);
        Move move4 = new Move(1, 6, 3, 8);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure2);
        board.setFigure(7, 2, figure1);
        board.setFigure(2, 2, figure2);
        board.setFigure(1, 3, figure1);
        board.setFigure(8, 7, figure2);
        board.setFigure(7, 6, figure4);
        board.setFigure(1, 6, figure1);
        board.setFigure(2, 7, figure3);

        testMove(move1);
        testMove(move2);
        testMove(move3);
        testMove(move4);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(6, 3).getColor();
        String result4 = board.getFigure(1, 3).getColor();
        String result5 = board.getFigure(2, 2).getColor();
        String result6 = board.getFigure(3, 1).getColor();
        String result7 = board.getFigure(8, 7).getColor();
        String result8 = board.getFigure(7, 6).getColor();
        String result9 = board.getFigure(6, 5).getColor();
        String result10 = board.getFigure(1, 6).getColor();
        String result11 = board.getFigure(2, 7).getColor();
        String result12 = board.getFigure(3, 8).getColor();


        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, " ");
        Assert.assertEquals(result3, "w");
        Assert.assertEquals(result4, " ");
        Assert.assertEquals(result5, " ");
        Assert.assertEquals(result6, "b");
        Assert.assertEquals(result7, " ");
        Assert.assertEquals(result8, " ");
        Assert.assertEquals(result9, "w");
        Assert.assertEquals(result10, " ");
        Assert.assertEquals(result11, " ");
        Assert.assertEquals(result12, "b");
    }

    @Test
    public void testMultipleBeatingPawn() {
        Move move1 = new Move(8, 1, 6, 3);
        Move move2 = new Move(6, 3, 8, 5);
        Move move3 = new Move(1, 6, 3, 8);
        Move move4 = new Move(3, 8, 5, 6);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure2);
        board.setFigure(7, 2, figure1);
        board.setFigure(7, 4, figure1);
        board.setFigure(1, 6, figure1);
        board.setFigure(2, 7, figure2);
        board.setFigure(4, 7, figure2);

        testMove(move1);
        testMove(move2);
        testMove(move3);
        testMove(move4);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(6, 3).getColor();
        String result4 = board.getFigure(7, 4).getColor();
        String result5 = board.getFigure(8, 5).getColor();
        String result6 = board.getFigure(1, 6).getColor();
        String result7 = board.getFigure(2, 7).getColor();
        String result8 = board.getFigure(3, 8).getColor();
        String result9 = board.getFigure(4, 7).getColor();
        String result10 = board.getFigure(5, 6).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, " ");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, " ");
        Assert.assertEquals(result5, "w");
        Assert.assertEquals(result6, " ");
        Assert.assertEquals(result7, " ");
        Assert.assertEquals(result8, " ");
        Assert.assertEquals(result9, " ");
        Assert.assertEquals(result10, "b");
    }

    @Test
    public void testSimpleBeatingQueen() {
        Move move1 = new Move(8, 1, 1, 8);
        Move move2 = new Move(1, 2, 6, 7);
        Move move3 = new Move(3, 3, 5, 5);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure3);
        board.setFigure(1, 2, figure4);
        board.setFigure(3, 3, figure3);
        board.setFigure(6, 3, figure4);
        board.setFigure(2, 3, figure2);
        board.setFigure(4, 4, figure1);

        testMove(move1);
        testMove(move2);
        testMove(move3);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(6, 3).getColor();
        String result3 = board.getFigure(1, 8).getColor();
        String result4 = board.getFigure(1, 2).getColor();
        String result5 = board.getFigure(2, 3).getColor();
        String result6 = board.getFigure(6, 7).getColor();
        String result7 = board.getFigure(3, 3).getColor();
        String result8 = board.getFigure(4, 4).getColor();
        String result9 = board.getFigure(5, 5).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, " ");
        Assert.assertEquals(result3, "d");
        Assert.assertEquals(result4, " ");
        Assert.assertEquals(result5, " ");
        Assert.assertEquals(result6, "e");
        Assert.assertEquals(result7, " ");
        Assert.assertEquals(result8, " ");
        Assert.assertEquals(result9, "d");
    }

    @Test
    public void test1MultipleBeatingQueen() {
        Move move1 = new Move(8, 1, 1, 8);
        Move move2 = new Move(3, 3, 8, 8);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure3);
        board.setFigure(7, 2, figure1);
        board.setFigure(5, 4, figure1);
        board.setFigure(3, 6, figure4);
        board.setFigure(3, 3, figure4);
        board.setFigure(4, 4, figure2);
        board.setFigure(6, 6, figure2);

        testMove(move1);
        testMove(move2);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(5, 4).getColor();
        String result4 = board.getFigure(3, 6).getColor();
        String result5 = board.getFigure(1, 8).getColor();
        String result6 = board.getFigure(3, 3).getColor();
        String result7 = board.getFigure(4, 4).getColor();
        String result8 = board.getFigure(6, 6).getColor();
        String result9 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, " ");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, " ");
        Assert.assertEquals(result5, "d");
        Assert.assertEquals(result6, " ");
        Assert.assertEquals(result7, " ");
        Assert.assertEquals(result8, " ");
        Assert.assertEquals(result9, "e");
    }

    @Test
    public void test2MultipleBeatingQueen() {
        Move move1 = new Move(8, 1, 5, 4);
        Move move2 = new Move(5, 4, 2, 1);

        board.initWhiteOrBlackMove();
        board.setFigure(8, 1, figure3);
        board.setFigure(7, 2, figure1);
        board.setFigure(3, 2, figure4);
        board.setFigure(1, 7, figure1);
        board.setFigure(2, 6, figure2);

        testMove(move1);
        testMove(move2);

        String result1 = board.getFigure(8, 1).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(5, 4).getColor();
        String result4 = board.getFigure(3, 2).getColor();
        String result5 = board.getFigure(2, 1).getColor();

        Assert.assertEquals(result1, " ");
        Assert.assertEquals(result2, " ");
        Assert.assertEquals(result3, " ");
        Assert.assertEquals(result4, " ");
        Assert.assertEquals(result5, "d");
    }

    @Test
    public void test1BeatingQueenNotAllowed() {
        Move move1 = new Move(5, 4, 8, 1);

        board.initWhiteOrBlackMove();
        board.setFigure(5, 4, figure3);
        board.setFigure(7, 2, figure1);
        board.setFigure(6, 3, figure1);

        testMove(move1);

        String result1 = board.getFigure(5, 4).getColor();
        String result2 = board.getFigure(7, 2).getColor();
        String result3 = board.getFigure(6, 3).getColor();
        String result5 = board.getFigure(8, 1).getColor();

        Assert.assertEquals(result1, "d");
        Assert.assertEquals(result2, "b");
        Assert.assertEquals(result3, "b");
        Assert.assertEquals(result5, " ");
    }

    @Test
    public void test2BeatingQueenNotAllowed() {
        Move move2 = new Move(3, 3, 8, 8);

        board.initWhiteOrBlackMove();
        board.setFigure(3, 3, figure4);
        board.setFigure(4, 4, figure2);
        board.setFigure(5, 5, figure2);
        board.setFigure(6, 6, figure2);

        testMove(move2);

        String result6 = board.getFigure(3, 3).getColor();
        String result7 = board.getFigure(4, 4).getColor();
        String result4 = board.getFigure(5, 5).getColor();
        String result8 = board.getFigure(6, 6).getColor();
        String result9 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result6, "e");
        Assert.assertEquals(result7, "w");
        Assert.assertEquals(result4, "w");
        Assert.assertEquals(result8, "w");
        Assert.assertEquals(result9, " ");
    }

    @Test
    public void testUserDialog() {

    }


}
