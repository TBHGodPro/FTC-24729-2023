package org.firstinspires.ftc.teamcode.modules.Wheels;

public class WheelPowers {
    public double backLeft;
    public double backRight;
    public double frontLeft;
    public double frontRight;
    
    public WheelPowers(double backLeft, double backRight, double frontLeft, double frontRight) {
        this.set(backLeft, backRight, frontLeft, frontRight);
    }
    
    public void set(double backLeft, double backRight, double frontLeft, double frontRight) {
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
    }
}
