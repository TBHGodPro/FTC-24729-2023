package org.firstinspires.ftc.teamcode.modules.ABPS;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.ABPSController.ABPSState;
import org.firstinspires.ftc.teamcode.modules.WheelController.WheelTarget;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

public class ABPSThread extends Thread {
    public MainOp op;

    public ABPSThread(MainOp op) {
        this.op = op;
    }

    @Override
    public void run() {
        op.movements.desiredAngle = op.abps.state == ABPSState.LEFT ? 90d : -90d;

        while ((Math.round(op.movements.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) / 10) != Math
                .round(op.movements.desiredAngle / 10)
                || op.abps.camera.processor.getDetections().size() == 0) && op.abps.state != ABPSState.STOPPED) {
            pause();
        }

        if (op.abps.state != ABPSState.STOPPED) {
            op.arm.gotToBackboardPosition();
        }

        while (op.abps.state != ABPSState.STOPPED) {
            List<AprilTagDetection> detections = op.abps.camera.processor.getDetections();

            if (detections.size() == 0) {
                continue;
            }

            AprilTagDetection bestDetection = null;

            for (AprilTagDetection detection : detections) {
                if (bestDetection == null)
                    bestDetection = detection;
    
                if (detection.id == 1)
                    bestDetection = detection;
                else if (detection.id == 2 && bestDetection.id != 1)
                    bestDetection = detection;
                else if (detection.id == 3 && bestDetection.id != 1 && bestDetection.id != 2)
                    bestDetection = detection;
            }

            double distance = bestDetection.ftcPose.range;

            if (distance <= 14)
                break;

            int wheelTicks = (int) (distance - 14) * 40;

            if (wheelTicks < 2)
                continue;

            op.wheels.setTarget(
                    new WheelTarget(wheelTicks, wheelTicks, wheelTicks, wheelTicks, (int) (wheelTicks / 1.6)));

            while (op.wheels.target != null && op.abps.state != ABPSState.STOPPED)
                pause();
        }

        if (op.abps.state != ABPSState.STOPPED) {
            op.arm.isHandClosed = false;
        }

        op.abps.state = ABPSState.STOPPED;
    }

    private void pause() {
        if (Thread.currentThread().isInterrupted())
            return;
        try {
            Thread.sleep(5);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }
}
