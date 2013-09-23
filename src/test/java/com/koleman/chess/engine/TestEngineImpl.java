package com.koleman.chess.engine;

import com.koleman.chess.model.Move;
import com.koleman.chess.model.PositionUtil;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Author Koleman Nix
 * Created On 7/19/12 at 10:19 AM
 */
public class TestEngineImpl extends TestCase {

    @Test
    public void testSetPosition() {
        Engine engine = new EngineImpl();
        engine.setPosition(PositionUtil.createStartingPosition());
        Move move = engine.computeMoveAtDepth(5);
        System.out.println(move.toSAN());
    }
}
