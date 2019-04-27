package checkers.moves;

import checkers.board.*;
import checkers.figures.*;
import exceptions.*;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Move implements Serializable {

    private char row1;
    private char row2;
    private int col1;
    private int col2;

    public Move(char row1, int col1, char row2, int col2) throws IncorrectMoveFormat {
        row1 = Character.toUpperCase(row1);
        row2 = Character.toUpperCase(row2);
        List possibles = Arrays.asList('A','B','C','D','E','F','G','H');

        if (possibles.contains(row1))
            this.row1 = row1;
        else
            throw new IncorrectMoveFormat();
        if (col1 <= 8 && col1 >= 1)
            this.col1 = col1;
        else
            throw new IncorrectMoveFormat();
        if (possibles.contains(row2))
            this.row2 = row2;
        else
            throw new IncorrectMoveFormat();
        if (col2 <= 8 && col2 >= 1)
            this.col2 = col2;
        else
            throw new IncorrectMoveFormat();
    }

    private int rowCharToInt(char row) {
        switch (row) {
            case 'A':
                return 1;
            case 'B':
                return 2;
            case 'C':
                return 3;
            case 'D':
                return 4;
            case 'E':
                return 5;
            case 'F':
                return 6;
            case 'G':
                return 7;
            default:
                return 8;
        }
    }

    public char getRow1() {
        return this.row1;
    }

    public int getRow1int() {
        return this.rowCharToInt(this.row1);
    }

    public int getCol1() {
        return this.col1;
    }

    public char getRow2() {
        return this.row2;
    }

    public int getRow2int() {
        return this.rowCharToInt(this.row2);
    }

    public int getCol2() {
        return this.col2;
    }

    public void makeMove(Board board) {
        board.setFigure(this.row2, this.col2, board.getFigure(this.row1, this.col1));
        board.setFigure(this.row1, this.col1, new None(false));
    }

    public void makeCapture(Board board, char row, int col) {
        this.makeMove(board);
        board.setFigure(row, col, new None(board.getFigure(this.row1, this.col1).getColor()));
    }

    @Override
    public String toString() {
        return "" + row1 + col1 + "-" + row2 + col2;
    }

}