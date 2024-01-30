package org.firstinspires.ftc.teamcode.autonomous.roadrunner.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.roadrunner.drive.SampleMecanumDrive;

@Autonomous(name = "Initial Test Op", group = "Testing")
public class InitialTestOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d startPose = new Pose2d(12, -72, radian(90));
        
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        
        drive.setPoseEstimate(startPose);
        
        Trajectory purple_pixel = drive.trajectoryBuilder(startPose)
                .forward(29)
                .build();
        
        Trajectory yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                .strafeTo(new Vector2d(14, -48))
                .splineToSplineHeading(new Pose2d(53, -42, radian(0)), 0)
                .build();
        
        Trajectory park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                .splineToLinearHeading(new Pose2d(53, -64, radian(90)), 0)
                .build();
        
        waitForStart();
        
        if (isStopRequested()) return;
        
        drive.followTrajectory(purple_pixel);
        sleep(1000);
        drive.followTrajectory(yellow_pixel);
        // Place Yellow
        sleep(2000);
        drive.followTrajectory(park);
        
        stop();
    }
    
    public double radian(double degrees) {
        return Math.toRadians(degrees);
    }
}
