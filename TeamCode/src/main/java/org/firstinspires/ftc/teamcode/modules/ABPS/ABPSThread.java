package org.firstinspires.ftc.teamcode.modules.ABPS;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSController.ABPSState;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.List;

@Config
public class ABPSThread extends Thread {
    // --- Constants ---
    
    public static double desiredDistance = 15.5;
    public static double desiredDistancePDOffset = -11;
    public static double strafeGain = 0.0025;
    public static double turnGain = 0.008;
    
    public static double kP = 0.02;
    public static double kD = 0.005;
    
    // -----------------
    
    public final MainOp op;
    
    public double dynamicStrafeGain = strafeGain;
    
    public final PDController pd = new PDController(kP, kD);
    
    public ABPSThread(MainOp op) {
        this.op = op;
    }
    
    @Override
    public void run() {
        if (op.abps.isDone()) return;
        
        op.camera.enableAprilTag();
        
        op.arm.goToBackboardPosition();
        op.arm.armPos = 700;
        
        op.movements.desiredAngle = op.abps.state == ABPSState.LEFT ? 90d : -90d;
        
        while ((Math.round(op.movements.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) / 10) != Math.round(op.movements.desiredAngle / 10) || op.camera.processor.getDetections().size() == 0) && op.abps.state != ABPSState.STOPPED) {
            pause();
        }
        
        op.movements.deactivate();
        
        pd.setP(kP);
        pd.setD(kD);
        
        while (op.abps.state != ABPSState.STOPPED) {
            List<AprilTagDetection> detections = op.camera.processor.getDetections();
            
            if (detections.size() == 0) {
                op.movements.setPowers(op.wheels, 0, 0, 0);
                continue;
            }
            
            AprilTagDetection bestDetection = null;
            
            for (AprilTagDetection detection : detections) {
                if (detection.ftcPose == null) continue;
                
                if (bestDetection == null) bestDetection = detection;
                
                //  Blue
                if (op.getAlliance() == Alliance.BLUE) {
                    if (detection.id == 2) bestDetection = detection;
                    else if (detection.id == 3 && bestDetection.id != 2) bestDetection = detection;
                    else if (detection.id == 1 && bestDetection.id != 3 && bestDetection.id != 2)
                        bestDetection = detection;
                } else {
                    // Red
                    if (detection.id == 5) bestDetection = detection;
                    else if (detection.id == 6 && bestDetection.id != 5) bestDetection = detection;
                    else if (detection.id == 4 && bestDetection.id != 6 && bestDetection.id != 5)
                        bestDetection = detection;
                }
            }
            
            if (bestDetection == null) {
                op.movements.setPowers(op.wheels, 0, 0, 0);
                continue;
            }
            
            AprilTagPoseFtc pose = bestDetection.ftcPose;
            
            double trueYaw = 0;
            
            if (bestDetection.id == 1 || bestDetection.id == 4) {
                trueYaw = pose.yaw - 2.5;
            } else if (bestDetection.id == 2 || bestDetection.id == 5) {
                trueYaw = pose.yaw;
            } else if (bestDetection.id == 3 || bestDetection.id == 6) {
                trueYaw = pose.yaw + 2.5;
            }
            
            double strafePower = (-trueYaw) * dynamicStrafeGain;
            double turnPower = (pose.bearing) * turnGain;
            
            if (Math.abs(pose.range - desiredDistance) < 1.5) break;
            
            double forwardPower = -pd.calculate(pose.range, desiredDistance + desiredDistancePDOffset);
            
            turnPower += op.movements.getYawCorrections(op.movements.getRawAngle());
            
            op.movements.setPowers(op.wheels, forwardPower, strafePower, turnPower);
        }
        
        op.arm.goToBackboardPosition();
        
        op.movements.setPowers(op.wheels, 0, 0, 0);
        
        op.camera.disableAprilTag();
        
        pause(800);
        
        if (op.abps.state != ABPSState.STOPPED && op.abps.shouldOpenWristWhenDone) {
            op.arm.isHandClosed = false;
        }
        
        op.abps.state = ABPSState.STOPPED;
        
        op.movements.activate();
    }
    
    private void pause() {
        if (Thread.currentThread().isInterrupted()) return;
        try {
            Thread.sleep(5);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void pause(int milliseconds) {
        if (Thread.currentThread().isInterrupted()) return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }
}
