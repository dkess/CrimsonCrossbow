package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Joystick;

/**
 * A wrapper for Joystick that adds helpful utility functions.
 *
 * @author Daniel Kessler
 */
public class JStick {
    public static final int XBOX_A = 1;
    public static final int XBOX_B = 2;
    public static final int XBOX_X = 3;
    public static final int XBOX_Y = 4;
    public static final int XBOX_LB = 5;
    public static final int XBOX_RB = 6;
    public static final int XBOX_BACK = 7;
    public static final int XBOX_START = 8;
    public static final int XBOX_LJ = 9;
    public static final int XBOX_RJ = 10;

    public static final int XBOX_LSX = 1; // left stick x
    public static final int XBOX_LSY = 2; // left stick y
    public static final int XBOX_TRIG = 3; // left is positive
    public static final int XBOX_RSX = 4; // right stick x
    public static final int XBOX_RSY = 5; // right stick y
    public static final int XBOX_DPAD = 6; // buggy

    public static final int JOYSTICK_KNOB = 3;

    public static final int MAX_BUTTONS = 12; // as specified in the docs
    public static final int MAX_AXES = 6; // as specificed in the docs

    private Joystick jstick;
    private boolean[] buttonPressed;
    private boolean[] buttonLastPressed;
    private double[] axes;
    private double[] slowAxes;
    private AxisInfo[] axisInfos;

    private double slow;

    public JStick(int port) {
        // initialize everything
        jstick = new Joystick(port);
        buttonPressed = new boolean[MAX_BUTTONS + 1];
        buttonLastPressed = new boolean[MAX_BUTTONS + 1];
        axes = new double[MAX_AXES + 1];
        slowAxes = new double[MAX_AXES + 1];
        slow = 2;
        
        axisInfos = new AxisInfo[MAX_AXES+1];
        for (int i = 0; i < axisInfos.length; i++) {
            axisInfos[i] = new AxisInfo();
        }
    }

    private class AxisInfo {
        public AxisInfo() {}
        
        private double currentValue;
        private double slowedValue;
        
        public double slowThreshold = 0.1;
        // if slowTime >= 0, the axis is being slowed
        public int slowTime = -1;
        private double initialSlowSpeed;
        private boolean movingUp;
        
        public void update(double newValue) {
            if (slowTime < 0) {
                if (newValue - currentValue > slowThreshold) {
                    slowTime = 0;
                    initialSlowSpeed = currentValue;
                    movingUp = true;
                } else if (currentValue - newValue > slowThreshold) {
                    slowTime = 0;
                    initialSlowSpeed = currentValue;
                    movingUp = false;
                } else {
                    currentValue = newValue;
                    slowedValue = newValue;
                }
            } else {
                if (currentValue - newValue > 2 * slowThreshold) {
                    slowTime = 0;
                    initialSlowSpeed = slowedValue;
                    movingUp = false;
                } else if (newValue - currentValue > 2 * slowThreshold) {
                    slowTime = 0;
                    initialSlowSpeed = slowedValue;
                    movingUp = true;
                }
                if (movingUp == (newValue > initialSlowSpeed)) {
                    int mod = movingUp ? 1 : -1;
                    slowedValue =
                            initialSlowSpeed + mod * 0.0004 * slowTime * slowTime;
                    slowTime++;
                } else {
                    slowTime = -1;
                }
                
                if (movingUp == (slowedValue > currentValue)) {
                    slowedValue = currentValue;
                    slowTime = -1;
                }
            }
            
            currentValue = newValue;
        }
        
        public double getValue() {
            return currentValue;
        }
        
        public double getSlowedValue() {
            if (slowTime < 0) {
                return currentValue;
            } else {
                return slowedValue;
            }
        }
    }
    
    public void update() {
        for (int i = 1; i < buttonPressed.length; ++i) {
            buttonLastPressed[i] = buttonPressed[i];
            buttonPressed[i] = jstick.getRawButton(i);
        }
        
        /*
        for (int i = 1; i < axes.length; ++i) {
            double newAxis = jstick.getRawAxis(i);

            if (newAxis - axes[i] > slow) {
                slowAxes[i] += slow;
            } else if (axes[i] - newAxis > slow) {
                slowAxes[i] -= slow;
            } else {
                slowAxes[i] = newAxis;
            }
            axes[i] = newAxis;    
        }
        */
        
        for (int i = 1; i < axisInfos.length; i++) {
            axisInfos[i].update(jstick.getRawAxis(i));
        }
    }

    /**
     * The output of joystick axes can be slowed down so that after each update
     * its output will only deviate from previous value at a maximum of the slow
     * value.
     *
     * @param s The new slow value. This should be a positive number.
     */
    public void setSlow(double s) {
        slow = Math.abs(s);
    }

    public double getSlow() {
        return slow;
    }

    /**
     * Gets the button value
     *
     * @param b The button number to be read.
     * @return The state of the button.
     */
    public boolean isPressed(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return buttonPressed[b] && !buttonLastPressed[b];
        } else {
            return false;
        }
    }

    /**
     * Gets whether or not the button is being released
     *
     * @param b The button number to be read.
     * @return True if the button was pressed in the last update but not now.
     */
    public boolean isReleased(int b) {
        if (b >= 0 && b < buttonPressed.length) {
            return !buttonPressed[b] && buttonLastPressed[b];
        } else {
            return false;
        }
    }

    /**
     * Gets the value of the axis.
     *
     * @param b The axis to read.
     * @return The value of the axis.
     */
    public double getAxis(int b) {
        if (b >= 0 && b < axes.length) {
            //return axes[b];
            return axisInfos[b].getValue();
        } else {
            return 0;
        }
    }

    public double getSlowedAxis(int b) {
        if (b >= 0 && b < axes.length) {
            //return slowAxes[b];
            return axisInfos[b].getSlowedValue();
        } else {
            return 0;
        }
    }
    
    public double getSlowTime(int b) {
        return axisInfos[b].slowTime;
    }
}