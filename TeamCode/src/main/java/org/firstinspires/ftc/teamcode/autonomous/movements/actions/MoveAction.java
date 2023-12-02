package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

public class MoveAction extends Action {
    public final WheelPositionAction action;
    
    public MoveAction(Direction direction, int steps, int timeMS) {
        WheelTarget target = new WheelTarget(0, 0, 0, 0, 0);
        
        switch (direction) {
            case FORWARD: {
                //target = new WheelTarget(steps, (int) (steps + steps * 0.055), steps, (int) (steps - steps * 0.055), timeMS);
                target = new WheelTarget(steps, steps, steps, steps, timeMS);
                
                break;
            }
            
            case BACKWARD: {
                target = new WheelTarget(-steps, -steps, -steps, -steps, timeMS);
                
                break;
            }
            
            case LEFT: {
                target = new WheelTarget(steps, -steps, -steps, steps, timeMS);
                
                break;
            }
            
            case RIGHT: {
                target = new WheelTarget(-steps, steps, steps, -steps, timeMS);
                
                break;
            }
        }
        
        action = new WheelPositionAction(target);
    }
    
    @Override
    public void execute(AutoOp op) {
        action.execute(op);
    }
}
