package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    
    // FIXED: Declare variable properly
    private int move_forward;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        int x = inches;      //Inches need to be inputted before running code
        // FIXED: Calculate ticks for 1 inch movement (moved after motor initialization)
        move_forward = (int)(50.31 * x);
        
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
        
        // FIXED: Updated telemetry to show single test
        telemetry.addData("Status", "Ready to move 1 inch");
        telemetry.addData("Movement", move_forward + " ticks = 1 inch");
        telemetry.addData("Ticks per inch", "50.31 (DUO Omni 90mm)");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // Only test the 1-inch movement (50.31 ticks)
            testTickMovement(move_forward, "Moving ("+ move_forward + " ticks or "+ x +"inches");
            
            telemetry.addData("=== MOVEMENT COMPLETE ===", "");
            telemetry.addData("Final Position Left", driveLeft.getCurrentPosition());
            telemetry.addData("Final Position Right", driveRight.getCurrentPosition());
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
            
            // Start moving with power
            driveLeft.setPower(0.5);
            driveRight.setPower(0.5);
            
            telemetry.addData("Status", "Motors should be moving...");
            telemetry.addData("Left Power", driveLeft.getPower());
            telemetry.addData("Right Power", driveRight.getPower());
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
        
        sleep(2000); // Pause between tests
    }
}
