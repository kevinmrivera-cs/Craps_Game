package controller;

import java.awt.EventQueue;

import model.Bank;
import model.Dice;
import model.Player;
import view.Display;

/**
 * Controller class for coordinating the game logic between the Model
 * (Player, Bank, Dice) and the View (Display).
 *
 * <p>This class:</p>
 * <ul>
 *     <li>Initializes and resets game state</li>
 *     <li>Handles betting and dice rolling</li>
 *     <li>Updates the View with results, points, and bank information</li>
 *     <li>Determines wins/losses and manages transitions between rounds</li>
 * </ul>
 *
 * @version 08/19/2026
 * @author Kevin Munoz-Rivera
 */
public final class GamePlay {

    /** Player model managing win/loss states and point logic. */
    private Player myPlayer;

    /** Bank model storing the player's money and bets. */
    private Bank myBank;

    /** Dice model used for rolling and generating totals. */
    private Dice myDice;

    /** GUI interface for displaying updates and receiving input. */
    private final Display myDisplay;

    /** Whether the player may still add to the current round's bet. */
    private boolean myCanBetThisRound;

    /**
     * Constructs a new GamePlay controller and initializes the UI.
     */
    public GamePlay() {
        myPlayer = new Player();
        myDice = new Dice();
        myBank = null;
        myCanBetThisRound = false;
        myDisplay = new Display(this);
    }

    /**
     * Launches the program using Swing's Event Dispatch Thread.
     *
     * @param theArgs command-line arguments (unused)
     */
    public static void main(final String[] theArgs) {
        EventQueue.invokeLater(GamePlay::new);
    }

    /**
     * Starts a new session with a new bank and clean scoreboard.
     *
     * @param theMoney initial bank amount
     */
    public void gameStart(final int theMoney) {
        myBank = new Bank(theMoney);
        myPlayer = new Player();
        myDice = new Dice();
        myCanBetThisRound = true;

        myDisplay.updateBankAndBet(myBank.getMyAccount(), 0);
        myDisplay.updateWins(0, 0);
        myDisplay.updatePoint(0);
        myDisplay.clearDiceDisplay();
        myDisplay.setResultText("PLACE YOUR BET");
        myDisplay.setStatusText("Choose a bet amount, then roll the dice.");
        myDisplay.setRollAndPlayAgain(false, false);
        myDisplay.setBetButtons(true, myBank.getMyAccount());
        myDisplay.startBackgroundMusic();
    }

    /**
     * Attempts to add money to the current round's bet.
     *
     * @param theMoney amount to bet
     */
    public void betMoney(final int theMoney) {
        if (!hasBank()) {
            myDisplay.showMessage("Start a session before placing a bet.");
            return;
        }

        if (!myCanBetThisRound) {
            myDisplay.showMessage(
                    "The dice have already been rolled. Finish this round first.");
            return;
        }

        try {
            myBank.moneyBet(theMoney);
            myDisplay.updateBankAndBet(myBank.getMyAccount(),
                    myBank.getMyCurrentRoundBet());
            myDisplay.setResultText("BET: $" + myBank.getMyCurrentRoundBet());
            myDisplay.setStatusText("Bet placed. Add more or roll when ready.");
            myDisplay.setRollAndPlayAgain(true, false);
            myDisplay.setBetButtons(true, myBank.getMyAccount());
        } catch (final IllegalArgumentException exception) {
            myDisplay.showMessage(exception.getMessage());
        }
    }

    /**
     * Rolls the dice and evaluates the current round.
     */
    public void rollDice() {
        if (!hasBank()) {
            myDisplay.showMessage("Start a session before rolling.");
            return;
        }

        if (myBank.getMyCurrentRoundBet() <= 0) {
            myDisplay.showMessage("Place a bet before rolling.");
            return;
        }

        if (!myPlayer.getTurn()) {
            myDisplay.showMessage("This round is over. Select Play Next Round.");
            return;
        }

        myCanBetThisRound = false;
        myDisplay.setBetButtons(false, myBank.getMyAccount());
        myDisplay.playDiceSound();

        final int previousPoint = myPlayer.getHitTotalPoints();

        myDice.roll2Dices();
        myDisplay.updateDiceDisplay(myDice.getMyRoll1(),
                myDice.getMyRoll2(), myDice.getMySumOfDicesRoll());

        myPlayer.resultDiceRoll(myDice.getMySumOfDicesRoll());
        myDisplay.updatePoint(myPlayer.getHitTotalPoints());
        myDisplay.updateWins(myPlayer.getMyWinCount(), myPlayer.getMyLostCount());

        if (!myPlayer.getTurn()) {
            handleEndOfRound();
        } else if (previousPoint == 0 && myPlayer.getHitTotalPoints() != 0) {
            myDisplay.setResultText("POINT: " + myPlayer.getHitTotalPoints());
            myDisplay.setStatusText("Roll the point again before rolling a 7.");
        } else {
            myDisplay.setResultText("NO DECISION");
            myDisplay.setStatusText("Point is " + myPlayer.getHitTotalPoints()
                    + ". Roll again.");
        }
    }

    /**
     * Resolves the payout and interface state at the end of a round.
     */
    private void handleEndOfRound() {
        final int bet = myBank.getMyCurrentRoundBet();

        if (myPlayer.getWinResult()) {
            myBank.moneyGain(bet);
            myDisplay.updateBankAndBet(myBank.getMyAccount(), bet);
            myDisplay.setResultText("PLAYER WINS!");
            myDisplay.setStatusText("You won $" + bet
                    + ". Select Play Next Round to continue.");
            myDisplay.playWinSound();
        } else {
            myDisplay.setResultText("HOUSE WINS");
            myDisplay.setStatusText("The house collected your $" + bet + " bet.");
            myDisplay.playLossSound();
        }

        myDisplay.setBetButtons(false, myBank.getMyAccount());

        if (myBank.getMyAccount() <= 0) {
            myDisplay.setRollAndPlayAgain(false, false);
            myDisplay.setStatusText("Your bank is empty. Reset or start a new session.");
            myDisplay.stopBackgroundMusic();
            myDisplay.showMessage("Your bank is empty. The session is over.");
        } else {
            myDisplay.setRollAndPlayAgain(false, true);
        }
    }

    /**
     * Prepares the models and view for the next round.
     */
    public void nextRound() {
        if (!hasBank()) {
            myDisplay.showMessage("Start a session first.");
            return;
        }

        if (myPlayer.getTurn()) {
            myDisplay.showMessage("Finish the current round before starting another.");
            return;
        }

        myPlayer.resetRound();
        myDice.reset();
        myBank.nextRound();
        myCanBetThisRound = true;

        myDisplay.clearDiceDisplay();
        myDisplay.updateBankAndBet(myBank.getMyAccount(), 0);
        myDisplay.updatePoint(0);
        myDisplay.setResultText("PLACE YOUR BET");
        myDisplay.setStatusText("Choose a bet amount for the next round.");
        myDisplay.setRollAndPlayAgain(false, false);
        myDisplay.setBetButtons(true, myBank.getMyAccount());
        myDisplay.startBackgroundMusic();
    }

    /**
     * Fully resets the game session and returns the view to its opening state.
     */
    public void resetWholeGame() {
        myPlayer = new Player();
        myBank = null;
        myDice = new Dice();
        myCanBetThisRound = false;
        myDisplay.resetSessionView();
    }

    /**
     * Checks whether a bank has been initialized.
     *
     * @return true if bank exists, false otherwise
     */
    public boolean hasBank() {
        return myBank != null;
    }
}
