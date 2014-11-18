package com.milkenknights.crimsoncrossbow;


public class CasterSubsystem extends Subsystem {
    private SolenoidPair caster;
    private JStick atkr;
    
    
    public CasterSubsystem(RobotConfig config) {
        caster = new SolenoidPair(config.getAsInt("sCasterA"),
                config.getAsInt("sCasterB"), true, true, false);
        
        atkr = JStickMultiton.getJStick(2);
    }
    
    public void teleopPeriodic() {
        if (atkr.isReleased(3)) {
            toggleCaster();
        }
    }
    
    public void toggleCaster() {
        caster.toggle();
    }
    
    public void setCasterState(boolean state) {
        caster.set(state);
    }
    
    public boolean getState() {
        return caster.get();
    }

}
