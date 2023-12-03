package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.StrafeDirection;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;

public class PushToCenter extends ActionGroup {
    public static final Action[] actionsStrafeLeft = {
            new MultiMoveAction(980, -200, 800),
    };
    
    public static final Action[] actionsStrafeRight = {
            new MultiMoveAction(980, 200, 800),
    };
    
    public PushToCenter(StrafeDirection strafeDirection) {
        super(strafeDirection == StrafeDirection.LEFT ? actionsStrafeLeft : actionsStrafeRight);
    }
}