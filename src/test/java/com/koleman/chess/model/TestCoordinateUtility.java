package com.koleman.chess.model;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * Author Koleman Nix
 * Created On 7/27/12 at 2:12 PM
 */
public class TestCoordinateUtility extends TestCase {
    @Test
    public void testConvert0x88ToChessPoint() {
        // Case 1
        int index = 112;
        int expectedX = 0;
        int expectedY = 7;
        ChessPoint chessPoint = CoordinateUtility.convert0x88ToChessPoint(index);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);

        // Case 2
        index = 119;
        expectedX = 7;
        expectedY = 7;
        chessPoint = CoordinateUtility.convert0x88ToChessPoint(index);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 3
        index = 7;
        expectedX = 7;
        expectedY = 0;
        chessPoint = CoordinateUtility.convert0x88ToChessPoint(index);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 4
        index = 68;
        expectedX = 4;
        expectedY = 4;
        chessPoint = CoordinateUtility.convert0x88ToChessPoint(index);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 5
        index = 82;
        expectedX = 2;
        expectedY = 5;
        chessPoint = CoordinateUtility.convert0x88ToChessPoint(index);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
    }

    @Test
    public void testConvertChessPointTo0x88() {

        // Case 1
        int expectedIndex = 112;
        int x = 0;
        int y = 7;
        ChessPoint chessPoint = new ChessPoint(x, y);
        int actualIndex = CoordinateUtility.convertChessPointTo0x88(chessPoint);
        assertEquals(expectedIndex, actualIndex);
        // Case 2
        expectedIndex = 119;
        x = 7;
        y = 7;
        chessPoint = new ChessPoint(x, y);
        actualIndex = CoordinateUtility.convertChessPointTo0x88(chessPoint);
        assertEquals(expectedIndex, actualIndex);
        // Case 3
        expectedIndex = 7;
        x = 7;
        y = 0;
        chessPoint = new ChessPoint(x, y);
        actualIndex = CoordinateUtility.convertChessPointTo0x88(chessPoint);
        assertEquals(expectedIndex, actualIndex);
        // Case 4
        expectedIndex = 68;
        x = 4;
        y = 4;
        chessPoint = new ChessPoint(x, y);
        actualIndex = CoordinateUtility.convertChessPointTo0x88(chessPoint);
        assertEquals(expectedIndex, actualIndex);
        // Case 5
        expectedIndex = 82;
        x = 2;
        y = 5;
        chessPoint = new ChessPoint(x, y);
        actualIndex = CoordinateUtility.convertChessPointTo0x88(chessPoint);
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testConvertSANToChessPoint() {
        // Case 1
        String san = "a8";
        int expectedX = 0;
        int expectedY = 7;
        ChessPoint chessPoint = CoordinateUtility.convertSANToChessPoint(san);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);

        // Case 2
        san = "h8";
        expectedX = 7;
        expectedY = 7;
        chessPoint = CoordinateUtility.convertSANToChessPoint(san);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 3
        san = "h1";
        expectedX = 7;
        expectedY = 0;
        chessPoint = CoordinateUtility.convertSANToChessPoint(san);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 4
        san = "e5";
        expectedX = 4;
        expectedY = 4;
        chessPoint = CoordinateUtility.convertSANToChessPoint(san);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
        // Case 5
        san = "c6";
        expectedX = 2;
        expectedY = 5;
        chessPoint = CoordinateUtility.convertSANToChessPoint(san);
        assertEquals(expectedX, chessPoint.x);
        assertEquals(expectedY, chessPoint.y);
    }

    @Test
    public void testInvertCoordinate() {
        int start = 0;
        assertEquals(7, CoordinateUtility.invertCoordinate(start));
        start = 1;
        assertEquals(6, CoordinateUtility.invertCoordinate(start));
        start = 2;
        assertEquals(5, CoordinateUtility.invertCoordinate(start));
        start = 3;
        assertEquals(4, CoordinateUtility.invertCoordinate(start));
        start = 4;
        assertEquals(3, CoordinateUtility.invertCoordinate(start));
        start = 5;
        assertEquals(2, CoordinateUtility.invertCoordinate(start));
        start = 6;
        assertEquals(1, CoordinateUtility.invertCoordinate(start));
        start = 7;
        assertEquals(0, CoordinateUtility.invertCoordinate(start));
    }

}
