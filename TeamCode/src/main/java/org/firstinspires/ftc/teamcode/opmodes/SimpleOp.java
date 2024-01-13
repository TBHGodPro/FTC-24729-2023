package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;
import org.firstinspires.ftc.teamcode.modules.Inputs.SimpleInputManager;

@TeleOp(name = "Simple Op", group = "OpModes")
public class SimpleOp extends MainOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public InputManager getInputManager() {
        return new SimpleInputManager(true);
    }
}
