package com.milkenknights.crimsoncrossbow;

/**
 * An abstract class for control systems. Implementations should access this
 * class and and the teleopPeriodic method, and control the subsystems based on
 * joystick inputs.
 *
 * Getting Joystick instances should be handled by the implementing class.
 *
 * @author Daniel
 */
public abstract class ControlSystem {

    protected CasterSubsystem casterSub;
    protected DriveSubsystem driveSub;
    protected HookClimbSubsystem hookClimbSub;
    protected PneumaticSubsystem pneumaticSub;
    protected ShooterSubsystem shooterSub;

    protected ControlSystem(CasterSubsystem sCaster,
            DriveSubsystem sDrive,
            HookClimbSubsystem sHookClimb,
            PneumaticSubsystem sPneumatic,
            ShooterSubsystem sShooter) {
        casterSub = sCaster;
        driveSub = sDrive;
        hookClimbSub = sHookClimb;
        pneumaticSub = sPneumatic;
        shooterSub = sShooter;
    }

    public abstract void teleopPeriodic();
}
