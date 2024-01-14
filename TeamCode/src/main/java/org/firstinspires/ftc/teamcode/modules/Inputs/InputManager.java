package org.firstinspires.ftc.teamcode.modules.Inputs;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.BaseModule;

public abstract class InputManager extends BaseModule {
    public InputManager(MainOp op) {
        super(op);
    }
    
    // Inputs
    public boolean armIntakePosition = false;
    public boolean armBackboardPosition = false;
    public boolean armOverheadPosition = false;
    
    public boolean leftHandClosed = false;
    public boolean rightHandClosed = false;
    
    public boolean leftHandOpen = false;
    public boolean rightHandOpen = false;
    
    public float forward = 0;
    public float strafe = 0;
    public float turn = 0;
    
    public boolean slowMoveUp = false;
    public boolean slowMoveDown = false;
    public boolean slowMoveLeft = false;
    public boolean slowMoveRight = false;
    
    public boolean realignIMU = false;
    public boolean realignIntake = false;
    
    public float armUp = 0;
    public float armDown = 0;
    
    public boolean wristUp = false;
    public boolean wristDown = false;
    
    public boolean ABPSLeft = false;
    public boolean ABPSRight = false;
    
    // Update Inputs
    public abstract void update();
    
    // Thread
    @Override
    public void loop() {
        update();
    }
}
