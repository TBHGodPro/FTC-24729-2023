package org.firstinspires.ftc.teamcode.modules.Inputs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.MainOp;

@Config
public class SimpleInputManager extends InputManager {
    // --- Constants ---
    
    public static double wheelMaxPower = 0.75;
    
    // -----------------
    
    public final boolean shouldOpenHandAtIntake;
    
    public SimpleInputManager(MainOp op, boolean shouldOpenHandAtIntake) {
        super(op);
        
        this.shouldOpenHandAtIntake = shouldOpenHandAtIntake;
    }
    
    @Override
    public void update() {
        Gamepad gamepad = op.gamepad;
        
        armIntakePosition = gamepad.b;
        armBackboardPosition = gamepad.a;
        armOverheadPosition = gamepad.guide;
        
        leftHandClosed = gamepad.x;
        rightHandClosed = gamepad.x;
        
        leftHandOpen = gamepad.y;
        rightHandOpen = gamepad.y;
        if (shouldOpenHandAtIntake && armIntakePosition) {
            leftHandOpen = true;
            rightHandOpen = true;
        }
        
        forward = -gamepad.left_stick_y * (float) (wheelMaxPower);
        strafe = gamepad.left_stick_x * (float) (wheelMaxPower);
        turn = gamepad.right_stick_x * (float) (wheelMaxPower);
        
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
