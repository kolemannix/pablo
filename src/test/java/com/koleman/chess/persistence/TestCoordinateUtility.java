package com.koleman.chess.persistence;

import com.koleman.chess.model.CoordinateUtility;
import junit.framework.TestCase;
import org.junit.Test;


/**
 * Author Koleman Nix
 * Created On 6/27/12 at 3:40 PM
 */
public class TestCoordinateUtility extends TestCase {
    @Test
    public void testConvert0x88ToSAN() {
        String expectedResult = "a4";
        int index = 48;
        String actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "d5";
        index = 67;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "f4";
        index = 53;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "a1";
        index = 0;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "a8";
        index = 112;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "h1";
        index = 7;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);

        expectedResult = "h8";
        index = 119;
        actualResult = CoordinateUtility.convert0x88ToSAN(index);
        assertEquals(expectedResult, actualResult);
    }
    @Test
    public void testConvertSANTo0x88() {
        String sanString = "a4";
        int expectedResult = 48;
        int actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "d5";
        expectedResult = 67;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "f4";
        expectedResult = 53;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "a1";
        expectedResult = 0;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "a8";
        expectedResult = 112;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "h1";
        expectedResult = 7;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);

        sanString = "h8";
        expectedResult = 119;
        actualResult = CoordinateUtility.convertSANTo0x88(sanString);
        assertEquals(expectedResult, actualResult);
    }
}
