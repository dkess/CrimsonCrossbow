package com.milkenknights.crimsoncrossbow;

/**
 * This control system uses three ATK3 controllers: two for driving and one for
 * AUX.
 *
 * @author Daniel
 */
public class TripleATKControl extends ControlSystem {

    JStick atkr, atkl, atka;

    public TripleATKControl(CasterSubsystem sCaster,
            DriveSubsystem sDrive,
            HookClimbSubsystem sHookClimb,
            ShooterSubsystem sShooter) {
        super(sCaster, sDrive, sHookClimb, sShooter);
        atkl = new JStick(1);
        atkr = new JStick(2);
        atka = new JStick(3);
    }

    public void teleopPeriodic() {
        atkl.update();
        atkr.update();
        atka.update();
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
        // controlled by AUX ATK button 11
        if (atka.isReleased(11)) {
            hookClimbSub.toggleHookClimb();
        }

        // SHOOTER CONTROL
        // AUX ATK 2 spins at high speed. AUX ATK 4 or 5 spin at low speed.
        // AUX trigger spins actuator (shoots the frisbee)
        if (atka.isPressed(2)) {
            shooterSub.bangBangShooter(true, ShooterSubsystem.SHOOTER_RPM_HIGH);
            System.out.println("high");
        } else if (atka.isPressed(4) || atka.isPressed(5)) {
            shooterSub.bangBangShooter(true, ShooterSubsystem.SHOOTER_RPM_LOW);
            System.out.println("low");
        } else {
            shooterSub.shooterOff();
            System.out.println("off");
        }

        shooterSub.activateActuator(atka.isPressed(1));
    }
}
