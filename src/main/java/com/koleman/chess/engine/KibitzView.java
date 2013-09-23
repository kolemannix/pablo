package com.koleman.chess.engine;

import com.koleman.chess.model.Move;

/**
 * Author Koleman Nix
 * Created On 7/19/12 at 9:16 AM
 */
public interface KibitzView {
    public void setBestMove(Move em);
    public void setPV(String pv);
    public void setCurrentPly(int ply);
    public void setNodesPerSecond(int nKps);
    public void setNodesSearched(int totalNodes);
    public void setEvaluation(double score);
    public void setProgress(int completed, int possible);
    public void setDeepestPly(int ply);
    public void clearDisplayBoard();
    public void clearAll();
    public void highlightMove(Move m);
    public void clearHighlightedMove();
    public void setDisplayBoard(String string);
}
