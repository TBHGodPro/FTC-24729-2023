package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;

public class PushToCenter extends ActionGroup {
    public static final Action[] actionsStrafeLeft = {
            new MultiMoveAction(910, -200, 1200),
    };
    
    public static final Action[] actionsStrafeRight = {
            new MultiMoveAction(910, 100, 1200),
    };
    
    public PushToCenter(SidewaysDirection sidewaysDirection) {
        super(sidewaysDirection == SidewaysDirection.LEFT ? actionsStrafeLeft : actionsStrafeRight);
    }
}