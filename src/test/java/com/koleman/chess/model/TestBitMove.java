package com.koleman.chess.model;

import junit.framework.TestCase;
import org.junit.Test;
import static com.koleman.chess.model.Definitions.*;

/**
 * Author Koleman Nix
 * Created On 7/15/12 at 1:38 AM
 */
public class TestBitMove extends TestCase {

    @Test
    public void testFromAndToIndex() {
        int moveInstance = BitMove.createMove(30, 34, WHITE_PAWN, BLACK_KNIGHT, WHITE_SHORT_CASTLE, ORDERING_DEFAULT);
        assertEquals(30, BitMove.getFromIndex(moveInstance));
        assertEquals(34, BitMove.getToIndex(moveInstance));

        moveInstance = BitMove.createMove(127, 0, 0, 0, 0, ORDERING_DEFAULT);
        assertEquals(127, BitMove.getFromIndex(moveInstance));
        assertEquals(0, BitMove.getToIndex(moveInstance));
    }

    @Test
    public void testFromAndToPiece() {
        int moveInstance = BitMove.createMove(30, 34, WHITE_PAWN, WHITE_KNIGHT, WHITE_SHORT_CASTLE, ORDERING_DEFAULT);
        assertEquals(WHITE_PAWN, BitMove.getFromPiece(moveInstance));
        assertEquals(WHITE_KNIGHT, BitMove.getToPiece(moveInstance));

        moveInstance = BitMove.createMove(30, 34, WHITE_QUEEN, BLACK_KING, WHITE_SHORT_CASTLE, ORDERING_DEFAULT);
        assertEquals(WHITE_QUEEN, BitMove.getFromPiece(moveInstance));
        assertEquals(BLACK_KING, BitMove.getToPiece(moveInstance));

        moveInstance = BitMove.createMove(30, 34, WHITE_QUEEN, EMPTY, WHITE_SHORT_CASTLE, ORDERING_DEFAULT);
        assertEquals(WHITE_QUEEN, BitMove.getFromPiece(moveInstance));
        assertEquals(EMPTY, BitMove.getToPiece(moveInstance));
    }

    @Test
    public void testMoveType() {
        int moveInstance = BitMove.createMove(1, 50, 0, 0, BLACK_LONG_CASTLE, ORDERING_DEFAULT);
        assertEquals(BLACK_LONG_CASTLE, BitMove.getType(moveInstance));

        moveInstance = BitMove.createMove(3, 50, 0, 0, PROMOTION_QUEEN, ORDERING_PROMOTION);
        assertEquals(PROMOTION_QUEEN, BitMove.getType(moveInstance));
    }

    @Test
    public void testOrderingScore() {
        int moveInstance = BitMove.createMove(0, 1, WHITE_PAWN, BLACK_QUEEN, PROMOTION_QUEEN, ORDERING_PROMOTION);

        assertEquals(ORDERING_PROMOTION, BitMove.getOrderingScore(moveInstance));
    }



}
