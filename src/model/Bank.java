package model;

/**
 * Represents the player's bank account for managing betting money.
 *
 * <p>This class tracks the player's total balance, the amount bet in
 * the current round, and performs validation to ensure the player
 * cannot bet more money than they own.</p>
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
public final class Bank {

    /** Total money available in the player's account. */
    private int myAccount;

    /** Money bet during the current round. */
    private int myCurrentRoundBet;

    /**
     * Private empty constructor to prevent creating a Bank with no balance.
     */
    private Bank() {
    }

    /**
     * Constructs a Bank with a starting amount of money.
     *
     * @param theMoney the initial account balance
     * @throws IllegalArgumentException if theMoney is zero or negative
     */
    public Bank(final int theMoney) {
        checkValidNum(theMoney);
        myAccount = theMoney;
        myCurrentRoundBet = 0;
    }

    /**
     * Validates that a money value is greater than zero.
     *
     * @param theMoney value to check
     * @throws IllegalArgumentException if money is less than or equal to zero
     */
    private static void checkValidNum(final int theMoney) {
        if (theMoney <= 0) {
            throw new IllegalArgumentException(
                    "Money must be greater than zero. Input was: " + theMoney);
        }
    }

    /**
     * Validates that the player has enough funds to place a bet.
     *
     * @param theMoney the bet amount
     * @param theAccount the current account balance
     * @throws IllegalArgumentException if the bet exceeds available funds
     */
    private static void checkValidAccount(final int theMoney,
                                          final int theAccount) {
        if (theMoney > theAccount) {
            throw new IllegalArgumentException("Not enough money to place that bet.");
        }
    }

    /**
     * Returns the current account balance.
     *
     * @return the player's account total
     */
    public int getMyAccount() {
        return myAccount;
    }

    /**
     * Returns the total amount bet in the current round.
     *
     * @return current round bet total
     */
    public int getMyCurrentRoundBet() {
        return myCurrentRoundBet;
    }

    /**
     * Resets the current round's bet total to zero.
     */
    public void nextRound() {
        myCurrentRoundBet = 0;
    }

    /**
     * Adds a winning payout to the player's account. Because the original
     * bet was already removed, this returns the bet and adds an equal win.
     *
     * @param theMoney the amount that was bet
     */
    public void moneyGain(final int theMoney) {
        checkValidNum(theMoney);
        myAccount += theMoney * 2;
    }

    /**
     * Deducts money from the account.
     *
     * @param theMoney the amount to remove
     */
    private void moneyLost(final int theMoney) {
        myAccount -= theMoney;
    }

    /**
     * Registers a bet for the round and deducts the bet amount.
     *
     * @param theMoney the bet amount
     * @throws IllegalArgumentException if the bet is invalid or exceeds balance
     */
    public void moneyBet(final int theMoney) {
        checkValidNum(theMoney);
        checkValidAccount(theMoney, myAccount);
        myCurrentRoundBet += theMoney;
        moneyLost(theMoney);
    }

    /**
     * Checks if the player still has uncommitted money available.
     *
     * @return true if account is greater than zero, false otherwise
     */
    public boolean hasMoney() {
        return myAccount > 0;
    }
}
