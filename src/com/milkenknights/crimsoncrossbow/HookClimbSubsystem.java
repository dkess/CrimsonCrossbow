package com.milkenknights.crimsoncrossbow;

public class HookClimbSubsystem extends Subsystem {

    private SolenoidPair hookClimb;

    public HookClimbSubsystem(RobotConfig config) {
        hookClimb = new SolenoidPair(config.getAsInt("sHookA"),
                config.getAsInt("sHookB"), true, true, false);
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
