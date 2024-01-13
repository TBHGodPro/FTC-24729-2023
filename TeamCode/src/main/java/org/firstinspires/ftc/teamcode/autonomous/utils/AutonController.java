package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;

public class AutonController extends Thread {
    public final AutoOp op;
    public final Gamepad gamepad;
    
    public AutonController(AutoOp op, Gamepad gamepad) {
        this.op = op;
        this.gamepad = gamepad;
    }
    
    public void init() {
        op.isAutonomous = true;
        
        op.arm.isHandLeftClosed = true;
        op.arm.isHandRightClosed = true;
    }
    
    public void run() {
        op.runOP();
        
        op.arm.armPos = 50;
        op.arm.wristPos = 2;
        op.arm.isHandLeftClosed = false;
        op.arm.isHandRightClosed = false;
    }
}
