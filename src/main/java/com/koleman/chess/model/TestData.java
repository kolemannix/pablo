package com.koleman.chess.model;

/**
 * Author Koleman Nix
 * Created On 7/18/12 at 10:03 AM
 */
public final class TestData {

    public TestData() {}

    public static final String COMPLICATED_FEN = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
    public static final String PROMOTION_FEN = "n1n5/PPPk4/8/8/8/8/4Kppp/5N1N b - - 0 1";
    public static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String AFTER_E4     = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    public static final String AFTER_E4_E5     = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
    public static final String AFTER_E4_E5_NF3     = "rnbqkbnr/pppp1ppp/8/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 0 2";
    public static final String AFTER_E4_E5_NF3_NC6     = "r1bqkbnr/pppp1ppp/2n5/4p3/4P3/5N2/PPPP1PPP/RNBQKB1R w KQkq - 0 3";
    public static final String AFTER_E4_E5_NF3_NC6_BC5     = "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 0 3";

    public static final int STARTING_PERFT_1   =  20;
    public static final int STARTING_PERFT_2   =  400;
    public static final int STARTING_PERFT_3   =  8902;
    public static final int STARTING_PERFT_4   =  197281;
    public static final int STARTING_PERFT_5   =  4865609;
    public static final int STARTING_PERFT_6   =  119060324;
    public static final long STARTING_PERFT_7  =  3195901860L;
    public static final long STARTING_PERFT_8  =  84998978956L;
    public static final long STARTING_PERFT_9  =  2439530234167L;
    public static final long STARTING_PERFT_10 =  69352859712417L;

    public static final int COMPLICATED_PERFT_1 = 48;
    public static final int COMPLICATED_PERFT_2 = 2039;
    public static final int COMPLICATED_PERFT_3 = 97862;
    public static final int COMPLICATED_PERFT_4 = 4085603;
    public static final int COMPLICATED_PERFT_5 = 193690690;
    public static final long COMPLICATED_PERFT_6 = 8031647685L;

    public static final int PROMOTION_PERFT_1 = 24;
    public static final int PROMOTION_PERFT_2 = 496;
    public static final int PROMOTION_PERFT_3 = 9483;
    public static final int PROMOTION_PERFT_4 = 182838;
    public static final int PROMOTION_PERFT_5 = 3605103;
    public static final int PROMOTION_PERFT_6 = 71179139;
}
