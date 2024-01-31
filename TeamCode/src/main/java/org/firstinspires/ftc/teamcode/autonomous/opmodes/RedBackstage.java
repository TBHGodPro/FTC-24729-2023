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

@Autonomous(name = "Red Backstage", group = "Red")
public class RedBackstage extends BaseAutonOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
    
    @Override
    public Pose2d getStartPose() {
        return new Pose2d(12, -72, radian(90));
    }
    
    @Override
    public void runTrajectories() {
        sleep(1000);
        
        hideArm();
        
        sleep(800);
        
        PossiblePropPosition position = camera.prop.position;
        camera.disablePropDetection();
        
        safeArm();
        
        Trajectory purple_pixel = null;
        Trajectory yellow_pixel = null;
        Trajectory park = null;
        
        switch (position) {
            case LEFT: {
                break;
            }
            
            case CENTER: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(12, -43))
                        .build();
                
                yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                        .strafeTo(new Vector2d(14, -48))
                        .splineToSplineHeading(new Pose2d(42.5, -44, radian(0)), 0)
                        .forward(10, SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                        .back(6, SampleMecanumDrive.getVelocityConstraint(10, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .splineToSplineHeading(new Pose2d(53, -62, radian(90)), 0)
                        .build();
                break;
            }
            
            case RIGHT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(23, -48))
                        .build();
                
                yellow_pixel = drive.trajectoryBuilder(purple_pixel.end())
                        .strafeTo(new Vector2d(23, -53))
                        .splineToSplineHeading(new Pose2d(42.5, -44, radian(0)), 0)
                        .forward(11.5, SampleMecanumDrive.getVelocityConstraint(20, DriveConstants.MAX_ANG_VEL, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                
                park = drive.trajectoryBuilder(yellow_pixel.end(), true)
                        .lineToLinearHeading(new Pose2d(53, -58, radian(90)), SampleMecanumDrive.getVelocityConstraint(15, DriveConstants.MAX_ANG_VEL / 1.5, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
        }
        
        drive.followTrajectory(purple_pixel);
        moveArm();
        drive.followTrajectory(yellow_pixel);
        release();
        drive.followTrajectory(park);
    }
}
