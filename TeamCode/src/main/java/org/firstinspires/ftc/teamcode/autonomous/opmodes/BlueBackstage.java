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
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.ABPSPushAction;
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

        arm.armPos = 850;

        awaitFrame();

        activeSleep(2000);
        
        PossiblePropPosition position = getCamera().prop.position;
        getCamera().disablePropDetection();

        arm.armPos = 300;
        
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
            new MultiMoveAction(-250, -550, 500),
            new TurnAction(-90),
            new MultiMoveAction(450, 300, 600),
            new ABPSPushAction(SidewaysDirection.LEFT),
            new MultiMoveAction(-200, -550, 600),
            new TurnAction(90)
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.LEFT),
            new MoveAction(Direction.BACKWARD, 250, 125),
            new MultiMoveAction(200, -800, 500),
            new ABPSPushAction(SidewaysDirection.LEFT),
            new MultiMoveAction(-250, -850, 650),
            new TurnAction(90)
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.AROUND),
            new MultiMoveAction(-400, -100, 350),
            new ABPSPushAction(SidewaysDirection.LEFT),
            new MultiMoveAction(-350, -1050, 650),
            new TurnAction(90)
    };
}