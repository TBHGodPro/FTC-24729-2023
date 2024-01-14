package org.firstinspires.ftc.teamcode.modules.Inputs;

import com.qualcomm.robotcore.hardware.Gamepad;

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
    
    @Override
    public void update() {
        Gamepad gamepad = op.gamepad;
        
        armIntakePosition = gamepad.b;
        armBackboardPosition = gamepad.a;
        armOverheadPosition = gamepad.y;
        
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
    }
}
