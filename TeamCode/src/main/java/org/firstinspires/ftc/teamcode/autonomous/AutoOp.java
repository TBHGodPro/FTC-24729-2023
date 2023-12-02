package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.autonomous.utils.AutonController;
import org.firstinspires.ftc.teamcode.modules.Camera.AutonomousCameraManager;

public abstract class AutoOp extends MainOp {
    public final AutonController controller;
    
    public AutoOp() {
        super();
        
        isAutonomous = true;
        
        gamepad = new Gamepad();
        
        controller = new AutonController(this, gamepad);
    }
    
    public AutonomousCameraManager getCamera() {
        return (AutonomousCameraManager) camera;
    }
    
    @Override
    public void init() {
        super.init();
        
        controller.init();
    }
    
    @Override
    public void start() {
        controller.start();
        
        super.start();
    }
    
    public abstract void runOP();
}
