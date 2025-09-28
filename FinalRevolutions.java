package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    
    // Declare all movement variables separately
    private int move_forward;
    private int move_backward;
    private int move_left;
    private int move_right;
    private int turn_left;
    private int turn_right;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        
        double revolutions = 2.5;   
        int degrees = 45;
        
        // Calculate ticks for revolutions of movement (different for each direction)
        move_forward = (int)(560 * revolutions);   // Forward movement
        move_backward = (int)(560 * revolutions);  // Backward movement
        move_left = (int)(560 * revolutions);      // Left movement (strafing if applicable)
        move_right = (int)(560 * revolutions);     // Right movement (strafing if applicable)
        
        // Calculate ticks for turning
        int ticks_for_degrees = (int)(5600 * degrees / 360);
        turn_left = ticks_for_degrees;
        turn_right = ticks_for_degrees;
        
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
        
        // Display movement variables
        telemetry.addData("Status", "Ready to move " + revolutions + " revolutions and turn " + degrees + "°");
        telemetry.addData("Forward Ticks", move_forward);
        telemetry.addData("Backward Ticks", move_backward);
        telemetry.addData("Left Ticks", move_left);
        telemetry.addData("Right Ticks", move_right);
        telemetry.addData("Turn Left Ticks", turn_left);
        telemetry.addData("Turn Right Ticks", turn_right);
        telemetry.addData("Formula Used", "5600 * (" + degrees + "/360) = " + ticks_for_degrees);
        telemetry.addData("Ticks per revolution", "560");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // 1. Move forward 2.5 revolutions
            testTickMovement(move_forward, "1. Moving Forward (" + move_forward + " ticks = " + revolutions + " revolutions)");
            sleep(2000);
            
            // 2. Turn left using turn_left variable
            testTurnMovement(-turn_left, turn_left, "2. Turning Left " + degrees + "°");
            sleep(2000);
            
            // 3. Turn right using turn_right variable (back to original heading)
            testTurnMovement(turn_right, -turn_right, "3. Turning Right " + degrees + "°");
            sleep(2000);
            
            // 4. Move backward 2.5 revolutions using move_backward variable
            testTickMovement(-move_backward, "4. Moving Backward (" + move_backward + " ticks = " + revolutions + " revolutions)");
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
