package org.firstinspires.ftc.teamcode.autonomous_old.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;

@Autonomous(name = "Wrist Reset", group = "Tuning")
public class WristReset extends AutoOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
    
    @Override
    public void runOP() {
        arm.wristPos = 1;
    }
}
