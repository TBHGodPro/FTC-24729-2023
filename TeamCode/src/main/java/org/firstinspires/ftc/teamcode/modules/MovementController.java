package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Utils;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelController;

@Config
public class MovementController extends BaseModule {
    // --- Constants ---
    
    public static final RevHubOrientationOnRobot hubOrientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.UP,
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD);
    
    public static double turningNonLinearity = 1.75; // 1 = linear
    
    public static double dpad_power = 0.3;
    
    public static double yawKP = 0.04;
    public static double yawKI = 0.1;
    public static double yawKD = 0.0038;
    
    // -----------------
    
    public final WheelController wheelController;
    
    public final IMU imu;
    
    public final InputManager inputs;
    
    public final PIDController yawPID;
    
    public final boolean doYawCorrections;
    
    public Double desiredAngle;
    
    public boolean shouldRun = true;
    
    public MovementController(MainOp op, WheelController wheelController, IMU imu, InputManager inputs, boolean doYawCorrections) {
        super(op);
        
        this.wheelController = wheelController;
        
        this.imu = imu;
        
        this.inputs = inputs;
        
        this.yawPID = new PIDController(yawKP, yawKI, yawKD);
        
        this.doYawCorrections = doYawCorrections;
    }
    
    @Override
    public void init() {
        imu.initialize(new IMU.Parameters(hubOrientation));
    }
    
    @Override
    public void op_start() {
        imu.resetYaw();
    }
    
    @Override
    public void loop() {
        if (!shouldRun) return;
        
        // Allow Input Changing
        double rawY = inputs.forward;
        double rawX = inputs.strafe;
        double rawTurn = inputs.turn;
        
        // D-Pad Movements
        if (inputs.slowMoveUp) rawY += dpad_power;
        if (inputs.slowMoveDown) rawY -= dpad_power;
        
        if (inputs.slowMoveRight) rawX += dpad_power;
        if (inputs.slowMoveLeft) rawX -= dpad_power;
        
        // Get Facing Angle
        double rawAngle = getRawAngle();
        
        // Headless Control Rotation
        
        // - Re-Straighten
        if (inputs.realignIMU) {
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
    
    @Override
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
        yawPID.setPID(yawKP, yawKI, yawKD);
        
        double diff = getAngularError(angle, desiredAngle);
        
        return yawPID.calculate(diff, 0) * 0.75;
    }
    
    public double getAngularError(double current, double target) {
        double diff = target - current;
        
        while (diff > 180) diff -= 360;
        while (diff < -180) diff += 360;
        
        return diff;
    }
    
    public void activate() {
        shouldRun = true;
    }
    
    public void deactivate() {
        shouldRun = false;
    }
}
