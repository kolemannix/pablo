package com.koleman.chess.model;

/**
 * Author Koleman Nix
 * Created On 6/25/12 at 11:59 AM
 */
public enum Team {
    WHITE, BLACK, EMPTY;

    @Override
    public String toString() {
        if (this == WHITE) {
            return "White";
        }
        if (this == BLACK) {
            return "Black";
        }
        return "Empty";
    }
}
