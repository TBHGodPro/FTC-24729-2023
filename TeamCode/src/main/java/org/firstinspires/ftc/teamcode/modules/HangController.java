package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmController;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;

public class HangController extends BaseModule {
    public final MovementController movements;
    public final ArmController arm;
    public final InputManager inputs;
    
    public boolean lastHangToggle = false;
    public boolean active = false;
    
    public HangController(MainOp op, InputManager inputs) {
        super(op);
        
        movements = op.movements;
        arm = op.arm;
        this.inputs = inputs;
    }
    
    @Override
    public void loop() {
        if (inputs.hangToggle) {
            lastHangToggle = true;
        } else if (lastHangToggle) {
            lastHangToggle = false;
            
            active = !active;
            
            if (active) {
                arm.setArmStaticPower(-0.4);
                movements.deactivate();
            } else {
                arm.setArmStaticPower(0);
                movements.activate();
            }
        } else {
            lastHangToggle = false;
        }
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
}
