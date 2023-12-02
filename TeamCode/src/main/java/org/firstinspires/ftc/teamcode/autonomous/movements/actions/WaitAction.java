package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;

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
