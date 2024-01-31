package org.firstinspires.ftc.teamcode.autonomous_old.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Utils.Alliance;
import org.firstinspires.ftc.teamcode.Utils.PossiblePropPosition;
import org.firstinspires.ftc.teamcode.Utils.SidewaysDirection;
import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.MovementHandler;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.actions.Action;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushPresetPath;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToCenter;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToLeft;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.presets.PushToRight;

@Disabled
@Autonomous(name = "Blue Wing Mini", group = "Wing")
public class BlueWingMini extends AutoOp {
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
        
        arm.armPos = 150;
        
        Action[] actions;
        
        switch (position) {
            case LEFT: {
                actions = BlueWingMiniPaths.LEFT;
                break;
            }
            
            case CENTER: {
                actions = BlueWingMiniPaths.CENTER;
                break;
            }
            
            case RIGHT: {
                actions = BlueWingMiniPaths.RIGHT;
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

class BlueWingMiniPaths {
    public static final Action[] LEFT = {
            new PushToLeft(PushPresetPath.AROUND),
    };
    
    public static final Action[] CENTER = {
            new PushToCenter(SidewaysDirection.RIGHT),
    };
    
    public static final Action[] RIGHT = {
            new PushToRight(PushPresetPath.STRAIGHT),
    };
}