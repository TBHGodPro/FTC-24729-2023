package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.MainOp;
import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.modules.Inputs.AdvancedInputManager;
import org.firstinspires.ftc.teamcode.modules.Inputs.InputManager;

@TeleOp(name = "Advanced Op", group = "OpModes")
public class AdvancedOp extends MainOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public InputManager getInputManager() {
        return new AdvancedInputManager(this);
    }
}
