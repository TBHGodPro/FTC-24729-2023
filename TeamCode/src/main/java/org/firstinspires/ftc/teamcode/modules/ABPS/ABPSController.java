package org.firstinspires.ftc.teamcode.modules.ABPS;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.BaseModule;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Automatic Backboard Positioning System

public class ABPSController extends BaseModule {
    public final boolean active;
    
    public final InputManager inputs;
    
    public final ABPSThread thread;
    public final Runnable emptyRunnable;
    public final ExecutorService executor = Executors.newSingleThreadExecutor();
    public ABPSState state = ABPSState.STOPPED;
    
    public boolean shouldOpenWristWhenDone = true;
    
    public ABPSController(MainOp op, boolean active) {
        super(op);
        
        this.active = active;
        
        this.inputs = op.inputs;
        
        thread = new ABPSThread(op);
        emptyRunnable = new Runnable() {
            @Override
            public void run() {
            }
        };
    }
    
    public void activate(ABPSState state, double strafeGainMult) {
        if (!this.active) return;
        
        this.state = state;
        
        thread.dynamicStrafeGain = ABPSThread.strafeGain * strafeGainMult;
        
        executor.submit(emptyRunnable);
        
        executor.submit(thread);
    }
    
    public boolean isDone() {
        return state == ABPSState.STOPPED;
    }
    
    @Override
    public void loop() {
        if (!this.active) return;
        
        if (executor.isTerminated() && !isDone()) {
            state = ABPSState.STOPPED;
            
            op.movements.activate();
        }
        
        if (inputs.ABPSLeft) {
            activate(ABPSState.LEFT, 1);
        }
        if (inputs.ABPSRight) {
            activate(ABPSState.RIGHT, 1);
        }
        
        if (!isDone()
                && (inputs.forward != 0 || inputs.strafe != 0 || inputs.turn != 0)) {
            state = ABPSState.STOPPED;
            
            executor.submit(emptyRunnable);
            
            op.wheels.setTarget(null);
            
            op.movements.activate();
        }
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        if (this.active) {
            telemetry.addData("Active", "True")
                    .addData("State", new Func<String>() {
                        @Override
                        public String value() {
                            return state == ABPSState.STOPPED ? "Stopped" : ("Active (" + state.name() + ")");
                        }
                    })
                    .addData("Detections", new Func<String>() {
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
        } else {
            telemetry.addData("Active", "False");
        }
    }
    
    public enum ABPSState {
        STOPPED,
        LEFT,
        RIGHT
    }
}
