package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class AutoPathRed extends LinearOpMode {

    private Follower follower;
    private LauncherPackage launcher;
    private Aim aim;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(80.9651, 132.5555, 90);
    private final Pose redPath = poseFactory.of(114.9433, 78.2552, 90);
    private final Pose redControl1 = poseFactory.of(111.9018, 109.2997, 0);
    private final Pose point2Start = poseFactory.of(114.9433, 78.2552, 90);
    private final Pose point2 = poseFactory.of(130.654, 26.8795, 180);


    // Autonomous routine
    public Command autoRoutine() {
        return deadline(
                sequential(
                        launcher.autoTotalLaunch(),
                        follow(follower, redPath()),
                        follow(follower, path2())
                ),
                Groups.loop(launcher.turretCorrecting())

        );
    }

    @Override
    public void runOpMode() {
        aim = new Aim(hardwareMap, telemetry, Constants.TeleopMode.RED);
        launcher = new LauncherPackage(hardwareMap, gamepad1, telemetry, aim, Constants.TeleopMode.RED);
        Scheduler.reset();
        follower = Constants.create(hardwareMap);
        follower.setPose(start);
        follower.update();

        waitForStart();
        schedule(autoRoutine());

        while (opModeIsActive()) {
            follower.update();
            Scheduler.execute();
            aim.update();
            launcher.update();

            telemetry.addData("x", follower.pose().x());
            telemetry.addData("y", follower.pose().y());
            telemetry.addData("heading", follower.pose().heading());

            if (follower.currentPath() != null) {
                telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
                telemetry.addData("Path number", follower.pathIndex());
            }

            telemetry.update();
        }
    }

    public Path redPath() {
        return Paths.curve(start, redControl1, redPath).linear(start, redPath);
    }

    public Path path2() {
        return Paths.line(point2Start, point2).linear(point2Start, point2);
    }


}
