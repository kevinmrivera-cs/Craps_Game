package model;

import java.util.Set;

/**
 * Represents a player in the Craps-style dice game.
 *
 * <p>This class handles:
 * <ul>
 *     <li>Tracking game state (turn status, win/loss)</li>
 *     <li>Point setting and determining win/loss results</li>
 *     <li>Counting total wins and losses</li>
 * </ul>
 * </p>
 *
 * <p>The Player does not roll dice directly; instead it receives the
 * sum of two dice and evaluates the result.</p>
 *
 * @version 08/19/2026
 * @author Kevin Munoz-Rivera
 */
public final class Player {

    /** Come-out roll values that result in an immediate loss. */
    private static final Set<Integer> COME_OUT_LOSSES = Set.of(2, 3, 12);

    /** Come-out roll values that result in an immediate win. */
    private static final Set<Integer> COME_OUT_WINS = Set.of(7, 11);

    /** Whether the player has won the current round. */
    private boolean myWin;

    /** Whether a point has been established. */
    private boolean myHitPointFirst;

    /** Whether the current turn is still active. */
    private boolean myTurn;

    /** The established point the player must roll again to win. */
    private int mySetTotalPoints;

    /** Total rounds won by the player. */
    private int myWinCount;

    /** Total rounds lost by the player. */
    private int myLostCount;

    /**
     * Constructs a Player with all counters cleared and a fresh turn active.
     */
    public Player() {
        myTurn = true;
        myWin = false;
        myHitPointFirst = false;
        mySetTotalPoints = 0;
        myWinCount = 0;
        myLostCount = 0;
    }

    /**
     * Validates that the sum of the dice roll is within the legal range (2-12).
     *
     * @param theSumDiceRoll sum of two dice
     * @throws IllegalArgumentException if value is outside 2-12
     */
    private static void checkValidNum(final int theSumDiceRoll) {
        if (theSumDiceRoll < Dice.MIN_NUM * 2
                || theSumDiceRoll > Dice.MAX_NUM * 2) {
            throw new IllegalArgumentException(
                    "The sum of the 2 dice must be from 2 - 12. Input was: "
                            + theSumDiceRoll);
        }
    }

    /**
     * Processes a dice roll result and updates player state.
     *
     * <p>On the come-out roll, 7 or 11 wins and 2, 3, or 12 loses. Any other
     * total establishes the point. After a point is established, rolling the
     * point wins, rolling 7 loses, and every other total continues the round.</p>
     *
     * @param theSumDiceRoll sum of the two dice rolled
     */
    public void resultDiceRoll(final int theSumDiceRoll) {
        checkValidNum(theSumDiceRoll);

        // Ignore extra results after the round has already ended.
        if (!myTurn) {
            return;
        }

        if (!myHitPointFirst) {
            processComeOutRoll(theSumDiceRoll);
        } else {
            processPointRoll(theSumDiceRoll);
        }
    }

    /**
     * Processes the first roll of a round.
     *
     * @param theSumDiceRoll the come-out roll total
     */
    private void processComeOutRoll(final int theSumDiceRoll) {
        if (COME_OUT_WINS.contains(theSumDiceRoll)) {
            finishRound(true);
        } else if (COME_OUT_LOSSES.contains(theSumDiceRoll)) {
            finishRound(false);
        } else {
            mySetTotalPoints = theSumDiceRoll;
            myHitPointFirst = true;
        }
    }

    /**
     * Processes a roll after a point has been established.
     *
     * @param theSumDiceRoll the roll total
     */
    private void processPointRoll(final int theSumDiceRoll) {
        if (theSumDiceRoll == mySetTotalPoints) {
            finishRound(true);
        } else if (theSumDiceRoll == 7) {
            finishRound(false);
        }
    }

    /**
     * Ends the current round and updates the correct counter.
     *
     * @param thePlayerWon true when the player won, false when the house won
     */
    private void finishRound(final boolean thePlayerWon) {
        myWin = thePlayerWon;
        myTurn = false;

        if (thePlayerWon) {
            myWinCount++;
        } else {
            myLostCount++;
        }
    }

    /**
     * Resets round-specific values but keeps total win/loss count.
     */
    public void resetRound() {
        myHitPointFirst = false;
        myWin = false;
        myTurn = true;
        mySetTotalPoints = 0;
    }

    /**
     * Returns the player's current point value.
     *
     * @return point value, or zero when no point is established
     */
    public int getHitTotalPoints() {
        return mySetTotalPoints;
    }

    /**
     * Returns the total number of wins.
     *
     * @return win counter
     */
    public int getMyWinCount() {
        return myWinCount;
    }

    /**
     * Returns the total number of losses.
     *
     * @return loss counter
     */
    public int getMyLostCount() {
        return myLostCount;
    }

    /**
     * Indicates whether the turn is still active.
     *
     * @return true if player is still rolling, false if round has ended
     */
    public boolean getTurn() {
        return myTurn;
    }

    /**
     * Returns true if the player won the current round.
     *
     * @return win state
     */
    public boolean getWinResult() {
        return myWin;
    }
}
