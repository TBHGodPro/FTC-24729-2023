package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;

@Autonomous(name = "Float Motors", group = "Tuning")
public class FloatMotors extends AutoOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
    
    @Override
    public void runOP() {
        arm.armLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        arm.armRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        
        wheels.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheels.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheels.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        wheels.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}
