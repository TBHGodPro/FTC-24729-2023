package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.StrafeDirection;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;

public class PlaceCenter extends ActionGroup {
    public static final Action[] actionsStrafeLeft = {
            new MultiMoveAction(825, -300, 800),
            new DropPurpleAndPickUpYellow()
    };
    
    public static final Action[] actionsStrafeRight = {
            new MultiMoveAction(825, 300, 800),
            new DropPurpleAndPickUpYellow()
    };
    
    public PlaceCenter(StrafeDirection strafeDirection) {
        super(strafeDirection == StrafeDirection.LEFT ? actionsStrafeLeft : actionsStrafeRight);
    }
}