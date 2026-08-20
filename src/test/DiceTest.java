package test;

import model.Dice;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit tests for validating behavior of the {@link Dice} model.
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
public class DiceTest {

    /**
     * Tests both dice and their sum over many rolls.
     */
    @Test
    public void testDiceRollWithinBoundaries() {
        final Dice dice = new Dice();

        for (int i = 0; i < 1000; i++) {
            dice.roll2Dices();

            assertTrue(dice.getMyRoll1() >= Dice.MIN_NUM
                    && dice.getMyRoll1() <= Dice.MAX_NUM);
            assertTrue(dice.getMyRoll2() >= Dice.MIN_NUM
                    && dice.getMyRoll2() <= Dice.MAX_NUM);
            assertEquals(dice.getMyRoll1() + dice.getMyRoll2(),
                    dice.getMySumOfDicesRoll());
        }
    }

    /**
     * Tests that reset clears every stored value.
     */
    @Test
    public void testReset() {
        final Dice dice = new Dice();
        dice.roll2Dices();
        dice.reset();

        assertEquals(0, dice.getMyRoll1());
        assertEquals(0, dice.getMyRoll2());
        assertEquals(0, dice.getMySumOfDicesRoll());
    }
}
