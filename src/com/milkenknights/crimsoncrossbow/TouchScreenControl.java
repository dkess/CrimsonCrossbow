/**
 * This control system uses two ATK3 controllers for driving.
 * It also uses a touchscreen for AUX
 *
 * @author Austin
 */
package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 * @author austinshalit
 */
public class TouchScreenControl extends ControlSystem {

    JStick atkr, atkl;
    NetworkTable controlTable;
    
    public TouchScreenControl(CasterSubsystem sCaster,
            DriveSubsystem sDrive,
            HookClimbSubsystem sHookClimb,
            PneumaticSubsystem sPneumatic,
            ShooterSubsystem sShooter,
            NetworkTableSystem networkTable) {
        super(sCaster, sDrive, sHookClimb, sPneumatic, sShooter);
        controlTable = networkTable.getControlTable();
        
        atkl = new JStick(1);
        atkr = new JStick(2);
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        // GEAR TOGGLE
        // controlled by right ATK trigger
        if (atkr.isReleased(1)) {
            driveSub.toggleGear();
        }

        // DRIVING
        if (driveSub.getDriveMode() == DriveSubsystem.TANK) {
            // TANK DRIVE
            // controlled by left and right ATK y axes
            driveSub.setTankSpeed(-atkl.getAxis(2), -atkr.getAxis(2));
        } else if (driveSub.getDriveMode() == DriveSubsystem.CHEESY) {
            // CHEESY DRIVE
            // Left ATK y controls power. Right ATK x controls turn
            // no quickturn
            driveSub.setCheesy(-atkl.getAxis(2), atkr.getAxis(1), false);
        }

        // left ATK 7 toggles between cheesy and tank
        if (atkl.isReleased(7)) {
            if (driveSub.getDriveMode() == DriveSubsystem.TANK) {
                driveSub.setDriveMode(DriveSubsystem.CHEESY);
            } else {
                driveSub.setDriveMode(DriveSubsystem.TANK);
            }
        }

        // left ATK 8 switches to tank drive. 9 switches to cheesy
        if (atkl.isReleased(8)) {
            driveSub.setDriveMode(DriveSubsystem.TANK);
        }

        if (atkl.isReleased(9)) {
            driveSub.setDriveMode(DriveSubsystem.CHEESY);
        }

        // CASTER TOGGLE
        // controlled by right ATK button 3
        if (atkr.isReleased(3)) {
            casterSub.toggleCaster();
        }

        // HOOK CLIMB
        // controlled by networktables
        hookClimbSub.setHookClimb(controlTable.getBoolean("hook_climb", false));

        // SHOOTER CONTROL
        // controlled by network tables
        if (controlTable.getString("shooter_speed", "").equals("HIGH")) {
            shooterSub.bangBangShooter(true, ShooterSubsystem.SHOOTER_RPM_HIGH);
            System.out.println("high");
        } else if (controlTable.getString("shooter_speed", "").equals("LOW")) {
            shooterSub.bangBangShooter(true, ShooterSubsystem.SHOOTER_RPM_LOW);
            System.out.println("low");
        } else {
            shooterSub.shooterOff();
            System.out.println("off");
        }

        shooterSub.activateActuator(controlTable.getBoolean("shooter_shoot", false));
        
        // PNEUMATIC CONTROL
        // controlled by network tables
        pneumaticSub.setCompressorState(controlTable.getBoolean("compressor_state", true));
    }
}
