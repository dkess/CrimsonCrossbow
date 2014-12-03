package com.milkenknights.crimsoncrossbow;

import edu.wpi.first.wpilibj.Compressor;

/**
 *
 * @author austinshalit
 */
public class PneumaticSubsystem extends Subsystem {

    private final Compressor compressor;

    public PneumaticSubsystem(RobotConfig config) {
        compressor = new Compressor(config.getAsInt("compressorPressureSwitch"),
                config.getAsInt("compressorRelayChannel"));
    }
    
    public void setCompressorState(boolean state) {
        if (state) {
            startCompressor();
        } else {
            stopCompressor();
        }
    }

    public void startCompressor() {
        compressor.start();
    }

    public void stopCompressor() {
        compressor.stop();
    }

    public boolean compressorState() {
        return compressor.enabled();
    }

}
