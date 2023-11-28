package org.firstinspires.ftc.teamcode.modules;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class Module {
    public abstract void addTelemetry(Telemetry telemetry);

    public void setupTelemetry(Telemetry telemetry) {
        telemetry.addLine("--- " + this.getClass().getName().replace("Controller", "")
                .replace("org.firstinspires.ftc.teamcode.modules.", "") + " ---");
        telemetry.addLine();

        addTelemetry(telemetry);

        telemetry.addLine();
    }
}
