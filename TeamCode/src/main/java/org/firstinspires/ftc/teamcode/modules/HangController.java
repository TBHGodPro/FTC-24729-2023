package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmController;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;

@Config
public class HangController extends BaseModule {
    // --- Constants ---
    
    public static double holdPitch = 43;
    public static double holdAccuracy = 6;
    
    public static double liftPower = 1.2;
    public static double lowerPower = 0.3;
    public static double holdPower = 0.5;
    
    public static double servoHeld = 0.4;
    public static double servoExtended = 1;
    
    // -----------------
    
    public final MovementController movements;
    public final ArmController arm;
    public final InputManager inputs;
    
    public Servo servo;
    
    public boolean active = false;
    public boolean isServoExtended = false;
    
    public double basePitch;
    
    public HangController(MainOp op, InputManager inputs, Servo hangServo) {
        super(op);
        
        movements = op.movements;
        arm = op.arm;
        this.inputs = inputs;
        
        this.servo = hangServo;
    }
    
    @Override
    public void loop() {
        if (active != inputs.hangToggle) {
            active = inputs.hangToggle;
            
            if (active) {
                basePitch = movements.imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
                
                movements.deactivate();
                arm.wristPos = 100;
            } else {
                arm.setArmStaticPower(0);
            }
        }
        
        if (active) {
            movements.wheelController.setPowers(0, 0, 0, 0);
            
            double pitch = movements.imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES);
            
            double realPitch = pitch - basePitch;
            
            if (Math.round(realPitch / holdAccuracy) == Math.round(holdPitch / holdAccuracy))
                arm.setArmStaticPower(-holdPower);
            else if (realPitch < holdPitch) arm.setArmStaticPower(-liftPower);
            else if (realPitch > holdPitch) arm.setArmStaticPower(-lowerPower);
            else arm.setArmStaticPower(-holdPower);
        }
        
        if (isServoExtended != inputs.hangServoToggle) {
            isServoExtended = inputs.hangServoToggle;
        }
        
        servo.setPosition(isServoExtended ? servoExtended : servoHeld);
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Active", new Func<String>() {
            @Override
            public String value() {
                return active + "";
            }
        });
    }
    
    @Override
    public void updateDashboardTelemetry() {
        telemetry.addData("Pitch", movements.imu.getRobotYawPitchRollAngles().getPitch(AngleUnit.DEGREES));
    }
}
