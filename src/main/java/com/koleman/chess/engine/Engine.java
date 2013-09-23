package com.koleman.chess.engine;

import com.koleman.chess.model.Move;
import com.koleman.chess.model.Position;

/**
 * Author Koleman Nix
 * Created On 7/19/12 at 9:11 AM
 */
public interface Engine {
    /**
     * Sets the position that the engine will be searching
     */
    public void setPosition(Position position);

    /**
     * Searches until the specified depth is reached, then returns the best move
     * @param depth How many plies to search
     */
    public Move computeMoveAtDepth(int depth);

    /**
     * Searches until the specified time has run out, and returns the best thing found so far
     * @param millis How long to search
     */
    public Move computeMoveTimed(int millis);

    /**
     * Searches forever.
     */
    public void computeMoveIndefinitely() throws InterruptedException;

    public void setKibitzView(KibitzView view);

    public void removeKibitzView();

    public void stopCalculating();
}
