

package org.firstinspires.ftc.teamcode.pedro;


import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import static com.pedropathing.ivy.Scheduler.schedule;


import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@Autonomous
public class NEWredAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

    }
    //    private Follower follower;
//    private LauncherPackage launcher;
//    private Aim aim;
//
//
//        private final PoseFactory poseFactory = PoseFactory.degrees();
//
//        private final Pose start = poseFactory.of(82.9693, 136.2815, 90);
//        private final Pose bLuepath = poseFactory.of(82.8596, 115.4884, -90.3023);
//        private final Pose point2Start = poseFactory.of(82.8596, 115.4884, 0);
//        private final Pose point2 = poseFactory.of(121.2348, 85.6138, 0);
//        private final Pose point3 = poseFactory.of(126.5015, 41.8754, 0);
//
//        public Path bLuepath() {
//            return Paths.line(start, bLuepath).tangent();
//        }
//
//        public Path path2() {
//            return Paths.line(point2Start, point2).linear(point2Start, point2);
//        }
//
//        // Autonomous routine
//        public Command autoRoutine() {
//            return sequential(
//                    follow(follower, path1()),
//                    launcher.totalLaunch(),
//                    follow(follower, path2())
//            );
//        }
//
//        @Override
//        public void runOpMode() {
//            aim = new Aim(hardwareMap, telemetry);
//            launcher = new LauncherPackage(hardwareMap, gamepad1, telemetry, aim);
//            Scheduler.reset();
//            follower = Constants.create(hardwareMap);
//            follower.setPose(start);
//            follower.update();
//
//            waitForStart();
//            schedule(autoRoutine());
//
//            while (opModeIsActive()) {
//                follower.update();
//                Scheduler.execute();
//                aim.update();
//                launcher.update();
//
//                telemetry.addData("x", follower.pose().x());
//                telemetry.addData("y", follower.pose().y());
//                telemetry.addData("heading", follower.pose().heading());
//
//                if (follower.currentPath() != null) {
//                    telemetry.addData("Current path distance remaining", follower.distanceToEndpoint());
//                    telemetry.addData("Path number", follower.pathIndex());
//                }
//
//                telemetry.update();
//            }
//        }
//
//        public Path path1() {
//            return Paths.line(start, point1).heading(Interpolator.piecewise().until(1, Interpolator.linear(point1Segment1Start, point1Segment1End)));
//        }
//
//        public Path path2() {
//            return Paths.line(point1, point2).reverseTangent();
//        }

}
