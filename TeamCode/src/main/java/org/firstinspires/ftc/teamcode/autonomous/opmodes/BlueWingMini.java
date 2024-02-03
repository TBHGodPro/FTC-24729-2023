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

@Autonomous(name = "Blue Wing Mini", group = "Blue")
public class BlueWingMini extends BaseAutonOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public Pose2d getStartPose() {
        return new Pose2d(-36, 72, radian(-90));
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
        
        switch (position) {
            case LEFT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .lineToConstantHeading(new Vector2d(-47, 54))
                        .splineToSplineHeading(new Pose2d(-33, 44, radian(0)), radian(0), SampleMecanumDrive.getVelocityConstraint(30, DriveConstants.MAX_ANG_VEL / 2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
            
            case CENTER: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .lineToConstantHeading(new Vector2d(-40, 32))
                        .splineToSplineHeading(new Pose2d(-40, 26.5, 90), 0, SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL / 2.2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
            
            case RIGHT: {
                purple_pixel = drive.trajectoryBuilder(getStartPose())
                        .strafeTo(new Vector2d(-36, 48))
                        .splineToSplineHeading(new Pose2d(-46, 38, radian(92)), radian(-180), SampleMecanumDrive.getVelocityConstraint(25, DriveConstants.MAX_ANG_VEL / 2.2, DriveConstants.TRACK_WIDTH), SampleMecanumDrive.getAccelerationConstraint(DriveConstants.MAX_ACCEL))
                        .build();
                break;
            }
        }
        
        drive.followTrajectory(purple_pixel);
    }
}
