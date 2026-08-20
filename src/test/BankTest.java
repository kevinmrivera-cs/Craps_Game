package test;

import model.Bank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for the {@link Bank} model.
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
public class BankTest {

    /**
     * Ensures the constructor rejects zero and negative balances.
     */
    @Test
    public void testInvalidStartingBalances() {
        assertThrows(IllegalArgumentException.class, () -> new Bank(0));
        assertThrows(IllegalArgumentException.class, () -> new Bank(-1));
    }

    /**
     * Ensures bets must be positive.
     */
    @Test
    public void testInvalidBetAmounts() {
        final Bank bank = new Bank(100);
        assertThrows(IllegalArgumentException.class, () -> bank.moneyBet(0));
        assertThrows(IllegalArgumentException.class, () -> bank.moneyBet(-10));
    }

    /**
     * Ensures a player cannot bet more than the available bank.
     */
    @Test
    public void testCannotBetMoreThanBank() {
        final Bank bank = new Bank(10);
        assertThrows(IllegalArgumentException.class, () -> bank.moneyBet(11));
    }

    /**
     * Tests that several bet additions update bank and round bet correctly.
     */
    @Test
    public void testMoneyBet() {
        final Bank bank = new Bank(200);
        bank.moneyBet(10);
        bank.moneyBet(40);

        assertEquals(150, bank.getMyAccount());
        assertEquals(50, bank.getMyCurrentRoundBet());
    }

    /**
     * Tests a one-to-one Craps payout after the original bet was deducted.
     */
    @Test
    public void testMoneyGain() {
        final Bank bank = new Bank(200);
        bank.moneyBet(50);
        bank.moneyGain(bank.getMyCurrentRoundBet());

        assertEquals(250, bank.getMyAccount());
        assertEquals(50, bank.getMyCurrentRoundBet());
    }

    /**
     * Tests resetting the round bet without changing the bank.
     */
    @Test
    public void testNextRound() {
        final Bank bank = new Bank(100);
        bank.moneyBet(25);
        bank.nextRound();

        assertEquals(75, bank.getMyAccount());
        assertEquals(0, bank.getMyCurrentRoundBet());
    }

    /**
     * Tests whether the bank correctly reports available money.
     */
    @Test
    public void testHasMoney() {
        final Bank bank = new Bank(1);
        assertTrue(bank.hasMoney());
        bank.moneyBet(1);
        assertFalse(bank.hasMoney());
    }
}
