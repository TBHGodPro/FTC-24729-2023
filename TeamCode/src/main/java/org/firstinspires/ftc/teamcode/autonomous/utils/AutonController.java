package org.firstinspires.ftc.teamcode.autonomous.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.AutoOp;

public class AutonController extends Thread {
    public final AutoOp op;
    public final Gamepad gamepad;
    
    public AutonController(AutoOp op, Gamepad gamepad) {
        this.op = op;
        this.gamepad = gamepad;
    }
    
    public void init() {
        op.isAutonomous = true;
        
        op.arm.isHandClosed = true;
    }
    
    public void run() {
        op.runOP();
        
        op.arm.armPos = 0;
        op.arm.wristPos = 2;
        op.arm.isHandClosed = false;
    }
}
