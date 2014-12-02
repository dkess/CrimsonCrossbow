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
    //JStick xbox;
    
    JStick atkl;
    JStick atkr;
    
    Drive drive;
    
    // true means high gear, false means low gear
    SolenoidPair driveGear;

    PIDController leftPID;
    PIDController rightPID;
    //PIDSystem gyroPID;

    Encoder leftDriveEncoder;
    Encoder rightDriveEncoder;

    Gyro gyro;

    boolean normalDriveGear;
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

    public DriveSubsystem(RobotConfig config) {
        //xbox = JStickMultiton.getJStick(1);
        
        atkl = JStickMultiton.getJStick(1);
        atkr = JStickMultiton.getJStick(2);
        
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
		
		leftPID = new PIDController(0,0,0, leftDriveEncoder, leftWheel);
		rightPID = new PIDController(0,0,0, rightDriveEncoder, rightWheel);
		
		
		
        /*
        leftPID = new PIDSystem(config.getAsDouble("driveDistance"),
                config.getAsDouble("drivePIDkp"),
                config.getAsDouble("drivePIDki"),
                config.getAsDouble("drivePIDkd"), .001);
        rightPID = new PIDSystem(config.getAsDouble("driveDistance"),
                config.getAsDouble("drivePIDkp"),
                config.getAsDouble("drivePIDki"),
                config.getAsDouble("drivePIDkd"), .001);
        gyroPID = new PIDSystem(config.getAsDouble("gyroAngle"),
                config.getAsDouble("gyrokp"),
                config.getAsDouble("gyroki"),
                config.getAsDouble("gyrokd"), .001);
        */
        /*
         leftDriveEncoder = new Encoder(config.getAsInt("leftEncA"),
         config.getAsInt("leftEncB"),
         true, CounterBase.EncodingType.k4X);
         rightDriveEncoder = new Encoder(config.getAsInt("rightEncA"),
         config.getAsInt("rightEncB"),
         true, CounterBase.EncodingType.k4X);
         */
        gyro = new Gyro(config.getAsInt("gyro"));

        gyro.reset();
    }

    public void teleopInit() {
        setDriveMode(TANK);
        reverseMode = false;
    }

    public void teleopPeriodic() {
        //if (xbox.isReleased(JStick.XBOX_LB)) {
        if (atkr.isReleased(1)) {
            driveGear.toggle();
            normalDriveGear = driveGear.get();
        }
        
        /*
        if (xbox.isReleased(JStick.XBOX_Y)) {
            slowMode = !slowMode;

            if (slowMode) {
                driveGear.set(false);
            } else {
                driveGear.set(normalDriveGear);
            }
        }

        if (xbox.isReleased(JStick.XBOX_X)) {
            reverseMode = !reverseMode;
        }
        */
        
        
        // TODO: change these negatives!!
        //setTankSpeed(-atkl.getSlowedAxis(2), -atkr.getSlowedAxis(2));
        setTankSpeed(-atkl.getAxis(2), -atkr.getAxis(2));
        /*
        SmartDashboard.putNumber("LSY", -atkl.getAxis(2));
        SmartDashboard.putNumber("LSY slow", -atkl.getSlowedAxis(2));
        SmartDashboard.putNumber("RSY", -atkr.getAxis(2));
        SmartDashboard.putNumber("RSY slow", -atkr.getSlowedAxis(2));
        SmartDashboard.putNumber("L slow time", atkl.getSlowTime(2));
        */
        
        if (atkl.getAxis(2) != atkl.getSlowedAxis(2)) {
            SmartDashboard.putNumber("L slowed", 0);
        } else {
            SmartDashboard.putNumber("L slowed", 1);
        }
        
        SmartDashboard.putBoolean("Drive gear high:", driveGear.get());
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

    public void setStraightPIDSetpoint(double setpoint) {
        //leftPID.changeSetpoint(setpoint);
        //rightPID.changeSetpoint(setpoint);
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

    public boolean pidOnTarget(double threshold) {
        //return leftPID.onTarget(threshold) && rightPID.onTarget(threshold);
		return leftPID.onTarget() && rightPID.onTarget();
    }

    /**
     * Updates wheels depending on driveMode (which should be set to the desired
     * mode with setDriveMode(). This method should be called during every loop
     * no matter what.
     */
    public void update() {
        SmartDashboard.putNumber("drivemode", driveMode);
        if (driveMode == CHEESY) {
            /*
            double power = xbox.getAxis(JStick.XBOX_LSY);
            double turn = xbox.getAxis(JStick.XBOX_RSX);
            boolean trigDown
                    = Math.abs(xbox.getAxis(JStick.XBOX_TRIG)) > 0.5;

            if (reverseMode) {
                power = -power;
                turn = -turn;
            }

            if (slowMode) {
                power = power * .5;
            }

            SmartDashboard.putNumber("power", power);
            SmartDashboard.putNumber("turn", turn);
            SmartDashboard.putBoolean("td", trigDown);

            drive.cheesyDrive(power, -turn, trigDown);
            */
        } else if (driveMode == TANK) {
            drive.tankDrive(tankLeftSpeed, tankRightSpeed);
        } else if (driveMode == PIDSTRAIGHT) {

        } else if (driveMode == FULLSPEED) {
            drive.tankDrive(1, 1);

        } else {
            drive.tankDrive(0, 0);
        }

        SmartDashboard.putNumber("left", drive.getLeft());
        SmartDashboard.putNumber("right", drive.getRight());
    }
}
