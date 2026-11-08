package org.firstinspires.ftc.teamcode.pedro;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LauncherPackage {

    private DcMotorEx launcher;
    private Servo hood;
    private Servo turret;
    private CRServo feeder;
    private double velocity = 1700;

    private Gamepad gamepad;

    private Command goLaunch;
    private Telemetry telemetry;
    private Aim aim;


    public LauncherPackage(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry, Aim aim, Constants.TeleopMode teleopMode) {
        this.launcher = hardwareMap.get(DcMotorEx.class,"launcher");
//        this.hood = hardwareMap.get(Servo.class, "hood");
        this.turret = hardwareMap.get(Servo.class, "turret");
        this.feeder = hardwareMap.get(CRServo.class, "feeder");
        this.gamepad = gamepad;
        this.telemetry = telemetry;
        this.aim = aim;

        this.goLaunch = totalLaunch();
        turret.setDirection(Servo.Direction.REVERSE);
        turret.setPosition(.5);


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

    public Command rotating() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                    // call aim.getRelativePosition() to get the x, y, z
                    double[] currentPose = aim.getRelativePosition();

                })
                .setExecute(() -> {
                    aim.getRelativePosition();
                    // move to the turret
//                Aim.hone(); //centers the hive into the middle
                })
                .setDone(() -> {
                    // return true to end the command
                    // when getRelativePosition() is within tolerance
                    if(Math.abs(aim.getXDegrees()) < 5) {
                        return true;
                    }
                    else {
                        return false;
                    }
//                return Aim.aligned(); //return true when aim is centered
                });
    }

    public Command feeding() {
        return Command.build()
                .setStart(() -> {
                    // executed on start
                    feeder.setPower(1);
                })
                .setExecute(() -> {
                })
                .setDone(() -> {
                    // return timer.
                    return true; //return true after 5s?
                });
    }

    public Command totalLaunch() {
        return sequential(
                rotating(),
                launching(),
                feeding(),
                waitMs(5000)
        );
    }

    public void correctTurret() {
        double error = aim.getXDegrees();
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

        if (gamepad.yWasPressed()) {
            if (goLaunch.isScheduled()) {
                telemetry.addLine("goLaunch has been scheduled");
            } else {
                goLaunch.schedule();
//                Scheduler.schedule(goLaunch);
                telemetry.addLine("schedule goLaunch");
            }
        }
        if(gamepad.xWasPressed()) {
            if(goLaunch.isScheduled()) {
                goLaunch.cancel();
            }
            launcher.setVelocity(0);
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
