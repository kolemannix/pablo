package com.koleman.chess.persistence;

import com.koleman.chess.model.CoordinateUtility;
import com.koleman.chess.model.PositionImpl;
import com.koleman.chess.model.Position;
import junit.framework.TestCase;
import org.junit.Test;
import java.lang.reflect.Field;
import static com.koleman.chess.model.Definitions.*;

/**
 * Author Koleman Nix
 * Created On 6/25/12 at 12:50 PM
 */
public class FENManagerTest extends TestCase {

    private static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String AFTER_E4     = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    private static final String AFTER_C5     = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
    private static final String AFTER_NF3    = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";

    @Test
    public void testRead() throws NoSuchFieldException, IllegalAccessException {

        Position p = PositionImpl.FENManager.read(STARTING_FEN);
        final Field field = PositionImpl.class.getDeclaredField("matrix");
        field.setAccessible(true);
        int[] matrix = (int[]) field.get(p);
        assert matrix[20] == WHITE_PAWN;
        assert matrix[4] == WHITE_KING;

        p = PositionImpl.FENManager.read(AFTER_E4);
        matrix = (int[]) field.get(p);
        assert matrix[20] == EMPTY;
        assert matrix[52] == WHITE_PAWN;

        p = PositionImpl.FENManager.read(AFTER_C5);
        int c5square = CoordinateUtility.convertSANTo0x88("c5");
        int c7square = CoordinateUtility.convertSANTo0x88("c7");
        matrix = (int[]) field.get(p);
        assert matrix[c7square] == EMPTY;
        assert matrix[c5square] == BLACK_PAWN;
    }

    @Test
    public void testWrite() {
        Position p = PositionImpl.FENManager.read(STARTING_FEN);
        String output = p.writeToFEN();
        System.out.println("Original:            " + STARTING_FEN);
        System.out.println("Parsed and Unparsed: " + output + "\n");
        assertEquals(STARTING_FEN, output);

        p = PositionImpl.FENManager.read(AFTER_E4);
        output = p.writeToFEN();
        System.out.println("Original:            " + AFTER_E4);
        System.out.println("Parsed and Unparsed: " + output + "\n");
        assertEquals(AFTER_E4, output);

        p = PositionImpl.FENManager.read(AFTER_C5);
        output = p.writeToFEN();
        System.out.println("Original:            " + AFTER_C5);
        System.out.println("Parsed and Unparsed: " + output + "\n");
        assertEquals(AFTER_C5, output);

        p = PositionImpl.FENManager.read(AFTER_NF3);
        output = p.writeToFEN();
        System.out.println("Original:            " + AFTER_NF3);
        System.out.println("Parsed and Unparsed: " + output + "\n");
        assertEquals(AFTER_NF3, output);
    }

}
