package org.firstinspires.ftc.teamcode.biobuzz.tests;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTCLib Mecanum Drive")
public class MecanumDriveOpMode extends LinearOpMode {

    // Define the 4 drive motors using FTCLib wrappers
    private Motor frontLeft, frontRight, backLeft, backRight;

    // Define the FTCLib Mecanum drive base object
    private MecanumDrive driveBase;

    // Define the FTCLib enhanced gamepad wrapper
    private GamepadEx driverGamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        // 1. Initialize motors using configuration names from your robot controller
        frontLeft  = new Motor(hardwareMap, "frontLeft");
        frontRight = new Motor(hardwareMap, "frontRight");
        backLeft   = new Motor(hardwareMap,"backLeft");
        backRight  = new Motor(hardwareMap, "backRight");

        // Left pair
        frontLeft.setInverted(false);
        backRight.setInverted(true);
        // Right pair
        frontRight.setInverted(true);
        backLeft.setInverted(true);

        // 2. Set zero power behavior (BRAKE prevents the robot from drifting when you let go)
        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        // 3. Initialize the MecanumDrive object (Pass the motors in order)
        driveBase = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);

        // 4. Wrap the hardware gamepad in FTCLib's GamepadEx
        driverGamepad = new GamepadEx(gamepad1);

        telemetry.addData("Status", "Initialized. Waiting for start...");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // 5. Drive the robot using joystick inputs
            // driveRobotRobotCentric arguments: (strafeSpeed, forwardSpeed, turnSpeed)
            driveBase.driveRobotCentric(
                    0.2*driverGamepad.getLeftX(),   // Left stick X handles strafing sideways
                    0.2*driverGamepad.getLeftY(),   // Left stick Y handles forward/backward
                    0.2*driverGamepad.getRightX()   // Right stick X handles rotation
            );

            // Optional: Provide telemetry output
            telemetry.addData("Status", "Running");
            telemetry.addData("Strafe Input", driverGamepad.getLeftX());
            telemetry.addData("Forward Input", driverGamepad.getLeftY());
            telemetry.addData("Turn Input", driverGamepad.getRightX());
            telemetry.update();
        }
    }
}
