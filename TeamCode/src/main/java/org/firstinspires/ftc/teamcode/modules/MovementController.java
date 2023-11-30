package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Utils.Utils;

public class MovementController extends Module {
    // --- Constants ---
    
    public static final RevHubOrientationOnRobot hubOrientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
    
    public static double turningNonLinearity = 1.75; // 1 = linear
    
    public static double steeringCounterCoeff = 30; // Lower = more power
    
    // -----------------
    
    public final IMU imu;
    
    public final Gamepad gamepad;
    
    public final boolean doYawCorrections;
    
    public Double desiredAngle;
    
    public boolean shouldRun = true;
    
    public MovementController(IMU imu, Gamepad gamepad, boolean doYawCorrections) {
        this.imu = imu;
        
        this.gamepad = gamepad;
        
        this.doYawCorrections = doYawCorrections;
    }
    
    public void init() {
        imu.initialize(new IMU.Parameters(hubOrientation));
    }
    
    public void prep() {
        imu.resetYaw();
    }
    
    public void updatePowers(WheelController wheelController) {
        if (!shouldRun) return;
        
        // Allow Input Changing
        double rawY = -gamepad.left_stick_y;
        double rawX = gamepad.left_stick_x;
        double rawTurn = gamepad.right_stick_x;
        
        // Get Facing Angle
        double rawAngle = getRawAngle();
        
        // Headless Control Rotation
        
        // - Re-Straighten
        if (gamepad.start) {
            imu.resetYaw();
            rawAngle = 0;
            if (desiredAngle != null) {
                desiredAngle = 0d;
            }
        }
        
        // - Calculations
        double angleDiff = (0 - rawAngle) * Math.PI / 180;
        
        double cos = Math.cos(angleDiff);
        double sin = Math.sin(angleDiff);
        
        // - Changed Variables
        double controlX = cos * rawX - sin * rawY;
        double controlY = sin * rawX + cos * rawY;
        
        // Turning Adjustments
        
        // - Declared Changed Variables
        double controlTurn;
        
        // - Non-Linearity
        controlTurn = rawTurn >= 0 ? Math.pow(rawTurn, turningNonLinearity) : -Math.pow(-rawTurn, turningNonLinearity);
        
        // - Yaw Correction
        if (doYawCorrections) {
            if (controlTurn != 0) {
                desiredAngle = null;
            } else {
                if (desiredAngle == null) {
                    if (Math.abs(imu.getRobotAngularVelocity(AngleUnit.DEGREES).zRotationRate) < 20) {
                        desiredAngle = rawAngle;
                    }
                } else {
                    controlTurn += getYawCorrections(rawAngle);
                }
            }
        }
        
        // Calculate and Send Powers
        setPowers(wheelController, controlY, controlX, controlTurn);
    }
    
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Angle", new Func<String>() {
                    @Override
                    public String value() {
                        return Utils.round(imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES)) + "°";
                    }
                })
                .addData("Desired Angle", new Func<String>() {
                    @Override
                    public String value() {
                        return Utils.round(desiredAngle == null ? 0 : desiredAngle) + "°";
                    }
                });
    }
    
    public void setPowers(WheelController wheels, double forward, double strafe, double turn) {
        // Calculate Powers
        double backLeft = forward - strafe + turn;
        double backRight = forward + strafe - turn;
        double frontLeft = forward + strafe + turn;
        double frontRight = forward - strafe - turn;
        
        // Send Powers
        wheels.setPowers(backLeft, backRight, frontLeft, frontRight);
    }
    
    public double getRawAngle() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    
    public double getYawCorrections(double angle) {
        if (angle != desiredAngle) {
            if (angle > desiredAngle) {
                return (((angle - desiredAngle) + 180) % 360 - 180) / steeringCounterCoeff;
            } else {
                return -(((desiredAngle - angle) + 180) % 360 - 180) / steeringCounterCoeff;
            }
        }
        
        return 0;
    }
    
    public void activate() {
        shouldRun = true;
    }
    
    public void deactivate() {
        shouldRun = false;
    }
}
