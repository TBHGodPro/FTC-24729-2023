package org.firstinspires.ftc.teamcode.modules.ABPS;

// 15 = 0.6
// 20 = 0.115
// 25 = 0.08
// 30 = 0.062

import com.acmerobotics.dashboard.config.Config;

@Config
public class ABPSMath {
    // --- Constants ---
    
    public static double A = 30_000;
    public static double B = -4;
    
    // -----------------
    
    public static double calc(double range) {
        return A * Math.pow(range, B);
    }
}
