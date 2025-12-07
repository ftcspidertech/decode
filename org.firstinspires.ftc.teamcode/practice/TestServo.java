package org.firstinspires.ftc.teamcode.practice;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp

public class TestServo extends LinearOpMode {
    // Variables
    private CRServo servo;
    
    @Override
    public void runOpMode() {
        //control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        servo = hardwareMap.get(CRServo.class, "servo");
        servo.setPower(-0.25);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            servo.setPower(1);
            //telemetry.addData("Servo Position",servo.getPosition());
            telemetry.addData("Status", "Full Rotation started ...");
            telemetry.update();
        }
    }
}
