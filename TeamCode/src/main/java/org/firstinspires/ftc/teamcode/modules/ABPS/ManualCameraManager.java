package org.firstinspires.ftc.teamcode.modules.ABPS;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class ManualCameraManager {
    public final WebcamName camera;

    public AprilTagProcessor processor;
    public VisionPortal portal;

    public ManualCameraManager(WebcamName camera) {
        this.camera = camera;
    }

    public void init() {
        processor = AprilTagProcessor.easyCreateWithDefaults();

        portal = VisionPortal.easyCreateWithDefaults(camera, processor);
    }
}
