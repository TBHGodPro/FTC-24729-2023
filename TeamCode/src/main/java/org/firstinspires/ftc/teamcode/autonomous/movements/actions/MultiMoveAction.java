package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

public class MultiMoveAction extends Action {
    public final WheelPositionAction action;
    
    public MultiMoveAction(int forward, int strafe, int timeMS) {
        int backLeftPos = forward - strafe;
        int backRightPos = forward + strafe;
        int frontLeftPos = forward + strafe;
        int frontRightPos = forward - strafe;
        
        action = new WheelPositionAction(new WheelTarget(backLeftPos, backRightPos, frontLeftPos, frontRightPos, timeMS));
    }
    
    @Override
    public void execute(AutoOp op) {
        action.execute(op);
    }
}
