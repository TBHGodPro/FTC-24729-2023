package org.firstinspires.ftc.teamcode.autonomous.movements.actions;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.autonomous.AutoOp;

public class TurnAction extends Action {
    public final double degrees;
    
    public TurnAction(double degrees) {
        this.degrees = degrees;
    }
    
    @Override
    public void execute(AutoOp op) {
        op.movements.desiredAngle -= degrees;
        
        while (Math.round(op.movements.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) / 5) != Math.round(op.movements.desiredAngle / 5))
            op.awaitFrame();
    }
}
