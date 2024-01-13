package org.firstinspires.ftc.teamcode.modules.Inputs;

import com.qualcomm.robotcore.hardware.Gamepad;

public class AdvancedInputManager extends InputManager {
    public boolean lastLeftBumper = false;
    public boolean lastRightBumper = false;
    public boolean lastX = false;
    
    @Override
    public void update(Gamepad gamepad) {
        armIntakePosition = gamepad.b;
        armBackboardPosition = gamepad.a;
        armOverheadPosition = gamepad.y;
        
        boolean leftHandState;
        if (gamepad.left_bumper) {
            leftHandState = lastLeftBumper == false ? leftHandOpen : leftHandClosed;
            lastLeftBumper = true;
        } else {
            leftHandState = leftHandClosed;
            lastLeftBumper = false;
        }
        
        boolean rightHandState;
        if (gamepad.right_bumper) {
            rightHandState = lastRightBumper == false ? rightHandOpen : rightHandClosed;
            lastRightBumper = true;
        } else {
            rightHandState = rightHandClosed;
            lastRightBumper = false;
        }
        
        if (gamepad.x) {
            if (lastX == false) {
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
        turn = gamepad.right_stick_x;
        
        // slowMoveUp = gamepad.dpad_up;
        // slowMoveDown = gamepad.dpad_down;
        slowMoveLeft = gamepad.dpad_left;
        slowMoveRight = gamepad.dpad_right;
        
        realignIMU = gamepad.start;
        realignIntake = gamepad.back;
        
        armUp = gamepad.right_trigger;
        armDown = gamepad.left_trigger;
        
        wristUp = gamepad.dpad_up;
        wristDown = gamepad.dpad_down;
    }
}
