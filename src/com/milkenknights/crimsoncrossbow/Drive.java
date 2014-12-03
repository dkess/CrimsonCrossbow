package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * An extension of RobotDrive that adds Cheesy Drive and maybe more in the
 * future.
 *
 * @author Daniel Kessler
 */
public class Drive extends RobotDrive {

    // thanks to team 254 for CheesyDrive
    // cheesy drive uses one joystick for throttle, and the other for turning
    // also supports a "quickturn" function that allows the robot to spin
    // in place
    double old_turn;
    double neg_inertia_accumulator;
    double quickStopAccumulator;

    /**
     * Team 254's cheesy drive
     *
     * Use one joystick for throttle, and the other for turning. Also supports a
     * "quickturn" function that allows the robot to spin in place
     *
     * @param power How fast the robot should go
     * @param turn The direction the robot should turn in
     * @param spin Whether or not the robot should go in "quickturn" mode
     * @return False if power is zero.
     */
    public boolean cheesyDrive(double power, double turn, boolean spin) {
        if ((power == 0 && !spin) || (spin && turn == 0)) {
            return false;
        }

        double neg_inertia = turn - old_turn;
        old_turn = turn;

        turn = curveInput(turn, 2);

        double neg_inertia_scalar = 2.5;

        double neg_inertia_power = neg_inertia * neg_inertia_scalar;
        neg_inertia_accumulator += neg_inertia_power;
        turn += neg_inertia_power;
        if (neg_inertia_accumulator > 1) {
            neg_inertia_accumulator -= 1;
        } else if (neg_inertia_accumulator < -1) {
            neg_inertia_accumulator += 1;
        } else {
            neg_inertia_accumulator = 0;
        }

        double rPower = 0;
        double lPower = 0;

        if (spin) {
            rPower = turn;
            lPower = -turn;
        } else {
            double overPower = 0.0;
            double angular_power = 0;
            angular_power = power * turn - quickStopAccumulator;
            if (quickStopAccumulator > 1) {
                quickStopAccumulator -= 1;
            } else if (quickStopAccumulator < -1) {
                quickStopAccumulator += 1;
            } else {
                quickStopAccumulator = 0;
            }

            rPower = lPower = power;
            lPower += angular_power;
            rPower -= angular_power;

            if (lPower > 1) {
                rPower -= overPower * (lPower - 1);
                lPower = 1;
            } else if (rPower > 1) {
                lPower -= overPower * (rPower - 1);
                rPower = 1;
            } else if (lPower < -1) {
                rPower += overPower * (-1 - lPower);
                lPower = -1;
            } else if (rPower < -1) {
                lPower += overPower * (-1 - rPower);
                rPower = -1;
            }
        }

        tankDrive(lPower, rPower);
        return true;
    }

    public double getRight() {
        return m_rearRightMotor.get();
    }

    public double getLeft() {
        return m_rearLeftMotor.get();
    }

    /**
     * Applies a sine function to input.
     *
     * This function is guaranteed to return -1 for in=-1, 0 for in=0, and 1 for
     * in=1, regardless of the value of iterations.
     *
     * @param in The original input. Should be on the interval [-1,1]
     * @param iterations How many times the sine function should be applied
     * @return
     */
    private double curveInput(double in, int iterations) {
        double out = in;
        while (iterations > 0) {
            out = Math.sin(Math.PI * out / 2);
            iterations--;
        }
        return out;
    }

    public Drive(int frontLeftMotor, int rearLeftMotor, int frontRightMotor,
            int rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    public Drive(SpeedController frontLeftMotor, SpeedController rearLeftMotor,
            SpeedController frontRightMotor, SpeedController rearRightMotor) {
        super(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    public Drive(int leftMotorChannel, int rightMotorChannel) {
        super(leftMotorChannel, rightMotorChannel);
    }

    public Drive(SpeedController leftMotor, SpeedController rightMotor) {
        super(leftMotor, rightMotor);
    }
}
