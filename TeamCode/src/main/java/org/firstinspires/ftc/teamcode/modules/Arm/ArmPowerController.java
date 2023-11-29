package org.firstinspires.ftc.teamcode.modules.Arm;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;

@Config
public class ArmPowerController {
    // --- Constants ---
    
    public static double kP = 0.002;
    public static double kI = 0.001;
    public static double kD = 0.00003;
    
    // -----------------
    
    public final PIDController pid;
    
    public ArmPowerController() {
        pid = new PIDController(kP, kI, kD);
    }
    
    public double calc(int current, int target) {
        pid.setPID(kP, kI, kD);
        
        double power = 0;
        
        power += pid.calculate(current, target);
        
        return power;
    }
}
