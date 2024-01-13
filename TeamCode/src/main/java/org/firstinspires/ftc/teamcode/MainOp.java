package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.modules.ABPS.ABPSController;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmController;
import org.firstinspires.ftc.teamcode.modules.BaseModule;
import org.firstinspires.ftc.teamcode.modules.Camera.AutonomousCameraManager;
import org.firstinspires.ftc.teamcode.modules.Camera.BaseCameraManager;
import org.firstinspires.ftc.teamcode.modules.Camera.ManualCameraManager;
import org.firstinspires.ftc.teamcode.modules.MovementController;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelController;

import java.util.ArrayList;
import java.util.List;

public abstract class MainOp extends BaseOp {
    public Boolean isAutonomous;
    
    public FtcDashboard dashboard;
    public Telemetry dashboardTelemetry;
    
    public List<LynxModule> hubs;
    
    public Gamepad gamepad;
    
    public ArrayList<BaseModule> modules = new ArrayList<BaseModule>();
    
    public BaseCameraManager camera;
    
    public WheelController wheels;
    public MovementController movements;
    public ArmController arm;
    public ABPSController abps;
    
    public abstract Alliance getAlliance();
    
    // Run once INIT is pressed
    public void init() {
        // Get Dashboard
        // http://192.168.43.1:8080/dash
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        
        // Check for Autonomous
        if (isAutonomous == null) {
            isAutonomous = false;
        }
        
        // Allow gamepad overriding (by autonomous)
        if (gamepad == null) {
            gamepad = gamepad1;
        }
        
        // Camera
        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = isAutonomous ? new AutonomousCameraManager(webcam, getAlliance()) : new ManualCameraManager(webcam);
        camera.init();
        
        // Wheels
        wheels = new WheelController(this, hardwareMap.get(DcMotorEx.class, "back_left"), hardwareMap.get(DcMotorEx.class, "back_right"), hardwareMap.get(DcMotorEx.class, "front_left"), hardwareMap.get(DcMotorEx.class, "front_right"));
        
        // Movements
        movements = new MovementController(this, wheels, hardwareMap.get(IMU.class, "imu"), gamepad, !shouldUseABPS(), /*!isAutonomous*/ true);
        
        // Arm & Wrist & Hand
        arm = new ArmController(this, isAutonomous, gamepad, hardwareMap.get(DcMotorEx.class, "arm"), hardwareMap.get(Servo.class, "wrist_left"), hardwareMap.get(Servo.class, "wrist_right"), hardwareMap.get(Servo.class, "claw_left"), hardwareMap.get(Servo.class, "claw_right"));
        
        // ABPS
        abps = new ABPSController(this, shouldUseABPS());
        
        // Telemetry
        telemetry.addLine("--- Bot ---");
        telemetry.addLine();
        telemetry.addData("FPS", new Func<String>() {
            @Override
            public String value() {
                return (frames / getRuntime()) + "/s";
            }
        });
        telemetry.addLine();
        
        movements.setupTelemetry(telemetry);
        wheels.setupTelemetry(telemetry);
        arm.setupTelemetry(telemetry);
        abps.setupTelemetry(telemetry);
        
        // Store Modules
        modules.add(movements);
        modules.add(wheels);
        modules.add(arm);
        modules.add(abps);
        
        // Init Modules
        for (BaseModule module : modules) module.init();
        
        // Bulk Encoder Caching
        hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : hubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
    }
    
    // Run in a loop after INIT is pressed until PLAY is pressed
    public void init_loop() {
        // FPS Logging
        frames += 1;
        
        // Update Encoder Caches
        updateBulkCache();
        
        // Loop Modules
        for (BaseModule module : modules) module.init_loop();
    }
    
    // Run once PLAY is pressed
    public void start() {
        frames = 0;
        
        // Start Modules
        for (BaseModule module : modules) module.start();
    }
    
    // Run in a loop after PLAY is pressed until STOP is pressed
    public void loop() {
        // FPS Logging
        frames += 1;
        
        // Update Encoder Caches
        updateBulkCache();
        
        // Commented due to major performance boost
        // camera.loop();
        
        // Loop Modules + Update Dashboard Telemetry
        for (BaseModule module : modules) {
            module.loop();
            
            module.getDashboardTelemetry(dashboardTelemetry);
        }
        dashboardTelemetry.update();
    }
    
    public void updateBulkCache() {
        // Update Encoder Caches
        for (LynxModule module : hubs) {
            module.clearBulkCache();
        }
    }
    
    // Run once STOP is pressed
    public void stop() {
    }
    
    public abstract boolean shouldUseABPS();
}
