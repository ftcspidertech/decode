package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    
    // Declare all variables properly
    private int move_forward;
    private int move_backward;
    private int turn_left_90;
    private int turn_right_90;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        
        int x = 10;   
        int degrees = 45;
        
        // Calculate ticks for x inches of movement
        move_forward = (int)(50.31 * x);   // Forward movement
        move_backward = (int)(50.31 * x);  // Backward movement
        
        // FIXED: Calculate ticks for degrees using your formula
        int ticks_for_degrees = (int)(5600* degrees / 360);
        turn_left_90 = ticks_for_degrees;
        turn_right_90 = ticks_for_degrees;
        
        // Set motor directions
        driveLeft.setDirection(DcMotor.Direction.REVERSE);
        driveRight.setDirection(DcMotor.Direction.FORWARD);
        
        // Reset encoders
        driveLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        driveRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        // Set motors to brake when power is zero
        driveLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        driveRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // FIXED: Corrected telemetry
        telemetry.addData("Status", "Ready to move " + x + " inches and turn " + degrees + "°");
        telemetry.addData("Forward Ticks", move_forward);
        telemetry.addData(degrees + "° Turn Ticks", ticks_for_degrees);
        telemetry.addData("Formula Used", "560 * (" + degrees + "/360) = " + ticks_for_degrees);
        telemetry.addData("Ticks per inch", "50.31");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // 1. Move forward x inches
            testTickMovement(move_forward, "1. Moving Forward (" + move_forward + " ticks = " + x + " inches)");
            sleep(2000);
            
            // 2. Turn left 90 degrees
            testTurnMovement(-turn_left_90, turn_left_90, "2. Turning Left " + degrees + "°");
            sleep(2000);
            
            // 3. Turn right 90 degrees (back to original heading)
            testTurnMovement(turn_right_90, -turn_right_90, "3. Turning Right " + degrees + "°");
            sleep(2000);
            
            // 4. Move backward x inches
            testTickMovement(-move_backward, "4. Moving Backward (" + move_backward + " ticks = " + x + " inches)");
            sleep(2000);
            
            telemetry.addData("=== MOVEMENT COMPLETE ===", "");
            telemetry.addData("Final Position Left", driveLeft.getCurrentPosition());
            telemetry.addData("Final Position Right", driveRight.getCurrentPosition());
            telemetry.addData("Expected Final Position", "Should be close to 0 (back at start)");
            telemetry.update();
            
            // Keep running to display results
            while (opModeIsActive()) {
                sleep(100);
            }
        }
    }
    
    /**
     * Move both motors in the same direction (forward/backward)
     */
    private void testTickMovement(int ticks, String testName) {
        
        telemetry.addData("=== STARTING TEST ===", "");
        telemetry.addData("Test", testName);
        telemetry.addData("Target Ticks", ticks);
        telemetry.update();
        
        // Get starting positions
        int startLeft = driveLeft.getCurrentPosition();
        int startRight = driveRight.getCurrentPosition();
        
        try {
            // Set target positions (both motors same direction)
            driveLeft.setTargetPosition(startLeft + ticks);
            driveRight.setTargetPosition(startRight + ticks);
            
            // Switch to RUN_TO_POSITION
            driveLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            // Set power based on direction
            double power = (ticks > 0) ? 0.5 : -0.5;
            driveLeft.setPower(power);
            driveRight.setPower(power);
            
            // Wait for movement to complete
            while (opModeIsActive() && (driveLeft.isBusy() || driveRight.isBusy())) {
                telemetry.addData("Status", "Moving...");
                telemetry.addData("Left Pos", driveLeft.getCurrentPosition());
                telemetry.addData("Right Pos", driveRight.getCurrentPosition());
                telemetry.addData("Direction", (ticks > 0) ? "FORWARD" : "BACKWARD");
                telemetry.update();
                sleep(100);
            }
            
            // Stop motors
            driveLeft.setPower(0);
            driveRight.setPower(0);
            
            // Switch back to normal mode
            driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            
            telemetry.addData("Movement Complete!", "");
            telemetry.update();
            
        } catch (Exception e) {
            telemetry.addData("ERROR", e.getMessage());
            telemetry.update();
        }
    }
    
    /**
     * Turn robot by moving motors in opposite directions
     * @param leftTicks - ticks for left motor
     * @param rightTicks - ticks for right motor  
     * @param testName - name of the test
     */
    private void testTurnMovement(int leftTicks, int rightTicks, String testName) {
        
        telemetry.addData("=== STARTING TURN ===", "");
        telemetry.addData("Test", testName);
        telemetry.addData("Left Ticks", leftTicks);
        telemetry.addData("Right Ticks", rightTicks);
        telemetry.update();
        
        // Get starting positions
        int startLeft = driveLeft.getCurrentPosition();
        int startRight = driveRight.getCurrentPosition();
        
        try {
            // Set target positions for turning
            driveLeft.setTargetPosition(startLeft + leftTicks);
            driveRight.setTargetPosition(startRight + rightTicks);
            
            // Switch to RUN_TO_POSITION
            driveLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            // Set power for turning (same power magnitude, motors go opposite directions)
            driveLeft.setPower(0.4);   // Slower for more precise turning
            driveRight.setPower(0.4);
            
            // Wait for turn to complete
            while (opModeIsActive() && (driveLeft.isBusy() || driveRight.isBusy())) {
                telemetry.addData("Status", "Turning...");
                telemetry.addData("Left Pos", driveLeft.getCurrentPosition());
                telemetry.addData("Right Pos", driveRight.getCurrentPosition());
                telemetry.addData("Left Target", driveLeft.getTargetPosition());
                telemetry.addData("Right Target", driveRight.getTargetPosition());
                telemetry.update();
                sleep(100);
            }
            
            // Stop motors
            driveLeft.setPower(0);
            driveRight.setPower(0);
            
            // Switch back to normal mode
            driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            
            telemetry.addData("Turn Complete!", "");
            telemetry.update();
            
        } catch (Exception e) {
            telemetry.addData("TURN ERROR", e.getMessage());
            telemetry.update();
        }
    }
}
