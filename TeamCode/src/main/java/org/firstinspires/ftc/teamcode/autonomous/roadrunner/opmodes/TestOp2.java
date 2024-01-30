package org.firstinspires.ftc.teamcode.autonomous.roadrunner.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.autonomous.roadrunner.BaseAutonOp;

@Autonomous(name = "Test Op 2", group = "Testing")
public class TestOp2 extends BaseAutonOp {
    @Override
    public void runTrajectories() {
    }
    
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
