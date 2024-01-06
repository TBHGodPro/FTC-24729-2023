public class MovementHandler {
  public static void main(String[] args) {
  }
}

package org.firstinspires.ftc.teamcode.autonomous.movements;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;

public class MovementHandler {
    public final AutoOp op;
    
    public final Action[] actions;
    
    public int nextIndex;
    
    public MovementHandler(AutoOp op, Action[] actions) {
        this.op = op;
        
        this.actions = actions;
        
        this.nextIndex = 0;
    }
    
    public boolean isDone() {
        return nextIndex >= actions.length;
    }
    
    public void runNext() {
        Action action = actions[nextIndex];
        nextIndex += 1;
        
        action.execute(op);
    }
}
