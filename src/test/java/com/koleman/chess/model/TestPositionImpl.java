package com.koleman.chess.model;

import junit.framework.TestCase;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.List;
import static com.koleman.chess.model.Definitions.*;
import static com.koleman.chess.model.TestData.*;

/**
 * Author Koleman Nix
 * Created On 7/8/12 at 1:09 PM
 */
public class TestPositionImpl extends TestCase {

    @Test
    public void testMakeMove() throws NoSuchFieldException, IllegalAccessException {
        Position p = PositionUtil.createFromFENString(STARTING_FEN);
        Field f = PositionImpl.class.getDeclaredField("matrix");
        f.setAccessible(true);
        int[] matrix = (int[]) f.get(p);
        assert matrix[20] == WHITE_PAWN;
        assert matrix[52] == EMPTY;

        p.makeMove(BitMove.createMove(
                CoordinateUtility.convertSANTo0x88("e2"), CoordinateUtility.convertSANTo0x88("e4"), WHITE_PAWN, EMPTY, ORDINARY, ORDERING_DEFAULT));
        assert matrix[20] == EMPTY;
        assert matrix[52] == WHITE_PAWN;

        // TODO Test all castling scenarios, all enPassant scenarios, and all promotion scenarios

    }

    @Test
    public void testCloneMethod() {
        Position instance = PositionUtil.createFromFENString(AFTER_E4_E5);
        Position clone = instance.deepClone();
        System.out.println(instance.writeToFEN());
        System.out.println(clone.writeToFEN());
        assertEquals(instance.writeToFEN(), clone.writeToFEN());
    }
    /**
     * The mac daddy test. Takes a long time to run.
     */
    @Test
    public void testPerftScore() throws InterruptedException {
        Position instance = PositionUtil.createStartingPosition();
        assertEquals(true, instance.testMoveGen());
    }

    @Test
    public void testTeamBits() {
        if (PositionImpl.checkTeamBit(WHITE_KING)) {
            fail();
        }
        if (PositionImpl.checkTeamBit(WHITE_PAWN)) {
            fail();
        }
        if (PositionImpl.checkTeamBit(WHITE_KNIGHT)) {
            fail();
        }
        if (PositionImpl.checkTeamBit(WHITE_BISHOP)) {
            fail();
        }
        if (PositionImpl.checkTeamBit(WHITE_QUEEN)) {
            fail();
        }
        if (PositionImpl.checkTeamBit(WHITE_ROOK)) {
            fail();
        }

        if (!PositionImpl.checkTeamBit(BLACK_KING)) {
            fail();
        }
        if (!PositionImpl.checkTeamBit(BLACK_PAWN)) {
            fail();
        }
        if (!PositionImpl.checkTeamBit(BLACK_KNIGHT)) {
            fail();
        }
        if (!PositionImpl.checkTeamBit(BLACK_BISHOP)) {
            fail();
        }
        if (!PositionImpl.checkTeamBit(BLACK_QUEEN)) {
            fail();
        }
        if (!PositionImpl.checkTeamBit(BLACK_ROOK)) {
            fail();
        }
    }

    @Test
    public void testMoveOrderingLogic() {
        Position instance = PositionUtil.createStartingPosition();
        assertEquals(0, instance.getCaptureMoves().size());
        List<Integer> moveList = instance.getPossibleMoves();
        for (Integer bitMove : moveList) {
            int orderingScore = BitMove.getOrderingScore(bitMove);
            assertEquals(ORDERING_DEFAULT, orderingScore);
        }

        instance = PositionUtil.createFromFENString(PROMOTION_FEN);
        moveList = instance.getPossibleMoves();
        int promotionMoves = 0;
        for (Integer bitMove : moveList) {
            if (BitMove.getOrderingScore(bitMove) == ORDERING_PROMOTION) {
                promotionMoves++;
            }
        }
        assertEquals(12, promotionMoves);
    }

    @Test
    public void testEquals() {
        Position instance = PositionUtil.createStartingPosition();
        Position other = PositionUtil.createStartingPosition();
        assertEquals(instance, other);

        instance = other.deepClone();
        other = instance.deepClone();

        assertEquals(instance, other);
    }
}
