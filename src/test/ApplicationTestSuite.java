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
    private Figure figure1 = new Pawn(FigureColor.BLACK_PAWN); //black
    private Figure figure2 = new Pawn(FigureColor.WHITE_PAWN); //white
    private Figure figure3 = new Queen(FigureColor.WHITE_QUEEN); //white
    private Figure figure4 = new Queen(FigureColor.BLACK_QUEEN); //black
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
        board.gameMoves.move(move, board);
    }

    @Test
    public void testGetFigureColor() {
        Assert.assertEquals(figure1.getColor(), FigureColor.BLACK_PAWN);
        Assert.assertEquals(figure2.getColor(), FigureColor.WHITE_PAWN);
        Assert.assertEquals(figure3.getColor(), FigureColor.WHITE_QUEEN);
        Assert.assertEquals(figure4.getColor(), FigureColor.BLACK_QUEEN);
    }

    @Test
    public void testGetFigure() {
        board.initWhiteOrBlackMove();
        board.setFigure(1, 2, figure1);

        FigureColor result1 = board.getFigure(1, 2).getColor();

        Assert.assertEquals(result1, FigureColor.BLACK_PAWN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(1, 1).getColor();
        FigureColor result4 = board.getFigure(2, 2).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.BLACK_PAWN);
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
        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(1, 8).getColor();

        testMove(move2);
        FigureColor result3 = board.getFigure(1, 1).getColor();
        FigureColor result4 = board.getFigure(8, 8).getColor();

        testMove(move3);
        FigureColor result5 = board.getFigure(1, 1).getColor();
        FigureColor result6 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.WHITE_QUEEN);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.BLACK_QUEEN);
        Assert.assertEquals(result5, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result6, FigureColor.BLACK_QUEEN);
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

        FigureColor result1 = board.getFigure(2, 3).getColor();
        FigureColor result2 = board.getFigure(1, 4).getColor();
        FigureColor result3 = board.getFigure(7, 2).getColor();
        FigureColor result4 = board.getFigure(8, 1).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.WHITE_QUEEN);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.BLACK_QUEEN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(6, 3).getColor();
        FigureColor result4 = board.getFigure(1, 3).getColor();
        FigureColor result5 = board.getFigure(2, 2).getColor();
        FigureColor result6 = board.getFigure(3, 1).getColor();
        FigureColor result7 = board.getFigure(8, 7).getColor();
        FigureColor result8 = board.getFigure(7, 6).getColor();
        FigureColor result9 = board.getFigure(6, 5).getColor();
        FigureColor result10 = board.getFigure(1, 6).getColor();
        FigureColor result11 = board.getFigure(2, 7).getColor();
        FigureColor result12 = board.getFigure(3, 8).getColor();


        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result3, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result4, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result5, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result6, FigureColor.BLACK_PAWN);
        Assert.assertEquals(result7, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result8, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result9, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result10, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result11, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result12, FigureColor.BLACK_PAWN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(6, 3).getColor();
        FigureColor result4 = board.getFigure(7, 4).getColor();
        FigureColor result5 = board.getFigure(8, 5).getColor();
        FigureColor result6 = board.getFigure(1, 6).getColor();
        FigureColor result7 = board.getFigure(2, 7).getColor();
        FigureColor result8 = board.getFigure(3, 8).getColor();
        FigureColor result9 = board.getFigure(4, 7).getColor();
        FigureColor result10 = board.getFigure(5, 6).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result5, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result6, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result7, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result8, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result9, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result10, FigureColor.BLACK_PAWN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(6, 3).getColor();
        FigureColor result3 = board.getFigure(1, 8).getColor();
        FigureColor result4 = board.getFigure(1, 2).getColor();
        FigureColor result5 = board.getFigure(2, 3).getColor();
        FigureColor result6 = board.getFigure(6, 7).getColor();
        FigureColor result7 = board.getFigure(3, 3).getColor();
        FigureColor result8 = board.getFigure(4, 4).getColor();
        FigureColor result9 = board.getFigure(5, 5).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result3, FigureColor.WHITE_QUEEN);
        Assert.assertEquals(result4, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result5, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result6, FigureColor.BLACK_QUEEN);
        Assert.assertEquals(result7, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result8, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result9, FigureColor.WHITE_QUEEN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(5, 4).getColor();
        FigureColor result4 = board.getFigure(3, 6).getColor();
        FigureColor result5 = board.getFigure(1, 8).getColor();
        FigureColor result6 = board.getFigure(3, 3).getColor();
        FigureColor result7 = board.getFigure(4, 4).getColor();
        FigureColor result8 = board.getFigure(6, 6).getColor();
        FigureColor result9 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result5, FigureColor.WHITE_QUEEN);
        Assert.assertEquals(result6, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result7, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result8, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result9, FigureColor.BLACK_QUEEN);
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

        FigureColor result1 = board.getFigure(8, 1).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(5, 4).getColor();
        FigureColor result4 = board.getFigure(3, 2).getColor();
        FigureColor result5 = board.getFigure(2, 1).getColor();

        Assert.assertEquals(result1, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result2, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result3, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result4, FigureColor.EMPTY_FIELD);
        Assert.assertEquals(result5, FigureColor.WHITE_QUEEN);
    }

    @Test
    public void test1BeatingQueenNotAllowed() {
        Move move1 = new Move(5, 4, 8, 1);

        board.initWhiteOrBlackMove();
        board.setFigure(5, 4, figure3);
        board.setFigure(7, 2, figure1);
        board.setFigure(6, 3, figure1);

        testMove(move1);

        FigureColor result1 = board.getFigure(5, 4).getColor();
        FigureColor result2 = board.getFigure(7, 2).getColor();
        FigureColor result3 = board.getFigure(6, 3).getColor();
        FigureColor result5 = board.getFigure(8, 1).getColor();

        Assert.assertEquals(result1, FigureColor.WHITE_QUEEN);
        Assert.assertEquals(result2, FigureColor.BLACK_PAWN);
        Assert.assertEquals(result3, FigureColor.BLACK_PAWN);
        Assert.assertEquals(result5, FigureColor.EMPTY_FIELD);
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

        FigureColor result6 = board.getFigure(3, 3).getColor();
        FigureColor result7 = board.getFigure(4, 4).getColor();
        FigureColor result4 = board.getFigure(5, 5).getColor();
        FigureColor result8 = board.getFigure(6, 6).getColor();
        FigureColor result9 = board.getFigure(8, 8).getColor();

        Assert.assertEquals(result6, FigureColor.BLACK_QUEEN);
        Assert.assertEquals(result7, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result4, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result8, FigureColor.WHITE_PAWN);
        Assert.assertEquals(result9, FigureColor.EMPTY_FIELD);
    }

    @Test
    public void testExceptionCatchMove() throws Exception {
        board.initWhiteOrBlackMove();
        Move move1 = new Move(8, 7, 7, 6);

        board.setFigure(1, 6, figure3);
        board.setFigure(6, 7, figure2);
        board.setFigure(7, 8, figure2);
        board.setFigure(8, 7, figure2);
        board.setFigure(4, 7, figure1);

        testMove(move1);
        board.computerMove();

        FigureColor result1 = board.getFigure(5, 6).getColor();
        FigureColor result2 = board.getFigure(5, 8).getColor();

        Assert.assertTrue(result1.equals(FigureColor.EMPTY_FIELD) || result1.equals(FigureColor.BLACK_PAWN));
        Assert.assertTrue(result2.equals(FigureColor.EMPTY_FIELD) || result2.equals(FigureColor.BLACK_PAWN));
    }


}
