package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ABPSAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.GamepadButtonAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WaitAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.utils.GamepadButton;

public class ABPSPushAction extends ActionGroup {
    public static final Action[] actionsLeft = {
            new ABPSAction(SidewaysDirection.LEFT, true),
            new MoveAction(Direction.FORWARD, 50, 50),
            new GamepadButtonAction(GamepadButton.y),
            new WaitAction(200),
    };
    
    public static final Action[] actionsRight = {
            new ABPSAction(SidewaysDirection.RIGHT, true),
            new MoveAction(Direction.FORWARD, 50, 50),
            new GamepadButtonAction(GamepadButton.y),
            new WaitAction(200)
    };
    
    public ABPSPushAction(SidewaysDirection direction) {
        super(direction == SidewaysDirection.LEFT ? actionsLeft : actionsRight);
    }
}
