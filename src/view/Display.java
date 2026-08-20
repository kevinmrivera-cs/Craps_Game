package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import controller.GamePlay;

/**
 * Main Swing user interface for the Craps game.
 *
 * <p>This frame displays:</p>
 * <ul>
 *     <li>Controls to roll, bet, and start/reset the session</li>
 *     <li>Current dice values and total</li>
 *     <li>Point value and win totals</li>
 *     <li>Bank and bet information</li>
 * </ul>
 *
 * <p>All user actions are delegated to the {@link GamePlay} controller.</p>
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
public final class Display extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Name of the opening title-screen card. */
    private static final String TITLE_SCREEN = "title";

    /** Name of the active game-table card. */
    private static final String GAME_SCREEN = "game";

    /** Main background color. */
    private static final Color BLACK = new Color(18, 18, 18);

    /** Panel background color. */
    private static final Color DARK_GRAY = new Color(35, 35, 35);

    /** Casino red accent color. */
    private static final Color CASINO_RED = new Color(165, 20, 35);

    /** Gold accent color. */
    private static final Color GOLD = new Color(228, 190, 82);

    /** Main text color. */
    private static final Color WHITE = new Color(245, 245, 245);

    /** Muted status text color. */
    private static final Color LIGHT_GRAY = new Color(190, 190, 190);

    /** Reference to the game controller to handle game actions. */
    private final GamePlay myController;

    /** Handles background music and game sound effects. */
    private final SoundManager mySoundManager;

    /** Switches between the title screen and the game table. */
    private CardLayout myScreenLayout;

    /** Holds the title screen and game table cards. */
    private JPanel myScreenPanel;

    /** Starting bank amount entered on the title screen. */
    private JTextField myStartingBankField;

    /** Menu item to start a new bank/session. */
    private JMenuItem myStartItem;

    /** Menu item to reset the current session. */
    private JMenuItem myResetItem;

    /** Button to roll the dice. */
    private JButton myRollButton;

    /** Displays the current point. */
    private JTextField myPointField;

    /** Label showing win/lose status text. */
    private JLabel myResultLabel;

    /** Label showing game instructions and current state. */
    private JLabel myStatusLabel;

    /** Button to start the next round after a result. */
    private JButton myPlayAgainButton;

    /** Animated display for the rolled pair of dice. */
    private JLabel myDiceAnimationLabel;

    /** Displays total of the two dice. */
    private JTextField myTotalField;

    /** Displays total wins for the player. */
    private JTextField myPlayerWinsField;

    /** Displays total wins for the house. */
    private JTextField myHouseWinsField;

    /** Displays the current bank amount. */
    private JTextField myBankField;

    /** Displays the current bet amount. */
    private JTextField myBetField;

    /** Bet button for +$1. */
    private JButton myAdd1Button;

    /** Bet button for +$5. */
    private JButton myAdd5Button;

    /** Bet button for +$10. */
    private JButton myAdd10Button;

    /** Bet button for +$50. */
    private JButton myAdd50Button;

    /** Bet button for +$100. */
    private JButton myAdd100Button;

    /** Bet button for +$500. */
    private JButton myAdd500Button;

    /**
     * Constructs the main display window for the Craps game.
     *
     * @param theController the controller that handles game logic
     */
    public Display(final GamePlay theController) {
        super("Lucky Seven Craps Casino");
        myController = theController;
        mySoundManager = new SoundManager();
        setupGUI();
    }

    /**
     * Initializes and lays out all GUI components.
     */
    private void setupGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(buildMenuBar());

        myScreenLayout = new CardLayout();
        myScreenPanel = new JPanel(myScreenLayout);
        myScreenPanel.add(makeTitleScreen(), TITLE_SCREEN);

        final JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBackground(BLACK);
        content.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        content.add(makeHeaderPanel(), BorderLayout.NORTH);

        final JPanel gamePanels = new JPanel(new GridLayout(1, 3, 12, 12));
        gamePanels.setOpaque(false);
        gamePanels.add(makeRoundPanel());
        gamePanels.add(makeCenterPanel());
        gamePanels.add(makeBankPanel());
        content.add(gamePanels, BorderLayout.CENTER);

        myScreenPanel.add(content, GAME_SCREEN);
        setContentPane(myScreenPanel);
        setMinimumSize(new Dimension(940, 580));
        setPreferredSize(new Dimension(1020, 640));
        pack();
        setLocationRelativeTo(null);
        setResizable(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent theEvent) {
                mySoundManager.close();
            }
        });

        setVisible(true);
        myStartingBankField.requestFocusInWindow();
    }

    /**
     * Builds the opening screen where the player enters a starting bank.
     *
     * @return title-screen panel
     */
    private JPanel makeTitleScreen() {
        final JPanel screen = new JPanel(new BorderLayout(20, 20));
        screen.setBackground(BLACK);
        screen.setBorder(BorderFactory.createEmptyBorder(55, 90, 55, 90));

        final JPanel titlePanel = new JPanel(new GridLayout(3, 1, 4, 4));
        titlePanel.setBackground(CASINO_RED);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 3),
                BorderFactory.createEmptyBorder(22, 20, 22, 20)));

        final JLabel casinoTitle = new JLabel(
                "LUCKY SEVEN", SwingConstants.CENTER);
        casinoTitle.setForeground(GOLD);
        casinoTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));

        final JLabel gameTitle = new JLabel("CRAPS", SwingConstants.CENTER);
        gameTitle.setForeground(WHITE);
        gameTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 54));

        final JLabel subtitle = new JLabel(
                "ROLL THE DICE. MAKE YOUR POINT.", SwingConstants.CENTER);
        subtitle.setForeground(WHITE);
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));

        titlePanel.add(casinoTitle);
        titlePanel.add(gameTitle);
        titlePanel.add(subtitle);

        final JPanel startPanel = createCasinoPanel("Open the Table");
        startPanel.setLayout(new GridLayout(4, 1, 10, 10));

        final JLabel prompt = createLabel("Enter your starting bank amount");
        myStartingBankField = new JTextField();
        myStartingBankField.setHorizontalAlignment(SwingConstants.CENTER);
        myStartingBankField.setBackground(BLACK);
        myStartingBankField.setForeground(GOLD);
        myStartingBankField.setCaretColor(GOLD);
        myStartingBankField.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        myStartingBankField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 2),
                BorderFactory.createEmptyBorder(7, 8, 7, 8)));
        myStartingBankField.addActionListener(theEvent -> handleStart());

        final JButton startButton = createActionButton("START CRAPS GAME");
        startButton.addActionListener(theEvent -> handleStart());

        final JLabel shortcut = new JLabel(
                "Press Enter or Ctrl+S to start", SwingConstants.CENTER);
        shortcut.setForeground(LIGHT_GRAY);
        shortcut.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));

        startPanel.add(prompt);
        startPanel.add(myStartingBankField);
        startPanel.add(startButton);
        startPanel.add(shortcut);

        screen.add(titlePanel, BorderLayout.CENTER);
        screen.add(startPanel, BorderLayout.SOUTH);
        return screen;
    }

    /**
     * Builds the title and status area.
     *
     * @return header panel
     */
    private JPanel makeHeaderPanel() {
        final JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(CASINO_RED);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 2),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        final JLabel title = new JLabel("LUCKY SEVEN CRAPS CASINO");
        title.setForeground(WHITE);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        myStatusLabel = new JLabel("Select Game > Start to open the table.");
        myStatusLabel.setForeground(WHITE);
        myStatusLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        myStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(title, BorderLayout.CENTER);
        panel.add(myStatusLabel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Builds the main menu bar with Game and Help menus.
     *
     * @return the constructed menu bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();

        final JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        myStartItem = new JMenuItem("Start");
        myStartItem.setMnemonic('S');
        myStartItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        myStartItem.addActionListener(theEvent -> handleStart());

        myResetItem = new JMenuItem("Reset Session");
        myResetItem.setMnemonic('R');
        myResetItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        myResetItem.setEnabled(false);
        myResetItem.addActionListener(theEvent -> handleReset());

        final JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('X');
        exitItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(theEvent -> handleExit());

        gameMenu.add(myStartItem);
        gameMenu.add(myResetItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);

        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        final JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        aboutItem.addActionListener(theEvent -> showAbout());

        final JMenuItem rulesItem = new JMenuItem("Rules");
        rulesItem.setMnemonic('U');
        rulesItem.addActionListener(theEvent -> showRules());

        helpMenu.add(aboutItem);
        helpMenu.add(rulesItem);

        bar.add(gameMenu);
        bar.add(helpMenu);
        return bar;
    }

    /**
     * Creates the panel containing point, result, and round controls.
     *
     * @return the constructed round panel
     */
    private JPanel makeRoundPanel() {
        final JPanel panel = createCasinoPanel("Round Controls");
        panel.setLayout(new BorderLayout(10, 10));

        final JPanel pointPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        pointPanel.setOpaque(false);
        pointPanel.add(createLabel("Current Point"));
        myPointField = createDisplayField("--", 24);
        pointPanel.add(myPointField);

        myResultLabel = new JLabel("TABLE CLOSED", SwingConstants.CENTER);
        myResultLabel.setForeground(GOLD);
        myResultLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        myResultLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CASINO_RED, 2),
                BorderFactory.createEmptyBorder(18, 8, 18, 8)));

        final JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        buttonPanel.setOpaque(false);
        myRollButton = createActionButton("ROLL DICE");
        myRollButton.setEnabled(false);
        myRollButton.addActionListener(theEvent -> myController.rollDice());

        myPlayAgainButton = createActionButton("PLAY NEXT ROUND");
        myPlayAgainButton.setEnabled(false);
        myPlayAgainButton.addActionListener(theEvent -> myController.nextRound());

        buttonPanel.add(myRollButton);
        buttonPanel.add(myPlayAgainButton);

        panel.add(pointPanel, BorderLayout.NORTH);
        panel.add(myResultLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the center panel containing dice and win totals.
     *
     * @return the constructed center panel
     */
    private JPanel makeCenterPanel() {
        final JPanel panel = createCasinoPanel("Dice Table");
        panel.setLayout(new BorderLayout(10, 10));

        final JPanel dicePanel = new JPanel(new BorderLayout());
        dicePanel.setOpaque(false);
        myDiceAnimationLabel = new JLabel(
                "Place a bet, then roll", SwingConstants.CENTER);
        myDiceAnimationLabel.setForeground(LIGHT_GRAY);
        myDiceAnimationLabel.setFont(new Font(
                Font.SANS_SERIF, Font.BOLD, 15));
        myDiceAnimationLabel.setPreferredSize(new Dimension(250, 180));
        dicePanel.add(myDiceAnimationLabel, BorderLayout.CENTER);

        final JPanel totalPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        totalPanel.setOpaque(false);
        totalPanel.add(createLabel("Roll Total"));
        myTotalField = createDisplayField("--", 28);
        totalPanel.add(myTotalField);

        final JPanel winsPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        winsPanel.setOpaque(false);
        winsPanel.setBorder(createTitledBorder("Scoreboard"));
        winsPanel.add(createLabel("Player Wins"));
        myPlayerWinsField = createDisplayField("0", 18);
        winsPanel.add(myPlayerWinsField);
        winsPanel.add(createLabel("House Wins"));
        myHouseWinsField = createDisplayField("0", 18);
        winsPanel.add(myHouseWinsField);

        final JPanel lowerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        lowerPanel.setOpaque(false);
        lowerPanel.add(totalPanel);
        lowerPanel.add(winsPanel);

        panel.add(dicePanel, BorderLayout.CENTER);
        panel.add(lowerPanel, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the right panel containing bank and bet controls.
     *
     * @return the constructed bank panel
     */
    private JPanel makeBankPanel() {
        final JPanel panel = createCasinoPanel("Bank & Bet");
        panel.setLayout(new BorderLayout(10, 10));

        final JPanel moneyPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        moneyPanel.setOpaque(false);
        moneyPanel.add(createLabel("Available Bank"));
        myBankField = createDisplayField("$0", 22);
        moneyPanel.add(myBankField);
        moneyPanel.add(createLabel("Current Round Bet"));
        myBetField = createDisplayField("$0", 22);
        moneyPanel.add(myBetField);

        final JPanel betPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        betPanel.setOpaque(false);
        betPanel.setBorder(createTitledBorder("Add to Bet"));

        myAdd1Button = createBetButton("+$1", 1);
        myAdd5Button = createBetButton("+$5", 5);
        myAdd10Button = createBetButton("+$10", 10);
        myAdd50Button = createBetButton("+$50", 50);
        myAdd100Button = createBetButton("+$100", 100);
        myAdd500Button = createBetButton("+$500", 500);

        betPanel.add(myAdd1Button);
        betPanel.add(myAdd5Button);
        betPanel.add(myAdd10Button);
        betPanel.add(myAdd50Button);
        betPanel.add(myAdd100Button);
        betPanel.add(myAdd500Button);

        setBetButtons(false, 0);
        panel.add(moneyPanel, BorderLayout.NORTH);
        panel.add(betPanel, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates a styled casino panel.
     *
     * @param theTitle title for the panel border
     * @return styled panel
     */
    private static JPanel createCasinoPanel(final String theTitle) {
        final JPanel panel = new JPanel();
        panel.setBackground(DARK_GRAY);
        panel.setBorder(BorderFactory.createCompoundBorder(
                createTitledBorder(theTitle),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));
        return panel;
    }

    /**
     * Creates a titled border using the casino colors.
     *
     * @param theTitle border title
     * @return styled titled border
     */
    private static Border createTitledBorder(final String theTitle) {
        final TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CASINO_RED, 2), theTitle);
        border.setTitleColor(GOLD);
        border.setTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        return border;
    }

    /**
     * Creates a standard label for a casino panel.
     *
     * @param theText label text
     * @return styled label
     */
    private static JLabel createLabel(final String theText) {
        final JLabel label = new JLabel(theText, SwingConstants.CENTER);
        label.setForeground(WHITE);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        return label;
    }

    /**
     * Creates a non-editable display field.
     *
     * @param theText initial text
     * @param theFontSize font size
     * @return styled text field
     */
    private static JTextField createDisplayField(final String theText,
                                                 final int theFontSize) {
        final JTextField field = new JTextField(theText);
        field.setEditable(false);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setBackground(BLACK);
        field.setForeground(GOLD);
        field.setCaretColor(GOLD);
        field.setFont(new Font(Font.MONOSPACED, Font.BOLD, theFontSize));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    /**
     * Creates a main action button.
     *
     * @param theText button text
     * @return styled button
     */
    private static JButton createActionButton(final String theText) {
        final JButton button = new JButton(theText);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        button.setBackground(CASINO_RED);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Creates a bet button and connects it to the controller.
     *
     * @param theText button text
     * @param theAmount amount added by the button
     * @return configured bet button
     */
    private JButton createBetButton(final String theText, final int theAmount) {
        final JButton button = new JButton(theText);
        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        button.setBackground(CASINO_RED);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.addActionListener(theEvent -> myController.betMoney(theAmount));
        return button;
    }

    /**
     * Starts a session using the bank amount entered on the title screen.
     */
    private void handleStart() {
        final String input = myStartingBankField.getText().trim();

        try {
            final int amount = Integer.parseInt(input);
            myController.gameStart(amount);
            myScreenLayout.show(myScreenPanel, GAME_SCREEN);
            myStartItem.setEnabled(false);
            myResetItem.setEnabled(true);
        } catch (final NumberFormatException exception) {
            showError("Enter a whole number greater than zero.");
            myStartingBankField.requestFocusInWindow();
            myStartingBankField.selectAll();
        } catch (final IllegalArgumentException exception) {
            showError(exception.getMessage());
            myStartingBankField.requestFocusInWindow();
            myStartingBankField.selectAll();
        }
    }

    /**
     * Handles the Reset Session menu action.
     */
    private void handleReset() {
        final int choice = JOptionPane.showConfirmDialog(
                this,
                "Reset the bank, bets, and scoreboard?",
                "Reset Session",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            myController.resetWholeGame();
        }
    }

    /**
     * Handles the Exit menu action and confirms with the user.
     */
    private void handleExit() {
        final int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Exit",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            mySoundManager.close();
            dispose();
            System.exit(0);
        }
    }

    /**
     * Shows an error dialog.
     *
     * @param theMessage error message
     */
    private void showError(final String theMessage) {
        JOptionPane.showMessageDialog(
                this,
                theMessage,
                "Invalid Input",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Updates the dice display with the latest roll values.
     *
     * @param theDie1 value for die one
     * @param theDie2 value for die two
     * @param theTotal sum of both dice
     */
    public void updateDiceDisplay(final int theDie1,
                                  final int theDie2,
                                  final int theTotal) {
        myDiceAnimationLabel.setText(null);
        myDiceAnimationLabel.setIcon(
                DiceAnimationProvider.getAnimation(theDie1, theDie2));
        myTotalField.setText(String.valueOf(theTotal));
    }

    /**
     * Clears the dice and total display.
     */
    public void clearDiceDisplay() {
        myDiceAnimationLabel.setIcon(null);
        myDiceAnimationLabel.setText("Place a bet, then roll");
        myTotalField.setText("--");
    }

    /**
     * Updates the point value in the GUI.
     *
     * @param thePoint the current point
     */
    public void updatePoint(final int thePoint) {
        myPointField.setText(thePoint == 0 ? "--" : String.valueOf(thePoint));
    }

    /**
     * Updates the displayed win totals for the player and house.
     *
     * @param thePlayerWins total wins for the player
     * @param theHouseWins total wins for the house
     */
    public void updateWins(final int thePlayerWins, final int theHouseWins) {
        myPlayerWinsField.setText(String.valueOf(thePlayerWins));
        myHouseWinsField.setText(String.valueOf(theHouseWins));
    }

    /**
     * Sets the large result banner text.
     *
     * @param theText result message to show
     */
    public void setResultText(final String theText) {
        myResultLabel.setText(theText);
    }

    /**
     * Sets the status instruction shown below the title.
     *
     * @param theText status message
     */
    public void setStatusText(final String theText) {
        myStatusLabel.setText(theText);
    }

    /**
     * Enables or disables the Roll and Play Next Round buttons.
     *
     * @param theRollEnabled true to enable Roll
     * @param thePlayAgainEnabled true to enable Play Next Round
     */
    public void setRollAndPlayAgain(final boolean theRollEnabled,
                                    final boolean thePlayAgainEnabled) {
        myRollButton.setEnabled(theRollEnabled);
        myPlayAgainButton.setEnabled(thePlayAgainEnabled);
    }

    /**
     * Enables only the bet buttons the player can currently afford.
     *
     * @param theEnabled true when betting is allowed
     * @param theAvailableBank money still available to bet
     */
    public void setBetButtons(final boolean theEnabled,
                              final int theAvailableBank) {
        myAdd1Button.setEnabled(theEnabled && theAvailableBank >= 1);
        myAdd5Button.setEnabled(theEnabled && theAvailableBank >= 5);
        myAdd10Button.setEnabled(theEnabled && theAvailableBank >= 10);
        myAdd50Button.setEnabled(theEnabled && theAvailableBank >= 50);
        myAdd100Button.setEnabled(theEnabled && theAvailableBank >= 100);
        myAdd500Button.setEnabled(theEnabled && theAvailableBank >= 500);
    }

    /**
     * Shows an informational message dialog.
     *
     * @param theMessage message text to display
     */
    public void showMessage(final String theMessage) {
        JOptionPane.showMessageDialog(
                this,
                theMessage,
                "Craps Casino",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates both bank and bet display fields.
     *
     * @param theBank the current bank value
     * @param theBet the current bet value
     */
    public void updateBankAndBet(final int theBank, final int theBet) {
        myBankField.setText("$" + theBank);
        myBetField.setText("$" + theBet);
    }

    /**
     * Resets the entire session view back to its initial state.
     */
    public void resetSessionView() {
        updateBankAndBet(0, 0);
        clearDiceDisplay();
        updatePoint(0);
        updateWins(0, 0);
        setResultText("TABLE CLOSED");
        setStatusText("Select Game > Start to open the table.");
        setRollAndPlayAgain(false, false);
        setBetButtons(false, 0);
        myStartItem.setEnabled(true);
        myResetItem.setEnabled(false);
        myStartingBankField.setText("");
        myScreenLayout.show(myScreenPanel, TITLE_SCREEN);
        myStartingBankField.requestFocusInWindow();
        mySoundManager.stopBackgroundMusic();
    }

    /**
     * Starts the looping casino background music.
     */
    public void startBackgroundMusic() {
        mySoundManager.startBackgroundMusic();
    }

    /**
     * Stops the looping casino background music.
     */
    public void stopBackgroundMusic() {
        mySoundManager.stopBackgroundMusic();
    }

    /**
     * Plays the dice rolling sound effect.
     */
    public void playDiceSound() {
        mySoundManager.playDiceRoll();
    }

    /**
     * Plays the player win sound effect.
     */
    public void playWinSound() {
        mySoundManager.playWin();
    }

    /**
     * Plays the player loss sound effect.
     */
    public void playLossSound() {
        mySoundManager.playLoss();
    }

    /**
     * Displays an About dialog with program information.
     */
    private void showAbout() {
        JOptionPane.showMessageDialog(
                this,
                "Lucky Seven Craps Casino\n"
                        + "Author: Kevin Munoz-Rivera\n"
                        + "Version: 2.0\n"
                        + "Java Version: " + System.getProperty("java.version"),
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays a dialog explaining the rules of Craps.
     */
    private void showRules() {
        JOptionPane.showMessageDialog(
                this,
                "COME-OUT ROLL\n"
                        + "7 or 11: Player wins\n"
                        + "2, 3, or 12: House wins\n"
                        + "4, 5, 6, 8, 9, or 10: That number becomes the point\n\n"
                        + "POINT PHASE\n"
                        + "Roll the point again before a 7 to win.\n"
                        + "A 7 before the point gives the house the win.\n"
                        + "All other totals continue the round.",
                "Craps Rules",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
