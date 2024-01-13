package org.firstinspires.ftc.teamcode.modules.Arm;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Utils.AngleUtils;
import org.firstinspires.ftc.teamcode.Utils.Profile.AsymmetricMotionProfile;
import org.firstinspires.ftc.teamcode.Utils.Profile.ProfileConstraints;
import org.firstinspires.ftc.teamcode.Utils.Profile.ProfileState;

@Config
public class ArmPowerController {
    // --- Constants ---
    
    public static double kP = 0.015;
    public static double kI = 0.05;
    public static double kD = 0.0002;
    public static double kF = 0.1;
    
    public static double velo = 2500;
    public static double accel = 2500;
    public static double decel = 1200;
    
    // -----------------
    
    public final PIDController pid;
    
    public AsymmetricMotionProfile profile;
    public ElapsedTime timer;
    
    public Integer overallTarget;
    public Double target;
    
    public int current;
    
    public ArmPowerController(int position) {
        pid = new PIDController(kP, kI, kD);
        
        current = position;
        
        timer = new ElapsedTime();
        
        clearTarget();
    }
    
    public void setTarget(int current, int target) {
        this.current = current;
        if (this.target != null && this.overallTarget == target) return;
        
        this.target = (double) target;
        this.overallTarget = target;
        
        profile = new AsymmetricMotionProfile(current, target, new ProfileConstraints(velo, accel, decel));
        timer.reset();
    }
    
    public void clearTarget() {
        this.target = null;
        this.overallTarget = null;
    }
    
    public double calc(int current) {
        this.current = current;
        if (this.target == null || this.overallTarget == null) return 0;
        
        pid.setPID(kP, kI, kD);
        
        ProfileState state = profile.calculate(timer.time());
        target = state.x;
        
        double power = 0;
        
        power += pid.calculate(current, target);
        
        power += getFeedForward(current);
        
        return power;
    }
    
    public double getFeedForward(double pos) {
        return Math.sin(convertPosToRadians(pos)) * kF;
    }
    
    public double convertPosToRadians(double pos) {
        return AngleUtils.toRadians((pos + 500d) / (100d / 9d));
    }
}
