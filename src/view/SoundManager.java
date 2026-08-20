package view;

import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Loads and plays the background music and sound effects used by the game.
 * Missing audio files do not stop the game from running.
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
public final class SoundManager {

    /** Background casino music. */
    private final Clip myCasinoTheme;

    /** Dice rolling sound effect. */
    private final Clip myDiceRoll;

    /** Player win sound effect. */
    private final Clip myWinSound;

    /** Player loss sound effect. */
    private final Clip myLossSound;

    /**
     * Loads all game audio clips.
     */
    public SoundManager() {
        myCasinoTheme = loadClip("casino-theme.wav", -18.0F);
        myDiceRoll = loadClip("dice-roll.wav", -5.0F);
        myWinSound = loadClip("win.wav", -7.0F);
        myLossSound = loadClip("loss.wav", -7.0F);
    }

    /**
     * Starts or resumes the looping background music.
     */
    public void startBackgroundMusic() {
        if (myCasinoTheme != null && !myCasinoTheme.isRunning()) {
            myCasinoTheme.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the looping background music.
     */
    public void stopBackgroundMusic() {
        stopClip(myCasinoTheme);
    }

    /**
     * Plays the dice rolling sound from its beginning.
     */
    public void playDiceRoll() {
        playClip(myDiceRoll);
    }

    /**
     * Plays the player win sound from its beginning.
     */
    public void playWin() {
        playClip(myWinSound);
    }

    /**
     * Plays the player loss sound from its beginning.
     */
    public void playLoss() {
        playClip(myLossSound);
    }

    /**
     * Stops and closes every loaded audio clip.
     */
    public void close() {
        closeClip(myCasinoTheme);
        closeClip(myDiceRoll);
        closeClip(myWinSound);
        closeClip(myLossSound);
    }

    /**
     * Loads one audio clip from the resources folder.
     *
     * @param theFileName audio file name
     * @param theVolumeDecibels requested volume adjustment
     * @return the loaded clip, or null when it cannot be loaded
     */
    private static Clip loadClip(final String theFileName,
                                 final float theVolumeDecibels) {
        try {
            URL location = SoundManager.class.getClassLoader()
                    .getResource("audio/" + theFileName);

            if (location == null) {
                final File fallback = new File("resources/audio", theFileName);
                if (!fallback.exists()) {
                    return null;
                }
                location = fallback.toURI().toURL();
            }

            try (AudioInputStream stream = AudioSystem.getAudioInputStream(location)) {
                final Clip clip = AudioSystem.getClip();
                clip.open(stream);
                setVolume(clip, theVolumeDecibels);
                return clip;
            }
        } catch (final Exception exception) {
            System.err.println("Unable to load audio file " + theFileName
                    + ": " + exception.getMessage());
            return null;
        }
    }

    /**
     * Adjusts a clip's master volume when the control is supported.
     *
     * @param theClip clip to adjust
     * @param theVolumeDecibels requested volume adjustment
     */
    private static void setVolume(final Clip theClip,
                                  final float theVolumeDecibels) {
        if (theClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            final FloatControl volume = (FloatControl) theClip.getControl(
                    FloatControl.Type.MASTER_GAIN);
            final float safeVolume = Math.max(volume.getMinimum(),
                    Math.min(volume.getMaximum(), theVolumeDecibels));
            volume.setValue(safeVolume);
        }
    }

    /**
     * Restarts and plays a sound effect.
     *
     * @param theClip clip to play
     */
    private static void playClip(final Clip theClip) {
        if (theClip != null) {
            theClip.stop();
            theClip.setFramePosition(0);
            theClip.start();
        }
    }

    /**
     * Stops a clip and rewinds it.
     *
     * @param theClip clip to stop
     */
    private static void stopClip(final Clip theClip) {
        if (theClip != null) {
            theClip.stop();
            theClip.setFramePosition(0);
        }
    }

    /**
     * Stops and closes a clip.
     *
     * @param theClip clip to close
     */
    private static void closeClip(final Clip theClip) {
        if (theClip != null) {
            theClip.stop();
            theClip.close();
        }
    }
}
