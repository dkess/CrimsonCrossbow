package com.milkenknights.crimsoncrossbow;

/**
 * A class that ensures there is only one instance of JStick per joystick.
 * JSticks created from this class can be updated all at once.
 *
 * @author danielk
 */
public class JStickMultiton {
    public static final int MAX_JOYSTICKS = 4;
    private static final JStick[] joysticks = new JStick[MAX_JOYSTICKS];

    public static JStick getJStick(int port) {
        // we have to subtract 1 from port because arrays are 0-indexed
        if (joysticks[port - 1] == null) {
            joysticks[port - 1] = new JStick(port);
        }

        return joysticks[port - 1];
    }

    public static void updateAll() {
        for (int i = 0; i < joysticks.length; i++) {
            if (joysticks[i] != null) {
                joysticks[i].update();
            }
        }
    }
}
