package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSController;

public class ABPSAction extends Action {
    public final ABPSController.ABPSState state;
    
    public ABPSAction(SidewaysDirection direction) {
        switch (direction) {
            case LEFT: {
                state = ABPSController.ABPSState.LEFT;
                break;
            }
            
            case RIGHT: {
                state = ABPSController.ABPSState.RIGHT;
                break;
            }
            
            default: {
                state = ABPSController.ABPSState.STOPPED;
                break;
            }
        }
    }
    
    @Override
    public void execute(AutoOp op) {
        op.abps.activate(state, 0);
        
        while (!op.abps.isDone()) op.awaitFrame();
        
        sleep(200);
    }
}
