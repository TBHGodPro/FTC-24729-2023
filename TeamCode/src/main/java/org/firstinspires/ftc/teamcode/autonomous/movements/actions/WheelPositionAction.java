package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

public class WheelPositionAction extends Action {
    public final WheelTarget target;
    
    public WheelPositionAction(WheelTarget target) {
        this.target = target;
    }
    
    @Override
    public void execute(AutoOp op) {
        op.wheels.setTarget(target);
        
        while (op.wheels.target != null) {
            op.awaitFrame();
        }
    }
}
