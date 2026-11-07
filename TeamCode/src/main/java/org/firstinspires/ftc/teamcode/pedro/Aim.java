package org.firstinspires.ftc.teamcode.pedro;




import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
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
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start();
    }


    public void update() {
        getRelativePosition();
    }

    public double[] getRelativePosition() {
        double[] difference = {x, y, z}; //x = left right deg y = degrees up and down z = size

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            difference[0] = x = result.getTx(); // How far left or right the target is (degrees)
            difference[1] = y = result.getTy(); // How far up or down the target is (degrees)
            difference[2] = z = result.getTa(); // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", difference[0]);
            telemetry.addData("Target Y", difference[1]);
            telemetry.addData("Target Area", difference[2]);
        } else {
            telemetry.addData("Limelight", "No Targets");
        }


        return difference;
    }

    public boolean isAprilTagFound() {
        // will return true only if april tag is in sight.
        return true;
    }
}
