package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import java.util.Enumeration;
import java.util.Vector;

public class Knight extends IterativeRobot {

    RobotConfig config;

    Vector subsystems;

    DriveSubsystem driveSubsystem;
    HookClimbSubsystem hookClimbSubsystem;
    CasterSubsystem casterSubsystem;
    ShooterSubsystem shooterSubsystem;
    PneumaticSubsystem pneumaticSubsystem;

    ControlSystem controlSystem;
    NetworkTableSystem networkTableSystem;

    public void robotInit() {
        config = new RobotConfig();

        driveSubsystem = new DriveSubsystem(config);
        hookClimbSubsystem = new HookClimbSubsystem(config);
        casterSubsystem = new CasterSubsystem(config);
        shooterSubsystem = new ShooterSubsystem(config);
        pneumaticSubsystem = new PneumaticSubsystem(config);

        networkTableSystem = new NetworkTableSystem(casterSubsystem,
                driveSubsystem,
                hookClimbSubsystem,
                pneumaticSubsystem, shooterSubsystem);
        controlSystem = new TouchScreenControl(casterSubsystem,
                driveSubsystem,
                hookClimbSubsystem,
                pneumaticSubsystem,
                shooterSubsystem, networkTableSystem);

        subsystems = new Vector(10);

        subsystems.addElement(driveSubsystem);
        subsystems.addElement(hookClimbSubsystem);
        subsystems.addElement(casterSubsystem);
        subsystems.addElement(shooterSubsystem);

        // since no more subsystems will be added, we can free the remaining
        // memory
        subsystems.trimToSize();

        pneumaticSubsystem.startCompressor();
    }

    public void autonomousInit() {
        driveSubsystem.setDriveMode(DriveSubsystem.PIDSTRAIGHT);
    }

    public void autonomousPeriodic() {
        networkTableSystem.update();
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

        networkTableSystem.update();
        
        // Feed the Watchdog.
        Watchdog.getInstance().feed();
    }
    
    public void disabledPeriodic() {
        networkTableSystem.update();
    }

    public void testPeriodic() {
    }

}
