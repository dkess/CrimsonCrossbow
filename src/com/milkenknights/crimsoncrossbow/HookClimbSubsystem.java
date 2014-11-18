package com.milkenknights.crimsoncrossbow;

public class HookClimbSubsystem extends Subsystem {
    JStick atk;
    private SolenoidPair hookClimb;
    
    public HookClimbSubsystem(RobotConfig config) {
        hookClimb = new SolenoidPair(config.getAsInt("sHookA"),
                config.getAsInt("sHookB"), true, true, false);
        
        atk = JStickMultiton.getJStick(2);
    }
    
    public void teleopPeriodic() {
        if (atk.isReleased(11)) {
            toggleHookClimb();
        }
    }
    
    public void toggleHookClimb() {
        hookClimb.toggle();
    }
    
    public void setHookClimb(boolean state) {
        hookClimb.set(state);
    }
    
    public boolean getState() {
        return hookClimb.get();
    }
}
