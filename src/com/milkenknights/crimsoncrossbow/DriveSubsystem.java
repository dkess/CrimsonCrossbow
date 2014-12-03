package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The subsystem that manages the robot's wheels and gear solenoids.
 *
 * @author Daniel
 * @author Jake
 */
public class DriveSubsystem extends Subsystem {

    Drive drive;

    // true means high gear, false means low gear
    SolenoidPair driveGear;

    PIDController leftPID;
    PIDController rightPID;
    //PIDSystem gyroPID;

    Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;

    Gyro gyro;

    boolean slowMode;
    boolean reverseMode;
    boolean runPID;
    boolean runGyro;

    int driveMode = 0;
    public static final int NONE = 0;
    public static final int CHEESY = 1;
    public static final int TANK = 2;
    public static final int PIDSTRAIGHT = 3;
    public static final int FULLSPEED = 4;

    double tankLeftSpeed;
    double tankRightSpeed;

    double cheesyPower;
    double cheesyTurn;
    boolean cheesyQuickturn;

    public DriveSubsystem(RobotConfig config) {
        Talon leftWheel = new Talon(config.getAsInt("tLeftWheel"));
        Talon rightWheel = new Talon(config.getAsInt("tRightWheel"));

        drive = new Drive(leftWheel, rightWheel);
        // this solenoid pair is TRUE if the robot is in high gear
        driveGear = new SolenoidPair(config.getAsInt("sDriveGearA"),
                config.getAsInt("sDriveGearB"), true, false, true);

        leftDriveEncoder = new Encoder(config.getAsInt("leftEncA"),
                config.getAsInt("leftEncB"),
                true, CounterBase.EncodingType.k4X);
        rightDriveEncoder = new Encoder(config.getAsInt("rightEncA"),
                config.getAsInt("rightEncB"),
                true, CounterBase.EncodingType.k4X);

        leftDriveEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
        rightDriveEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);

        leftPID = new PIDController(0, 0, 0, leftDriveEncoder, leftWheel);
        rightPID = new PIDController(0, 0, 0, rightDriveEncoder, rightWheel);

        gyro = new Gyro(config.getAsInt("gyro"));

        gyro.reset();
    }

    public void toggleGear() {
        driveGear.toggle();
    }

    public void teleopInit() {
        setDriveMode(TANK);
        reverseMode = false;

        leftPID.startLiveWindowMode();
        rightPID.startLiveWindowMode();

        SmartDashboard.putData("left pid", leftPID);
        SmartDashboard.putData("right pid", rightPID);
    }

    public void setLeftSpeed(double speed) {
        tankLeftSpeed = speed;
    }

    public void setRightSpeed(double speed) {
        tankRightSpeed = speed;
    }

    public void setTankSpeed(double left, double right) {
        tankLeftSpeed = left;
        tankRightSpeed = right;
    }

    public void setCheesy(double power, double turn, boolean quickturn) {
        cheesyPower = power;
        cheesyTurn = turn;
        cheesyQuickturn = quickturn;
    }

    public void setStraightPIDSetpoint(double setpoint) {
        leftPID.setSetpoint(setpoint);
        rightPID.setSetpoint(setpoint);
    }

    public void setDriveMode(int mode) {
        driveMode = mode;
        if (driveMode == PIDSTRAIGHT) {
            leftPID.enable();
            rightPID.enable();
            leftPID.reset();
            rightPID.reset();
        } else {
            leftPID.disable();
            rightPID.disable();
        }
    }

    public int getDriveMode() {
        return driveMode;
    }

    public boolean pidOnTarget(double threshold) {
        return leftPID.onTarget() && rightPID.onTarget();
    }
    
    public double getSpeed() {
        return (drive.getLeft() + drive.getRight()) / 2;
    }

    /**
     * Updates wheels depending on driveMode (which should be set to the desired
     * mode with setDriveMode(). This method should be called during every loop
     * no matter what.
     */
    public void update() {
        leftPID.updateTable();
        rightPID.updateTable();

        SmartDashboard.putNumber("drivemode", driveMode);
        if (driveMode == CHEESY) {
            drive.cheesyDrive(cheesyPower, cheesyTurn, cheesyQuickturn);
        } else if (driveMode == TANK) {
            drive.tankDrive(tankLeftSpeed, tankRightSpeed);
        } else if (driveMode == PIDSTRAIGHT) {

        } else if (driveMode == FULLSPEED) {
            drive.tankDrive(1, 1);

        } else {
            drive.tankDrive(0, 0);
        }

        SmartDashboard.putNumber("l dist", leftDriveEncoder.getDistance());
        SmartDashboard.putNumber("r dist", rightDriveEncoder.getDistance());
        SmartDashboard.putNumber("left", drive.getLeft());
        SmartDashboard.putNumber("right", drive.getRight());
    }
}
