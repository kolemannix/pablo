package com.koleman.chess.engine;

import com.koleman.chess.model.BitMove;
import com.koleman.chess.model.Move;
import com.koleman.chess.model.Position;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.koleman.chess.model.Definitions.*;

/**
 * Author Koleman Nix
 * Created On 7/19/12 at 9:15 AM
 */
public class EngineImpl implements Engine {
    /** Aspiration window */
    private static final int ASP_WINDOW = 10;
    /** The maximum depth at which to perform quiescent searches, to prevent a quiescent explosion */
    private static final int MAX_QUIES = 10;
    /** Null Move reduction constant */
    private static final int R = 2;
    /** The Checkmate constant. It's a very high score because, well, checkmates are good. */
    private static final int MATE = 150000;
    /** Virtual Infinity */
    private static final int INFINITY = 300000;
    /** The time interval, in milliseconds */
    private static final int TIME_INTERVAL = 100;

    private KibitzView kibitzView;
    private int nodesSearched = 0;
    private boolean timedOut = false;
    private boolean shouldKibitz = false;
    private int timeRemaining = 0;
    private Timer timer;
    private int A = -INFINITY, B = INFINITY;
    private int deepestPly = 0;
    private Position position;
    private int difficulty = -1;

    public EngineImpl(KibitzView kview) {
        kibitzView = kview;
        shouldKibitz = true;
    }

    public EngineImpl(int dif) {
        difficulty = dif;
        shouldKibitz = false;
    }
    public EngineImpl() {
        shouldKibitz = false;
        difficulty = 3;
    }

    public int evaluatePosition(Position position) {
        int score = evaluateMaterial(position);
        return score * position.getTeamToMove();
    }

    /**
     * Simple, unintelligent evaluation function for material, in centipawns.
     * (pawns worth 100 points). It also gives development bonuses. For example,
     * in the first 20 moves, a bishop in its starting position is worth less
     * than a bishop anywhere else on the position. Likewise, pawns in the center 4
     * squares are worth more than pawns on their starting position. Also,
     * Knights receive a bonus for occupying the center. Rooks get a 7th rank
     * bonus, etc.
     *
     * @param position The position to evaluate
     * @return The calculated score. Positive if white is winning, negative if
     * black is winning. (hoorah for zero-sum games!)
     */
    public int evaluateMaterial(Position position) {
        int whitePoints = 0;
        int blackPoints = 0;

        for (int i = 0; i < 128; i++) {
            if ((i & 0x88) == 0) {
                int piece = position.getPiece(i);
                switch (piece) {
                    case WHITE_PAWN:
                        if (i > 15 && i < 24) {
                            whitePoints -= 10;
                        }
                        if (i == 51 || i == 52 || i == 67 || i == 68) {
                            whitePoints += 25;
                        }
                        whitePoints += VAL_PAWN;
                        break;
                    case WHITE_KNIGHT:
                        if (i == 1 || i == 6) {
                            whitePoints -= 25;
                        }
                        if (i == 39 || i == 32) {
                            whitePoints -= 15;
                        }
                        whitePoints += VAL_KNIGHT;
                        break;
                    case WHITE_BISHOP:
                        if (i == 2 || i == 5) {
                            whitePoints -= 25;
                        }
                        whitePoints += VAL_BISHOP;
                        break;
                    case WHITE_ROOK:
                        // Rook on the 7th and 8th rank bonus
                        if (i > 95 && i < 104) {
                            whitePoints += 50;
                        }
                        if (i > 111 && i < 120) {
                            whitePoints += 25;
                        }
                        whitePoints += VAL_ROOK;
                        break;
                    case WHITE_QUEEN:
                        whitePoints += VAL_QUEEN;
                        break;
                    case WHITE_KING:
                        whitePoints += VAL_KING;
                        break;
                    case BLACK_PAWN:
                        if (i > 95 && i < 104) {
                            blackPoints -= 10;
                        }
                        if (i == 51 || i == 52 || i == 67 || i == 68) {
                            blackPoints += 25;
                        }
                        blackPoints += VAL_PAWN;
                        break;
                    case BLACK_KNIGHT:
                        if (i == 113 || i == 118) {
                            blackPoints -= 25;
                        }
                        if (i == 80 || i == 87) {
                            blackPoints -= 15;
                        }
                        blackPoints += VAL_KNIGHT;
                        break;
                    case BLACK_BISHOP:
                        if (i == 114 || i == 117) {
                            blackPoints -= 25;
                        }
                        blackPoints += VAL_BISHOP;
                        break;
                    case BLACK_ROOK:
                        // Rook on the 7th and 8th rank bonus
                        if (i > 15 && i < 24) {
                            blackPoints += 50;
                        }
                        if (i > -1 && i < 8) {
                            blackPoints += 25;
                        }
                        blackPoints += VAL_ROOK;
                        break;
                    case BLACK_QUEEN:
                        blackPoints += VAL_QUEEN;
                        break;
                    case BLACK_KING:
                        blackPoints += VAL_KING;
                        break;
                }
            }
        }
        return whitePoints - blackPoints;
    }

    /**
     * Very similar to ComputeMoveTimed(). However, rather than inputting an
     * allotted time, the caller simply inputs the desired depth to search. Be
     * careful calling this function. If you use a really deep depth (10+) while
     * many pieces are still on the position, your computer may start growling at
     * you. This function doesn't update the Diagnostic engine window.
     *
     * @param depth The depth at which to search.
     * @return The best move found.
     */
    @Override
    public Move computeMoveAtDepth(int depth) {
        timedOut = false;
        deepestPly = 0;
        return mainSearchRoot(position, depth);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * This is the main function of the Engine. Give it a position to search, and a
     * time limit, and it will return the best move found. It uses a non
     * quiescent search at depth 0 to find its first move, then proceeds to use
     * a quiescent search with iterative deepening to find a better move. It
     * keeps deepening until time runs out, at which point it returns its best
     * thing so far.
     * @param millis The time allotted to complete the search. (in milliseconds)
     * @return The Best Move found.
     */
    public Move computeMoveTimed(int millis) {
        if (shouldKibitz) {
            kibitzView.clearAll();
            kibitzView.clearDisplayBoard();
        }
        nodesSearched = 0;
        timedOut = false;
        deepestPly = 0;
        int alpha = -INFINITY;
        int beta = INFINITY;
        long startTime;
        long endTime;
        Move bestMove = mainSearchRootNoQuies(position, 1, alpha, beta);
        if (shouldKibitz) kibitzView.setBestMove(bestMove);
        timeRemaining = millis;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        };
        timer = new Timer("Engine Timer");
        timer.scheduleAtFixedRate(task, 0, TIME_INTERVAL);
        int depth = 0;
        while (!timedOut) {
            startTime = System.currentTimeMillis();
            Move move = mainSearchRoot(position, depth);
            endTime = System.currentTimeMillis();
            if (move != null && shouldKibitz) {
                kibitzView.highlightMove(move);
                bestMove = move;
            }
            depth++;
            if (!timedOut && shouldKibitz) {
                int time = (int) endTime - (int) startTime;
                kibitzView.setBestMove(move);
                kibitzView.setCurrentPly(depth - 1);
                if (time != 0) {
                    kibitzView.setNodesPerSecond(nodesSearched / (int) (endTime - startTime));
                }
            }
        }
        return bestMove;
    }

    public Move computeMoveBasedOnDifficulty() {
        if (difficulty == DIFFICULTY_HARD) {
            return computeMoveAtDepth(4);
        }
        if (difficulty == DIFFICULTY_MEDIUM) {
            return computeMoveAtDepth(3);
        }
        if (difficulty == DIFFICULTY_EASY) {
            return computeMoveAtDepth(2);
        }
        return computeMoveAtDepth(3);
    }

    /**
     * This is the main function of the Engine. Give it a position to search, and a
     * time limit, and it will return the best move found. It uses a non
     * quiescent search at depth 0 to find its first move, then proceeds to use
     * a quiescent search with iterative deepening to find a better move. It
     * keeps deepening forever, setting the best move found so far in the kibitz view.
     */
    @Override
    public void computeMoveIndefinitely() throws InterruptedException {
        if (shouldKibitz) {
            kibitzView.clearAll();
            kibitzView.clearDisplayBoard();
        }
        nodesSearched = 0;
        deepestPly = 0;
        int alpha = -INFINITY;
        int beta = INFINITY;
        long startTime;
        long endTime;
        Move bestMove = mainSearchRootNoQuies(position, 1, alpha, beta);
        if (shouldKibitz) {
            kibitzView.setBestMove(bestMove);
        }
        int depth = 0;
        while (true) {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            startTime = System.currentTimeMillis();
            Move move = mainSearchRoot(position, depth);
            endTime = System.currentTimeMillis();
            if (move != null && shouldKibitz) {
                Move m = move;
                kibitzView.highlightMove(m);
                kibitzView.setBestMove(m);
            }
            depth++;
            if (shouldKibitz) {
                int time = (int) endTime - (int) startTime;
                kibitzView.setBestMove(move);
                kibitzView.setCurrentPly(depth - 1);
                if (time != 0) {
                    kibitzView.setNodesPerSecond(nodesSearched / (int) (endTime - startTime));
                }
            }
        }
    }

    @Override
    public void setKibitzView(KibitzView view) {
        this.kibitzView = view;
        shouldKibitz = true;
    }

    @Override
    public void removeKibitzView() {
        shouldKibitz = false;
        this.kibitzView = null;
    }

    @Override
    public void stopCalculating() {
        timedOut = true;
    }

    /**
     * Updates the timeRemaining global variable every TIME_INTERVAL (currently
     * 100 ms), and updates the timedOut boolean global variable appropriately.
     */
    private void updateTime() {
        timeRemaining -= TIME_INTERVAL;
        if (timeRemaining <= 100) {
            timedOut = true;
            timer.cancel();
        }
    }

    /**
     * This search will return the best Move found, rather than simply an
     * integer score. It calls Negamax WITH quiescent search, and is thus much
     * more time-consuming, and much more thorough, at a given depth than
     * mainSearchRootNoQuies(). Call this once you are sure that you at least
     * have one decent move to return, and can afford to try to find a better
     * one.
     *
     * @param position The position to search
     * @param depth The depth at which to search. Note: Quiescent evaluation
     * will run much deeper than this depth at some points, but the complete
     * search will not exceed this depth.
     * @return
     */
    private Move mainSearchRoot(Position position, int depth) {
        int score;
        int bestMove = -1;
        int best = -INFINITY;
        List<Integer> moves = position.getPossibleMoves();
        int total = moves.size();
        int progress = 0;
        A = -INFINITY;
        B = INFINITY;

        for (Integer move : moves) {
            if (timedOut  || Thread.interrupted()) {
                return null;
            }
            Position positionCopy = position.deepClone();
            positionCopy.makeMove(move);
            score = -NegamaxQuiescent(positionCopy, 1, depth - 1, A, B, true);
            if (shouldKibitz) {
                kibitzView.setDisplayBoard(positionCopy.toString());
                progress++;
                kibitzView.setNodesSearched(nodesSearched);
                kibitzView.setProgress(progress, total);
                kibitzView.setDeepestPly(deepestPly);
            }
//            System.out.print(BitMove.getMoveObject(move).toSAN() + ": " + score);
            if (position.getTeamToMove() == WHITE_TEAM) {
                System.out.print(BitMove.getMoveObject(move).toSAN() + ": " + score);
            } else {
                System.out.print(BitMove.getMoveObject(move).toSAN() + ": " + -score);
            }
            if (score > best) {
                best = score;
                if (shouldKibitz) kibitzView.setEvaluation((score/100.0)*(position.getTeamToMove()));
                bestMove = move;
                System.out.println(" *(New Best)");
            } else {
                System.out.println();
            }
        }
        // Set up the aspiration window for the next search.

        if (best <= A || best >= B) {
            A = -INFINITY;
            B = INFINITY;
        } else {
            A = best - ASP_WINDOW;
            B = best + ASP_WINDOW;
        }
        return BitMove.getMoveObject(bestMove);
    }

    /**
     * This search will return the best MOVE found, rather than simply an
     * integer score. This search calls Negamax without quiescent evaluation. As
     * such, it is subject to the horizon effect. A good use of this function is
     * to call it with a depth of 1 or 0, just to ensure that you have a decent
     * move to return when the time runs out. (useful for short calculation
     * times, like 1 second)
     *
     * @param position The board to search
     * @param depth The depth at which to search
     * @param alpha Alpha value. Use -infinity
     * @param beta Beta value. Use infinity.
     * @return The best move found.
     */
    private Move mainSearchRootNoQuies(Position position, int depth, int alpha, int beta) {
        int score;
        int bestMove = -1;
        int best = -INFINITY;
        for (Integer move : position.getPossibleMoves()) {
            if (timedOut) {
                return null;
            }
            Position boardCopy = position.deepClone();
            boardCopy.makeMove(move);
            score = -Negamax(boardCopy, 1, depth - 1, alpha, beta);
            if (shouldKibitz) {
                kibitzView.setNodesSearched(nodesSearched);
            }
            if (score > best) {
                best = score;
                bestMove = move;
            }
        }
        return BitMove.getMoveObject(bestMove);
    }

    public void stop() {
        timedOut = true;
    }

    public void unstop() {
        timedOut = false;
    }

    /**
     * A Negamax search which uses Quiescent evaluation. This means that once
     * the depth parameter is reached, it will run a quiescent search, which
     * evaluates all possible nodes until there are no captures available, and
     * the position is "quiet".
     *
     * @param board The board to search
     * @param ply the current ply. If this isn't a recursive call, this should
     * be set to 0.
     * @param depth The depth to search. Anything more than 6 with a quiescent
     * search is asking for a long wait.
     * @param alpha The alpha value. If this isn't a recursive call, alpha
     * should be set to negative Infinity
     * @param beta The beta value. If this isn't a recursive call, beta should
     * be set to positive infinity
     * @param allowNull Whether or not to use null move forward pruning. This
     * improves search speed, but can mess up the evaluation in late game
     * scenarios when Zugzwangs are more common.
     * @return The score of the best continuation.
     */
    public int NegamaxQuiescent(Position board, int ply, int depth, int alpha, int beta, boolean allowNull) {
        nodesSearched++;
        if (timedOut) {
            return 0;
        }
        int val;
        if (allowNull && !board.inCheck()) {
            board.makeNullMove(); // Making a null-move
            val = -NegamaxQuiescent(board, ply + 1, depth - 1 - R, -beta, -beta + 1, false); // Evaluating the position.
            board.makeNullMove(); // Unmaking a null-move

            if (val >= beta) {
                return val; // Cutoff
            }
        }
        if (depth <= 0) {
            return Quies(board, ply + 1, alpha, beta);
        }
        List<Integer> legalMoves = board.getPossibleMoves();

        if (legalMoves.isEmpty()) {
            // It's a checkmate!
            if (board.inCheck()) {
                return -MATE + ply;
            } else {
                // It's a stalemate!
                return 0;
            }
        }
        int best = -INFINITY;
        for (Integer move : legalMoves) {
            Position boardCopy = board.deepClone();
            boardCopy.makeMove(move);
            val = -NegamaxQuiescent(boardCopy, ply + 1, depth - 1, -beta, -alpha, true); // Note the minus sign here.

            if (val >= beta) {
                return beta;
            }
            if (val > alpha) {
                alpha = val;
            }
        }
        return alpha;

    }

    /**
     * A quiescent search. This function searches the board parameter until no
     * more captures can be made, and returns the score of the 'quiet' position.
     * This solves the 'horizon' problem.
     *
     * @param position The board to analyze
     * @param ply the current ply (again, if this isn't a recursive call, set it
     * to 0)
     * @param alpha If non-recursive call, set to negative infinity.
     * @param beta If non-recursive call, set to positive infinity.
     * @return The score of the quiet position.
     */
    public int Quies(Position position, int ply, int alpha, int beta) {
        nodesSearched++;

        if (ply > deepestPly) {
            deepestPly = ply;
        }
        if (timedOut) {
            return 0;
        }
        if (position.inCheck()) {
            if (position.isCheckmate()) {
                return -(MATE + ply);
            }
            return Negamax(position, ply + 1, 1, alpha, beta);
        }
        int val = evaluatePosition(position);

        if (val >= beta) {
            return beta;
        }
        if (val > alpha) {
            alpha = val;
        }
        if (ply >= MAX_QUIES) {
            return alpha;
        }

        List<Integer> moves = position.getCaptureMoves();
        for (Integer move : moves) {
            Position boardCopy = position.deepClone();
            boardCopy.makeMove(move);
            val = -Quies(boardCopy, ply + 1, -beta, -alpha);
            if (val >= beta) {
                return beta;
            }
            if (val > alpha) {
                alpha = val;
            }
        }
        return alpha;
    }

    /**
     * A negamax search without quiescent evaluation. Subject to the limits of
     * the horizon effect.
     *
     * @param board The board to search.
     * @param ply The current ply (0 if this is a root call, recursive calls
     * will be ply + 1.
     * @param depth The depth to search ( can go much deeper than
     * negamaxQuiescent because quiescent searched take a lot of time )
     * @param alpha The alpha value. If not recursive call, set to negative
     * Infinity
     * @param beta The beta value. If not recursive call, set to positive
     * Infinity.
     * @return
     */
    public int Negamax(Position board, int ply, int depth, int alpha, int beta) {
        nodesSearched++;
        int val;
        if (timedOut) {
            return 0;
        }
        if (depth <= 0) {
            return evaluatePosition(board);
        }
        List<Integer> legalMoves = board.getPossibleMoves();
        int best = -INFINITY;
        if (legalMoves.isEmpty()) {
            // It's a checkmate!
            if (board.inCheck()) {
                return -(MATE + ply);
            } else {
                // It's a stalemate!
                return 0;
            }
        }
        for (Integer move : legalMoves) {
            Position boardCopy = board.deepClone();
            boardCopy.makeMove(move);
            val = -Negamax(boardCopy, ply + 1, depth - 1, -beta, -alpha); // Note the minus sign here.

            if (val >= beta) {
                return beta;
            }
            if (val > alpha) {
                alpha = val;
            }
        }
        return alpha;
    }
}

