package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name = "MecanumDriveTest", group = "Linear OpMode")
public class MecanumDriveTest extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note the strings used here as parameters
        // to 'hardwareMap.get' must correspond to the names assigned in the robot configuration
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");

        // Most robots need the motors on one side to be reversed to drive forward
        // Reverse the motor that runs backward when called forwards
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        // Optional: Set motor modes
        setMotorRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // POV Mode uses left stick to go forward & strafe, and right stick to rotate.
            double y = -gamepad1.left_stick_y; // Remember, Y stick is reversed on gamepad
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            
            if(x!=0 || y!=0 || rx!=0){
                // Combine the joystick requests for each axis-motion to determine each wheel's power.
                // The math is as follows:
                // frontLeftPower = drive + strafe + turn
                // backLeftPower = drive - strafe + turn
                // frontRightPower = drive - strafe - turn
                // backRightPower = drive + strafe - turn
    
                double frontLeftPower = y + x + rx;
                double backLeftPower = y - x + rx;
                double frontRightPower = y - x - rx;
                double backRightPower = y + x - rx;
    
                // Normalize the values so the maximum is always 1.0, ensuring the power
                // ratio is maintained while preventing motor power from exceeding 100%
                double denominator = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
                denominator = Math.max(denominator, Math.abs(frontRightPower));
                denominator = Math.max(denominator, Math.abs(backRightPower));
                denominator = Math.max(denominator, 1.0); // Ensure denominator is at least 1.0
    
                frontLeftMotor.setPower(frontLeftPower / denominator);
                backLeftMotor.setPower(backLeftPower / denominator);
                frontRightMotor.setPower(frontRightPower / denominator);
                backRightMotor.setPower(backRightPower / denominator);
    
                // Telemetry to debug
                telemetry.addData("Status", "Running");
                telemetry.addData("Front Left Power", frontLeftPower / denominator);
                telemetry.addData("Back Left Power", backLeftPower / denominator);
                telemetry.addData("Front Right Power", frontRightPower / denominator);
                telemetry.addData("Back Right Power", backRightPower / denominator);
                telemetry.update();
            }else{
                frontLeftMotor.setPower(0);
                backLeftMotor.setPower(0);
                frontRightMotor.setPower(0);
                backRightMotor.setPower(0);
            }

        }
    }

    private void setMotorRunMode(DcMotor.RunMode mode) {
        frontLeftMotor.setMode(mode);
        backLeftMotor.setMode(mode);
        frontRightMotor.setMode(mode);
        backRightMotor.setMode(mode);
    }
}
