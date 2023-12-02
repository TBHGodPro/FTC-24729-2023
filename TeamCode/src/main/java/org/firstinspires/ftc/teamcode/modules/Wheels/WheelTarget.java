package org.firstinspires.ftc.teamcode.modules.Wheels;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WheelTarget {
    public int backLeft;
    public int backRight;
    public int frontLeft;
    public int frontRight;
    public int targetTime;
    public ElapsedTime currentTime;
    
    public WheelTarget(int backLeft, int backRight, int frontLeft, int frontRight, int targetTime) {
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.targetTime = targetTime;
        this.currentTime = new ElapsedTime();
    }
    
    public void resetTimer() {
        this.currentTime.reset();
    }
}
