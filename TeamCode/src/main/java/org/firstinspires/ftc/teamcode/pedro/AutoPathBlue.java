package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.api.Paths;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.pedropathing.paths.interpolator.Interpolator;

@Autonomous
public class AutoPathBlue extends LinearOpMode {

    private Follower follower;
    private LauncherPackage launcher;
    private Aim aim;

    private final PoseFactory poseFactory = PoseFactory.degrees();

    private final Pose start = poseFactory.of(104.4382, 133.0878, 90);
    private final Pose point1 = poseFactory.of(82.9835, 133.637, 0);
    private final Pose point1Segment1Start = poseFactory.of(82.9835, 133.637, 0);
    private final Pose point1Segment1End = poseFactory.of(82.9835, 133.637, 0);
    private final Pose point2 = poseFactory.of(82.8114, 115.3467, 89.461);


    // Autonomous routine
    public Command autoRoutine() {
        return sequential(
                follow(follower, path1()),
                shoot(),
                follow(follower, path2())
        );
    }

    @Override
    public void runOpMode() {
        aim = new Aim(hardwareMap, telemetry);
        launcher = new LauncherPackage(hardwareMap, gamepad1, telemetry, aim);
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

    public Path path1() {
        return Paths.line(start, point1).heading(Interpolator.piecewise().until(1, Interpolator.linear(point1Segment1Start, point1Segment1End)));
    }

    public Path path2() {
        return Paths.line(point1, point2).reverseTangent();
    }

    public Command shoot() {
        // return a command that does the state machine to go shoot
        return launcher.totalLaunch();
    }
}
