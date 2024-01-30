package org.firstinspires.ftc.teamcode.autonomous_old.movements.actions;

import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSController;

public class ABPSAction extends Action {
    public final ABPSController.ABPSState state;
    public final boolean keepWristClosed;
    
    public ABPSAction(SidewaysDirection direction, boolean keepWristClosed) {
        this.keepWristClosed = keepWristClosed;
        
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
        if (keepWristClosed) op.abps.shouldOpenWristWhenDone = false;
        op.abps.activate(state, 0.5);
        
        while (!op.abps.isDone()) op.awaitFrame();
        
        if (keepWristClosed) op.abps.shouldOpenWristWhenDone = true;
        
        sleep(200);
    }
}
