package org.firstinspires.ftc.teamcode.modules.Inputs;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;

public class AdvancedInputManager extends InputManager {
    public AdvancedInputManager(MainOp op) {
        super(op);
    }
    
    public boolean lastLeftBumper = false;
    public boolean lastRightBumper = false;
    public boolean lastX = false;
    
    public boolean lastSlowModeCheck = false;
    
    public boolean isInSlowMode = false;
    
    public boolean lastEndgame = false;
    public EndgameStage endgameStage = EndgameStage.NONE;
    
    public enum EndgameStage {
        NONE,
        PREP,
        DRONE_RELEASE,
        DRONE_RETRACT,
        HANG,
        HANG_SERVO_EXTEND,
        HANG_SERVO_RETRACT
    }
    
    @Override
    public void op_start() {
        droneToggle = false;
        hangToggle = false;
        hangServoToggle = false;
    }
    
    @Override
    public void update() {
        Gamepad gamepad = op.gamepad;
        
        armIntakePosition = gamepad.b;
        armBackboardPosition = gamepad.a;
        // armOverheadPosition = gamepad.y;
        armSafePosition = gamepad.y;
        
        boolean leftHandState;
        if (gamepad.left_bumper) {
            leftHandState = lastLeftBumper ? leftHandClosed : leftHandOpen;
            lastLeftBumper = true;
        } else {
            leftHandState = leftHandClosed;
            lastLeftBumper = false;
        }
        
        boolean rightHandState;
        if (gamepad.right_bumper) {
            rightHandState = lastRightBumper ? rightHandClosed : rightHandOpen;
            lastRightBumper = true;
        } else {
            rightHandState = rightHandClosed;
            lastRightBumper = false;
        }
        
        if (gamepad.x) {
            if (!lastX) {
                if (rightHandState == leftHandState) {
                    rightHandState = !rightHandState;
                    leftHandState = !leftHandState;
                } else {
                    rightHandState = true;
                    leftHandState = true;
                }
            }
            
            lastX = true;
        } else {
            lastX = false;
        }
        
        leftHandClosed = leftHandState;
        leftHandOpen = !leftHandState;
        rightHandClosed = rightHandState;
        rightHandOpen = !rightHandState;
        
        forward = -gamepad.left_stick_y;
        strafe = gamepad.left_stick_x;
        turn = gamepad.right_stick_x * 0.85f;
        if (isInSlowMode) {
            forward *= 0.375f;
            strafe *= 0.225f;
            turn *= 0.65f;
        }
        
        // Overridden by wrist up/down
        // Role taken by DPad Left and Right
        // slowMoveUp = gamepad.dpad_up;
        // slowMoveDown = gamepad.dpad_down;
        // slowMoveLeft = gamepad.dpad_left;
        // slowMoveRight = gamepad.dpad_right;
        
        if (gamepad.dpad_left || gamepad.dpad_right) {
            if (!lastSlowModeCheck) {
                isInSlowMode = !isInSlowMode;
            }
            
            lastSlowModeCheck = true;
        } else {
            lastSlowModeCheck = false;
        }
        
        realignIMU = gamepad.start;
        realignIntake = gamepad.back;
        
        armUp = gamepad.right_trigger;
        armDown = gamepad.left_trigger;
        
        wristUp = gamepad.dpad_up;
        wristDown = gamepad.dpad_down;
        
        if (gamepad.guide) {
            if (!lastEndgame) {
                switch (endgameStage) {
                    case NONE: {
                        endgameStage = EndgameStage.PREP;
                        
                        break;
                    }
                    
                    case PREP: {
                        endgameStage = EndgameStage.DRONE_RELEASE;
                        
                        droneToggle = true;
                        
                        break;
                    }
                    
                    case DRONE_RELEASE: {
                        endgameStage = EndgameStage.DRONE_RETRACT;
                        
                        droneToggle = false;
                        
                        break;
                    }
                    
                    case DRONE_RETRACT: {
                        endgameStage = EndgameStage.HANG;
                        
                        hangToggle = true;
                        
                        break;
                    }
                    
                    case HANG: {
                        endgameStage = EndgameStage.HANG_SERVO_EXTEND;
                        
                        hangServoToggle = true;
                        hangToggle = true;
                        
                        break;
                    }
                    
                    case HANG_SERVO_EXTEND: {
                        endgameStage = EndgameStage.HANG_SERVO_RETRACT;
                        
                        hangServoToggle = false;
                        hangToggle = false;
                        
                        break;
                    }
                    
                    case HANG_SERVO_RETRACT: {
                        endgameStage = EndgameStage.HANG_SERVO_EXTEND;
                        
                        hangServoToggle = true;
                    }
                }
            }
            
            lastEndgame = true;
        } else {
            lastEndgame = false;
        }
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Endgame Stage", new Func<String>() {
            @Override
            public String value() {
                return endgameStage.name();
            }
        });
    }
}
