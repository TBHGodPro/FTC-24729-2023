package org.firstinspires.ftc.teamcode.modules.Arm;

import com.arcrobotics.ftclib.controller.PIDController;

public class ArmPIDF {
    // --- Constants ---
    
    public double kP = 0.02;
    public double kI = 0.0;
    public double kD = 0.0;
    
    // -----------------
    
    public PIDController pid;
    
    public ArmPIDF() {
        pid = new PIDController(kP, kI, kD);
    }
    
    public double calc(int current, int target) {
        double power = 0;
        
        power += pid.calculate(current, target);
        
        return power;
    }
}
