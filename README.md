# Craps_Game
# Lucky Seven Craps Casino

Lucky Seven Craps Casino is a Java Swing implementation of the classic casino dice game. It features standard Craps rules, player betting, bank management, animated dice rolls, sound effects, and a casino-themed interface.

The project follows the Model-View-Controller design pattern and includes JUnit tests for its core game logic.

## Features

- Standard Craps come-out and point-phase rules
- Opening title screen
- Starting-bank entry before gameplay
- Additive betting controls
- One-to-one winning payouts
- Animated GIFs for all 36 dice combinations
- Player and house win counters
- Current point and roll-total displays
- Casino background music
- Dice-roll, win, and loss sound effects
- Black, red, and gold Java Swing interface
- Ctrl+S shortcut for starting the game
- JUnit 5 tests for the model classes

## How to Play

### Come-Out Roll

- Rolling 7 or 11 wins the round.
- Rolling 2, 3, or 12 loses the round.
- Rolling 4, 5, 6, 8, 9, or 10 establishes the point.

### Point Phase

After a point is established:

- Roll the point again before rolling a 7 to win.
- Roll a 7 before repeating the point to lose.
- Any other roll continues the round.

## Starting the Game

1. Launch the application.
2. Enter a starting bank amount on the title screen.
3. Select **Start Craps Game**, press **Enter**, or use **Ctrl+S**.
4. Choose a bet amount.
5. Select **Roll Dice**.
6. Continue rolling until the player or house wins.
7. Select **Play Next Round** to continue playing.

## Project Structure

```text
Craps_Game/
├── resources/
│   ├── audio/
│   │   ├── casino-theme.wav
│   │   ├── dice-roll.wav
│   │   ├── loss.wav
│   │   └── win.wav
│   └── images/
│       └── dice/
│           └── roll_1_1.gif ... roll_6_6.gif
├── src/
│   ├── controller/
│   │   └── GamePlay.java
│   ├── model/
│   │   ├── Bank.java
│   │   ├── Dice.java
│   │   └── Player.java
│   ├── test/
│   │   ├── BankTest.java
│   │   ├── DiceTest.java
│   │   └── PlayerTest.java
│   └── view/
│       ├── DiceAnimationProvider.java
│       ├── Display.java
│       └── SoundManager.java
├── .gitignore
└── README.md
```

## Technologies

- Java
- Java Swing
- Model-View-Controller architecture
- JUnit 5
- IntelliJ IDEA

## Running in IntelliJ IDEA

1. Clone or download the repository.
2. Open the project folder in IntelliJ IDEA.
3. Mark `src` as the **Sources Root**.
4. Mark `resources` as the **Resources Root**.
5. Configure JUnit 5 if you want to run the tests.
6. Run `controller.GamePlay`.

JDK 17 or newer is recommended.

## Testing

The project includes tests for:

- Bank creation and betting
- Winning payouts
- Dice value ranges
- Come-out roll outcomes
- Point establishment
- Point wins
- Seven-out losses
- Round resets

Run the test classes under `src/test` using JUnit 5.

## Controls

| Action | Control |
|---|---|
| Start game | Title-screen button, Enter, or Ctrl+S |
| Reset session | Game menu or Ctrl+R |
| Exit game | Game menu or Ctrl+X |
| Place bet | Betting buttons |
| Roll dice | Roll Dice button |
| Continue playing | Play Next Round button |

## Architecture

The application uses the Model-View-Controller pattern:

- **Model:** Manages the bank, dice, player state, wins, losses, and Craps rules.
- **View:** Displays the Swing interface, title screen, animations, and audio.
- **Controller:** Coordinates gameplay and communicates between the model and view.

## Audio and Animation Assets

The game includes four original WAV audio files for background music and gameplay feedback.

The dice animations include all 36 possible combinations of two six-sided dice. These GIFs were reused from the Pig Game project in the same gaming portfolio.

## Author

Kevin Munoz-Rivera
