package com.koleman.chess.model;

/**
 * Author Koleman Nix
 * Created On 7/10/12 at 12:02 PM
 */
public final class Definitions {

    private Definitions() {}

    public static final int ATTACK_NONE = 0;
    public static final int ATTACK_KQR = 1;
    public static final int ATTACK_QR = 2;
    public static final int ATTACK_KQBwP = 3;
    public static final int ATTACK_KQBbP = 4;
    public static final int ATTACK_QB = 5;
    public static final int ATTACK_N = 6;
    // Formula: attacked_square - attacking_square + 128 = pieces able to attack
    public static final int[] ATTACK_ARRAY = {0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, //0-19
            0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, //20-39
            0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, //40-59
            5, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, //60-79
            2, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, 2, 6, 5, 0, //80-99
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 4, 1, 4, 6, 0, 0, 0, 0, 0, //100-119
            0, 2, 2, 2, 2, 2, 2, 1, 0, 1, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, //120-139
            0, 0, 6, 3, 1, 3, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 6, //140-159
            2, 6, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 2, 0, 0, 5, //160-179
            0, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 2, 0, 0, 0, 5, 0, 0, 0, //180-199
            0, 0, 0, 5, 0, 0, 0, 0, 2, 0, 0, 0, 0, 5, 0, 0, 0, 0, 5, 0, //200-219
            0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 5, 0, 0, 5, 0, 0, 0, 0, 0, 0, //220-239
            2, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //240-256
    // Same as attack array but gives the delta needed to get to the square
    public static final int[] DELTA_ARRAY = {0, 0, 0, 0, 0, 0, 0, 0, 0, -17, 0, 0, 0, 0, 0, 0, -16, 0, 0, 0,
            0, 0, 0, -15, 0, 0, -17, 0, 0, 0, 0, 0, -16, 0, 0, 0, 0, 0, -15, 0,
            0, 0, 0, -17, 0, 0, 0, 0, -16, 0, 0, 0, 0, -15, 0, 0, 0, 0, 0, 0,
            -17, 0, 0, 0, -16, 0, 0, 0, -15, 0, 0, 0, 0, 0, 0, 0, 0, -17, 0, 0,
            -16, 0, 0, -15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -17, -33, -16, -31, -15, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -18, -17, -16, -15, -14, 0, 0, 0, 0, 0,
            0, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
            0, 0, 14, 15, 16, 17, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 31,
            16, 33, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 16, 0, 0, 17,
            0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0, 16, 0, 0, 0, 17, 0, 0, 0,
            0, 0, 0, 15, 0, 0, 0, 0, 16, 0, 0, 0, 0, 17, 0, 0, 0, 0, 15, 0,
            0, 0, 0, 0, 16, 0, 0, 0, 0, 0, 17, 0, 0, 15, 0, 0, 0, 0, 0, 0,
            16, 0, 0, 0, 0, 0, 0, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    // END Attack- and delta-array and constants
    // Credits to Jonatan Pettersson (http://mediocrechess.blogspot.com) for the attack arrays
    // and for the idea.


    public static final int WHITE_PAWN = 1;
    public static final int WHITE_KNIGHT = 2;
    public static final int WHITE_BISHOP = 3;
    public static final int WHITE_ROOK = 4;
    public static final int WHITE_QUEEN = 5;
    public static final int WHITE_KING = 6;
    public static final int EMPTY = 0;
    public static final int BLACK_PAWN = 9;
    public static final int BLACK_KNIGHT = 10;
    public static final int BLACK_BISHOP = 11;
    public static final int BLACK_ROOK = 12;
    public static final int BLACK_QUEEN = 13;
    public static final int BLACK_KING = 14;

    public static final int WHITE_TEAM = 1;
    public static final int BLACK_TEAM = -1;

    public static final int VAL_PAWN = 100;
    public static final int VAL_KNIGHT = 275;
    public static final int VAL_BISHOP = 300;
    public static final int VAL_ROOK = 500;
    public static final int VAL_QUEEN = 900;
    public static final int VAL_KING = 10000;

    public static final int[] DELTA_KNIGHT = {18, 14, 33, 31, -18, -14, -33, -31};
    public static final int[] DELTA_BISHOP = {17, 15, -17, -15};
    public static final int[] DELTA_ROOK = {1, 16, -1, -16};
    public static final int[] DELTA_QUEEN = {1, 15, 16, 17, -1, -15, -16, -17};
    public static final int[] DELTA_KING = {1, 15, 16, 17, -1, -15, -16, -17};

    public static final int MAX_MOVES = 100;

    public static final int INVALID_MOVE = -1;
    public static final int ORDINARY = 0;
    public static final int WHITE_SHORT_CASTLE = 1;
    public static final int WHITE_LONG_CASTLE = 2;
    public static final int BLACK_SHORT_CASTLE = 3;
    public static final int BLACK_LONG_CASTLE = 4;
    public static final int EN_PASSANT = 5;
    public static final int PROMOTION_KNIGHT = 6;
    public static final int PROMOTION_BISHOP = 7;
    public static final int PROMOTION_ROOK = 8;
    public static final int PROMOTION_QUEEN = 9;

    public static final int DIFFICULTY_IMPULSIVE = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;

    // 6 bits
    public static final int ORDERING_PROMOTION = 63;
    public static final int ORDERING_DEFAULT = 0;




}
