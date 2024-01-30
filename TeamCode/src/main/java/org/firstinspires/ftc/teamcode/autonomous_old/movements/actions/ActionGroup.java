package org.firstinspires.ftc.teamcode.autonomous_old.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;

public class ActionGroup extends Action {
    public final Action[] actions;
    
    public ActionGroup(Action[] actions) {
        this.actions = actions;
    }
    
    @Override
    public void execute(AutoOp op) {
        int nextAction = 0;
        
        while (nextAction < actions.length) {
            actions[nextAction].execute(op);
            
            nextAction += 1;
        }
    }
}
