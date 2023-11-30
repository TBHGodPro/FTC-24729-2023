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
public abstract class CameraManager {
    // --- Constants ---
    
    public static double aprilTagDecimation = 2;
    
    public static int cameraExposureMS = 15;
    public static int cameraGain = 250;
    
    // -----------------
    
    public WebcamName camera;
    
    public VisionPortal portal;
    
    public CameraStreamProcessor stream;
    
    public AprilTagProcessor processor;
    
    public CameraManager(WebcamName camera) {
        this.camera = camera;
        
        stream = new CameraStreamProcessor();
        
        processor = AprilTagProcessor.easyCreateWithDefaults();
    }
    
    public abstract void init();
    
    public void setupCameraSettings() {
        setManualExposure(cameraExposureMS, cameraGain);
        processor.setDecimation((float) aprilTagDecimation);
    }
    
    public void setupDashboard() {
        FtcDashboard.getInstance().startCameraStream(stream, 0);
    }
    
    public void loop() {
        setupCameraSettings();
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
        
        ExposureControl exposureControl = portal.getCameraControl(ExposureControl.class);
        
        if (exposureControl.getExposure(TimeUnit.MILLISECONDS) != exposureMS) {
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);
        }
        
        GainControl gainControl = portal.getCameraControl(GainControl.class);
        
        if (gainControl.getGain() != gain) {
            gainControl.setGain(gain);
            sleep(20);
        }
    }
    
    private void sleep(long ms) {
        try {
            Thread.sleep(20);
        } catch (Exception e) {
        }
    }
}
