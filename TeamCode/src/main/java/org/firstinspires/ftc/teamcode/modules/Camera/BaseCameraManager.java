package org.firstinspires.ftc.teamcode.modules.Camera;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.concurrent.TimeUnit;

@Config
public abstract class BaseCameraManager {
    // --- Constants ---
    
    public static float aprilTagDecimation = 2f;
    
    public static int cameraExposureMS = 2;
    public static int cameraGain = 100;
    
    // -----------------
    
    public final WebcamName camera;
    
    public VisionPortal portal;
    
    public final CameraStreamProcessor stream;
    
    public final AprilTagProcessor processor;
    
    public ExposureControl exposure;
    public GainControl gain;
    
    public BaseCameraManager(WebcamName camera) {
        this.camera = camera;
        
        stream = new CameraStreamProcessor();
        
        processor = AprilTagProcessor.easyCreateWithDefaults();
    }
    
    public abstract void init();
    
    public void setupCameraSettings() {
        updateCameraExposure();
    }
    
    public void updateCameraExposure() {
        setManualExposure(cameraExposureMS, cameraGain);
        processor.setDecimation(aprilTagDecimation);
        
        disableAprilTag();
    }
    
    public void setupDashboard() {
        FtcDashboard.getInstance().startCameraStream(stream, 0);
    }
    
    public void loop() {
        updateCameraExposure();
    }
    
    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls
        
        if (portal == null) {
            return;
        }
        
        // Make sure camera is streaming before we try to set the exposure controls
        if (portal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            while (portal.getCameraState() != VisionPortal.CameraState.STREAMING) {
                sleep(20);
            }
        }
        
        if (exposure == null) {
            exposure = portal.getCameraControl(ExposureControl.class);
        }
        
        if (exposure.getExposure(TimeUnit.MILLISECONDS) != exposureMS) {
            if (exposure.getMode() != ExposureControl.Mode.Manual) {
                exposure.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposure.setExposure(exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);
        }
        
        if (this.gain == null) {
            this.gain = portal.getCameraControl(GainControl.class);
        }
        
        if (this.gain.getGain() != gain) {
            this.gain.setGain(gain);
            sleep(20);
        }
    }
    
    private void sleep(long ms) {
        try {
            Thread.sleep(20);
        } catch (Exception e) {
        }
    }
    
    public void enableAprilTag() {
        portal.setProcessorEnabled(processor, true);
    }
    
    public void disableAprilTag() {
        portal.setProcessorEnabled(processor, false);
    }
    
    public void enablePropDetection() {
    }
    
    public void disablePropDetection() {
    }
}
