package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Alliance;

@TeleOp(name = "ABPS Op", group = "OpModes")
public class ABPSOp extends MainOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public boolean shouldUseABPS() {
        return true;
    }
}
