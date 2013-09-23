package com.koleman.chess.model;

import java.util.*;

import static com.koleman.chess.model.Definitions.*;
import static com.koleman.chess.model.TestData.*;
import static com.koleman.chess.model.TestData.PROMOTION_FEN;
import static com.koleman.chess.model.TestData.PROMOTION_PERFT_5;

/**
 * Author Koleman Nix
 * Created On 6/25/12 at 1:04 PM
 */
public class PositionImpl implements Position {
    private int[] matrix;
    private int sideToMove;
    private int fullMoveCount;
    private int halfMoveCount;
    private int halfMoveClock;
    private int enPassantSquare;
    private boolean whiteCastleShort, whiteCastleLong, blackCastleShort, blackCastleLong;

    // To keep track of the kings' locations. (Saves a lot of iterations)
    private int whiteKing;
    private int blackKing;

    public PositionImpl() {
        matrix = new int[128];
        for (int x = 0; x < 128; x++) {
            if ((x & 0x88) == 0)  {
                matrix[x] = 0;
            }
        }
        sideToMove = 1;
        fullMoveCount = 0;
        halfMoveClock = 0;
        enPassantSquare = -1;
        whiteKing = -1;
        blackKing = -1;

        whiteCastleShort = false;
        whiteCastleLong = false;
        blackCastleShort = false;
        blackCastleLong = false;
    }

    @Override
    public int getTeamToMove() {
        return sideToMove;
    }

    @Override
    public int getFullMoveCount() {
        return fullMoveCount;
    }

    @Override
    public int getHalfMoveCount() {
        return halfMoveCount;
    }

    @Override
    public List<Integer> getPossibleMoves() {
        List<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < 128; i++) {
            if ((i & 0x88) == 0) {
                int piece = matrix[i];
                if (piece != 0) {
                    if (sideToMove == 1) {
                        if (!checkTeamBit(piece)) {
                            resultList.addAll(generateLegalMoves(i));
                        }
                    } else {
                        if (checkTeamBit(piece)) {
                            resultList.addAll(generateLegalMoves(i));
                        }
                    }
                }
            }
        }
        // If the move ordering bits were the first six, the following line would make no sense. But since the move ordering values
        // are stored in the greatest (leftmost) 6 bits of the move integer, the move with the highest move ordering score will also
        // happen to be a greater integer (who cares about the first 26 bits?) than any other move with a lower score.
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List<Integer> getCaptureMoves() {
        List<Integer> resultList = new ArrayList<Integer>();
        for (int i = 0; i < 128; i++) {
            if ((i & 0x88) == 0) {
                int piece = matrix[i];
                if (piece != 0) {
                    if (sideToMove == 1) {
                        if (!checkTeamBit(piece)) {
                            resultList.addAll(generateCaptureMoves(i));
                        }
                    } else {
                        if (checkTeamBit(piece)) {
                            resultList.addAll(generateCaptureMoves(i));
                        }
                    }
                }
            }
        }
        Collections.sort(resultList);
        return resultList;
    }
    private List<Integer> generateCaptureMoves(int start) {
        List<Integer> possibleDestinations = getPseudoLegalMoves(start);
        List<Integer> resultList = new ArrayList<Integer>();
        for (Integer dest : possibleDestinations) {
            int move = checkMoveInternal(start, dest);
            if (BitMove.getToPiece(move) != EMPTY) {
                if (move != -1) {
                    if ((matrix[start] == WHITE_PAWN) && ((dest >= 112) && (dest <= 119))) {
                        resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_KNIGHT, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_BISHOP, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_ROOK, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_QUEEN, ORDERING_PROMOTION));
                    } else if ((matrix[start] == BLACK_PAWN) && ((dest >= 0) && (dest <= 7))) {
                        resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_KNIGHT, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_BISHOP, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_ROOK, ORDERING_PROMOTION));
                        resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_QUEEN, ORDERING_PROMOTION));
                    } else {
                        resultList.add(move);
                    }
                }
            }
        }
        return resultList;
    }
    private List<Integer> generateLegalMoves(int start) {
        List<Integer> possibleDestinations = getPseudoLegalMoves(start);
        List<Integer> resultList = new ArrayList<Integer>();
        for (Integer dest : possibleDestinations) {
            int move = checkMoveInternal(start, dest);
            if (move != -1) {
                if ((matrix[start] == WHITE_PAWN) && ((dest >= 112) && (dest <= 119))) {
                    resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_KNIGHT, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_BISHOP, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_ROOK, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, WHITE_PAWN, 0, PROMOTION_QUEEN, ORDERING_PROMOTION));
                } else if ((matrix[start] == BLACK_PAWN) && ((dest >= 0) && (dest <= 7))) {
                    resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_KNIGHT, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_BISHOP, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_ROOK, ORDERING_PROMOTION));
                    resultList.add(BitMove.createMove(start, dest, BLACK_PAWN, 0, PROMOTION_QUEEN, ORDERING_PROMOTION));
                } else {
                    resultList.add(move);
                }
            }
        }
        return resultList;
    }
    private List<Integer> getPseudoLegalMoves(int start) {
        List<Integer> resultSet = new ArrayList<Integer>();
        int index;

        switch (matrix[start]) {
            case WHITE_PAWN:
                if (((start+16) & 0x88) == 0) {
                    // If there's nothing directly in front, add that move
                    if (matrix[start+16] == 0) {
                        resultSet.add(start + 16);
                        // If there's nothing two squares in front, and we are on our starting rank, add that move
                        if ((start >= 16 && start <= 24) &&
                                matrix[start+32] == 0) {
                            resultSet.add(start + 32);
                        }
                    }
                }
                if (((start+15) & 0x88) == 0) {
                    resultSet.add(start+15);
                }
                if (((start+17) & 0x88) == 0) {
                    resultSet.add(start+17);
                }
                return resultSet;
            case BLACK_PAWN:
                if (((start-16) & 0x88) == 0) {
                    if (matrix[start-16] == 0) {
                        resultSet.add(start-16);
                        if ((start >= 96 && start <= 103) &&
                                matrix[start-32] == 0) {
                            resultSet.add(start-32);
                        }
                    }
                }
                if (((start-15) & 0x88) == 0) {
                    resultSet.add(start-15);
                }
                if (((start-17) & 0x88) == 0) {
                    resultSet.add(start-17);
                }
                return resultSet;
            case WHITE_KNIGHT:
                for (int delta : DELTA_KNIGHT) {
                    if (((start + delta) & 0x88) == 0) {
                        resultSet.add(start + delta);
                    }
                }
                return resultSet;
            case BLACK_KNIGHT:
                for (int delta : DELTA_KNIGHT) {
                    if (((start + delta) & 0x88) == 0) {
                        resultSet.add(start + delta);
                    }
                }
                return resultSet;
            case WHITE_BISHOP:
                for (int delta : DELTA_BISHOP) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case BLACK_BISHOP:
                for (int delta : DELTA_BISHOP) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case WHITE_ROOK:
                for (int delta : DELTA_ROOK) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case BLACK_ROOK:
                for (int delta : DELTA_ROOK) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case WHITE_QUEEN:
                for (int delta : DELTA_QUEEN) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case BLACK_QUEEN:
                for (int delta : DELTA_QUEEN) {
                    index = start + delta;
                    while ((index & 0x88) == 0) {
                        resultSet.add(index);
                        if (matrix[index] != 0) {
                            // Break, we've run into a piece.
                            break;
                        }
                        index += delta;
                    }
                }
                return resultSet;
            case WHITE_KING:
                for (int delta : DELTA_KING) {
                    if (((start + delta) & 0x88) == 0) {
                        resultSet.add(start + delta);
                    }
                }
                if (whiteCastleShort) resultSet.add(6);
                if (whiteCastleLong)resultSet.add(2);
                return resultSet;
            case BLACK_KING:
                for (int delta : DELTA_KING) {
                    if (((start + delta) & 0x88) == 0) {
                        resultSet.add(start + delta);
                    }
                }
                if (blackCastleShort) resultSet.add(118);
                if (blackCastleLong)resultSet.add(114);
                return resultSet;
        }
        throw new IllegalArgumentException("Start coordinate must have a piece on it");
    }
    /**
     * Checks a move's legality, and returns a Move Object with the appropriate fields populated
     * Invariants: lots. Shouldn't be called by anything except internal processes. I could make this method check that, but that is one
     * more if() statement that I just don't need.
     * @param start Move start
     * @param end Move end
     * @return a Move object if legal, null if illegal
     */
    private int checkMoveInternal(int start, int end) {
        int type = ORDINARY;
        int startPiece = matrix[start];
        int endPiece = matrix[end];

        // ORDERING CANNOT BE MORE THAN 63!!!
        int ordering = ORDERING_DEFAULT;
        // ORDERING CANNOT BE MORE THAN 63!!!

        // Can't capture your own teammate
        if (endPiece != 0) {
            if ((checkTeamBit(matrix[start]) == checkTeamBit(matrix[end]))) {
                return -1;
            }
        }
        // En Passant and Pawn Capture Garbage
        switch (startPiece) {
            case WHITE_PAWN:
                if (end == start+15) {
                    if (matrix[end] == 0) {
                        if (end == enPassantSquare) {
                            type = EN_PASSANT;
                        } else {
                            return -1;
                        }
                    }
                }
                if (end == start+17) {
                    if (endPiece == 0) {
                        if (end == enPassantSquare) {
                            type = EN_PASSANT;
                        } else {
                            return -1;
                        }
                    }
                }
                break;
            case BLACK_PAWN:
                if (end == start-15) {
                    if (matrix[end] == 0) {
                        if (end == enPassantSquare) {
                            type = EN_PASSANT;
                        } else {
                            return -1;
                        }
                    }
                }
                if (end == start-17) {
                    if (endPiece == 0) {
                        if (end == enPassantSquare) {
                            type = EN_PASSANT;
                        } else {
                            return -1;
                        }
                    }
                }
                break;
            case WHITE_KING:
                if (start == 4 && end == 6) {
                    // A king is trying to castle short. Check the in-between squares first
                    if (matrix[5] != 0) return -1;
                    if (matrix[6] != 0) return -1;
                    // No need to check rook position; castling rights will be gone if it's not there, so just check the rights
                    if (!whiteCastleShort) return -1;
                    // Can't castle in check or through attacked squares
                    if (isAttacked(4, -sideToMove)) return -1;
                    if (isAttacked(5, -sideToMove)) return -1;
                    if (isAttacked(6, -sideToMove)) return -1;
                    // We made it through the gauntlet. You can castle now.
                    type = WHITE_SHORT_CASTLE;
                }
                if (start == 4 && end == 2) {
                    // A king is trying to castle long. Check the in-between squares first
                    if (matrix[1] != 0) return -1;
                    if (matrix[2] != 0) return -1;
                    if (matrix[3] != 0) return -1;
                    // No need to check rook position; castling rights will be gone if it's not there, so just check the rights
                    if (!whiteCastleLong) return -1;
                    // Can't castle in check or through attacked squares
                    if (isAttacked(2, -sideToMove)) return -1;
                    if (isAttacked(3, -sideToMove)) return -1;
                    if (isAttacked(4, -sideToMove)) return -1;
                    // We made it through the gauntlet. You can castle now.
                    type = WHITE_LONG_CASTLE;
                }
                break;
            case BLACK_KING:
                if (start == 116 && end == 118) {
                    // A king is trying to castle short. Check the in-between squares first
                    if (matrix[117] != 0) return -1;
                    if (matrix[118] != 0) return -1;
                    // No need to check rook position; castling rights will be gone if it's not there, so just check the rights
                    if (!blackCastleShort) return -1;
                    // Can't castle in check or through attacked squares
                    if (isAttacked(116, -sideToMove)) return -1;
                    if (isAttacked(117, -sideToMove)) return -1;
                    if (isAttacked(118, -sideToMove)) return -1;
                    // We made it through the gauntlet. You can castle now.
                    type = BLACK_SHORT_CASTLE;
                }
                if (start == 116 && end == 114) {
                    // A king is trying to castle long. Check the in-between squares first
                    if (matrix[115] != 0) return -1;
                    if (matrix[114] != 0) return -1;
                    if (matrix[113] != 0) return -1;
                    // No need to check rook position; castling rights will be gone if it's not there, so just check the rights
                    if (!blackCastleLong) return -1;
                    // Can't castle in check or through attacked squares
                    if (isAttacked(114, -sideToMove)) return -1;
                    if (isAttacked(115, -sideToMove)) return -1;
                    if (isAttacked(116, -sideToMove)) return -1;
                    // We made it through the gauntlet. You can castle now.
                    type = BLACK_LONG_CASTLE;
                }
                break;
        }
        // Move Ordering Logic
        // The greater the endPiece, the better.
        // The smaller the startPiece, the better.
        // (According to MVV/LVA move ordering principles
        if (sideToMove == WHITE_TEAM) {
            ordering = (endPiece) / (startPiece+8);
            // Multiply by 2 to ensure even number, which ensures even division, which preserves accuracy by avoiding rounding
            // add 8 because black piece constants are higher than white ones (whitepawn = 1, blackpawn = 9). That would
            // break this logic. So we put them on equal ground. Note that subtracting 8 from endPiece would be a disaster
            // in the case the endPiece was EMPTY (0). We would get -16 / whitePiece.
        } else {
            if (endPiece != 0) ordering = (endPiece+8) / startPiece;
            // Multiply by 2 to ensure even number, which ensures even division, which preserves accuracy by avoiding rounding
            // add 8 because black piece constants are higher than white ones (whitepawn = 1, blackpawn = 9). That would
            // break this logic. So we put them on equal ground.
        }

        if (endPiece != 0) {
            ordering += 32;
        }
        // Check for Pins
        matrix[end] = matrix[start];
        matrix[start] = 0;
        if (sideToMove == WHITE_TEAM) {
            // The blackKing and whiteKing Coordinates will not be updated... we have to make a separate
            // sanity check to be sure the king isn't the one moving.
            if (startPiece == WHITE_KING) {
                // The piece trying to move happens to be the king.
                if (isAttacked(end, -sideToMove)) {
                    // He's trying to move into check. Fix things and return Illegal
                    matrix[start] = matrix[end];
                    matrix[end] = endPiece;
                    return INVALID_MOVE;
                }
            } else if (isAttacked(whiteKing, -sideToMove)) {
                // The piece trying to move is pinned
                // Fix things, then exit
                matrix[start] = matrix[end];
                matrix[end] = endPiece;
                return INVALID_MOVE;
            }
        } else {
            if (startPiece == BLACK_KING) {
                // The piece trying to move happens to be the king.
                if (isAttacked(end, -sideToMove)) {
                    // He's trying to move into check. Fix things and return Illegal
                    matrix[start] = matrix[end];
                    matrix[end] = endPiece;
                    return INVALID_MOVE;
                }
            } else if (isAttacked(blackKing, -sideToMove)) {
                // The piece trying to move is pinned
                // Fix things, then exit
                matrix[start] = matrix[end];
                matrix[end] = endPiece;
                return INVALID_MOVE;
            }
        }
        matrix[start] = matrix[end];
        matrix[end] = endPiece;
        return BitMove.createMove(start, end, startPiece, endPiece, type, ordering);
    }
    public void makeMove(Move move) {
        makeMove(BitMove.createMove(move.start, move.end, move.startPiece, move.endPiece, move.type, move.ordering));
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof Position) {
            if (((Position) that).writeToFEN().contentEquals(this.writeToFEN())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes the specified move and toggles the side to move. Does not check legality.
     * @param move the *legal* move to make
     */
    @Override
    public void makeMove(int move) {

        // Reset enPassant square
        enPassantSquare = -1;
        int fromIndex = BitMove.getFromIndex(move);
        int toIndex = BitMove.getToIndex(move);
        // Save the start piece so EnPassant logic can check it
        int startPiece = matrix[fromIndex];

        // Actually make the move
        matrix[toIndex] = matrix[fromIndex];
        matrix[fromIndex] = 0;
        // Handle special move types
        switch (BitMove.getType(move)) {
            case ORDINARY:
                if (startPiece == WHITE_PAWN) {
                    // If it's a two-square pawn move, update the enPassant index
                    if (Math.abs(fromIndex - toIndex) == 32) {
                        // The enPassant square will always be the average on a 0x88 board
                        enPassantSquare = (fromIndex + toIndex) / 2;
                    }
                }
                if (startPiece == BLACK_PAWN) {
                    // If it's a two-square pawn move, update the enPassant index
                    if (Math.abs(fromIndex - toIndex) == 32) {
                        // The enPassant square will always be the average on a 0x88 board
                        enPassantSquare = (fromIndex + toIndex) / 2;
                    }
                }
                break;
            case EN_PASSANT:
                // Only one of these squares will be occupied. We can clear them both and save an if() statement.
                matrix[toIndex + 16] = 0;
                matrix[toIndex - 16] = 0;
                break;
            case WHITE_SHORT_CASTLE:
                // Move the rook from h1 to f1
                matrix[5] = matrix[7];
                matrix[7] = 0;
                break;
            case WHITE_LONG_CASTLE:
                matrix[3] = matrix[0];
                matrix[0] = 0;
                break;
            case BLACK_SHORT_CASTLE:
                matrix[117] = matrix[119];
                matrix[119] = 0;
                break;
            case BLACK_LONG_CASTLE:
                matrix[115] = matrix[112];
                matrix[112] = 0;
                break;
            case PROMOTION_QUEEN:
                if (startPiece == WHITE_PAWN) matrix[toIndex] = WHITE_QUEEN;
                else matrix[toIndex] = BLACK_QUEEN;
                break;
            case PROMOTION_ROOK:
                if (startPiece == WHITE_PAWN) matrix[toIndex] = WHITE_ROOK;
                else matrix[toIndex] = BLACK_ROOK;
                break;
            case PROMOTION_BISHOP:
                if (startPiece == WHITE_PAWN) matrix[toIndex] = WHITE_BISHOP;
                else matrix[toIndex] = BLACK_BISHOP;
                break;
            case PROMOTION_KNIGHT:
                if (startPiece == WHITE_PAWN) matrix[toIndex] = WHITE_KNIGHT;
                else matrix[toIndex] = BLACK_KNIGHT;
                break;
        }
        switch (startPiece) {
            case WHITE_ROOK:
                if (fromIndex == 0) {
                    whiteCastleLong = false;
                }
                if (fromIndex == 7) {
                    whiteCastleShort = false;
                }
                break;
            case BLACK_ROOK:
                if (fromIndex == 112) {
                    blackCastleLong = false;
                }
                if (fromIndex == 119) {
                    blackCastleShort = false;
                }
                break;
            case WHITE_KING:
                whiteCastleShort = false;
                whiteCastleLong = false;
                // Update the king coordinate
                whiteKing = toIndex;
                break;
            case BLACK_KING:
                blackCastleShort = false;
                blackCastleLong = false;
                // Update the king coordinate
                blackKing = toIndex;
                break;
        }
        switch (toIndex) {
            // If any piece moves to any of these corners, castling is no longer possible
            // This takes care of the case where an enemy pieces captures the rook. Instead of checking for the rook's presence
            // when castling, just kill the rights when a piece moves to the rook's starting square.
            case 7:
                whiteCastleShort = false;
                break;
            case 0:
                whiteCastleLong = false;
                break;
            case 112:
                blackCastleLong = false;
                break;
            case 119:
                blackCastleShort = false;
                break;
        }
        // Housekeeping
        if (sideToMove == -1) {
            // Every time black moves, increment the full turn count
            fullMoveCount++;
        }
        sideToMove *= -1;
        halfMoveCount++;
    }

    @Override
    public int getPiece(int index) {
        return matrix[index];
    }

    @Override
    /**
     * Instead of simply returning a <code>boolean</code>, this method will return Definitions.INVALID_MOVE if the
     * move was determined illegal, or a bitmove (just a primitive int) if the move was legal.
     */
    public int checkMove(int start, int end) {
        List<Integer> possibleMoves = getPossibleMoves();
        boolean foundAMatch = false;
        for (int i : possibleMoves) {
            if ((BitMove.getFromIndex(i) == start &&
                    BitMove.getToIndex(i) == end)) {
                foundAMatch = true;
            }
        }
        int bitMove;
        if (foundAMatch) {
            bitMove = checkMoveInternal(start, end);
        } else {
            return INVALID_MOVE;
        }

        // Can't move from an empty square
        /**
         * The real checkMoveInternal method doesn't take this into account. One of it's chief invariants
         * is the fact that the start coordinate of the move to check will always be a piece,
         * because the move is coming from the getPossibleDestinations() function.
         */
        if (BitMove.getFromPiece(bitMove) == EMPTY) {
            return INVALID_MOVE;
        }
        return (bitMove == INVALID_MOVE) ? INVALID_MOVE : bitMove;
    }

    @Override
    public boolean inCheck() {
        if (sideToMove == WHITE_TEAM) {
            return isAttacked(whiteKing, -sideToMove);
        } else {
            return isAttacked(blackKing, -sideToMove);
        }
    }

    @Override
    public boolean isCheckmate() {
        // TODO I foresee this method being a major timesink. Optimize it.
        return inCheck() && getPossibleMoves().isEmpty();
    }

    /**
     * Takes an index and checks if the index can be attacked by 'side'
     *
     * @param attacked The attacked index
     * @param sideToMove If white is attacking, 1; else, make it -1
     * @return boolean True it can be attacked, false it can't
     */
    public final boolean isAttacked(int attacked, int sideToMove) {
        int pieceAttack;
        // White's turn
        if (sideToMove == 1) {
            // Pawns, only two possible squares
            if (((attacked - 17) & 0x88) == 0
                    && matrix[attacked - 17] == WHITE_PAWN) {
                return true;
            }
            if (((attacked - 15) & 0x88) == 0
                    && matrix[attacked - 15] == WHITE_PAWN) {
                return true;
            }

            for (int i = 0; i < 128; i++) {
                if ((i & 0x88) == 0) {
                    if (matrix[i] != 0) {
                        if (matrix[i] == WHITE_KNIGHT) {
                            if (ATTACK_ARRAY[attacked - i + 128] == ATTACK_N) {
                                return true;
                            }
                        }
                        if (matrix[i] == WHITE_BISHOP) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_KQBwP || pieceAttack == ATTACK_KQBbP
                                    || pieceAttack == ATTACK_QB) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == WHITE_ROOK) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_QR || pieceAttack == ATTACK_KQR) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == WHITE_QUEEN) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack != ATTACK_NONE && pieceAttack != ATTACK_N) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == WHITE_KING) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_KQBwP || pieceAttack == ATTACK_KQBbP
                                    || pieceAttack == ATTACK_KQR) {
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            if (((attacked + 17) & 0x88) == 0
                    && matrix[attacked + 17] == BLACK_PAWN) {
                return true;
            }
            if (((attacked + 15) & 0x88) == 0
                    && matrix[attacked + 15] == BLACK_PAWN) {
                return true;
            }

            for (int i = 0; i < 128; i++) {
                if ((i & 0x88) == 0) {
                    if (matrix[i] != 0) {
                        if (matrix[i] == BLACK_KNIGHT) {
                            if (ATTACK_ARRAY[attacked - i + 128] == ATTACK_N) {
                                return true;
                            }
                        }
                        if (matrix[i] == BLACK_BISHOP) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_KQBwP || pieceAttack == ATTACK_KQBbP
                                    || pieceAttack == ATTACK_QB) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == BLACK_ROOK) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_QR || pieceAttack == ATTACK_KQR) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == BLACK_QUEEN) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack != ATTACK_NONE && pieceAttack != ATTACK_N) {
                                if (traverseDelta(i, attacked)) {
                                    return true;
                                }
                            }
                        }
                        if (matrix[i] == BLACK_KING) {
                            pieceAttack = ATTACK_ARRAY[attacked - i + 128];
                            if (pieceAttack == ATTACK_KQBwP || pieceAttack == ATTACK_KQBbP
                                    || pieceAttack == ATTACK_KQR) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false; // If the loops didn't return true, no piece can attack
        // the square
    } // END isAttacked()

    /**
     * Used by isAttacked() to traverse a piece's delta to see if it runs in to
     * any pieces on the way to the attacked square
     *
     * Important: May not be called with an attacker that can't reach the
     * attacked square by its delta. Will cause endless loop. The safety
     * measures are commented out below for a small gain in time.
     *
     * @param attacker The attacking square
     * @param attacked The attacked square
     * @return boolean True if the piece can reach the attacked square, false if
     * not
     */
    private boolean traverseDelta(int attacker, int attacked) {
        int deltaIndex = attacker; // Initialize from first square
        int delta = DELTA_ARRAY[attacked - attacker + 128]; // Find the delta
        // while((deltaIndex & 0x88) == 0) // Traverse until off the board
        while (true) {
            deltaIndex += delta; // Add the delta to move to the next square
            // We reached the attacked square, so we return true
            if (deltaIndex == attacked) {
                return true;
            }
            // A piece was found on the way, so return false
            if (matrix[deltaIndex] != 0) {
                return false;
            }
        }
    }
    @Override
    public void makeNullMove() {
        sideToMove *= -1;
    }

    @Override
    public Position deepClone() {
        PositionImpl clone = new PositionImpl();
        System.arraycopy(this.matrix, 0, clone.matrix, 0, 128);
        clone.whiteCastleShort = whiteCastleShort;
        clone.whiteCastleLong = whiteCastleLong;
        clone.blackCastleShort = blackCastleShort;
        clone.blackCastleLong = blackCastleLong;
        clone.enPassantSquare = enPassantSquare;
        clone.fullMoveCount = fullMoveCount;
        clone.halfMoveCount = halfMoveCount;
        clone.halfMoveClock = halfMoveClock;
        clone.sideToMove = sideToMove;
        clone.whiteKing = whiteKing;
        clone.blackKing = blackKing;
        return clone;
    }

    @Override
    public boolean testMoveGen() {
        Position instance = PositionUtil.createFromFENString(STARTING_FEN);
        if (STARTING_PERFT_5 != PositionUtil.perft(instance, 5)) {
            return false;
        }
        instance = PositionUtil.createFromFENString(COMPLICATED_FEN);
        if (COMPLICATED_PERFT_5 != PositionUtil.perft(instance, 5)) {
            return false;
        }
        instance = PositionUtil.createFromFENString(PROMOTION_FEN);
        if (PROMOTION_PERFT_5 != PositionUtil.perft(instance, 5)) {
            return false;
        }
        return true;
    }

    @Override
    public String writeToFEN() {
        return FENManager.write(this);
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder lineBuffer = new StringBuilder();
        int count = 0;

        Character c = ' ';
        for (int i = 0; i < 128; i++) {

            if (count == 8) {
                count = 0;
                lineBuffer.append(" |");
                lineBuffer.reverse();
                sb.append(lineBuffer);
                lineBuffer = new StringBuilder();

                sb.append("\n--------------------------------- \n");
            }
            // If it's on the legal board
            if ((i & 0x88) == 0) {
                switch (matrix[i]) {
                    case WHITE_PAWN:   c = 'P'; break;
                    case BLACK_PAWN:   c = 'p'; break;
                    case WHITE_KNIGHT: c = 'N'; break;
                    case BLACK_KNIGHT: c = 'n'; break;
                    case WHITE_BISHOP: c = 'B'; break;
                    case BLACK_BISHOP: c = 'b'; break;
                    case WHITE_ROOK: c = 'R'; break;
                    case BLACK_ROOK: c = 'r'; break;
                    case WHITE_QUEEN: c = 'Q'; break;
                    case BLACK_QUEEN: c = 'q'; break;
                    case WHITE_KING: c = 'K'; break;
                    case BLACK_KING: c = 'k'; break;
                    case EMPTY:          c = ' '; break;
                }
                lineBuffer.append(" | ");
                lineBuffer.append(c);
                count++;
            }
        }
        sb.reverse();
        sb.append("\n ---------------------------------\n\n");
        return sb.toString().trim();

    }

    /**
     * Returns a boolean value signifying whether or not the kth bit in the
     * given integer x is set.
     *
     * @param piece the piece variable to check
     * @return a Boolean, whether or not the bit is set. If true, piece is black. Else, piece is white.
     */
    public static boolean checkTeamBit(int piece) {
        return (piece >> 3) == 1;
    }
    public static class FENManager {

        public static String write(PositionImpl position) {

            /* Just as I learned in writing the PositionImpl.toString() method, getting a 1-d array out
           into a string without mirroring anything is tricky
           So we use two buffers, one for lines, and the other for the whole string
           In order to prevent left-right mirroring, we reverse each line before
           inserting it into the buffer. At the end, we reverse the entire buffer to prevent
           top-bottom mirroring.
            */
            StringBuilder sb = new StringBuilder();
            StringBuilder lineBuffer = new StringBuilder();
            int count = 0;
            int blankCounter = 0;
            for (int i = 0; i < 128; i++) {
                if (count == 8) {
                    count = 0;
                    lineBuffer.reverse();
                    sb.append(lineBuffer);
                    blankCounter = 0;
                    lineBuffer = new StringBuilder();
                    if (i != 120) sb.append('/');
                }
                if ((i & 0x88) == 0)  {
                    char c = ' ';
                    switch (position.matrix[i]) {
                        case WHITE_PAWN:   c = 'P'; break;
                        case BLACK_PAWN:   c = 'p'; break;
                        case WHITE_KNIGHT: c = 'N'; break;
                        case BLACK_KNIGHT: c = 'n'; break;
                        case WHITE_BISHOP: c = 'B'; break;
                        case BLACK_BISHOP: c = 'b'; break;
                        case WHITE_ROOK: c = 'R'; break;
                        case BLACK_ROOK: c = 'r'; break;
                        case WHITE_QUEEN: c = 'Q'; break;
                        case BLACK_QUEEN: c = 'q'; break;
                        case WHITE_KING: c = 'K'; break;
                        case BLACK_KING: c = 'k'; break;
                        case 0:          c = ' '; break;
                    }
                    if (c == ' ') {
                        // If it's an empty square, increment blank counter
                        // If the last piece we drew was a blank one, and this one is too,
                        // erase the previous char and write a higher number
                        if (blankCounter > 0) {
                            lineBuffer.deleteCharAt(lineBuffer.length()-1);
                            lineBuffer.append(Integer.toString(blankCounter+1));
                        } else {
                            lineBuffer.append(1);
                        }
                        blankCounter++;
                    } else {
                        // Otherwise, reset the counter and write the regular piece character
                        blankCounter = 0;
                        lineBuffer.append(c);
                    }
                    count++;
                }
            }
            sb.reverse();
            sb.append(' ');
            if (position.sideToMove == WHITE_TEAM) {
                sb.append('w');
            } else {
                sb.append('b');
            }
            sb.append(' ');
            boolean noOneCanCastle = true;
            if (position.whiteCastleShort) {
                sb.append('K');
                noOneCanCastle = false;
            }
            if (position.whiteCastleLong) {
                sb.append('Q');
                noOneCanCastle = false;
            }
            if (position.blackCastleShort) {
                sb.append('k');
                noOneCanCastle = false;
            }
            if (position.blackCastleLong) {
                sb.append('q');
                noOneCanCastle = false;
            }
            if (noOneCanCastle) {
                sb.append('-');
            }
            sb.append(' ');
            if (position.enPassantSquare == -1) {
                sb.append('-');
            } else {
                sb.append(CoordinateUtility.convert0x88ToSAN(position.enPassantSquare));
            }
            sb.append(' ');
            sb.append(Integer.toString(position.halfMoveClock));
            sb.append(' ');
            sb.append(Integer.toString(position.fullMoveCount));

            return sb.toString();
        }
        public static Position read(String input) {
            int[] matrix = new int[128];
            int phase = 1;
            int index = 112;
            PositionImpl p = new PositionImpl();
            Character character;
            boolean alreadySentIt = false;
            for (int i = 0; i < input.length(); i++) {
                character = input.charAt(i);

                if (Character.isWhitespace(character)) {
                    phase++;
                    continue;
                }
                // Phase 1 is the piece configuration phase
                if (phase == 1) {
                    if (character == '/') {
                        // Move down one column, back to the beginning
                        index -= 24;
                    }
                    if (Character.isLetter(character)) {
                        switch (character) {
                            case 'P':
                                matrix[index] = WHITE_PAWN;
                                break;
                            case 'p':
                                matrix[index] = BLACK_PAWN;
                                break;
                            case 'N':
                                matrix[index] = WHITE_KNIGHT;
                                break;
                            case 'n':
                                matrix[index] = BLACK_KNIGHT;
                                break;
                            case 'B':
                                matrix[index] = WHITE_BISHOP;
                                break;
                            case 'b':
                                matrix[index] = BLACK_BISHOP;
                                break;
                            case 'R':
                                matrix[index] = WHITE_ROOK;
                                break;
                            case 'r':
                                matrix[index] = BLACK_ROOK;
                                break;
                            case 'Q':
                                matrix[index] = WHITE_QUEEN;
                                break;
                            case 'q':
                                matrix[index] = BLACK_QUEEN;
                                break;
                            case 'K':
                                matrix[index] = WHITE_KING;
                                p.whiteKing = index;
                                break;
                            case 'k':
                                matrix[index] = BLACK_KING;
                                p.blackKing = index;
                                break;
                        }
                        index++;
                    }
                    if (Character.isDigit(character)) {
                        int count = Integer.parseInt(character.toString());
                        for (int ii = count; ii > 0; ii--) {
                            matrix[index] = 0;
                            index++;
                        }
                    }
                }
                // Team to Move phase
                if (phase == 2) {
                    if (character == 'w') {
                        p.sideToMove = WHITE_TEAM;
                    }
                    if (character == 'b') {
                        // Default is white, so now it will be black's turn
                        p.sideToMove = BLACK_TEAM;
                    }
                }
                //Castling phase
                if (phase == 3) {
                    if (character == '-') {
                        continue;
                    }
                    if (character == 'K') {
                        p.whiteCastleShort = true;
                    }
                    if (character == 'Q') {
                        p.whiteCastleLong = true;
                    }
                    if (character == 'k') {
                        p.blackCastleShort = true;
                    }
                    if (character == 'q') {
                        p.blackCastleLong = true;
                    }
                }
                // En Passant phase
                if (phase == 4) {
                    if (character != '-' && !alreadySentIt) {
                        char nextChar = input.charAt(i+1);
                        String sanString = String.valueOf(character) + String.valueOf(nextChar);
                        p.enPassantSquare = CoordinateUtility.convertSANTo0x88(sanString);
                        alreadySentIt = true;
                        continue;
                    }
                }
                // Half Move Clock Phase
                if (phase == 5) {
                    if (Character.isDigit(character)) {
                        p.halfMoveClock = Integer.parseInt(character.toString());
                    }
                }
                // Full Move Clock Phase
                if (phase == 6) {
                    if (Character.isDigit(character)) {
                        p.fullMoveCount = Integer.parseInt(character.toString());
                        p.halfMoveCount = p.fullMoveCount * 2;
                    }
                }
            }
            p.matrix = matrix;
            return p;
        }
    }
}
