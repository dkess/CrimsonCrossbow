package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    
    public void robotInit() {
        config = new RobotConfig();
		//config.loadFile();

        compressor = new Compressor(config.getAsInt("compressorPressureSwitch"),
                config.getAsInt("compressorRelayChannel"));
        driveSubsystem = new DriveSubsystem(config);
        hookClimbSubsystem = new HookClimbSubsystem(config);
        casterSubsystem = new CasterSubsystem(config);
        shooterSubsystem = new ShooterSubsystem(config);

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
    }

    public void autonomousPeriodic() {
        SmartDashboard.putNumber("test",1);
    }

    public void teleopInit() {
        for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
            ((Subsystem) e.nextElement()).teleopInit();
        }
    }

    public void teleopPeriodic() {
        JStickMultiton.updateAll();

        for (Enumeration e = subsystems.elements(); e.hasMoreElements();) {
            Subsystem s = (Subsystem) e.nextElement();
            //System.out.println("Subsystem "+i+" running");
            double fullTime = 0;
            double startTime = Timer.getFPGATimestamp();

            s.teleopPeriodic();

            //System.out.println("teleop: "+(Timer.getFPGATimestamp() - startTime));
            fullTime = Timer.getFPGATimestamp() - startTime;

            s.update();

            //System.out.println("update: "+(Timer.getFPGATimestamp() - (startTime + fullTime)));
            fullTime += Timer.getFPGATimestamp() - (startTime + fullTime);
            //System.out.println("total "+i+": "+fullTime);
        }

        // Feed the Watchdog.
        Watchdog.getInstance().feed();
    }

    public void testPeriodic() {}

}
