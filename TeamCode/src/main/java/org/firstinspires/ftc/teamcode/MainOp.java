package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
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
import org.firstinspires.ftc.teamcode.modules.Camera.AutonomousCameraManager;
import org.firstinspires.ftc.teamcode.modules.Camera.CameraManager;
import org.firstinspires.ftc.teamcode.modules.Camera.ManualCameraManager;
import org.firstinspires.ftc.teamcode.modules.Module;
import org.firstinspires.ftc.teamcode.modules.MovementController;
import org.firstinspires.ftc.teamcode.modules.WheelController;

import java.util.ArrayList;

public abstract class MainOp extends BaseOp {
    public Boolean isAutonomous;
    
    public FtcDashboard dashboard;
    public Telemetry dashboardTelemetry;
    
    public Gamepad gamepad;
    
    public ArrayList<Module> modules = new ArrayList<Module>();
    
    public CameraManager camera;
    
    public WheelController wheels;
    public MovementController movements;
    public ArmController arm;
    public ABPSController abps;
    
    public abstract Alliance getAlliance();
    
    // Run once INIT is pressed
    public void init() {
        // Get Dashboard
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
        
        WebcamName webcam = hardwareMap.get(WebcamName.class, "Webcam 1");
        camera = isAutonomous ? new AutonomousCameraManager(webcam, getAlliance()) : new ManualCameraManager(webcam);
        camera.init();
        
        wheels = new WheelController(
                hardwareMap.get(DcMotorEx.class, "back_left"),
                hardwareMap.get(DcMotorEx.class, "back_right"),
                hardwareMap.get(DcMotorEx.class, "front_left"),
                hardwareMap.get(DcMotorEx.class, "front_right"));
        wheels.init();
        
        movements = new MovementController(hardwareMap.get(IMU.class, "imu"), gamepad, !isAutonomous);
        movements.init();
        
        arm = new ArmController(
                isAutonomous,
                gamepad,
                hardwareMap.get(DcMotorEx.class, "arm"),
                hardwareMap.get(Servo.class, "wrist"),
                hardwareMap.get(Servo.class, "hand"));
        arm.init();
        
        abps = new ABPSController(this);
        
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
        
        modules.add(movements);
        modules.add(wheels);
        modules.add(arm);
        modules.add(abps);
    }
    
    // Run in a loop after INIT is pressed until PLAY is pressed
    public void init_loop() {
        frames += 1;
    }
    
    // Run once PLAY is pressed
    public void start() {
        frames = 0;
        
        movements.prep();
        
        arm.prep();
    }
    
    // Run in a loop after PLAY is pressed until STOP is pressed
    public void loop() {
        frames += 1;
        movements.updatePowers(wheels);
        
        // Commented due to major performance boost
        // camera.loop();
        
        wheels.update();
        
        arm.update();
        
        abps.loop();
        
        for (Module module : modules) {
            module.getDashboardTelemetry(dashboardTelemetry);
            
            dashboardTelemetry.update();
        }
    }
    
    // Run once STOP is pressed
    public void stop() {
    }
}
