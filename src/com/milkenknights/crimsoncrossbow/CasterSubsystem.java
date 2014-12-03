package com.milkenknights.crimsoncrossbow;

public class CasterSubsystem extends Subsystem {

    private SolenoidPair caster;

    public CasterSubsystem(RobotConfig config) {
        caster = new SolenoidPair(config.getAsInt("sCasterA"),
                config.getAsInt("sCasterB"), true, true, false);
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
