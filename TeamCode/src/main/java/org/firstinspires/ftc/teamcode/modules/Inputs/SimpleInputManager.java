package org.firstinspires.ftc.teamcode.modules.Inputs;

import com.qualcomm.robotcore.hardware.Gamepad;

public class SimpleInputManager extends InputManager {
    @Override
    public void update(Gamepad gamepad) {
        armIntakePosition = gamepad.b;
        armBackboardPosition = gamepad.a;
        armOverheadPosition = gamepad.guide;
        
        leftHandClosed = gamepad.x;
        rightHandClosed = gamepad.x;
        
        leftHandOpen = gamepad.y;
        rightHandOpen = gamepad.y;
        
        forward = -gamepad.left_stick_y;
        strafe = gamepad.left_stick_x;
        turn = gamepad.right_stick_x;
        
        slowMoveUp = gamepad.dpad_up;
        slowMoveDown = gamepad.dpad_down;
        slowMoveLeft = gamepad.dpad_left;
        slowMoveRight = gamepad.dpad_right;
        
        realignIMU = gamepad.start;
        realignIntake = gamepad.back;
        
        armUp = gamepad.right_trigger;
        armDown = gamepad.left_trigger;
        
        wristUp = gamepad.right_bumper;
        wristDown = gamepad.left_bumper;
    }
}
