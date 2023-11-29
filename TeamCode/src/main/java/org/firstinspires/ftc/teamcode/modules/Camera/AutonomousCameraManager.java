package org.firstinspires.ftc.teamcode.modules.Camera;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

public class AutonomousCameraManager extends CameraManager {
    public AutonomousCameraManager(WebcamName camera) {
        super(camera);
    }
    
    public void init() {
        portal = new VisionPortal.Builder()
                .addProcessor(processor)
                .addProcessor(stream)
                .setCamera(camera)
                .build();
        
        setupCameraSettings();
        
        setupDashboard();
    }
}
