package org.firstinspires.ftc.teamcode.modules.Wheels;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Utils;
import org.firstinspires.ftc.teamcode.modules.BaseModule;

@Config
public class WheelController extends BaseModule {
    // --- Constants ---
    
    public static int setPositionClearance = 5;
    public static int setPositionTimeLeewayMS = 200;
    
    // -----------------
    
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;
    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    
    public static final DcMotorEx.Direction leftDirection = DcMotorEx.Direction.REVERSE;
    public static final DcMotorEx.Direction rightDirection = DcMotorEx.Direction.FORWARD;
    
    public static final ZeroPowerBehavior zeroPowerBehavior = ZeroPowerBehavior.BRAKE;
    
    public final WheelPowers powers;
    
    public WheelTarget target = null;
    
    public WheelController(MainOp op, DcMotorEx backLeft, DcMotorEx backRight, DcMotorEx frontLeft,
                           DcMotorEx frontRight) {
        super(op);
        
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        
        powers = new WheelPowers(0, 0, 0, 0);
    }
    
    @Override
    public void init() {
        backLeft.setDirection(leftDirection);
        frontLeft.setDirection(leftDirection);
        
        backRight.setDirection(rightDirection);
        frontRight.setDirection(rightDirection);
        
        backLeft.setZeroPowerBehavior(zeroPowerBehavior);
        backRight.setZeroPowerBehavior(zeroPowerBehavior);
        frontLeft.setZeroPowerBehavior(zeroPowerBehavior);
        frontRight.setZeroPowerBehavior(zeroPowerBehavior);
        
        resetZeroPositions();
    }
    
    @Override
    public void loop() {
        if (target == null) {
            setRunMode(RunMode.RUN_WITHOUT_ENCODER);
            
            // Normalize Powers
            double maxOneTwo = Math.max(powers.backLeft, powers.backRight);
            double maxOneTwoThree = Math.max(maxOneTwo, powers.frontLeft);
            double maxAll = Math.max(maxOneTwoThree, powers.frontRight);
            
            double activeBackLeft = powers.backLeft;
            double activeBackRight = powers.backRight;
            double activeFrontLeft = powers.frontLeft;
            double activeFrontRight = powers.frontRight;
            
            if (maxAll > 1) {
                activeBackLeft /= maxAll;
                activeBackRight /= maxAll;
                activeFrontLeft /= maxAll;
                activeFrontRight /= maxAll;
            }
            
            backLeft.setPower(activeBackLeft);
            backRight.setPower(activeBackRight);
            frontLeft.setPower(activeFrontLeft);
            frontRight.setPower(activeFrontRight);
        } else {
            if ((isAtTarget(backLeft.getCurrentPosition(), target.backLeft)
                    && isAtTarget(backRight.getCurrentPosition(), target.backRight)
                    && isAtTarget(frontLeft.getCurrentPosition(), target.frontLeft)
                    && isAtTarget(frontRight.getCurrentPosition(), target.frontRight))
                    || (target.currentTime.milliseconds() >= (target.targetTime + setPositionTimeLeewayMS))) {
                resetZeroPositions();
                
                target = null;
                
                setRunMode(RunMode.RUN_WITHOUT_ENCODER);
            } else if (backLeft.getMode() != RunMode.RUN_TO_POSITION) {
                backLeft.setTargetPosition(target.backLeft);
                backRight.setTargetPosition(target.backRight);
                frontLeft.setTargetPosition(target.frontLeft);
                frontRight.setTargetPosition(target.frontRight);
                
                setRunMode(RunMode.RUN_TO_POSITION);
                
                backLeft.setVelocity((double) Math.abs(target.backLeft) / target.targetTime * 1000);
                backRight.setVelocity((double) Math.abs(target.backRight) / target.targetTime * 1000);
                frontLeft.setVelocity((double) Math.abs(target.frontLeft) / target.targetTime * 1000);
                frontRight.setVelocity((double) Math.abs(target.frontRight) / target.targetTime * 1000);
            }
        }
    }
    
    public void setTarget(WheelTarget target) {
        resetZeroPositions();
        
        if (target != null)
            target.resetTimer();
        
        this.target = target;
    }
    
    public void setPowers(double backLeft, double backRight, double frontLeft, double frontRight) {
        powers.set(
                backLeft,
                backRight,
                frontLeft,
                frontRight);
    }
    
    public boolean isAtTarget(int current, int target) {
        return Math.round(target / setPositionClearance) == Math.round(current / setPositionClearance);
    }
    
    public void setRunMode(RunMode mode) {
        if (backLeft.getMode() == mode)
            return;
        
        backLeft.setMode(mode);
        backRight.setMode(mode);
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
    }
    
    public void resetZeroPositions() {
        RunMode mode = backLeft.getMode();
        
        setRunMode(RunMode.STOP_AND_RESET_ENCODER);
        
        setRunMode(mode);
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Target", new Func<String>() {
                    @Override
                    public String value() {
                        return target == null ? "NONE"
                                : target.backLeft + ", " + target.backRight + ", " + target.frontLeft + ", " + target.frontRight
                                + " in " + target.currentTime.milliseconds() + "/" + target.targetTime;
                    }
                })
                .addData("Current", new Func<String>() {
                    @Override
                    public String value() {
                        return backLeft == null ? "0 0 0 0" : backLeft.getCurrentPosition() + " " + backRight.getCurrentPosition() + " " + frontLeft.getCurrentPosition() + " " + frontRight.getCurrentPosition();
                    }
                })
                .addData("Powers", new Func<String>() {
                    @Override
                    public String value() {
                        return Utils.round(powers.backLeft) + ", " + Utils.round(powers.backRight) + ", "
                                + Utils.round(powers.frontLeft) + ", "
                                + Utils.round(powers.frontRight);
                    }
                });
    }
}