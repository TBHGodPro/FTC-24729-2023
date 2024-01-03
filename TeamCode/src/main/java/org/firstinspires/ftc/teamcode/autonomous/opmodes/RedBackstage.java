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
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WheelPositionAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.ABPSPushAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToRight;
import org.firstinspires.ftc.teamcode.autonomous.movements.utils.GamepadButton;
import org.firstinspires.ftc.teamcode.modules.Wheels.WheelTarget;

@Autonomous(name = "Red Backstage", group = "Backstage")
public class RedBackstage extends AutoOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
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
                actions = RedBackstagePaths.LEFT;
                break;
            }
            
            case CENTER: {
                actions = RedBackstagePaths.CENTER;
                break;
            }
            
            case RIGHT: {
                actions = RedBackstagePaths.RIGHT;
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

class RedBackstagePaths {
    public static final Action[] LEFT = {
            new PushToLeft(PushPresetPath.AROUND),
            new MultiMoveAction(-400, 0, 350),
            //new ABPSAction(SidewaysDirection.RIGHT, true),
//            new MultiMoveAction(-100, -250, 500),
//            new MoveAction(Direction.FORWARD, 150, 100),
//            new GamepadButtonAction(GamepadButton.y),
//            new WaitAction(350),
//            new MultiMoveAction(-300, 700, 650),
//            new TurnAction(-90)
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.RIGHT),
            new MoveAction(Direction.BACKWARD, 250, 125),
            new MultiMoveAction(200, 800, 500),
            //new ABPSPushAction(SidewaysDirection.RIGHT),
            //new MultiMoveAction(-250, 675, 650),
            //new TurnAction(-90)
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.STRAIGHT),
            new MultiMoveAction(-250, 550, 500),
            new TurnAction(90),
            new MultiMoveAction(300, -250, 500),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-200, 450, 600),
            new TurnAction(-90)
    };
}