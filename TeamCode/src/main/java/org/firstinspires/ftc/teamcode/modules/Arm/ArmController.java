package org.firstinspires.ftc.teamcode.modules.Arm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.BaseModule;

@Config
public class ArmController extends BaseModule {
    // --- Constants ---
    
    public static double armNonLinearity = 2; // 1 = linear
    
    public static double armManualPower = 0.5;
    
    public static int armMaxPos = 2600;
    
    public static int armPositionClearance = 35;
    
    public static int armPositionOffset = 75;
    
    public static double wristPosInterval = 0.01;
    
    public static double wristAngleCorrectionCoeff = 3100;
    
    public static double handFrontOpenPos = 0.85;
    public static double handFrontClosedPos = 0.61;
    public static double handBackOpenPos = 0.65;
    public static double handBackClosedPos = 0.89;
    
    // - Intake Position
    public static int armIntakePosManual = 170;
    public static int armIntakePosAutonomous = 170;
    public int armIntakePos;
    public final boolean shouldOpenHandAtIntake;
    public double wristIntakePos = 0.5;
    
    // - Backboard Position
    
    public static int armBackboardPosManual = 510;
    public static int armBackboardPosAutonomous = 270;
    public int armBackboardPos;
    public double wristBackboardPos = 0.75;
    
    // - Overhead Position
    public int armOverheadPos = 1950;
    public double wristOverheadPos = 1.81;
    
    public static final DcMotorEx.Direction armDirection = DcMotorEx.Direction.REVERSE;
    public static final ZeroPowerBehavior armZeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    
    // -----------------
    
    public final boolean isAutonomous;
    public final Gamepad gamepad;
    
    public final DcMotorEx arm;
    public final ArmPowerController armPower;
    
    public final Servo wristLeft;
    public final Servo wristRight;
    
    public final Servo handFront;
    public final Servo handBack;
    
    public int armPos = 0;
    public double wristPos = 1;
    public boolean isHandClosed = false;
    
    public ArmController(MainOp op, boolean isAutonomous, Gamepad gamepad, DcMotorEx arm, Servo wristLeft, Servo wristRight, Servo handFront, Servo handBack) {
        super(op);
        
        this.isAutonomous = isAutonomous;
        
        this.gamepad = gamepad;
        
        this.arm = arm;
        this.armPower = new ArmPowerController(0);
        this.armPower.setTarget(0, armPos);
        
        this.wristLeft = wristLeft;
        this.wristRight = wristRight;
        
        this.handFront = handFront;
        this.handBack = handBack;
        
        this.shouldOpenHandAtIntake = !isAutonomous;
        this.armIntakePos = isAutonomous ? armIntakePosAutonomous : armIntakePosManual;
        this.armBackboardPos = isAutonomous ? armBackboardPosAutonomous : armBackboardPosManual;
    }
    
    @Override
    public void init() {
        arm.setDirection(armDirection);
        arm.setZeroPowerBehavior(armZeroPowerBehavior);
        arm.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
        wristLeft.setDirection(Servo.Direction.FORWARD);
        wristRight.setDirection(Servo.Direction.REVERSE);
        
        handFront.setDirection(Servo.Direction.REVERSE);
        handBack.setDirection(Servo.Direction.FORWARD);
    }
    
    @Override
    public void start() {
        resetZeroPosition();
    }
    
    @Override
    public void loop() {
        // Arm Control
        
        // - Manual Power
        double armPower = 0;
        
        armPower += Math.pow(gamepad.right_trigger, armNonLinearity);
        armPower -= Math.pow(gamepad.left_trigger, armNonLinearity);
        
        // - Manual Override
        if (armPower != 0) {
            this.armPower.clearTarget();
            
            if (armPower > 0 && armPos >= armMaxPos) armPower = 0;
            else armPower *= armManualPower;
            
            armPos = arm.getCurrentPosition();
            armPower += this.armPower.getFeedForward(armPos);
        } else {
            this.armPower.setTarget(arm.getCurrentPosition(), armPos);
            
            armPower = this.armPower.calc(arm.getCurrentPosition());
        }
        
        // - Set Power
        arm.setPower(armPower);
        
        // Wrist Control
        if (gamepad.right_bumper) {
            wristPos += wristPosInterval;
        }
        if (gamepad.left_bumper) {
            wristPos -= wristPosInterval;
        }
        
        wristPos = Math.max(Math.min(wristPos, 2), -1);
        double parsedWristPos = Math.max(Math.min(wristPos - (armPos / wristAngleCorrectionCoeff), 1), 0);
        
        wristLeft.setPosition(parsedWristPos);
        wristRight.setPosition(parsedWristPos);
        
        // Hand Control
        if (gamepad.x) {
            isHandClosed = true;
        }
        if (gamepad.y) {
            isHandClosed = false;
        }
        
        handFront.setPosition(isHandClosed ? handFrontClosedPos : handFrontOpenPos);
        handBack.setPosition(isHandClosed ? handBackClosedPos : handBackOpenPos);
        
        // Intake Position
        if (gamepad.b) {
            goToIntakePosition();
        }
        
        // Backboard Position
        if (gamepad.a) {
            goToBackboardPosition();
        }
        
        // Overhead Position
        if (gamepad.guide) {
            goToOverheadPosition();
        }
        
        // Reset Intake Position
        if (gamepad.back) {
            int offset = armIntakePos - arm.getCurrentPosition();
            double wristOffset = wristIntakePos - wristPos;
            
            armIntakePos -= offset;
            wristIntakePos -= wristOffset;
            
            armBackboardPos -= offset;
            wristBackboardPos -= wristOffset;
            
            armOverheadPos -= offset;
            wristOverheadPos -= wristOffset;
        }
    }
    
    public void resetZeroPosition() {
        arm.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
        armPos = 0;
        
        arm.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void goToIntakePosition() {
        armPos = armIntakePos;
        wristPos = wristIntakePos;
        if (shouldOpenHandAtIntake) {
            isHandClosed = false;
        }
    }
    
    public void goToBackboardPosition() {
        armPos = armBackboardPos;
        wristPos = wristBackboardPos;
    }
    
    public void goToOverheadPosition() {
        armPos = armOverheadPos;
        wristPos = wristOverheadPos;
    }
    
    public void goToPosition(ArmPosition position) {
        switch (position) {
            case INTAKE:
                goToIntakePosition();
                break;
            case BACKBOARD:
                goToBackboardPosition();
                break;
            case OVERHEAD:
                goToOverheadPosition();
                break;
        }
    }
    
    public boolean isAtPosition() {
        return Math.round(armPos / armPositionClearance) == Math.round((arm.getCurrentPosition() + armPositionOffset) / armPositionClearance);
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Arm Position", new Func<String>() {
                    @Override
                    public String value() {
                        return arm.getCurrentPosition() + "";
                    }
                })
                .addData("Target Arm Position", new Func<String>() {
                    @Override
                    public String value() {
                        return armPos + "";
                    }
                })
                .addData("Wrist Position", new Func<String>() {
                    @Override
                    public String value() {
                        return wristPos + "";
                    }
                })
                .addData("True Wrist Position", new Func<String>() {
                    @Override
                    public String value() {
                        return Math.max(Math.min(wristPos - (armPos / wristAngleCorrectionCoeff), 1), 0) + "";
                    }
                })
                .addData("Hand State", new Func<String>() {
                    @Override
                    public String value() {
                        return isHandClosed ? "Closed" : "Open";
                    }
                });
    }
    
    @Override
    public void getDashboardTelemetry(Telemetry telemetry) {
        telemetry.addData("Arm Target Pos", armPos);
        telemetry.addData("Arm Current Pos", arm.getCurrentPosition());
        telemetry.addData("Arm Power", arm.getPower() * 100);
        telemetry.addData("At Position", isAtPosition());
        telemetry.addData("Arm Profile Pos", armPower.target != null ? armPower.target : -1);
        telemetry.addData("Arm Profile Timer", armPower.timer.time());
    }
}
