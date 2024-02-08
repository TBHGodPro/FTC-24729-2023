package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.autonomous.BaseAutonOp;
import org.firstinspires.ftc.teamcode.autonomous.roadrunner.drive.DriveConstants;
import org.firstinspires.ftc.teamcode.autonomous.roadrunner.drive.SampleMecanumDrive;

@Autonomous(name = "Red Wing", group = "Red")
public class RedWing extends BaseAutonOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
    
    @Override
    public Pose2d getStartPose() {
        return new Pose2d(-36, -72, radian(90));
    }
    
    @Override
    public void runTrajectories() {
        sleep(1000);
        
        hideArm();
        
        sleep(800);
        
        PossiblePropPosition position = camera.prop.position;
        camera.disablePropDetection();
        //camera.setManualExposure(BaseCameraManager.cameraExposureMS + 8, BaseCameraManager.cameraGain);
        
        safeArm();
        
        Trajectory purple_pixel = null;
        Trajectory yellow_prep = null;
        Trajectory yellow_pixel1 = null;
        Trajectory yellow_pixel2 = null;
        Trajectory park = null;
        long waitUntilTimeMS = 0;
        
        switch (position) {
            case LEFT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(-36, -48))
                        .splineToSplineHeading(new Pose2d(-46, -38, radian(-92)), radian(180), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL / 2.2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                yellow_prep = drive.trajectoryBuilder(purple_pixel.end())
                        .back(4)
                        .splineToSplineHeading(new Pose2d(-36, -31.5, 0), 0)
                        .build();
                
                waitUntilTimeMS = 18_000;
                
                yellow_pixel1 = drive.trajectoryBuilder(yellow_prep.end())
                        .lineTo(new Vector2d(36, -31.5))
                        .build();
                
                yellow_pixel2 = drive.trajectoryBuilder(yellow_pixel1.end())
                        .splineToConstantHeading(new Vector2d(47, -51), 0)
                        .forward(4.2, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel2.end())
                        .back(4, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
            
            case CENTER: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .lineToConstantHeading(new Vector2d(-40, -35))
                        .splineToSplineHeading(new Pose2d(-36, -26, radian(-89)), radian(45), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL / 2.2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                yellow_prep = drive.trajectoryBuilder(purple_pixel.end(), true)
                        .back(5)
                        .splineToSplineHeading(new Pose2d(-36, -24, radian(0)), radian(-45), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL / 2.2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                waitUntilTimeMS = 18_000;
                
                yellow_pixel1 = drive.trajectoryBuilder(yellow_prep.end())
                        .lineTo(new Vector2d(36, -24))
                        .build();
                
                yellow_pixel2 = drive.trajectoryBuilder(yellow_pixel1.end())
                        .splineToConstantHeading(new Vector2d(44, -49.25), 0)
                        .forward(6.5, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel2.end())
                        .back(4, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
            
            case RIGHT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .lineToConstantHeading(new Vector2d(-47, -54))
                        .splineToSplineHeading(new Pose2d(-33, -44, radian(0)), radian(0), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL / 2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                yellow_prep = drive.trajectoryBuilder(purple_pixel.end(), true)
                        .back(4)
                        .splineToConstantHeading(new Vector2d(-36, -15.75), 0)
                        .build();
                
                waitUntilTimeMS = 18_000;
                
                yellow_pixel1 = drive.trajectoryBuilder(yellow_prep.end())
                        .lineTo(new Vector2d(36, -15.75))
                        .build();
                
                yellow_pixel2 = drive.trajectoryBuilder(yellow_pixel1.end())
                        .splineToConstantHeading(new Vector2d(48, -46.5), 0)
                        .forward(6.4, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel2.end())
                        .back(4, SampleMecanumDrive.getVelocityConstraint(5, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
        }
        
        drive.followTrajectory(purple_pixel);
        drive.followTrajectory(yellow_prep);
        while (runtime.milliseconds() < waitUntilTimeMS) sleep(2);
        drive.followTrajectory(yellow_pixel1);
        moveArm(125);
        drive.followTrajectory(yellow_pixel2);
        release();
        drive.followTrajectory(park);
    }
}
