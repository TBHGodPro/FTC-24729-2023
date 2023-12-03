package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;

public class PushToRight extends ActionGroup {
    public static final Action[] actionsStraight = {
            new MultiMoveAction(650, 400, 700),
    };
    
    public static final Action[] actionsAround = {
    };
    
    public PushToRight(PushPresetPath path) {
        super(path == PushPresetPath.AROUND ? actionsAround : actionsStraight);
    }
}
