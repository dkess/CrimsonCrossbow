package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author daniel
 */
public class ShooterSubsystem extends Subsystem {

    public static final double SHOOTER_RPM_HIGH = 3700;
    public static final double SHOOTER_RPM_LOW = 3400;

    private SpeedController shooter;
    private SpeedController actuator;
    private SpeedController kicker;

    private Counter shooterEnc;

    public ShooterSubsystem(RobotConfig config) {
        shooter = new Talon(config.getAsInt("tShooter"));
        actuator = new Talon(config.getAsInt("tActuator"));
        kicker = new Talon(config.getAsInt("tKicker"));

        shooterEnc = new Counter(config.getAsInt("shooterEnc"));

        shooterEnc.start();
    }

    public void activateActuator(boolean on) {
        actuator.set(on ? 0.4 : 0);
    }

    public void bangBangShooter(boolean on, double targetRPM) {
        double shooterOutput;
        if (on) {
            shooterOutput = getBangBang(targetRPM, 0.6, shooterEnc);
        } else {
            shooterOutput = 0;
        }
        shooter.set(shooterOutput);
        kicker.set(shooterOutput);
		System.out.println(shooterOutput);
    }

    public void shooterOff() {
        shooter.set(0);
        kicker.set(0);
		System.out.println("0 off");
    }
	
    /**
     * Uses the Bang-Bang algorithm to return a power to send to the motor.
     * Calculates RPM based on what source gives it
     *
     * @param targetRPM The desired RPM
     * @param slowOutput What the function to return if the real RPM is greater
     * than the target
     * @param source The CounterBase that monitors the speed of the motor.
     * @return slowOutput if current > target, or 1 if current < target
     */
    public static double getBangBang(double targetRPM, double slowOutput, CounterBase source) {
        return periodToRPM(source.getPeriod()) < targetRPM ? 1 : slowOutput;
    }

    /**
     * Converts a period value (time in seconds between two ticks) to RPM.
     *
     * @param periodInSeconds
     * @return The RPM
     */
    public static double periodToRPM(double periodInSeconds) {
        return 60 / periodInSeconds;
    }
}
