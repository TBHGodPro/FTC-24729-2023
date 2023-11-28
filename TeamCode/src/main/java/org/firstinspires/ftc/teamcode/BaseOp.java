package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class BaseOp extends OpMode {
    public long frames = 0;

    /**
     * Pause execution of the OpMode for the indicated number of milliseconds
     * 
     * @param milliseconds
     *
     *                     Note: during this pause, the motors will continue running
     *                     at their previous power settings.
     */
    protected void sleep(long milliseconds) {
        if (Thread.currentThread().isInterrupted())
            return;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException exc) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Pause execution of the thread until one frame (loop) has been completed
     */
    public void awaitFrame() {
        long currentFrames = frames;

        while (frames <= currentFrames + 1) {
            sleep(5);
        }
    }
}
