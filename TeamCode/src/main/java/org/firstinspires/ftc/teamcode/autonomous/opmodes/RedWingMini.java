package org.firstinspires.ftc.teamcode.autonomous.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous.movements.presets.PushToRight;

@Autonomous(name = "Red Wing Mini", group = "Wing")
public class RedWingMini extends AutoOp {
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
                actions = RedWingMiniPaths.LEFT;
                break;
            }
            
            case CENTER: {
                actions = RedWingMiniPaths.CENTER;
                break;
            }
            
            case RIGHT: {
                actions = RedWingMiniPaths.RIGHT;
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

class RedWingMiniPaths {
    public static final Action[] LEFT = {
            new PushToLeft(PushPresetPath.STRAIGHT),
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.LEFT),
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.AROUND),
    };
}