package com.koleman.chess.model;

/**
 * Author Koleman Nix
 * Created On 7/15/12 at 1:21 AM
 * Static utility class for handling move integers. Instead of instantiating millions of Move objects,
 * I am representing moves with carefully crafted 32-bit integers. This class handles the complex bitwise
 * operations so the Board class doesn't have to.
 */
public class BitMove {
    // The FROM and TO members each take up 7 bits.
    private static final int FROM_INDEX_SHIFT = 0;
    /* TAKES UP 7 BITS      00000000000000000000000001111111*/
    private static final int TO_INDEX_SHIFT = 7;
    /* TAKES UP 7 MORE BITS 00000000000000000022222221111111*/
    private static final int FROM_PIECE_SHIFT = 14;
    /* TAKES UP 4 MORE BITS 00000000000000333322222221111111*/
    private static final int TO_PIECE_SHIFT = 18;
    /* TAKES UP 4 MORE BITS 00000000004444333322222221111111*/
    private static final int TYPE_SHIFT = 22;
    /* TAKES UP 4 MORE BITS 00000055554444333322222221111111*/
    private static final int ORDERING_SHIFT = 26;
    /* TAKES UP LAST 6 BITS 66666655554444333322222221111111*/
    public static final int INDEX_MASK = 127;
    public static final int PIECE_MASK = 15;
    public static final int TYPE_MASK = 15;
    public static final int ORDERING_MASK = 63;

    public static int createMove(int fromIndex, int toIndex, int fromPiece, int toPiece, int moveType, int orderingScore) {
        return fromIndex | (toIndex << TO_INDEX_SHIFT) |
                (fromPiece << FROM_PIECE_SHIFT) | (toPiece << TO_PIECE_SHIFT) |
                (moveType << TYPE_SHIFT) | (orderingScore << ORDERING_SHIFT);
    }
    public static int getFromIndex(int move) {
        return (move & INDEX_MASK);
    }
    public static int getToIndex(int move) {
        return ((move >> TO_INDEX_SHIFT) & INDEX_MASK);
    }
    public static int getFromPiece(int move) {
        return ((move >> FROM_PIECE_SHIFT) & PIECE_MASK);
    }
    public static int getToPiece(int move) {
        return ((move >> TO_PIECE_SHIFT) & PIECE_MASK);
    }
    public static int getType(int move) {
        return ((move >> TYPE_SHIFT) & TYPE_MASK);
    }
    public static int getOrderingScore(int move) {
        return ((move >> ORDERING_SHIFT) & ORDERING_MASK);
    }

    /**
     * Returns a string in the form of "e2e4" (not a Standard Algebraic Notation string) representing the
     * given bit move.
     * @param bitMove An integer representing a valid BitMove.
     * @return
     */
    public static String getMoveString(int bitMove) {
        return getMoveObject(bitMove).toSAN();
    }
    /* The following methods are here to interface with the Move class */

    /**
     * Takes a BitMove integer and converts it to a real Move object.
     * @param bitMove a BitMove integer
     * @return A Move object corresponding to the same move
     */
    public static Move getMoveObject(int bitMove) {
        return new Move(getFromIndex(bitMove), getToIndex(bitMove), getFromPiece(bitMove), getToPiece(bitMove), getType(bitMove), getOrderingScore(bitMove));
    }
}