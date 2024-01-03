package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WheelPositionAction;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

public class PushToLeft extends ActionGroup {
    public static final Action[] actionsStraight = {
            new MultiMoveAction(650, -600, 1200),
    };
    
    public static final Action[] actionsAround = {
            new MultiMoveAction(675, 200, 750),
            new WheelPositionAction(new WheelTarget(-450, 950, -450, 950, 950)),
            new TurnAction(-90),
            new MoveAction(Direction.FORWARD, 150, 300)
    };
    
    public PushToLeft(PushPresetPath path) {
        super(path == PushPresetPath.AROUND ? actionsAround : actionsStraight);
    }
}
