package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "OneRevTest", group = "Debug")
public class OneRevTest extends LinearOpMode {
    
    private DcMotor leftDriveMotor = null;
    private DcMotor rightDriveMotor = null;
    
    // Motor specs - adjust this value if your motor is different
    private static final double TICKS_PER_MOTOR_REV = 560; // Common for many FTC motors
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        leftDriveMotor = hardwareMap.get(DcMotor.class, "leftDriveMotor");
        rightDriveMotor = hardwareMap.get(DcMotor.class, "rightDriveMotor");
        
        // Set motor directions
        leftDriveMotor.setDirection(DcMotor.Direction.REVERSE);
        rightDriveMotor.setDirection(DcMotor.Direction.FORWARD);
        
        // Reset encoders
        leftDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        // Set motors to brake when power is zero
        leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        telemetry.addData("Status", "Ready to move one revolution");
        telemetry.addData("Motor Revolution", TICKS_PER_MOTOR_REV + " ticks");
        telemetry.addData("Press", "Play to start");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            telemetry.addData("=== STARTING ONE REVOLUTION TEST ===", "");
            telemetry.update();
            
            // Get starting positions
            int startLeft = leftDriveMotor.getCurrentPosition();
            int startRight = rightDriveMotor.getCurrentPosition();
            
            telemetry.addData("Starting Left Position", startLeft);
            telemetry.addData("Starting Right Position", startRight);
            telemetry.addData("Target Ticks", (int)TICKS_PER_MOTOR_REV);
            telemetry.update();
            sleep(2000);
            
            // Set target positions for one revolution
            int targetTicks = (int)TICKS_PER_MOTOR_REV;
            leftDriveMotor.setTargetPosition(startLeft + targetTicks);
            rightDriveMotor.setTargetPosition(startRight + targetTicks);
            
            // Switch to RUN_TO_POSITION mode
            leftDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDriveMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            // Start moving at moderate speed
            leftDriveMotor.setPower(0.5);
            rightDriveMotor.setPower(0.5);
            
            telemetry.addData("Status", "Moving forward one revolution...");
            telemetry.update();
            
            // Monitor movement
            while (opModeIsActive() && (leftDriveMotor.isBusy() || rightDriveMotor.isBusy())) {
                
                int currentLeft = leftDriveMotor.getCurrentPosition();
                int currentRight = rightDriveMotor.getCurrentPosition();
                int movedLeft = currentLeft - startLeft;
                int movedRight = currentRight - startRight;
                
                telemetry.addData("=== MOVING ===", "");
                telemetry.addData("Left Position", currentLeft);
                telemetry.addData("Right Position", currentRight);
                telemetry.addData("Left Moved", movedLeft);
                telemetry.addData("Right Moved", movedRight);
                telemetry.addData("Left Busy?", leftDriveMotor.isBusy() ? "YES" : "NO");
                telemetry.addData("Right Busy?", rightDriveMotor.isBusy() ? "YES" : "NO");
                telemetry.update();
                
                sleep(100);
            }
            
            // Stop motors
            leftDriveMotor.setPower(0);
            rightDriveMotor.setPower(0);
            
            // Get final positions and calculate results
            int finalLeft = leftDriveMotor.getCurrentPosition();
            int finalRight = rightDriveMotor.getCurrentPosition();
            int totalMovedLeft = finalLeft - startLeft;
            int totalMovedRight = finalRight - startRight;
            
            telemetry.addData("=== MOVEMENT COMPLETE ===", "");
            telemetry.addData("Target Ticks", targetTicks);
            telemetry.addData("Left Moved", totalMovedLeft);
            telemetry.addData("Right Moved", totalMovedRight);
            telemetry.addData("Left Error", totalMovedLeft - targetTicks);
            telemetry.addData("Right Error", totalMovedRight - targetTicks);
            
            // Check if movement was successful (within 50 ticks is pretty good)
            boolean leftSuccess = Math.abs(totalMovedLeft - targetTicks) < 50;
            boolean rightSuccess = Math.abs(totalMovedRight - targetTicks) < 50;
            
            telemetry.addData("Left Success?", leftSuccess ? "YES" : "NO");
            telemetry.addData("Right Success?", rightSuccess ? "YES" : "NO");
            telemetry.addData("Overall Success?", (leftSuccess && rightSuccess) ? "YES" : "NO");
            telemetry.update();
            
            // Switch back to normal encoder mode
            leftDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDriveMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            
            // Keep the program running so you can see results
            while (opModeIsActive()) {
                sleep(100);
            }
        }
    }
}
