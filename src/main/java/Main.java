import com.koleman.chess.engine.Engine;
import com.koleman.chess.engine.EngineUtil;
import com.koleman.chess.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Map;

import static com.koleman.chess.model.Definitions.INVALID_MOVE;

/**
 * Author Koleman Nix
 * Created On 7/16/12 at 4:25 PM
 */
public class Main {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static PrintStream outputStream = System.out;
    private static com.koleman.chess.model.Position position = PositionUtil.createStartingPosition();
    private static Engine engine = EngineUtil.createDefaultEngine();
    private static int depth = 3;

    private static final String HELP_STRING = "Commands: " +
            "\n show - Outputs a visual representation of the current position" +
            "\n perft {depth} - Calculates a perft score for the current position at the given depth" +
            "\n divide {depth} - Prints out a list of possible moves and corresponding child nodes for each move at the given depth" +
            "\n fen - Outputs the FEN String for the current position" +
            "\n exit - Exits the program" +
            "\n setposition {FEN string} - Sets the current position to the fen string specified" +
            "\n reset - Resets the position to the default starting position" +
            "\n move {e2e4} - Makes the specified move if legal" +
            "\n testmovegen - Runs the perft test suite, cross-checking pablo's results with verified perft scores for various positions" +
            "\n yourmove - Pablo will search the current position and make a move based on his findings" +
            "\n think - Pablo will think about the position and reveal his thoughts to you" +
            "\n nullmove - Toggles whose turn it is" +
            "\n setdepth {depth} - sets the search depth" +
            "";
    public static void main(String[] args) throws IOException {
        outputStream.println("Welcome to pablo, a chess engine written in Java by Koleman Nix. Type help for help.");
        String inputString;
        while (true) {
            outputStream.print("pablo> ");
            inputString = reader.readLine();

            // Case Sensitive Commands Go Here
            if (inputString.contains("setposition ")) {
                executeSetPositionCommand(inputString);
                continue;
            }

            // Case Insensitive Commands Go Here
            inputString = inputString.toLowerCase();
            if (inputString.contentEquals("help")) {
                outputStream.println(HELP_STRING);
                continue;
            }
            if (inputString.contains("setdepth ")) {
                executeSetDepthCommand(inputString);
                continue;
            }
            if (inputString.contains("perft ")) {
                executePerftCommand(inputString);
                continue;
            }
            if (inputString.contains("divide ")) {
                executeDivideCommand(inputString);
                continue;
            }
            if (inputString.contains("move ")) {
                executeMoveCommand(inputString);
                continue;
            }
            if (inputString.contentEquals("fen")) {
                executeFenCommand();
                continue;
            }
            if (inputString.contentEquals("show")) {
                outputStream.println("Current Position: ");
                outputStream.println(position.toString());
                continue;
            }
            if (inputString.contentEquals("exit")) {
                outputStream.println("Bye!");
                System.exit(0);
            }
            if (inputString.contentEquals("reset")) {
                outputStream.println("Resetting current position to the starting position");
                position = PositionUtil.createStartingPosition();
                continue;
            }
            if (inputString.contentEquals("testmovegen")) {
                outputStream.println("Running perft test suite. This could take some time...");
                executeTestMoveGen();
                continue;
            }
            if (inputString.contentEquals("yourmove")) {
                executeYourMove();
                continue;
            }
            if (inputString.contentEquals("think")) {
                executeThink();
                continue;
            }
            if (inputString.contentEquals("nullmove")) {
                executeNullMove();
                continue;
            }
            outputStream.println("Unknown Command: " + inputString);
        }
    }

    private static void executePerftCommand(String input) {
        input = input.trim();
        int depth = Integer.parseInt(Character.toString(input.charAt(input.length() - 1)));
        if (depth == 0) {
            outputStream.println("Depth cannot be 0 or double-digit");
        }
        if (depth >= 7) {
            outputStream.println("Grab a cup of coffee and sit down. It's gonna be a long ride...");
        }
        long startTime = System.currentTimeMillis();
        long score = PositionUtil.perft(position, depth);
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        outputStream.println("Nodes at depth " + depth+ ": " + score);
        outputStream.println("Time to Calculate: " + elapsed + "ms");

    }
    private static void executeFenCommand() {
        outputStream.println("Current Position: " + position.writeToFEN());
    }
    private static void executeSetPositionCommand(String input) {
        input = input.trim();
        input = input.substring(12, input.length());
        position = PositionUtil.createFromFENString(input);
    }
    private static void executeDivideCommand(String input) {
        input = input.trim();
        int depth = Integer.parseInt(Character.toString(input.charAt(input.length() - 1)));
        if (depth == 0) {
            outputStream.println("Done! That was easy... there isn't much to calculate at depth 0.");
        }
        Map<String, Long> divideMap = PositionUtil.divide(position, depth);
        for (String key : divideMap.keySet()) {
            outputStream.println(key + " " + divideMap.get(key));
        }
        outputStream.println("Moves: " + divideMap.size());
    }
    private static void executeMoveCommand(String input) {
        input = input.trim();
        int spaceIndex = input.indexOf(' ');
        input = input.substring(spaceIndex+1, input.length());
        String firstCoord = input.substring(0, 2);
        String secondCoord = input.substring(2, 4);
        int firstIndex = CoordinateUtility.convertSANTo0x88(firstCoord);
        int secondIndex = CoordinateUtility.convertSANTo0x88(secondCoord);
        int bitMove = position.checkMove(firstIndex, secondIndex);
        if (bitMove != INVALID_MOVE) {
            position.makeMove(bitMove);
        } else {
            outputStream.println("Illegal Move");
        }
    }
    private static void executeTestMoveGen() {
        if (position.testMoveGen()) {
            System.out.println("Success!");
        } else {
            System.out.println("There's a problem.");
        }
    }
    private static void executeYourMove() {
        outputStream.println("Thinking...");
        engine.setPosition(position.deepClone());
        Move move = engine.computeMoveAtDepth(depth);
        position.makeMove(move);
        System.out.println(position.toString());
        System.out.println(move.toSAN());
    }
    private static void executeThink() {
        outputStream.println("Thinking...");
        engine.setPosition(position.deepClone());
        Move move = engine.computeMoveAtDepth(depth);
        outputStream.println("Best Move: " + move.toSAN());
    }
    private static void executeNullMove() {
        outputStream.println("Making Nullmove");
        position.makeNullMove();
    }
    private static void executeSetDepthCommand(String input) {
        input = input.trim();
        depth = Integer.parseInt(Character.toString(input.charAt(input.length() - 1)));
        System.out.println("Setting default search depth to " + depth);
    }
}
