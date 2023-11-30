package org.firstinspires.ftc.teamcode.modules.ABPS;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSController.ABPSState;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;

import java.util.List;

@Config
public class ABPSThread extends Thread {
    // --- Constants ---
    
    public static double desiredDistance = 14;
    public static double forwardGain = 0.08;
    public static double strafeGain = 0.05;
    public static double turnGain = 0.02;
    
    // -----------------
    
    public MainOp op;
    
    public ABPSThread(MainOp op) {
        this.op = op;
    }
    
    @Override
    public void run() {
        if (op.abps.state == ABPSState.STOPPED) return;
        
        op.movements.desiredAngle = op.abps.state == ABPSState.LEFT ? 90d : -90d;
        
        while ((Math.round(op.movements.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) / 10) != Math.round(op.movements.desiredAngle / 10) || op.camera.processor.getDetections().size() == 0) && op.abps.state != ABPSState.STOPPED) {
            pause();
        }
        
        op.movements.deactivate();
        
        if (op.abps.state != ABPSState.STOPPED) {
            op.arm.gotToBackboardPosition();
        }
        
        while (op.abps.state != ABPSState.STOPPED) {
            List<AprilTagDetection> detections = op.camera.processor.getDetections();
            
            if (detections.size() == 0) continue;
            
            AprilTagDetection bestDetection = null;
            
            for (AprilTagDetection detection : detections) {
                if (detection.ftcPose == null) continue;
                
                if (bestDetection == null) bestDetection = detection;
                
                if (detection.id == 1) bestDetection = detection;
                else if (detection.id == 2 && bestDetection.id != 1) bestDetection = detection;
                else if (detection.id == 3 && bestDetection.id != 1 && bestDetection.id != 2)
                    bestDetection = detection;
            }
            
            if (bestDetection == null) continue;
            
            AprilTagPoseFtc pose = bestDetection.ftcPose;
            
            double trueYaw = 0;
            
            if (bestDetection.id == 1) {
                trueYaw = pose.yaw;
            } else if (bestDetection.id == 2) {
                trueYaw = pose.yaw + 5;
            } else if (bestDetection.id == 3) {
                trueYaw = pose.yaw + 5;
            }
            
            if (pose.range <= desiredDistance) break;
            
            double forwardPower = (pose.range - desiredDistance) * forwardGain;
            double strafePower = (-trueYaw) * strafeGain;
            double turnPower = (pose.bearing) * turnGain;
            
            turnPower += op.movements.getYawCorrections(op.movements.getRawAngle());
            
            op.movements.setPowers(op.wheels, forwardPower, strafePower, turnPower);
        }
        
        if (op.abps.state != ABPSState.STOPPED) {
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
}
