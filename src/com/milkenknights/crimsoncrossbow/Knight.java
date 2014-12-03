package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import java.util.Enumeration;
import java.util.Vector;

public class Knight extends IterativeRobot {

    RobotConfig config;

    Vector subsystems;

    Compressor compressor;
    DriveSubsystem driveSubsystem;
    HookClimbSubsystem hookClimbSubsystem;
    CasterSubsystem casterSubsystem;
    ShooterSubsystem shooterSubsystem;

    ControlSystem controlSystem;

    public void robotInit() {
        config = new RobotConfig();

        compressor = new Compressor(config.getAsInt("compressorPressureSwitch"),
                config.getAsInt("compressorRelayChannel"));
        driveSubsystem = new DriveSubsystem(config);
        hookClimbSubsystem = new HookClimbSubsystem(config);
        casterSubsystem = new CasterSubsystem(config);
        shooterSubsystem = new ShooterSubsystem(config);

        controlSystem = new TripleATKControl(casterSubsystem,
                driveSubsystem,
                hookClimbSubsystem,
                shooterSubsystem);

        subsystems = new Vector(10);

        subsystems.addElement(driveSubsystem);
        subsystems.addElement(hookClimbSubsystem);
        subsystems.addElement(casterSubsystem);
        subsystems.addElement(shooterSubsystem);

        // since no more subsystems will be added, we can free the remaining
        // memory
        subsystems.trimToSize();

        compressor.start();
    }

    public void autonomousInit() {
        driveSubsystem.setDriveMode(DriveSubsystem.PIDSTRAIGHT);
    }

    public void autonomousPeriodic() {

    }

    public void teleopInit() {
        for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
            ((Subsystem) e.nextElement()).teleopInit();
        }
    }

    public void teleopPeriodic() {
        controlSystem.teleopPeriodic();

        for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
            ((Subsystem) e.nextElement()).update();
        }

        // Feed the Watchdog.
        Watchdog.getInstance().feed();
    }

    public void testPeriodic() {
    }

}
