package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmPosition;

public class ArmPositionAction extends Action {
    public final ArmPosition position;
    
    public ArmPositionAction(ArmPosition position) {
        this.position = position;
    }
    
    @Override
    public void execute(AutoOp op) {
        op.arm.goToPosition(position);
        
        op.awaitFrame();
        
        while (!op.arm.isAtPosition()) op.awaitFrame();
    }
}
