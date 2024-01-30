package org.firstinspires.ftc.teamcode.autonomous_old.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.ABPSPushAction;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToRight;

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
            new MoveAction(Direction.BACKWARD, 400, 500),
            new TurnAction(180),
            new MultiMoveAction(750, -250, 1100),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-250, 875, 950),
            new TurnAction(-90)
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.RIGHT),
            new MoveAction(Direction.BACKWARD, 250, 250),
            new MultiMoveAction(100, 300, 500),
            new TurnAction(90),
            new MoveAction(Direction.FORWARD, 550, 550),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-250, 675, 650),
            new TurnAction(-90)
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