package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.MainOp;

public abstract class BaseModule {
    public final MainOp op;
    
    public BaseModule(MainOp op) {
        this.op = op;
    }
    
    public void setupTelemetry(Telemetry telemetry) {
        telemetry.addLine("--- " + this.getClass().getName().replace("Controller", "")
                .replace("org.firstinspires.ftc.teamcode.modules.", "") + " ---");
        telemetry.addLine();
        
        addTelemetry(telemetry);
        
        telemetry.addLine();
    }
    
    public void init() {
    }
    
    public void init_loop() {
    }
    
    public void start() {
    }
    
    public void loop() {
    }
    
    public void addTelemetry(Telemetry telemetry) {
    }
    
    public void getDashboardTelemetry(Telemetry telemetry) {
    }
}
