package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    
    // FIXED: Declare both variables properly
    private int move_forward;
    private int move_backward;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        
        int x = 10;   // Distance in inches
        
        // Calculate ticks for x inches of movement
        move_forward = (int)(50.31 * x);   // Forward movement (positive ticks)
        move_backward = (int)(50.31 * x);  // FIXED: Backward uses positive ticks too
        
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
        
        // FIXED: Corrected telemetry string formatting
        telemetry.addData("Status", "Ready to move " + x + " inches");
        telemetry.addData("Forward Ticks", move_forward);
        telemetry.addData("Backward Ticks", move_backward);
        telemetry.addData("Ticks per inch", "50.31");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // Move forward x inches
            testTickMovement(move_forward, "Moving Forward (" + move_forward + " ticks = " + x + " inches)");
            
            // Pause between movements
            sleep(2000);
            
            // Move backward x inches (using negative ticks for backward direction)
            testTickMovement(-move_backward, "Moving Backward (-" + move_backward + " ticks = " + x + " inches)");
            
            telemetry.addData("=== MOVEMENT COMPLETE ===", "");
            telemetry.addData("Final Position Left", driveLeft.getCurrentPosition());
            telemetry.addData("Final Position Right", driveRight.getCurrentPosition());
            telemetry.addData("Expected Final Position", "Should be close to 0 (returned to start)");
            telemetry.update();
            
            // Keep running to display results
            while (opModeIsActive()) {
                sleep(100);
            }
        }
    }
    
    /**
     * Test movement with specific number of ticks
     */
    private void testTickMovement(int ticks, String testName) {
        
        telemetry.addData("=== STARTING TEST ===", "");
        telemetry.addData("Test", testName);
        telemetry.addData("Target Ticks", ticks);
        telemetry.update();
        
        // Get starting positions
        int startLeft = driveLeft.getCurrentPosition();
        int startRight = driveRight.getCurrentPosition();
        
        telemetry.addData("Starting Left", startLeft);
        telemetry.addData("Starting Right", startRight);
        telemetry.update();
        sleep(1000);
        
        // Try to move to target
        try {
            // Set target positions
            driveLeft.setTargetPosition(startLeft + ticks);
            driveRight.setTargetPosition(startRight + ticks);
            
            telemetry.addData("Target Left", startLeft + ticks);
            telemetry.addData("Target Right", startRight + ticks);
            telemetry.update();
            
            // Switch to RUN_TO_POSITION
            driveLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            // FIXED: Set appropriate power based on direction
            double power = (ticks > 0) ? 0.5 : -0.5;  // Positive for forward, negative for backward
            driveLeft.setPower(power);
            driveRight.setPower(power);
            
            telemetry.addData("Status", "Motors should be moving...");
            telemetry.addData("Left Power", driveLeft.getPower());
            telemetry.addData("Right Power", driveRight.getPower());
            telemetry.addData("Direction", (ticks > 0) ? "FORWARD" : "BACKWARD");
            telemetry.update();
            
            // Wait up to 5 seconds for movement
            int waitTime = 0;
            while (opModeIsActive() && (driveLeft.isBusy() || driveRight.isBusy()) && waitTime < 50) {
                
                int currentLeft = driveLeft.getCurrentPosition();
                int currentRight = driveRight.getCurrentPosition();
                
                telemetry.addData("Current Left", currentLeft);
                telemetry.addData("Current Right", currentRight);
                telemetry.addData("Left Moving?", driveLeft.isBusy() ? "YES" : "NO");
                telemetry.addData("Right Moving?", driveRight.isBusy() ? "YES" : "NO");
                telemetry.addData("Left Target", driveLeft.getTargetPosition());
                telemetry.addData("Right Target", driveRight.getTargetPosition());
                
                // Show progress
                int progressLeft = currentLeft - startLeft;
                int progressRight = currentRight - startRight;
                telemetry.addData("Progress Left", progressLeft + " / " + ticks);
                telemetry.addData("Progress Right", progressRight + " / " + ticks);
                telemetry.update();
                
                sleep(100);
                waitTime++;
            }
            
            // Stop motors
            driveLeft.setPower(0);
            driveRight.setPower(0);
            
            // Get final positions
            int finalLeft = driveLeft.getCurrentPosition();
            int finalRight = driveRight.getCurrentPosition();
            int movedLeft = finalLeft - startLeft;
            int movedRight = finalRight - startRight;
            
            telemetry.addData("=== TEST RESULTS ===", "");
            telemetry.addData("Target was", ticks);
            telemetry.addData("Left moved", movedLeft);
            telemetry.addData("Right moved", movedRight);
            telemetry.addData("Average moved", (movedLeft + movedRight) / 2);
            telemetry.addData("Success?", (Math.abs(movedLeft - ticks) < 50) ? "YES" : "NO");
            telemetry.update();
            
            // Switch back to normal mode
            driveLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            driveRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            
        } catch (Exception e) {
            telemetry.addData("ERROR", "Test failed: " + e.getMessage());
            telemetry.update();
        }
        
        sleep(3000); // Longer pause to see results
    }
}
