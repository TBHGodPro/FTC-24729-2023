package org.firstinspires.ftc.teamcode.autonomous.movements.presets;

import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ActionGroup;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ArmPositionAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.GamepadButtonAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WaitAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.utils.GamepadButton;
import org.firstinspires.ftc.teamcode.modules.Arm.ArmPosition;

public class DropPurpleAndPickUpYellow extends ActionGroup {
    public static final Action[] actions = {
            new ArmPositionAction(ArmPosition.INTAKE),
            new GamepadButtonAction(GamepadButton.y),
            new MoveAction(Direction.BACKWARD, 100, 100),
            new GamepadButtonAction(GamepadButton.x),
            new WaitAction(250),
            new GamepadButtonAction(GamepadButton.a)
    };
    
    public DropPurpleAndPickUpYellow() {
        super(actions);
    }
}
