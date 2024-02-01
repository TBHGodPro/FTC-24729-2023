package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.autonomous.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmController;
import org.firstinspires.ftc.teamcode.modules.Camera.AutonomousCameraManager;
import org.firstinspires.ftc.teamcode.modules.DroneController;
import org.firstinspires.ftc.teamcode.modules.HangController;

@Config
public abstract class BaseAutonOp extends LinearOpMode {
    // --- Constants ---
    
    public static double wristBackboardOffset = -0.1;
    
    // -----------------
    
    public abstract Alliance getAlliance();
    
    public abstract Pose2d getStartPose();
    
    public abstract void runTrajectories();
    
    public FtcDashboard dashboard;
    public Telemetry dashboardTelemetry;
    
    public SampleMecanumDrive drive;
    public AutonomousCameraManager camera;
    
    public AprilTagLocalizer localizer;
    
    public int targetArm;
    public int safeArm;
    public double targetWrist;
    public double targetClaw;
    
    public DcMotorEx armLeft;
    public DcMotorEx armRight;
    public Servo wrist;
    public Servo claw;
    
    @Override
    public void runOpMode() {
        // http://192.168.43.1:8080/dash
        dashboard = FtcDashboard.getInstance();
        dashboardTelemetry = dashboard.getTelemetry();
        
        drive = new SampleMecanumDrive(hardwareMap);
        camera = new AutonomousCameraManager(hardwareMap.get(WebcamName.class, "Webcam 1"), getAlliance());
        
        drive.setPoseEstimate(getStartPose());
        camera.init();
        camera.enableAprilTag();
        
        localizer = new AprilTagLocalizer(this, camera.processor);
        //localizer.start();
        
        waitForStart();
        
        hardwareMap.get(Servo.class, "hang").setPosition(HangController.servoHeld);
        hardwareMap.get(Servo.class, "drone").setPosition(DroneController.holdPosition);
        
        hardwareMap.get(Servo.class, "claw_right").setPosition(ArmController.handRightOpenPos - 0.1);
        claw = hardwareMap.get(Servo.class, "claw_left");
        claw.setPosition(ArmController.handLeftClosedPos);
        
        wrist = hardwareMap.get(Servo.class, "wrist");
        wrist.setPosition(1);
        
        armLeft = hardwareMap.get(DcMotorEx.class, "arm_left");
        armRight = hardwareMap.get(DcMotorEx.class, "arm_right");
        
        armLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        armLeft.setZeroPowerBehavior(ArmController.armZeroPowerBehavior);
        armRight.setZeroPowerBehavior(ArmController.armZeroPowerBehavior);
        armLeft.setDirection(ArmController.armLeftDirection);
        armRight.setDirection(ArmController.armRightDirection);
        
        armLeft.setTargetPositionTolerance(20);
        armRight.setTargetPositionTolerance(20);
        
        ArmController emptyArmController = new ArmController(null, true, null, null, null, null, null, null);
        targetArm = emptyArmController.armBackboardPos;
        safeArm = emptyArmController.armSafePos;
        
        targetWrist = emptyArmController.wristBackboardPos;
        targetWrist = Math.max(Math.min(targetWrist - (targetArm / ArmController.wristAngleCorrectionCoeff), 1), 0);
        targetWrist += wristBackboardOffset;
        
        targetClaw = ArmController.handLeftOpenPos;
        
        runTrajectories();
        
        sleep(400);
        
        safeArm();
        
        while (opModeIsActive()) sleep(5);
    }
    
    public void moveArm() {
        wrist.setPosition(targetWrist);
        
        armLeft.setTargetPosition(targetArm);
        armRight.setTargetPosition(targetArm);
        
        armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        armLeft.setPower(0.5);
        armRight.setPower(0.5);
        
        while (armLeft.isBusy() || armRight.isBusy()) sleep(10);
    }
    
    public void hideArm() {
        wrist.setPosition(1);
        
        armLeft.setTargetPosition(850);
        armRight.setTargetPosition(850);
        
        armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        armLeft.setPower(0.5);
        armRight.setPower(0.5);
        
        while (armLeft.isBusy() || armRight.isBusy()) sleep(10);
    }
    
    public void safeArm() {
        wrist.setPosition(1);
        
        armLeft.setTargetPosition(safeArm);
        armRight.setTargetPosition(safeArm);
        
        armLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        armLeft.setPower(0.5);
        armRight.setPower(0.5);
        
        while (armLeft.isBusy() || armRight.isBusy()) sleep(10);
    }
    
    public void release() {
        claw.setPosition(targetClaw);
        sleep(300);
    }
    
    public double radian(double degrees) {
        return Math.toRadians(degrees);
    }
}
