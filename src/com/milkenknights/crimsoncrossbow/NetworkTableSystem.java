package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * A wrapper for the WPI NetworkTable
 *
 * @author austinshalit
 */
public class NetworkTableSystem {

    private final NetworkTable dataTable;
    private final NetworkTable controlTable;
    
    private final CasterSubsystem casterSub;
    private final DriveSubsystem driveSub;
    private final HookClimbSubsystem hookClimbSub;
    private final ShooterSubsystem shooterSub;

    public NetworkTableSystem(CasterSubsystem sCaster,
            DriveSubsystem sDrive,
            HookClimbSubsystem sHookClimb,
            ShooterSubsystem sShooter) {
        casterSub = sCaster;
        driveSub = sDrive;
        hookClimbSub = sHookClimb;
        shooterSub = sShooter;
        
        dataTable = NetworkTable.getTable("data_table");
        controlTable = NetworkTable.getTable("control_table");
    }

    public void update() {
        dataTable.putNumber("drive_speed", driveSub.getSpeed());
        
        dataTable.putBoolean("hook_climb", hookClimbSub.getState());
        
        dataTable.putNumber("shooter_rpm", shooterSub.getRPM());
        dataTable.putBoolean("shooter_shooting", shooterSub.getShooting());
    }

    public NetworkTable getControlTable() {
        return controlTable;
    }
}
