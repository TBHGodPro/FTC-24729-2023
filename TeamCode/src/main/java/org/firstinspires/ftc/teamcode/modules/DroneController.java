package org.firstinspires.ftc.teamcode.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;

@Config
public class DroneController extends BaseModule {
    // --- Constants ---
    
    public static double holdPosition = 0;
    public static double releasePosition = 0.4;
    
    // -----------------
    
    public final InputManager inputs;
    
    public final Servo servo;
    
    public boolean released = false;
    
    public DroneController(MainOp op, InputManager inputs, Servo droneServo) {
        super(op);
        
        this.inputs = inputs;
        
        this.servo = droneServo;
    }
    
    @Override
    public void init() {
        servo.setPosition(holdPosition);
        
        released = false;
    }
    
    @Override
    public void loop() {
        released = inputs.droneToggle;
        
        servo.setPosition(released ? releasePosition : holdPosition);
    }
    
    @Override
    public void addTelemetry(Telemetry telemetry) {
        telemetry.addData("Released", new Func<String>() {
            @Override
            public String value() {
                return released + "";
            }
        });
    }
}
