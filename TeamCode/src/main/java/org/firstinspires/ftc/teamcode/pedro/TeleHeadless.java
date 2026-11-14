package org.firstinspires.ftc.teamcode.pedro;


import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;

@TeleOp
public class TeleHeadless extends LinearOpMode {

    LauncherPackage launcher;
    Aim aim;
    IntakePackage intake;
    Constants.TeleopMode teleopMode = Constants.TeleopMode.NOT_SET;
    //
    Follower follower;

    @Override
    public void runOpMode() {

        while(teleopMode == Constants.TeleopMode.NOT_SET) {
            telemetry.addLine("Press x for blue, and y for red");
            telemetry.update();
            if(gamepad1.xWasPressed()) {
                teleopMode = Constants.TeleopMode.BLUE;
            }
            else if(gamepad1.yWasPressed()) {
                teleopMode = Constants.TeleopMode.RED;
            }
        }
        telemetry.addLine(teleopMode + " is chosen");
        telemetry.update();

        aim = new Aim(hardwareMap, telemetry, teleopMode);
        launcher = new LauncherPackage(hardwareMap, gamepad1, telemetry, aim, teleopMode);

        intake = new IntakePackage(hardwareMap, gamepad1, telemetry);
        //Since the scheduler is static, we need to reset it before each OpMode
        //so commands don't carry over from one OpMode to the next
        Scheduler.reset();

        follower = Constants.create(hardwareMap);


        waitForStart();

        while (opModeIsActive()) {
            // Run the scheduler each loop
            Scheduler.execute();
            launcher.update();
            intake.update();
            aim.update();

            if(gamepad1.startWasPressed()) {
                follower.setPose(new Pose(0, 0, Math.toRadians(90)));
            }

            DrivePowers powers = ManualDrive.fieldCentric(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    follower.pose().heading()
            );
            follower.manual(powers);
            follower.update();

            telemetry.addLine("fresh");
            telemetry.update();
        }
    }
}