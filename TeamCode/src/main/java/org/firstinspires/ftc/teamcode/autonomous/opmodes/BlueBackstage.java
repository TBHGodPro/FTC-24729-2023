package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ABPSAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.GamepadButtonAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WaitAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToRight;
import org.firstinspires.ftc.teamcode.autonomous.movements.utils.GamepadButton;

@Autonomous(name = "Blue Backstage", group = "Backstage")
public class BlueBackstage extends AutoOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.BLUE;
    }
    
    @Override
    public void runOP() {
        sleep(1000);
        
        PossiblePropPosition position = getCamera().prop.position;
        getCamera().disablePropDetection();
        
        Action[] actions;
        
        switch (position) {
            case LEFT: {
                actions = BlueBackstagePaths.LEFT;
                break;
            }
            
            case CENTER: {
                actions = BlueBackstagePaths.CENTER;
                break;
            }
            
            case RIGHT: {
                actions = BlueBackstagePaths.RIGHT;
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + position);
        }
        
        MovementHandler movements = new MovementHandler(this, actions);
        
        while (!movements.isDone()) movements.runNext();
        
        sleep(500);
    }
}

class BlueBackstagePaths {
    public static final Action[] LEFT = {
            new PushToLeft(PushPresetPath.STRAIGHT),
            new MultiMoveAction(-250, -550, 450),
            new MoveAction(Direction.FORWARD, 500, 150),
            new ABPSAction(SidewaysDirection.LEFT, true),
            new MultiMoveAction(-100, -280, 500),
            new MoveAction(Direction.FORWARD, 100, 200),
            new GamepadButtonAction(GamepadButton.y),
            new WaitAction(350),
            new MultiMoveAction(-200, -600, 650),
            new MoveAction(Direction.FORWARD, 300, 200),
            new TurnAction(90)
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.LEFT),
            new MoveAction(Direction.BACKWARD, 250, 125),
            new MultiMoveAction(200, -800, 500),
            new ABPSAction(SidewaysDirection.LEFT, false),
            new MultiMoveAction(-100, -1050, 650),
            new MoveAction(Direction.FORWARD, 200, 200),
            new TurnAction(90)
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.AROUND),
            new MultiMoveAction(-400, -100, 350),
            new ABPSAction(SidewaysDirection.LEFT, false),
            new MultiMoveAction(-100, -1350, 650),
            new MoveAction(Direction.FORWARD, 200, 200),
            new TurnAction(90)
    };
}