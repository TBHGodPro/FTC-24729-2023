package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Alliance;

@TeleOp(name = "RedOp", group = "OpModes")
public class RedOp extends MainOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
}
