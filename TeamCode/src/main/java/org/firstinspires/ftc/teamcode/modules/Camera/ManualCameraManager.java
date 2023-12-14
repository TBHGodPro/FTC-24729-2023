package org.firstinspires.ftc.teamcode.modules.Camera;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

public class ManualCameraManager extends BaseCameraManager {
    public ManualCameraManager(WebcamName camera) {
        super(camera);
    }
    
    public void init() {
        portal = new VisionPortal.Builder()
                .setCamera(camera)
                .addProcessor(processor)
                .addProcessor(stream)
                .build();
        
        setupCameraSettings();
        
        setupDashboard();
    }
}
