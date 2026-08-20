package view;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Loads the animated GIF for a particular two-dice combination.
 *
 * @author Kevin Munoz-Rivera
 * @version 08/19/2026
 */
final class DiceAnimationProvider {

    /** Classpath folder containing the dice-roll GIF files. */
    private static final String RESOURCE_FOLDER = "images/dice/";

    /** Prevents construction of this resource helper. */
    private DiceAnimationProvider() {
    }

    /**
     * Loads a fresh animation for the supplied dice values.
     *
     * <p>Reading fresh bytes lets a repeated dice combination restart from
     * the GIF's first frame instead of remaining on its previous last frame.</p>
     *
     * @param theDieOne first die value, from one through six
     * @param theDieTwo second die value, from one through six
     * @return icon containing the matching dice-roll animation
     */
    static ImageIcon getAnimation(final int theDieOne, final int theDieTwo) {
        validateDie(theDieOne);
        validateDie(theDieTwo);

        final String fileName = "roll_" + theDieOne + "_"
                + theDieTwo + ".gif";
        URL resource = DiceAnimationProvider.class.getClassLoader()
                .getResource(RESOURCE_FOLDER + fileName);

        if (resource == null) {
            final File fallback = new File(
                    "resources/images/dice", fileName);
            if (fallback.exists()) {
                try {
                    resource = fallback.toURI().toURL();
                } catch (final IOException exception) {
                    throw loadFailure(fileName, exception);
                }
            }
        }

        if (resource == null) {
            throw new IllegalStateException(
                    "Dice animation was not found: " + fileName);
        }

        try (InputStream input = resource.openStream()) {
            return new ImageIcon(input.readAllBytes());
        } catch (final IOException exception) {
            throw loadFailure(fileName, exception);
        }
    }

    /**
     * Validates one die value.
     *
     * @param theValue die value to validate
     */
    private static void validateDie(final int theValue) {
        if (theValue < 1 || theValue > 6) {
            throw new IllegalArgumentException(
                    "A die value must be between 1 and 6.");
        }
    }

    /**
     * Creates a consistent exception for a GIF that cannot be read.
     *
     * @param theFileName GIF file name
     * @param theCause original input/output problem
     * @return resource-loading exception
     */
    private static IllegalStateException loadFailure(
            final String theFileName, final IOException theCause) {
        return new IllegalStateException(
                "Dice animation could not be loaded: " + theFileName,
                theCause);
    }
}
