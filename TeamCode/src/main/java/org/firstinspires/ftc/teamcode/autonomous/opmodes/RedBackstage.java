package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.Direction;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.StrafeDirection;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.ABPSAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.MultiMoveAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.TurnAction;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToCenter;

@Autonomous(name = "Red Backstage", group = "Backstage")
public class RedBackstage extends AutoOp {
    @Override
    public Alliance getAlliance() {
        return Alliance.RED;
    }
    
    @Override
    public void runOP() {
        sleep(1000);
        
        PossiblePropPosition position = getCamera().prop.position;
        
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
        
        sleep(1500);
    }
}

class RedBackstagePaths {
    public static final Action[] LEFT = {};
    
    public static final Action[] CENTER = {
            new PushToCenter(StrafeDirection.RIGHT),
            new MoveAction(Direction.BACKWARD, 250, 125),
            new MultiMoveAction(200, 800, 500),
            new ABPSAction(StrafeDirection.RIGHT),
            new MultiMoveAction(-100, 850, 650),
            new MoveAction(Direction.FORWARD, 200, 200),
            new TurnAction(-90)
    };
    
    public static final Action[] RIGHT = {};
}