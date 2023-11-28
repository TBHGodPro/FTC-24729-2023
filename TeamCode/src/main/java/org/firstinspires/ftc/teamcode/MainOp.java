package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.modules.ABPSController;
import org.firstinspires.ftc.teamcode.modules.ArmController;
import org.firstinspires.ftc.teamcode.modules.MovementController;
import org.firstinspires.ftc.teamcode.modules.WheelController;

@TeleOp(name = "MainOp")
public class MainOp extends BaseOp {
    public Boolean isAutonomous;

    public Gamepad gamepad;

    public WheelController wheels;
    public MovementController movements;
    public ArmController arm;
    public ABPSController abps;

    // Run once INIT is pressed
    public void init() {
        // Check for Autonomous
        if (isAutonomous == null) {
            isAutonomous = false;
        }

        // Allow gamepad overriding (by autonomous)
        if (gamepad == null) {
            gamepad = gamepad1;
        }

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

        abps = new ABPSController(this, gamepad, hardwareMap.get(WebcamName.class, "Webcam 1"));

        abps.init();

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

        wheels.update();

        arm.update();

        abps.loop();
    }

    // Run once STOP is pressed
    public void stop() {
    }
}
