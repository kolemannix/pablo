package com.koleman.chess.model;

/**
 * Author Koleman Nix
 * Created On 6/25/12 at 1:01 PM
 */

/**
 * A 0-based representation of a point (or square) on a chess board. X can be 0-7, Y can be 0-7.
 */
public class ChessPoint {
    public final int x, y;

    public ChessPoint(int x, int y) {
        if (x > 7 || x < 0) {
            throw new IllegalArgumentException("Invalid X value");
        }
        if (y > 7 || y < 0) {
            throw new IllegalArgumentException("Invalid Y value");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChessPoint) {
            ChessPoint other = (ChessPoint)o;
            return (this.x == other.x &&
                    this.y == other.y);
        }
        return false;
    }
}
