package test;

import model.Player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for validating the {@link Player} class behavior.
 *
 * @version 08/19/2026
 * @author Kevin Munoz-Rivera
 */
public class PlayerTest {

    /**
     * Tests invalid dice sums below and above the legal range.
     */
    @Test
    public void testInvalidDiceRollSums() {
        final Player player = new Player();
        assertThrows(IllegalArgumentException.class,
                () -> player.resultDiceRoll(1));
        assertThrows(IllegalArgumentException.class,
                () -> player.resultDiceRoll(13));
    }

    /**
     * Tests that every valid point number is established correctly.
     */
    @Test
    public void testSetTotalPoints() {
        final int[] points = {4, 5, 6, 8, 9, 10};

        for (final int point : points) {
            final Player player = new Player();
            player.resultDiceRoll(point);
            assertEquals(point, player.getHitTotalPoints());
            assertTrue(player.getTurn());
        }
    }

    /**
     * Tests immediate come-out wins on 7 and 11.
     */
    @Test
    public void testComeOutWins() {
        for (final int roll : new int[]{7, 11}) {
            final Player player = new Player();
            player.resultDiceRoll(roll);
            assertFalse(player.getTurn());
            assertTrue(player.getWinResult());
            assertEquals(1, player.getMyWinCount());
            assertEquals(0, player.getMyLostCount());
        }
    }

    /**
     * Tests immediate come-out losses on 2, 3, and 12.
     */
    @Test
    public void testComeOutLosses() {
        for (final int roll : new int[]{2, 3, 12}) {
            final Player player = new Player();
            player.resultDiceRoll(roll);
            assertFalse(player.getTurn());
            assertFalse(player.getWinResult());
            assertEquals(0, player.getMyWinCount());
            assertEquals(1, player.getMyLostCount());
        }
    }

    /**
     * Tests winning by rolling the established point again.
     */
    @Test
    public void testPointWin() {
        final Player player = new Player();
        player.resultDiceRoll(8);
        player.resultDiceRoll(8);

        assertFalse(player.getTurn());
        assertTrue(player.getWinResult());
        assertEquals(1, player.getMyWinCount());
        assertEquals(0, player.getMyLostCount());
    }

    /**
     * Tests losing by rolling 7 after a point has been established.
     */
    @Test
    public void testSevenOutLoss() {
        final Player player = new Player();
        player.resultDiceRoll(6);
        player.resultDiceRoll(7);

        assertFalse(player.getTurn());
        assertFalse(player.getWinResult());
        assertEquals(0, player.getMyWinCount());
        assertEquals(1, player.getMyLostCount());
    }

    /**
     * Tests that non-decision totals continue the point phase.
     */
    @Test
    public void testPointPhaseNonDecisionRollsContinue() {
        final int[] continuingRolls = {2, 3, 4, 5, 8, 9, 10, 11, 12};

        for (final int roll : continuingRolls) {
            final Player player = new Player();
            player.resultDiceRoll(6);
            player.resultDiceRoll(roll);

            assertTrue(player.getTurn(), "Roll should continue: " + roll);
            assertFalse(player.getWinResult());
            assertEquals(6, player.getHitTotalPoints());
        }
    }

    /**
     * Tests that extra results cannot change a completed round.
     */
    @Test
    public void testCompletedRoundCannotCountTwice() {
        final Player player = new Player();
        player.resultDiceRoll(7);
        player.resultDiceRoll(7);
        player.resultDiceRoll(2);

        assertEquals(1, player.getMyWinCount());
        assertEquals(0, player.getMyLostCount());
    }

    /**
     * Tests round reset while preserving cumulative counters.
     */
    @Test
    public void testResetRound() {
        final Player player = new Player();
        player.resultDiceRoll(7);
        player.resetRound();

        assertTrue(player.getTurn());
        assertFalse(player.getWinResult());
        assertEquals(0, player.getHitTotalPoints());
        assertEquals(1, player.getMyWinCount());
        assertEquals(0, player.getMyLostCount());
    }

    /**
     * Tests cumulative wins and losses across several rounds.
     */
    @Test
    public void testCumulativeCounters() {
        final Player player = new Player();

        player.resultDiceRoll(11);
        player.resetRound();

        player.resultDiceRoll(4);
        player.resultDiceRoll(4);
        player.resetRound();

        player.resultDiceRoll(3);
        player.resetRound();

        player.resultDiceRoll(9);
        player.resultDiceRoll(7);

        assertEquals(2, player.getMyWinCount());
        assertEquals(2, player.getMyLostCount());
    }
}
