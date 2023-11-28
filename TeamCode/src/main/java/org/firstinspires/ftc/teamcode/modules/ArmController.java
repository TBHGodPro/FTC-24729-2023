package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ArmController extends Module {
    // --- Constants ---

    public static final double armNonLinearity = 2; // 1 = linear

    public static final int armManualInterval = 20;

    public static final int armMaxPos = 1960;

    public static final int armSpeed = 1200;

    public static final double wristPosInterval = 0.0045;

    public static final double wristMinRange = 0.25;
    public static final double wristMaxRange = 1;

    public static final double wristAngleCorrectionCoeff = 2500;

    public static final double handOpenPos = 0.65;
    public static final double handClosedPos = 1;

    // - Intake Position
    public final int armIntakePos;
    public static final int armIntakePosManual = 175;
    public static final int armIntakePosAutonomous = 315;

    public static final double wristIntakePos = 0.52;

    public final boolean shouldOpenHandAtIntake;

    // - Backboard Position
    public final int armBackboardPos;
    public static final int armBackboardPosManual = 480;
    public static final int armBackboardPosAutonomous = 625;

    public static final double wristBackboardPos = 0.89;

    // - Overhead Position
    public static final int armOverheadPos = 1950;

    public static final double wristOverheadPos = 1.8;

    // -----------------

    public final boolean isAutonomous;

    public final Gamepad gamepad;

    public final DcMotorEx arm;
    public static final DcMotorEx.Direction armDirection = DcMotorEx.Direction.REVERSE;
    public static final ZeroPowerBehavior armZeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    public int armPos = 0;

    public final Servo wrist;
    public static final Servo.Direction wristDirection = Servo.Direction.REVERSE;
    public double wristPos = 1;

    public final Servo hand;
    public static final Servo.Direction handDirection = Servo.Direction.FORWARD;
    public boolean isHandClosed = false;

    public ArmController(boolean isAutonomous, Gamepad gamepad, DcMotorEx arm, Servo wrist, Servo hand) {
        this.isAutonomous = isAutonomous;

        this.gamepad = gamepad;

        this.arm = arm;
        this.wrist = wrist;
        this.hand = hand;

        shouldOpenHandAtIntake = !isAutonomous;
        this.armIntakePos = isAutonomous ? armIntakePosAutonomous : armIntakePosManual;
        this.armBackboardPos = isAutonomous ? armBackboardPosAutonomous : armBackboardPosManual;
    }

    public void init() {
        arm.setDirection(armDirection);
        arm.setZeroPowerBehavior(armZeroPowerBehavior);
        arm.setMode(RunMode.STOP_AND_RESET_ENCODER);

        wrist.setDirection(wristDirection);
        wrist.scaleRange(wristMinRange, wristMaxRange);

        hand.setDirection(handDirection);
    }

    public void prep() {
        resetZeroPosition();
    }

    public void update() {
        // Arm Control
        double armPower = 0;

        armPower += Math.pow(gamepad.right_trigger, armNonLinearity);
        armPower -= Math.pow(gamepad.left_trigger, armNonLinearity);

        int nextPos = (int) (armPos + armPower * armManualInterval);
        if (nextPos <= armMaxPos) {
            armPos = nextPos;
        }

        arm.setTargetPosition(armPos);

        // Wrist Control
        if (gamepad.right_bumper) {
            wristPos += wristPosInterval;
        }
        if (gamepad.left_bumper) {
            wristPos -= wristPosInterval;
        }

        wristPos = Math.max(Math.min(wristPos, 2), -1);

        wrist.setPosition(Math.max(Math.min(wristPos - (armPos / wristAngleCorrectionCoeff), 1), 0));

        // Hand Control
        if (gamepad.x) {
            isHandClosed = true;
        }
        if (gamepad.y) {
            isHandClosed = false;
        }

        hand.setPosition(isHandClosed ? handClosedPos : handOpenPos);

        // Intake Position
        if (gamepad.b) {
            goToIntakePosition();
        }

        // Backboard Position
        if (gamepad.a) {
            gotToBackboardPosition();
        }

        // Overhead Position
        if (gamepad.guide) {
            goToOverheadPosition();
        }

        // Reset Arm
        if (gamepad.back) {
            resetZeroPosition();
        }
    }

    public void resetZeroPosition() {
        arm.setMode(RunMode.STOP_AND_RESET_ENCODER);

        armPos = 0;

        arm.setTargetPosition(armPos);

        arm.setMode(RunMode.RUN_TO_POSITION);

        arm.setVelocity(armSpeed);
    }

    public void goToIntakePosition() {
        armPos = armIntakePos;
        wristPos = wristIntakePos;
        if (shouldOpenHandAtIntake) {
            isHandClosed = false;
        }
    }

    public void gotToBackboardPosition() {
        armPos = armBackboardPos;
        wristPos = wristBackboardPos;
    }

    public void goToOverheadPosition() {
        armPos = armOverheadPos;
        wristPos = wristOverheadPos;
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
}
