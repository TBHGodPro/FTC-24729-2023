package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous.AutoOp;

public abstract class Action {
    public abstract void execute(AutoOp op);
    
    protected void sleep(long milliseconds) {
        if (Thread.currentThread().isInterrupted())
            return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }
}
