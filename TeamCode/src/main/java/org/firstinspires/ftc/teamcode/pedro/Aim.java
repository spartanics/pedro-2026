package org.firstinspires.ftc.teamcode.pedro;




import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.BooleanSupplier;

public class Aim {

    private Limelight3A limelight;
    private boolean aligned;
    Telemetry telemetry;

    double x, y, z;

    public Aim(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.limelight = hardwareMap.get(Limelight3A.class, "limelight"); // or logitech Webcam
    }


    public void update() {
        // always tries to find the apriltag
        // and then update the relative position
    }

    public double[] getRelativePosition() {
        double[] ret = {x, y, z};
        return ret;
    }

    public boolean isAprilTagFound() {
        // will return true only if april tag is in sight.
        return true;
    }
}
