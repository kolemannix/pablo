package com.koleman.chess.model;


/**
 * Author Koleman Nix
 * Created On 6/27/12 at 3:40 PM
 */
public final class CoordinateUtility {
    private CoordinateUtility() {}
    public static String convert0x88ToSAN(int index) {
        int column = 0;
        int row = 0;
        if ((index & 0x88) == 0) {
            column = index % 16;
            row = (index / 16) + 1;
        }
        char c = convertFileNumberToSANChar(column);
        return Character.toString(c) + row;
    }

    public static int convertSANTo0x88(String SAN) {
        char fileChar = SAN.charAt(0);
        int rank = Integer.parseInt(Character.toString(SAN.charAt(1)));
        int file = convertSANCharToFileNumber(fileChar);
        int result = (16 * (rank - 1)) + file;
        return result;
    }
    public static char convertFileNumberToSANChar(int theNumber) {
        return (char)(theNumber + 97);
    }
    public static int convertSANCharToFileNumber(char sanChar) {
        return (int)sanChar - 97;
    }
    public static ChessPoint convertSANToChessPoint(String san) {
        char fileChar = san.charAt(0);
        int file = convertSANCharToFileNumber(fileChar);
        int rank = Integer.parseInt(Character.toString(san.charAt(1)))-1;
        return new ChessPoint(file, rank);
    }
    public static ChessPoint convert0x88ToChessPoint(int index) {
        int column = 0;
        int row = 0;
        if ((index & 0x88) == 0) {
            column = index % 16;
            row = (index / 16);
        }
        return new ChessPoint(column, row);
    }
    public static int convertChessPointTo0x88(ChessPoint input) {
        return convertChessPointTo0x88(input.x, input.y);
    }
    public static int convertChessPointTo0x88(int x, int y) {
        return (16 * (y)) + x;
    }
    /**
     * Inverts the Specified coordinate. 0 returns 7, 1 returns 6, 2 returns 5, etc.
     */
    public static int invertCoordinate(int coordinate) {
        switch (coordinate) {
            case 0: return 7;
            case 1: return 6;
            case 2: return 5;
            case 3: return 4;
            case 4: return 3;
            case 5: return 2;
            case 6: return 1;
            case 7: return 0;
        }
        return 0;
    }
}
