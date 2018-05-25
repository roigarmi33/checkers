package checkers.gameplay;

import checkers.Menu;
import checkers.board.Board;
import exceptions.IncorrectMoveFormat;
import exceptions.UnknownException;

import java.util.List;
import java.util.Scanner;

public class InGameUI {

    private static String[] options = {"h", "p", "s", "x"};

    //TEMPORARY!!!
    public static void printMoveHistory(List<String> moves) {
        if(moves.isEmpty())
            System.out.println("No moves history.");
        else
            for (String m : moves)
                System.out.println(m);
    }

    public static String sideMenu(int line,List<String> moves){
        String s = "            ";
        switch(line){
            case 1:
                return "\t\t\t\t\t\t\t\t\t╔═══════╦═══════╦════════╗";
            case 2:
                return "\t\t\t║ PAWN  ║ QUEEN ║        ║";
            case 3:
                return "\t\t╠═══════╬═══════╬════════╣";
            case 4:
                return "\t\t║ ┌───┐ ║ ╔═══╗ ║        ║";
            case 5:
                return "\t\t║ │ █ │ ║ ║ █ ║ ║ WHITE  ║";
            case 6:
                return "\t\t║ └───┘ ║ ╚═══╝ ║        ║";
            case 7:
                return "\t\t╠═══════╬═══════╬════════╣";
            case 8:
                return "\t\t║ ┌───┐ ║ ╔═══╗ ║        ║";
            case 9:
                return "\t\t║ │   │ ║ ║   ║ ║ BLACK  ║";
            case 10:
                return "\t\t║ └───┘ ║ ╚═══╝ ║        ║";
            case 11:
                return "\t\t╠═══════╩═══════╩════════╣";
            case 12:
                return "\t\t║ MENU\t\t\t ║";
            case 13:
                return "\t\t║(h) full moves history  ║";
            case 14:
                return "\t\t║(p) simple board        ║";
            case 15:
                return "\t\t║(s) save and exit       ║";
            case 16:
                return "\t\t║(x) exit without saving ║";
            case 17:
                return "\t\t╠════════════════════════╣";
            case 18:
                return "\t\t║      LAST 10 MOVES\t ║";
            case 19:
                if(moves.size() >= 1)
                    s = moves.get(moves.size() - 1);
                return "\t\t║      " + s + "      ║";
            case 20:
                if(moves.size() >= 2)
                    s = moves.get(moves.size() - 2);
                return "\t\t║      " + s + "      ║";
            case 21:
                if(moves.size() >= 3)
                    s = moves.get(moves.size() - 3);
                return "\t\t║      " + s + "      ║";
            case 22:
                if(moves.size() >= 4)
                    s = moves.get(moves.size() - 4);
                return "\t\t║      " + s + "      ║";
            case 23:
                if(moves.size() >= 5)
                    s = moves.get(moves.size() - 5);
                return "\t\t║      " + s + "      ║";
            case 24:
                if(moves.size() >= 6)
                    s = moves.get(moves.size() - 6);
                return "\t\t║      " + s + "      ║";
            case 25:
                if(moves.size() >= 7)
                    s = moves.get(moves.size() - 7);
                return "\t\t║      " + s + "      ║";
            case 26:
                if(moves.size() >= 8)
                    s = moves.get(moves.size() - 8);
                return "\t\t║      " + s + "      ║";
            case 27:
                if(moves.size() >= 9)
                    s = moves.get(moves.size() - 9);
                return "\t\t║      " + s + "      ║";
            case 28:
                if(moves.size() >= 10)
                    s = moves.get(moves.size() - 10);
                return "\t\t║      " + s + "      ║";
            case 29:
                return "\t\t\t╚════════════════════════╝";
            default:
                throw new UnknownException();
        }
    }

    public static void printBoard(Board board, boolean simplePrint, boolean player){
        Menu.cls();
        if(simplePrint)
            board.printSimple();
        else
            System.out.println(board);
        System.out.print("Player: " + (player ? "BLACK" : "WHITE"));
        System.out.println(". Enter your next move, or \"h\" for move history: ");
    }

    public static void printCapture(String captures){
        System.out.println("You have to capture: " + captures);
    }

    public static void printMultiCapture(String captures){
        System.out.println("Possible captures: " + captures);
    }

    public static void printCaptureObligatory(){
        System.out.println("Capture is obligatory!");
        Menu.waitForEnter();
    }

    public static void printIncorrectMoveFormat(){
        System.out.println("Incorrect move format! Proper format example: E4-D5");
        Menu.waitForEnter();
    }

    public static String[] getMoveOrOption(String captures){
        Scanner sc = new Scanner(System.in);
        String s;
        s = sc.nextLine();
        String[] result;
        for(String o : options) {
            if (s.equals(o)){
                result = new String[1];
                result[0] = s;
                return result;
            }
        }
        s = s.toUpperCase();
        try{
            validate(s);
            if(captures.isEmpty() || captures.contains(s)) {
                String[] sArray = s.split("-");
                char x1 = sArray[0].charAt(0);
                int y1 = Character.getNumericValue(sArray[0].charAt(1));
                char x2 = sArray[1].charAt(0);
                int y2 = Character.getNumericValue(sArray[1].charAt(1));
                result = new String[4];
                result[0] = "" + x1;
                result[1] = "" + y1;
                result[2] = "" + x2;
                result[3] = "" + y2;
            }
            else{
                InGameUI.printCaptureObligatory();
                return null;
            }
        }
        catch(IncorrectMoveFormat e){
            InGameUI.printIncorrectMoveFormat();
            return null;
        }
        return result;
    }

    private static void validate(String s) throws IncorrectMoveFormat {
        String[] sArray = s.split("-");
        if (sArray.length != 2)
            throw new IncorrectMoveFormat();
        for (String t : sArray)
            if (t.length() != 2)
                throw new IncorrectMoveFormat();
    }

    public static void printMakingMove(char x1, int y1, char x2, int y2){
        System.out.println("Trying to make move: " + x1 + y1 + " to " + x2 + y2 + ".");
    }

    public static void printMoveDone(){
        System.out.println("Move done.");
    }

    public static void printCaptureDone(){
        System.out.println("Capture done.");
    }

    public static void printIncorrectMove(String s){
        System.out.println("Incorrect move: " + s);
    }

    public static void endOfGame(Board board, boolean simplePrint){
        Menu.cls();
        if(simplePrint)
            board.printSimple();
        else
            System.out.println(board);
        System.out.println("game over!");
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