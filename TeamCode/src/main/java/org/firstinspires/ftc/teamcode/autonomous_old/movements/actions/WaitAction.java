package org.firstinspires.ftc.teamcode.autonomous_old.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;

public class WaitAction extends Action {
    public final long timeMS;
    
    public WaitAction(long timeMS) {
        this.timeMS = timeMS;
    }
    
    @Override
    public void execute(AutoOp op) {
        try {
            Thread.sleep(timeMS);
        } catch (Exception e) {
        }
    }
}
