package org.firstinspires.ftc.teamcode.pedro;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.race;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class IntakePackage {
    DcMotor intake;
    Telemetry telemetry;
    Gamepad gamepad;
    Command runIntakeFor;
    public IntakePackage(HardwareMap hardwareMap, Gamepad gamepad, Telemetry telemetry) {
        this.intake = hardwareMap.get(DcMotorEx.class,"intake");
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public void update() {
        intake.setPower(gamepad.right_trigger - gamepad.left_trigger);
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
}