// PID System, just provide it with your tuning variables and setpoint
// and it will provide a function that takes your current position and
// outputs a thing.
package com.milkenknights.crimsoncrossbow;

public class PIDSystem {
    private double setpoint;
    private final double deadzone;

    private final double kp;
    private final double ki;
    private final double kd;

    private double sumOfError;
    private double lastError;

    private double error;

    public PIDSystem(double setpoint, double kp, double ki, double kd, double deadzone) {
        this.setpoint = setpoint;
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.deadzone = deadzone;
        this.sumOfError = 0;
        this.lastError = 0;
    }

    public void changeSetpoint(double setpoint) {
        this.setpoint = setpoint;
        reset();
    }

    public void reset() {
        this.sumOfError = 0;
        this.lastError = 0;
    }

    private double PFunction(double error) {
        double pValue = kp * error;
        return pValue;
    }

    private double IFunction(double error) {
        sumOfError += error;
        double iValue = ki * sumOfError;
        return iValue;
    }

    private double DFunction(double error) {
        double changeInError = error - lastError;
        lastError = error;
        double dValue = kd * changeInError;
        return dValue;
    }

    public double update(double position) {
        error = setpoint - position;
        double output = PFunction(error) + IFunction(error) + DFunction(error);
        if (Math.abs(output) < deadzone) {
            return 0.0;
        } else {
            return output;
        }
    }

    /**
     * Returns true if the PID error is within threshold.
     *
     * @param threshold
     * @return true if the PID error is within the threshold.
     */
    public boolean onTarget(double threshold) {
        return Math.abs(error) <= threshold;
    }
}
