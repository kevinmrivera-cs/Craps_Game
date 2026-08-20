package model;

import java.util.Random;

/**
 * Represents a pair of six-sided dice used in the game.
 *
 * <p>This class handles rolling two dice, storing their values,
 * computing the sum, and resetting values when needed.</p>
 *
 * <p>The dice use {@link java.util.Random} for generating values between
 * {@link #MIN_NUM} and {@link #MAX_NUM}.</p>
 *
 * @version 12/09/2025
 * @author Kevin Munoz-Rivera
 */
public final class Dice {

    /** Shared Random instance for all dice rolls. */
    private static final Random RANDOM = new Random();

    /** Minimum roll value for a die. */
    public static final int MIN_NUM = 1;

    /** Maximum roll value for a die. */
    public static final int MAX_NUM = 6;

    /** Result of the first die. */
    private int myRoll1;

    /** Result of the second die. */
    private int myRoll2;

    /** Combined sum of the two dice. */
    private int mySumOfDicesRoll;

    /**
     * Constructs a Dice object with initial rolls set to zero.
     */
    public Dice() {
        myRoll1 = 0;
        myRoll2 = 0;
        mySumOfDicesRoll = 0;
    }

    /**
     * Rolls a single die and returns the result.
     *
     * @return a random number between MIN_NUM and MAX_NUM
     */
    private int rollDice() {
        return RANDOM.nextInt(MAX_NUM) + MIN_NUM;
    }

    /**
     * Rolls both dice and updates roll values and total sum.
     */
    public void roll2Dices() {
        myRoll1 = rollDice();  // Roll first die
        myRoll2 = rollDice();  // Roll second die
        mySumOfDicesRoll = myRoll1 + myRoll2; // Compute total
    }

    /**
     * Returns the value of the first die.
     *
     * @return value of die #1
     */
    public int getMyRoll1() {
        return myRoll1;
    }

    /**
     * Returns the value of the second die.
     *
     * @return value of die #2
     */
    public int getMyRoll2() {
        return myRoll2;
    }

    /**
     * Returns the sum of both dice.
     *
     * @return sum of roll1 + roll2
     */
    public int getMySumOfDicesRoll() {
        return mySumOfDicesRoll;
    }

    /**
     * Resets all dice rolls and total sum back to zero.
     */
    public void reset() {
        myRoll1 = 0;
        myRoll2 = 0;
        mySumOfDicesRoll = 0;
    }
}
