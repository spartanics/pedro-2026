package org.firstinspires.ftc.teamcode.pedro;




import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;
import java.util.function.BooleanSupplier;

public class Aim {

    private Limelight3A limelight;
    private boolean aligned;
    Telemetry telemetry;
    Constants.TeleopMode teleopMode;

    double x, y, z;

    public Aim(HardwareMap hardwareMap, Telemetry telemetry, Constants.TeleopMode teleopMode) {
        this.telemetry = telemetry;
        this.limelight = hardwareMap.get(Limelight3A.class, "limelight"); // or logitech Webcam
        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)
        limelight.start();
        limelight.pipelineSwitch(0);
        this.teleopMode = teleopMode;
    }


    public void update() {
        if (this.teleopMode == Constants.TeleopMode.RED ) {
            limelight.pipelineSwitch(2);
        }
        if (this.teleopMode == Constants.TeleopMode.BLUE) {
            limelight.pipelineSwitch(0);
        }

        getRelativePosition();
    }

    public boolean isResultMatchWithTeleopMode(LLResult result) {
        String ids = "";
        List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
        for (LLResultTypes.FiducialResult fiducial : fiducialResults) {
            int tagId = fiducial.getFiducialId(); // The numeric ID of the tag
            ids += tagId + " ";

            if (this.teleopMode == Constants.TeleopMode.RED && tagId <= 37) {
                return true;
            }
            if (this.teleopMode == Constants.TeleopMode.BLUE && tagId > 37) {
                return true;
            }

        }
        telemetry.addData("teleopMode: ", teleopMode);
        telemetry.addData("ID: ", "%s", ids);
        return false;

    }
    public double[] getRelativePosition() {
        double[] difference = {x, y, z}; //x = left right deg y = degrees up and down z = size

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid() && isResultMatchWithTeleopMode(result)) {
            difference[0] = x = result.getTx(); // How far left or right the target is (degrees)
            difference[1] = y = result.getTy(); // How far up or down the target is (degrees)
            difference[2] = z = result.getTa(); // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", difference[0]);
            telemetry.addData("Target Y", difference[1]);
            telemetry.addData("Target Area", difference[2]);
        } else {
            x = 0;
            y = 0;
            z = 0;
            telemetry.addData("Limelight", "No Targets");
            telemetry.addData("result:", "%b/%b", (result != null), (result != null && result.isValid()));

        }


        return difference;
    }

    public boolean isAprilTagFound() {
        // will return true only if april tag is in sight.
        return true;
    }

    public double getXDegrees() {
        return x;
    }
}
