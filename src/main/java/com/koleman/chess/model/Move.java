package com.koleman.chess.model;
import static com.koleman.chess.model.Definitions.*;
/**
 * Author Koleman Nix
 * Created On 7/8/12 at 11:42 AM
 */
public class Move implements Comparable{

    public final int start, end;
    public final int startPiece;
    public final int endPiece;
    public final int type;
    public final int ordering;

    public Move(int start, int end, int startPiece, int endPiece, int type, int ordering) {
        this.start = start;
        this.end = end;
        this.startPiece = startPiece;
        this.endPiece = endPiece;
        this.type = type;
        this.ordering = ordering;
    }
    public Move(String startSAN, String endSAN, int startPiece, int endPiece, int type, int ordering) {
        this.start = CoordinateUtility.convertSANTo0x88(startSAN);
        this.end = CoordinateUtility.convertSANTo0x88(endSAN);
        this.startPiece = startPiece;
        this.endPiece = endPiece;
        this.type = type;
        this.ordering = ordering;
    }

    public String toShorthand() {
        String start = CoordinateUtility.convert0x88ToSAN(this.start);
        String end = CoordinateUtility.convert0x88ToSAN(this.end);

        return start + "" + end;
    }
    public String toSAN() {
        StringBuilder sb = new StringBuilder();

        if (type == WHITE_SHORT_CASTLE) {
            sb.append("0-0");
            return sb.toString();
        }
        if (type == WHITE_LONG_CASTLE) {
            sb.append("0-0-0");
            return sb.toString();
        }
        if (type == BLACK_SHORT_CASTLE) {
            sb.append("0-0");
            return sb.toString();
        }
        if (type == BLACK_LONG_CASTLE) {
            sb.append("0-0-0");
            return sb.toString();
        }
        switch (startPiece) {
            case WHITE_PAWN:
                if (endPiece != EMPTY) {
                    // Pawn Capture
                    // Print the start file
                    sb.append(CoordinateUtility.convert0x88ToSAN(start).charAt(0));
                }
                break;
            case BLACK_PAWN:
                if (endPiece != EMPTY) {
                    // Pawn Capture
                    // Print the start file
                    sb.append(CoordinateUtility.convert0x88ToSAN(start).charAt(0));
                }
                break;
            case WHITE_KNIGHT: sb.append('N'); break;
            case BLACK_KNIGHT: sb.append('N'); break;
            case WHITE_BISHOP: sb.append('B'); break;
            case BLACK_BISHOP: sb.append('B'); break;
            case WHITE_ROOK: sb.append('R'); break;
            case BLACK_ROOK: sb.append('R'); break;
            case WHITE_QUEEN: sb.append('Q'); break;
            case BLACK_QUEEN: sb.append('Q'); break;
            case WHITE_KING: sb.append('K'); break;
            case BLACK_KING: sb.append('K'); break;
        }
        if (endPiece != EMPTY) {
            sb.append('x');
        }
        sb.append(CoordinateUtility.convert0x88ToSAN(end));

        if (type == PROMOTION_KNIGHT) {
            sb.append("=");
            sb.append("N");
        }
        if (type == PROMOTION_BISHOP) {
            sb.append("=");
            sb.append("B");
        }
        if (type == PROMOTION_ROOK) {
            sb.append("=");
            sb.append("R");
        }
        if (type == PROMOTION_QUEEN) {
            sb.append("=");
            sb.append("Q");
        }

        return sb.toString();
    }
    @Override
    public int compareTo(Object o) {
        // Implements MVV/LVA move ordering (most valuable victim, least valuable attacker)
        Move otherMove = (Move)o;

        // Both startPieces and both endPieces will be same color - otherwise this logic would break
        if (this.endPiece > otherMove.endPiece) {
            return 1;
        }
        if (this.endPiece < otherMove.endPiece) {
            return -1;
        }
        if (this.endPiece == otherMove.endPiece) {
            if (this.startPiece < this.endPiece) {
                return 1;
            }
            if (this.startPiece > this.endPiece) {
                return -1;
            }
        }
        return 0;
    }
}
