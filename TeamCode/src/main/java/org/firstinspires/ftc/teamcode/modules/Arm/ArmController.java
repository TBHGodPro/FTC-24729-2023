package org.firstinspires.ftc.teamcode.modules.Arm;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.BaseModule;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;

@Config
public class ArmController extends BaseModule {
    // --- Constants ---
    
    public static double armNonLinearity = 2; // 1 = linear
    
    public static double armManualPower = 0.5;
    
    public static int armMaxPos = 2600;
    
    public static int armPositionClearance = 35;
    
    public static int armPositionOffset = 75;
    
    public static double wristPosInterval = 0.01;
    
    public static double wristAngleCorrectionCoeff = 2300;
    
    public static double handLeftOpenPos = 0.77;
    public static double handLeftClosedPos = 0.55;
    public static double handRightOpenPos = 0.26;
    public static double handRightClosedPos = 0.45;
    
    public static int armOffset = 0;
    
    public static double armInputDeadzone = 0.1;
    
    // - Intake Position
    public static int armIntakePosManual = 200;
    public static int armIntakePosAutonomous = 200;
    public int armIntakePos;
    public double wristIntakePos = 0.44;
    
    // - Backboard Position
    
    public static int armBackboardPosManual = 510;
    public static int armBackboardPosAutonomous = 270;
    public int armBackboardPos;
    public double wristBackboardPos = 0.7;
    
    // - Overhead Position
    public int armOverheadPos = 1350;
    public double wristOverheadPos = 1.44;
    
    public static final DcMotorEx.Direction armDirection = DcMotorEx.Direction.REVERSE;
    public static final ZeroPowerBehavior armZeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    
    // -----------------
    
    public final boolean isAutonomous;
    public final InputManager inputs;
    
    public final DcMotorEx arm;
    public final ArmPowerController armPower;
    
    public final Servo wristLeft;
    public final Servo wristRight;
    
    public final Servo handLeft;
    public final Servo handRight;
    
    public int armPos = 0;
    public double wristPos = 1;
    public boolean isHandLeftClosed = false;
    public boolean isHandRightClosed = false;
    
    public ArmController(MainOp op, boolean isAutonomous, InputManager inputs, DcMotorEx arm, Servo wristLeft, Servo wristRight, Servo handLeft, Servo handRight) {
        super(op);
        
        this.isAutonomous = isAutonomous;
        
        this.inputs = inputs;
        
        this.arm = arm;
        this.armPower = new ArmPowerController(0);
        this.armPower.setTarget(0, armPos);
        
        this.wristLeft = wristLeft;
        this.wristRight = wristRight;
        
        this.handLeft = handLeft;
        this.handRight = handRight;
        
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
        
        handLeft.setDirection(Servo.Direction.FORWARD);
        handRight.setDirection(Servo.Direction.FORWARD);
    }
    
    @Override
    public void op_start() {
        resetZeroPosition();
    }
    
    @Override
    public void loop() {
        // Arm Control
        
        // - Manual Power
        double armPower = 0;
        
        armPower += Math.pow(inputs.armUp, armNonLinearity);
        armPower -= Math.pow(inputs.armDown, armNonLinearity);
        
        // - Manual Override
        if (Math.abs(armPower) > armInputDeadzone) {
            this.armPower.clearTarget();
            
            if (armPower > 0 && armPos >= armMaxPos) armPower = 0;
            else armPower *= armManualPower;
            
            armPos = getPosition();
            armPower += this.armPower.getFeedForward(armPos);
        } else {
            this.armPower.setTarget(getPosition(), armPos);
            
            armPower = this.armPower.calc(getPosition());
        }
        
        // - Set Power
        arm.setPower(armPower);
        
        // Wrist Control
        if (inputs.wristUp) {
            wristPos += wristPosInterval;
        }
        if (inputs.wristDown) {
            wristPos -= wristPosInterval;
        }
        
        wristPos = Math.max(Math.min(wristPos, 2), -1);
        double parsedWristPos = Math.max(Math.min(wristPos - (armPos / wristAngleCorrectionCoeff), 1), 0);
        
        wristLeft.setPosition(parsedWristPos);
        wristRight.setPosition(parsedWristPos);
        
        // Hand Control
        if (inputs.leftHandClosed) {
            isHandLeftClosed = true;
        }
        if (inputs.leftHandOpen) {
            isHandLeftClosed = false;
        }
        
        if (inputs.rightHandClosed) {
            isHandRightClosed = true;
        }
        if (inputs.rightHandOpen) {
            isHandRightClosed = false;
        }
        
        handLeft.setPosition(isHandLeftClosed ? handLeftClosedPos : handLeftOpenPos);
        handRight.setPosition(isHandRightClosed ? handRightClosedPos : handRightOpenPos);
        
        // Intake Position
        if (inputs.armIntakePosition) {
            goToIntakePosition();
        }
        
        // Backboard Position
        if (inputs.armBackboardPosition) {
            goToBackboardPosition();
        }
        
        // Overhead Position
        if (inputs.armOverheadPosition) {
            goToOverheadPosition();
        }
        
        // Reset Intake Position
        if (inputs.realignIntake) {
            int offset = armIntakePos - getPosition();
            double wristOffset = wristIntakePos - wristPos;
            
            armIntakePos -= offset;
            wristIntakePos -= wristOffset;
            
            armBackboardPos -= offset;
            wristBackboardPos -= wristOffset;
            
            armOverheadPos -= offset;
            wristOverheadPos -= wristOffset;
        }
    }
    
    public int getPosition() {
        return arm.getCurrentPosition() + armOffset;
    }
    
    public void resetZeroPosition() {
        arm.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
        armPos = 0;
        
        arm.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void goToIntakePosition() {
        armPos = armIntakePos;
        wristPos = wristIntakePos;
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
        return Math.round(armPos / armPositionClearance) == Math.round((getPosition() + armPositionOffset) / armPositionClearance);
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Arm Position", new Func<String>() {
                    @Override
                    public String value() {
                        return getPosition() + "";
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
                        return "Left: " + (isHandLeftClosed ? "Closed" : "Open") + ", Right: " + (isHandRightClosed ? "Closed" : "Open");
                    }
                });
    }
    
    @Override
    public void updateDashboardTelemetry() {
        telemetry.addData("Arm Target Pos", armPos);
        telemetry.addData("Arm Current Pos", getPosition());
        telemetry.addData("Arm Power", arm.getPower() * 100);
        telemetry.addData("At Position", isAtPosition());
        telemetry.addData("Arm Profile Pos", armPower.target != null ? armPower.target : -1);
        telemetry.addData("Arm Profile Timer", armPower.timer.time());
    }
}
