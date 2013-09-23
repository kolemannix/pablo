package com.koleman.chess.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author Koleman Nix
 * Created On 7/17/12 at 10:33 AM
 */
public final class PositionUtil {

    private PositionUtil() {};

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static Position createStartingPosition() {
        return createFromFENString(STARTING_FEN);
    }

    public static Position createFromFENString(String fen) {
        return PositionImpl.FENManager.read(fen);
    }
    public static Position createEmptyPosition() {
        return new PositionImpl();
    }

    /**
     * Returns the perft score for the given position.
     * @param position The position to search
     * @param depth The depth to search it at
     * @return The number of nodes found.
     */
    public static long perft(Position position, int depth) {
        return miniMaxRoot(position, depth);
    }
    /**
     * Basic minimax function that returns not an evaluation but the number of nodes at the given depth.
     * @param depth
     * @param position The position to search
     * @return a long primitive representing the perft score at that depth.
     */
    private static long miniMax(Position position, int depth) {
        long nodes = 0;
        if (depth == 0) {
            return 1;
        }
        List<Integer> moves = position.getPossibleMoves();
        Position copy;
        for (Integer move : moves) {
            copy = position.deepClone();
            copy.makeMove(move);
            nodes += miniMax(copy, depth-1);
        }
        return nodes;
    }
    private static long miniMaxRoot(Position position, int depth) {
        long nodes = 0;
        if (depth == 0) {
            return 1;
        }
        List<Integer> moves = position.getPossibleMoves();
        int progress = 0;
        Position copy;
        for (Integer move : moves) {
            progress++;
            System.out.println("Progress: " + progress + "/" + moves.size());
            copy = position.deepClone();
            copy.makeMove(move);
            nodes += miniMax(copy, depth-1);
        }
        return nodes;
    }
    public static Map<String, Long> divide(Position position, int depth) {
        Map<String, Long> map = new HashMap<String, Long>();
        List<Integer> moves = position.getPossibleMoves();
        Position copy;
        for (Integer move : moves) {
            copy = position.deepClone();
            copy.makeMove(move);
            long newOnes = miniMax(copy, depth-1);
            map.put(BitMove.getMoveString(move), newOnes);
        }
        return map;
    }
}
