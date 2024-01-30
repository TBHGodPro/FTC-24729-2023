package org.firstinspires.ftc.teamcode.autonomous_old.movements.actions;

import org.firstinspires.ftc.teamcode.autonomous_old.AutoOp;
import org.firstinspires.ftc.teamcode.autonomous_old.movements.utils.GamepadButton;

import java.lang.reflect.Field;

public class GamepadButtonAction extends Action {
    public final GamepadButton button;
    public Boolean active;
    
    public GamepadButtonAction(GamepadButton button) {
        this.button = button;
    }
    
    public GamepadButtonAction(GamepadButton button, boolean active) {
        this.button = button;
        this.active = active;
    }
    
    @Override
    public void execute(AutoOp op) {
        try {
            Field field = op.gamepad.getClass().getDeclaredField(button.name());
            
            if (active == null) {
                field.set(op.gamepad, true);
                
                op.awaitFrame();
                
                field.set(op.gamepad, false);
            } else {
                field.set(op.gamepad, active);
            }
        } catch (Exception e) {
        }
    }
}
