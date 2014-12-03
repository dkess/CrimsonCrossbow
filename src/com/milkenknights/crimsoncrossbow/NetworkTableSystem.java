package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Compressor;
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
    private final PneumaticSubsystem pneumaticSub;
    private final ShooterSubsystem shooterSub;
    

    public NetworkTableSystem(CasterSubsystem sCaster,
            DriveSubsystem sDrive,
            HookClimbSubsystem sHookClimb,
            PneumaticSubsystem sPneumatic,
            ShooterSubsystem sShooter) {
        casterSub = sCaster;
        driveSub = sDrive;
        hookClimbSub = sHookClimb;
        pneumaticSub = sPneumatic;
        shooterSub = sShooter;
        
        dataTable = NetworkTable.getTable("data_table");
        controlTable = NetworkTable.getTable("control_table");
    }

    public void update() {
        dataTable.putNumber("drive_speed", driveSub.getSpeed());
        
        dataTable.putBoolean("hook_climb", hookClimbSub.getState());
        
        dataTable.putNumber("shooter_rpm", shooterSub.getRPM());
        dataTable.putBoolean("shooter_shooting", shooterSub.getShooting());
        
        dataTable.putBoolean("compressor_enabled", pneumaticSub.compressorState());
    }

    public NetworkTable getControlTable() {
        return controlTable;
    }
}
