package com.koleman.chess.model;

import java.util.List;

/**
 * Author Koleman Nix
 * Created On 6/25/12 at 11:49 AM
 */
public interface Position {
    public int getTeamToMove();
    public int getFullMoveCount();
    public int getHalfMoveCount();
    public List<Integer> getPossibleMoves();
    public List<Integer> getCaptureMoves();
    public void makeMove(int move);
    public void makeMove(Move move);
    public int getPiece(int index);
    public int checkMove(int start, int end);
    public boolean inCheck();
    public boolean isCheckmate();
    public void makeNullMove();
    public Position deepClone();
    public boolean testMoveGen();
    public String writeToFEN();
}
