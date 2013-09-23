package com.koleman.chess.engine;

/**
 * Author Koleman Nix
 * Created On 7/19/12 at 10:21 AM
 */
public class EngineUtil {
    public static Engine createDefaultEngine() {
        return new EngineImpl();
    }
}
