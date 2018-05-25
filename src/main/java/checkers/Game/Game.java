package checkers.Game;

import checkers.Menu;
import checkers.board.*;
import checkers.figures.*;
import checkers.moves.*;
import exceptions.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Board board;
    private List<String> moves;
    private boolean player;
    private boolean simplePrint;
    private int whiteQueenMoves;
    private int blackQueenMoves;

    public Game() {
        board = new Board();
        moves = new LinkedList<>();
        player = false;
        simplePrint = false;
        whiteQueenMoves = 0;
        blackQueenMoves = 0;

		board.setFigure('A', 2, new Pawn(true));
		board.setFigure('A', 4, new Pawn(true));
		board.setFigure('A', 6, new Pawn(true));
		board.setFigure('A', 8, new Pawn(true));
		board.setFigure('B', 1, new Pawn(true));
		board.setFigure('B', 3, new Pawn(true));
		board.setFigure('B', 5, new Pawn(true));
		board.setFigure('B', 7, new Pawn(true));
		board.setFigure('C', 2, new Pawn(true));
        board.setFigure('C', 4, new Pawn(true));
        board.setFigure('C', 6, new Pawn(true));
		board.setFigure('C', 8, new Pawn(true));

		board.setFigure('F', 1, new Pawn(false));
		board.setFigure('F', 3, new Pawn(false));
        board.setFigure('F', 5, new Pawn(false));
        board.setFigure('F', 7, new Pawn(false));
        board.setFigure('G', 2, new Pawn(false));
        board.setFigure('G', 4, new Pawn(false));
		board.setFigure('G', 6, new Pawn(false));
		board.setFigure('G', 8, new Pawn(false));
		board.setFigure('H', 1, new Pawn(false));
		board.setFigure('H', 3, new Pawn(false));
		board.setFigure('H', 5, new Pawn(false));
		board.setFigure('H', 7, new Pawn(false));
    }

    public void play(){
        boolean b;
        do {
            if (VictoryValidator.validateEndOfGame(board, whiteQueenMoves, blackQueenMoves, player)) {
                endOfGame();
                break;
            }
            b = this.waitForMove();
        } while (b);
    }

    private boolean waitForMove() {
        Scanner sc = new Scanner(System.in);
        String s;
        String captures = "";
        Menu.cls();
        if(this.simplePrint)
            board.printSimple();
        else
            System.out.println(board);
        System.out.print("Player: " + (player ? "BLACK" : "WHITE"));
        System.out.println(". Enter your next move, or \"h\" for move history: ");
        try {
            (new CapturePossibilityValidator(board,player)).validateCapturePossibility();
        }
        catch(CapturePossibleException e){
            captures = e.getMessage();
            System.out.println("You have to capture: " + captures);
        }
        s = sc.nextLine();
        if(inGameMenu(s)) {
            if (s.equals("x"))
                return false;
            else
                return true;
        }
        s = s.toUpperCase();
        try{
            validate(s);
            if(captures.isEmpty() || captures.contains(s))
                this.makeMove(s);
            else{
                System.out.println("You have to capture!");
                Menu.waitForEnter();
                return true;
            }
        }
        catch(IncorrectMoveFormat e){
            System.out.println("Incorrect move format! Proper format example: E4-D5");
            Menu.waitForEnter();
            return true;
        }
        return true;
    }

    private void printMoveHistory() {
        if(moves.isEmpty())
            System.out.println("No moves history.");
        else
            for (String m : moves)
                System.out.println(m);
    }

    private void validate(String s) throws IncorrectMoveFormat {
        String[] sArray = s.split("-");
        if (sArray.length != 2)
            throw new IncorrectMoveFormat();
        for (String t : sArray)
            if (t.length() != 2)
                throw new IncorrectMoveFormat();
    }

    private void makeMove(String s) throws IncorrectMoveFormat{
        String[] sArray = s.split("-");
        char x1 = sArray[0].charAt(0);
        int y1 = Character.getNumericValue(sArray[0].charAt(1));
        char x2 = sArray[1].charAt(0);
        int y2 = Character.getNumericValue(sArray[1].charAt(1));
        System.out.println("Trying to make move: " + x1 + y1 + " to " + x2 + y2 + ".");
        Move move = new Move(x1, y1, x2, y2);
        try {
            MoveValidator.validateMove(move, this.board, this.player);
            moves.add((player ? "black: " : "white: ") + move);
            move.makeMove(board);
            if(board.getFigure(move.getRow2(),move.getCol2()) instanceof Queen){
                if(player)
                    blackQueenMoves++;
                else
                    whiteQueenMoves++;
            }
            else{
                if(player)
                    blackQueenMoves = 0;
                else
                    whiteQueenMoves = 0;
            }
            this.player = !this.player;
            System.out.println("Move done.");
        }catch (CaptureException e){
            moves.add((player ? "black: " : "white: ") + move);
            move.makeCapture(board,e.getRow(),e.getCol());
            multiCapture(move);
            System.out.println("Capture done.");
            if(player)
                blackQueenMoves = 0;
            else
                whiteQueenMoves = 0;
            this.player = !this.player;
        }catch (IncorrectMoveException e){
            System.out.println("Incorrect move: " + e.getMessage());
        }finally{
            if((board.getFigure(move.getRow2(), move.getCol2()) instanceof Pawn)
                    && board.getFigure(move.getRow2(), move.getCol2()).getColor()
                    && (move.getRow2()) == 'H')
                board.setFigure('H', move.getCol2(), new Queen(true));
            if((board.getFigure(move.getRow2(), move.getCol2()) instanceof Pawn)
                    && !board.getFigure(move.getRow2(), move.getCol2()).getColor()
                    && (move.getRow2()) == 'A')
                board.setFigure('A', move.getCol2(), new Queen(false));
            Menu.waitForEnter();
        }
    }

    private void multiCapture(Move move) {
        do {
            try{
                (new CapturePossibilityValidator(board,player)).validateCapturePossibilityForOneFigure(move.getRow2(),move.getCol2());
                break;
            }
            catch(CapturePossibleException e){
                Scanner sc = new Scanner(System.in);
                String s;
                Menu.cls();
                if(simplePrint)
                    board.printSimple();
                else
                    System.out.println(board);
                System.out.print("Player: " + (player ? "BLACK" : "WHITE"));
                System.out.println(". You have to continue capturing, enter your move or \"h\" for move history: ");
                System.out.println("Possible captures: " + e.getMessage());
                s = sc.nextLine();
                s = s.toUpperCase();
                try{
                    validate(s);
                    if(e.getMessage().contains(s)){
                        String[] sArray = s.split("-");
                        char x1 = sArray[0].charAt(0);
                        int y1 = Character.getNumericValue(sArray[0].charAt(1));
                        char x2 = sArray[1].charAt(0);
                        int y2 = Character.getNumericValue(sArray[1].charAt(1));
                        move = new Move(x1, y1, x2, y2);
                        try{
                            MoveValidator.validateMove(move,this.board,this.player);
                            System.out.println("Incorrect move!");
                            Menu.waitForEnter();
                        }
                        catch(CaptureException e1){
                            moves.add((player ? "black: " : "white: ") + move);
                            move.makeCapture(board,e1.getRow(),e1.getCol());
                        }
                        catch(IncorrectMoveException e1){
                            System.out.println("Incorrect move!");
                            Menu.waitForEnter();
                        }
                    }
                    else {
                        System.out.println("Incorrect move!");
                        Menu.waitForEnter();
                    }
                }
                catch(IncorrectMoveFormat e1){
                    System.out.println("Incorrect move format! Proper format example: E4-D5");
                    Menu.waitForEnter();
                    continue;
                }
                continue;
            }
        }while (true);
    }

    private boolean inGameMenu(String s){
        switch (s) {
            case "h":
                this.printMoveHistory();
                Menu.waitForEnter();
                return true;
            case "p":
                this.simplePrint = !this.simplePrint;
                return true;
            case "x":
                return true;
            default:
                break;
        }
        return false;
    }

    private void endOfGame(){
        Menu.cls();
        if(this.simplePrint)
            board.printSimple();
        else
            System.out.println(board);
        System.out.println("Game over!");
        if(VictoryValidator.isDraw()) {
            System.out.println("\tDRAW!");
        }
        else {
            if (VictoryValidator.getWinner())
                System.out.println("\tBLACK WINS!");
            else
                System.out.println("\tWHITE WINS!");
        }
        Menu.waitForEnter();
    }

}