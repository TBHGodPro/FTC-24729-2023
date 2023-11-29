package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSThread;
import org.firstinspires.ftc.teamcode.modules.Camera.CameraManager;
import org.firstinspires.ftc.teamcode.modules.Camera.ManualCameraManager;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Automatic Backboard Positioning System

public class ABPSController extends Module {
    public final MainOp op;
    
    public final Gamepad gamepad;
    
    public final CameraManager camera;
    
    public final ABPSThread thread;
    public final Runnable emptyRunnable;
    public final ExecutorService executor = Executors.newSingleThreadExecutor();
    public ABPSState state = ABPSState.STOPPED;
    
    public ABPSController(MainOp op, Gamepad gamepad, WebcamName camera) {
        this.op = op;
        
        this.gamepad = gamepad;
        
        this.camera = new ManualCameraManager(camera);
        
        thread = new ABPSThread(op);
        emptyRunnable = new Runnable() {
            @Override
            public void run() {
            }
        };
    }
    
    public void init() {
        camera.init();
    }
    
    public void loop() {
        if (executor.isTerminated()) {
            state = ABPSState.STOPPED;
        }
        
        if (gamepad.dpad_left) {
            state = ABPSState.LEFT;
            
            executor.submit(emptyRunnable);
            
            executor.submit(thread);
        }
        if (gamepad.dpad_right) {
            state = ABPSState.RIGHT;
            
            executor.submit(emptyRunnable);
            
            executor.submit(thread);
        }
        
        if (state != ABPSState.STOPPED
                && (gamepad.left_stick_x != 0 || gamepad.left_stick_y != 0 || gamepad.right_stick_x != 0
                || gamepad.right_stick_y != 0)) {
            state = ABPSState.STOPPED;
            
            executor.submit(emptyRunnable);
            
            op.wheels.setTarget(null);
        }
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        
        telemetry.addData("State", new Func<String>() {
                    @Override
                    public String value() {
                        return state == ABPSState.STOPPED ? "Stopped" : ("Active (" + state.name() + ")");
                    }
                })
                .addData("Detections", new Func<String>() {
                    @Override
                    public String value() {
                        return camera.processor.getDetections().size() + "";
                    }
                })
                .addData("Info", new Func<String>() {
                    @Override
                    public String value() {
                        List<AprilTagDetection> detections = camera.processor.getDetections();
                        
                        StringBuilder msg = new StringBuilder();
                        
                        for (AprilTagDetection detection : detections) {
                            if (detection.ftcPose == null) msg.append("\n\nNULL");
                            else {
                                msg.append("\n");
                                msg.append("\nID = ").append(detection.id);
                                msg.append("\nXYZ = ").append(detection.ftcPose.x).append(", ").append(detection.ftcPose.y).append(",").append(detection.ftcPose.z);
                                msg.append("\nYaw = ").append(detection.ftcPose.yaw);
                                msg.append("\nRange = ").append(detection.ftcPose.range);
                            }
                        }
                        
                        return msg.toString();
                    }
                });
    }
    
    public enum ABPSState {
        STOPPED,
        LEFT,
        RIGHT
    }
}
