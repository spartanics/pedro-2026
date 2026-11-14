package org.firstinspires.ftc.teamcode.pedro;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.race;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class IntakePackage {
    DcMotor intake;
    Servo flower;
    Telemetry telemetry;
    Gamepad gamepad;
    Command runIntakeFor;
    public IntakePackage(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry) {
        this.intake = hardwareMap.get(DcMotorEx.class,"intake");
        this.flower = hardwareMap.get(Servo.class, "flower");
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public void update() {
        intake.setPower(gamepad.right_trigger - gamepad.left_trigger);
        if(gamepad.rightBumperWasPressed()) {
            flower.setPosition(1);
        } else if(gamepad.leftBumperWasPressed()) {
            flower.setPosition(0);
        }

        telemetry.addData("Intake Power", intake.getPower());
    }

    public Command runIntakeFor(float seconds) {
        return race(
                waitMs(seconds * 1000),
                runIntake()
        );
    }
    public Command runIntake() {

        return Command.build()
                .setStart(() -> {
                    // executed on start
                    intake.setPower(1);
                })
                .setExecute(() -> {
                })
                .setDone(() -> {
                    return false;
                })
                .setEnd((endCondition) -> {
                    intake.setPower(0);
                });

    }

    public Command runFlower() {

        return Command.build()
                .setStart(() -> {
                    // executed on start
                    flower.setPosition(1);
                })
                .setExecute(() -> {
                })
                .setDone(() -> {
                    return gamepad.rightBumperWasPressed();
                })
                .setEnd((endCondition) -> {
                    flower.setPosition(0);
                });

    }
}