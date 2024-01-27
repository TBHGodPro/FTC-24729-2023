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
    
    public static int armPositionOffset = 0;
    
    public static double wristPosInterval = 0.01;
    
    public static double wristAngleCorrectionCoeff = 2300;
    
    public static double handLeftOpenPos = 0.77;
    public static double handLeftClosedPos = 0.55;
    public static double handRightOpenPos = 0.26;
    public static double handRightClosedPos = 0.45;
    
    public static double armInputDeadzone = 0.1;
    
    public static int armOffset = 75;
    public static double wristOffset = 0;
    
    // - Intake Position
    public static int armIntakePosManual = 250;
    public static int armIntakePosAutonomous = 250;
    public int armIntakePos;
    public double wristIntakePos = 0.31;
    
    // - Backboard Position
    
    public static int armBackboardPosManual = 510;
    public static int armBackboardPosAutonomous = 270;
    public int armBackboardPos;
    public double wristBackboardPos = 0.6;
    
    // - Overhead Position
    public int armOverheadPos = 1470;
    public double wristOverheadPos = 1.37;
    
    // - Safe Position
    public int armSafePos = 185;
    public double wristSafePos = 0.75;
    
    public static final DcMotorEx.Direction armLeftDirection = DcMotorEx.Direction.REVERSE;
    public static final DcMotorEx.Direction armRightDirection = DcMotorEx.Direction.REVERSE;
    public static final ZeroPowerBehavior armZeroPowerBehavior = ZeroPowerBehavior.FLOAT;
    
    // -----------------
    
    public final boolean isAutonomous;
    public final InputManager inputs;
    
    public final DcMotorEx armLeft;
    public final DcMotorEx armRight;
    public final ArmPowerController armPower;
    
    public final Servo wristLeft;
    public final Servo wristRight;
    
    public final Servo handLeft;
    public final Servo handRight;
    
    public int armPos;
    public double wristPos = 1;
    public boolean isHandLeftClosed = false;
    public boolean isHandRightClosed = false;
    
    public double armStaticPower = 0;
    
    public ArmController(MainOp op, boolean isAutonomous, InputManager inputs, DcMotorEx armLeft, DcMotorEx armRight, Servo wristLeft, Servo wristRight, Servo handLeft, Servo handRight) {
        super(op);
        
        this.isAutonomous = isAutonomous;
        
        this.inputs = inputs;
        
        this.armLeft = armLeft;
        this.armRight = armRight;
        
        this.armPos = armOffset;
        
        this.armPower = new ArmPowerController(0);
        this.armPower.setTarget(0, armPos);
        
        this.wristLeft = wristLeft;
        this.wristRight = wristRight;
        
        this.handLeft = handLeft;
        this.handRight = handRight;
        
        this.armIntakePos = isAutonomous ? armIntakePosAutonomous : armIntakePosManual;
        this.armBackboardPos = isAutonomous ? armBackboardPosAutonomous : armBackboardPosManual;
    }
    
    public void setArmStaticPower(double power) {
        this.armStaticPower = power;
    }
    
    @Override
    public void init() {
        armLeft.setDirection(armLeftDirection);
        armLeft.setZeroPowerBehavior(armZeroPowerBehavior);
        armLeft.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
        armRight.setDirection(armRightDirection);
        armRight.setZeroPowerBehavior(armZeroPowerBehavior);
        armRight.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
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
        double armPower = armStaticPower;
        
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
        armLeft.setPower(armPower);
        armRight.setPower(armPower);
        
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
        
        // Safe Position
        if (inputs.armSafePosition) {
            goToSafePosition();
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
            
            armSafePos -= offset;
            wristSafePos -= wristOffset;
        }
    }
    
    public int getPosition() {
        return armLeft.getCurrentPosition() + armOffset;
    }
    
    public int getOtherPosition() {
        return armRight.getCurrentPosition() + armOffset;
    }
    
    public void resetZeroPosition() {
        armLeft.setMode(RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(RunMode.STOP_AND_RESET_ENCODER);
        
        armPos = 0;
        
        armLeft.setMode(RunMode.RUN_WITHOUT_ENCODER);
        armRight.setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
    
    public void goToIntakePosition() {
        armPos = armIntakePos;
        wristPos = wristIntakePos + wristOffset;
    }
    
    public void goToBackboardPosition() {
        armPos = armBackboardPos;
        wristPos = wristBackboardPos + wristOffset;
    }
    
    public void goToOverheadPosition() {
        armPos = armOverheadPos;
        wristPos = wristOverheadPos + wristOffset;
    }
    
    public void goToSafePosition() {
        armPos = armSafePos;
        wristPos = wristSafePos + wristOffset;
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
                        return getPosition() + " (" + getOtherPosition() + ")";
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
        telemetry.addData("Other Arm Current Pos", getOtherPosition());
        telemetry.addData("Arm Power", armRight.getPower() * 100);
        telemetry.addData("At Position", isAtPosition());
        telemetry.addData("Arm Profile Pos", armPower.target != null ? armPower.target : -1);
        telemetry.addData("Arm Profile Timer", armPower.timer.time());
    }
}
