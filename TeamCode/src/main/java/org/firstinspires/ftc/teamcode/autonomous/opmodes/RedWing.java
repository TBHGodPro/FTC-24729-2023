package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.WaitAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.ABPSPushAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToRight;

@Autonomous(name = "Red Wing", group = "Wing")
public class RedWing extends AutoOp {
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
        
        arm.armPos = 150;
        
        Action[] actions;
        
        switch (position) {
            case LEFT: {
                actions = RedWingPaths.LEFT;
                break;
            }
            
            case CENTER: {
                actions = RedWingPaths.CENTER;
                break;
            }
            
            case RIGHT: {
                actions = RedWingPaths.RIGHT;
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

class RedWingPaths {
    public static final Action[] LEFT = {
            new PushToLeft(PushPresetPath.STRAIGHT),
            new MultiMoveAction(-200, -500, 800),
            new MoveAction(Direction.FORWARD, 1050, 1200),
            new TurnAction(90),
            new WaitAction(7_500),
            new MoveAction(Direction.FORWARD, 2950, 3500),
            new MultiMoveAction(150, 600, 1000),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-200, -450, 300),
            new TurnAction(-90)
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.LEFT),
            new MultiMoveAction(-100, -700, 900),
            new MoveAction(Direction.FORWARD, 750, 1000),
            new TurnAction(90),
            new WaitAction(6_000),
            new MoveAction(Direction.FORWARD, 2800, 3000),
            new MultiMoveAction(50, 830, 1000),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-200, -800, 350),
            new TurnAction(-90)
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.AROUND),
            new MoveAction(Direction.BACKWARD, 150, 320),
            new TurnAction(-90),
            new MultiMoveAction(815, 100, 1500),
            new TurnAction(90),
            new WaitAction(7_000),
            new MoveAction(Direction.FORWARD, 2175, 2500),
            new MultiMoveAction(50, 980, 1800),
            new ABPSPushAction(SidewaysDirection.RIGHT),
            new MultiMoveAction(-200, -1000, 400),
            new TurnAction(-90)
    };
}