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

@Autonomous(name = "Blue Backstage", group = "Blue")
public class BlueBackstage extends BaseAutonOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public Pose2d getStartPose() {
        return new Pose2d(12, 72, radian(-90));
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
        Trajectory yellow_pixel = null;
        Trajectory park = null;
        
        switch (position) {
            case LEFT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(23, 48))
                        .build();
                
                yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                        .strafeTo(new Vector2d(23, 53))
                        .splineToSplineHeading(new Pose2d(42.5, 42, radian(0)), 0)
                        .forward(11.5, SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                        .back(6, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .splineToSplineHeading(new Pose2d(56, 58, radian(-90)), 0)
                        .build();
                break;
            }
            
            case CENTER: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(12, 43))
                        .build();
                
                yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                        .strafeTo(new Vector2d(14, 48))
                        .splineToSplineHeading(new Pose2d(42.5, 41, radian(0)), 0)
                        .forward(9.2, SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                        .back(6, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .splineToSplineHeading(new Pose2d(53, 64, radian(-90)), radian(0))
                        .build();
                break;
            }
            
            case RIGHT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .lineToConstantHeading(new Vector2d(23, 54))
                        .splineToSplineHeading(new Pose2d(9, 44, radian(-180)), radian(-180), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL / 2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                        .lineToConstantHeading(new Vector2d(23, 43))
                        .splineToSplineHeading(new Pose2d(37, 38, radian(0)), 0)
                        .forward(16.5, SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                        .back(6, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .splineToSplineHeading(new Pose2d(53, 69, radian(-90)), radian(0))
                        .build();
                break;
            }
        }
        
        drive.followTrajectory(purple_pixel);
        moveArm(40);
        drive.followTrajectory(yellow_pixel);
        release();
        drive.followTrajectory(park);
    }
}
