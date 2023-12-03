package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WheelPositionAction;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

public class PushToLeft extends ActionGroup {
    public static final Action[] actionsStraight = {
            new MultiMoveAction(650, -400, 700),
    };
    
    public static final Action[] actionsAround = {
            new MultiMoveAction(575, 200, 450),
            new WheelPositionAction(new WheelTarget(-350, 1050, -350, 1050, 550)),
            new TurnAction(-90)
    };
    
    public PushToLeft(PushPresetPath path) {
        super(path == PushPresetPath.AROUND ? actionsAround : actionsStraight);
    }
}
