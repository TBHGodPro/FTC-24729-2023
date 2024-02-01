package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;
import java.util.List;

public class AprilTagLocalizer extends Thread {
    public static final Pose2d[] tagLocations = {
            // Testing Override
            // TODO: REMOVE
            new Pose2d(54, -30, 0),
            new Pose2d(54, -36, 0),
            new Pose2d(54, -42, 0),
            
            // Blue
            new Pose2d(54, 42, 0),
            new Pose2d(54, 36, 0),
            new Pose2d(54, 30, 0),
            
            // Red
            new Pose2d(54, -30, 0),
            new Pose2d(54, -36, 0),
            new Pose2d(54, -42, 0)
    };
    
    public final BaseAutonOp op;
    public final AprilTagProcessor aprilTag;
    
    public AprilTagLocalizer(BaseAutonOp op, AprilTagProcessor aprilTag) {
        this.op = op;
        this.aprilTag = aprilTag;
    }
    
    @Override
    public void run() {
        op.telemetry.addData("Runtime", new Func<String>() {
                    @Override
                    public String value() {
                        return op.getRuntime() + "";
                    }
                }).addData("Detections", new Func<String>() {
                    @Override
                    public String value() {
                        return op.camera.processor.getDetections().size() + "";
                    }
                })
                .addData("Info", new Func<String>() {
                    @Override
                    public String value() {
                        List<AprilTagDetection> detections = op.camera.processor.getDetections();
                        
                        StringBuilder msg = new StringBuilder();
                        
                        for (AprilTagDetection detection : detections) {
                            if (detection.ftcPose == null) msg.append("\n\nNULL");
                            else {
                                msg.append("\n");
                                msg.append("\nFound" + " = " + "ID " + detection.id + " (" + detection.metadata.name + ")");
                                msg.append("\nRange" + " = " + detection.ftcPose.range + " inches");
                                msg.append("\nBearing" + " = " + detection.ftcPose.bearing + " degrees");
                                msg.append("\nYaw" + " = " + detection.ftcPose.yaw + " degrees");
                            }
                        }
                        
                        return msg.toString();
                    }
                });
        
        op.waitForStart();
        
        while (op.opModeIsActive()) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                return;
            }
            
            op.telemetry.update();
            
            ArrayList<AprilTagDetection> detections = aprilTag.getDetections();
            
            if (detections.size() < 1) continue;
            
            AprilTagDetection detection = detections.get(0);
            
            // Tag
            Pose2d pose = tagLocations[detection.id - 1];
            // Offset
            pose = pose.plus(new Pose2d(-detection.ftcPose.range, -detection.ftcPose.x, -detection.ftcPose.bearing));
            // Camera Offset
            pose = pose.plus(new Pose2d(5, -8, 0));
            
            op.drive.setPoseEstimate(pose);
        }
    }
}
