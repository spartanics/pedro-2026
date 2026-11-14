package org.firstinspires.ftc.teamcode.pedro;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LauncherPackage {

    private DcMotorEx launcher;
    private Servo hood;
    private Servo turret;
    private Servo angular;
    private CRServo feeder;
    private CRServo back_feed;
    private double velocity = 1700;

    private Gamepad gamepad;

    private Command teleGoLaunch;
    private Telemetry telemetry;
    private Aim aim;


    public LauncherPackage(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry, Aim aim, Constants.TeleopMode teleopMode) {
        this.launcher = hardwareMap.get(DcMotorEx.class,"launcher");
//        this.hood = hardwareMap.get(Servo.class, "hood");
        this.turret = hardwareMap.get(Servo.class, "turret");
        this.feeder = hardwareMap.get(CRServo.class, "feeder");
        this.back_feed = hardwareMap.get(CRServo.class, "back_feed");
        this.angular = hardwareMap.get(Servo.class, "angular");
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.aim = aim;

        this.teleGoLaunch = totalLaunch();
        turret.setDirection(Servo.Direction.REVERSE);
        turret.setPosition(.5);
        this.angular.setPosition(0.5);
        back_feed.setDirection(DcMotorSimple.Direction.REVERSE);


    }

    public Command angle() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                })
                .setExecute(() -> {
                    angular.setPosition(0.5);
                })
                .setDone(() -> {
                    // return true to end the command
                    return true;
                });
    }

    public Command launching() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                })
                .setExecute(() -> {
                    launcher.setVelocity(velocity);
                })
                .setDone(() -> {
                    // return true to end the command
                    return (Math.abs(launcher.getVelocity() - velocity) < 50);
                });
    }

    public Command stopLaunching() {
        return Command.build()
                .setStart(() -> {
                    launcher.setVelocity(0);
                })
                .setExecute(() -> {

                })
                .setDone(() -> {
                    // return true to end the command
                    return true;
                });
    }


    public Command feeding() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                    feeder.setPower(1);
                    back_feed.setPower(1);
                })
                .setExecute(() -> {
                })
                .setDone(() -> {
                    // return timer.
                    return true; //return true after 5s?
                });
    }

    public Command stopFeeding() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                    feeder.setPower(0);
                    back_feed.setPower(0);
                })
                .setExecute(() -> {
                })
                .setDone(() -> {
                    // return timer.
                    return true; //return true after 5s?
                });
    }

    public Command turretCorrecting() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                })
                .setExecute(() -> {
                    correctTurret();
                })
                .setDone(() -> {
                    // return timer.
                    return true;
                });
    }

    public Command totalLaunch() {
        return deadline(
                    sequential(
                        //rotating(),
                        launching(),
                        feeding()
                        //waitMs(5000)
            ),
                angle()
        );
    }

    public Command autoTotalLaunch() {
        return sequential(
                //rotating(),
                launching(),
                feeding(),
                waitMs(20000),
                stopLaunching(),
                stopFeeding()
        );
    }



    public void correctTurret() {
        aim.update();
        double error = aim.getXDegrees();
        //Xdegrees is relative to the april tag
        //if(aprilTagIsSeen)
        //straight - xDegrees
        //Pose aprilPose = new Pose(xCoord, yCoord, );
        // if negative move to the left
        // if positive move to the right
        double k_p = 0.0005; //0.0001
        double movement = k_p * error;
        double pos = turret.getPosition() + movement;
        pos = Math.max(Math.min(pos, 1), 0);

        turret.setPosition(pos);
    }

    public void update() {
        correctTurret();
        angular.setPosition(0.5);

        if (gamepad.yWasPressed()) {
            if (teleGoLaunch.isScheduled()) {
                telemetry.addLine("goLaunch has been scheduled");
            } else {
                teleGoLaunch.schedule();
//                Scheduler.schedule(goLaunch);
                telemetry.addLine("schedule goLaunch");
            }
        }
        if(gamepad.xWasPressed()) {
            if(teleGoLaunch.isScheduled()) {
                teleGoLaunch.cancel();
            }
            launcher.setVelocity(0);
            feeder.setPower(0);
            back_feed.setPower(0);
        }

        if(gamepad.aWasPressed()) {

            feeder.setPower(-1);
            back_feed.setPower(1);
        }

        if (gamepad.dpadDownWasPressed()) {
            velocity -= 10;
            if (velocity < 600) {
                velocity = 600;
            }
        }

        if (gamepad.dpadUpWasPressed()) {
            velocity += 10;
            if (velocity > 2700) {
                velocity = 2700;
            }
        }

        telemetry.addLine("launcher.update");
        telemetry.addData("turret position: ","%.2f", turret.getPosition());
        telemetry.addData("velocity: ", "%.0f", velocity);
    }
}
