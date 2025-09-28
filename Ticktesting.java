package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        
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
        
        telemetry.addData("Status", "Ready to test different tick amounts");
        telemetry.addData("Test Plan", "Will try: 100, 500, 1000, 2000 ticks");
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // Test 1: Very small number of ticks
            testTickMovement(100, "Test 1: 100 ticks");
            sleep(2000);
            
            // Test 2: Small number
            testTickMovement(500, "Test 2: 500 ticks");
            sleep(2000);
            
            // Test 3: Medium number  
            testTickMovement(1000, "Test 3: 1000 ticks");
            sleep(2000);
            
            // Test 4: Larger number
            testTickMovement(2000, "Test 4: 2000 ticks");
            sleep(2000);
            
            // Test 5: Try the original 3-meter calculation
            double TICKS_PER_MOTOR_REV = 560;
            double WHEEL_DIAMETER_CM = 3.5;
            double WHEEL_CIRCUMFERENCE_CM = WHEEL_DIAMETER_CM * Math.PI;
            double TICKS_PER_CM = TICKS_PER_MOTOR_REV / WHEEL_CIRCUMFERENCE_CM;
            int ticksFor3Meters = (int)(300 * TICKS_PER_CM);
            
            testTickMovement(ticksFor3Meters, "Test 5: Calculated 3m (" + ticksFor3Meters + " ticks)");
            
            telemetry.addData("=== ALL TESTS COMPLETE ===", "");
            telemetry.addData("Final Position Left", driveLeft.getCurrentPosition());
            telemetry.addData("Final Position Right", driveRight.getCurrentPosition());
            telemetry.update();
            
            // Keep running
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
            
            // Switch to RUN_TO_POSITION
            driveLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            driveRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            
            // Start moving
            driveLeft.setPower(0.5);
            driveRight.setPower(0.5);
            
            telemetry.addData("Status", "Motors should be moving...");
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
            telemetry.addData("Success?", (Math.abs(movedLeft - ticks) < 100) ? "YES" : "NO");
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
