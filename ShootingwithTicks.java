package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.BNO055IMU;

@Autonomous(name = "MyFIRSTJavaOpMode", group = "Debug")
public class MyFIRSTJavaOpMode extends LinearOpMode {
    
    private DcMotor driveLeft = null;
    private DcMotor driveRight = null;
    private Servo actuator = null;  // Servo for actuator mechanism
    private DcMotor shootwheel = null;  // Motor for shooting artifacts
    private Servo artifactstopper = null;  // Servo to control artifact flow
    private ColorSensor color1 = null;
    private DistanceSensor distance1 = null;
    private BNO055IMU imu = null;
    
    // Declare movement variables
    private int move_forward;
    private int move_backward;
    private int turn_left;
    private int turn_right;
    
    // Shooting variables
    private double shootPower = 0.8;
    private boolean isShooting = false;
    private int nArtifacts;
    
    @Override
    public void runOpMode() {
        
        // Initialize motors and servos
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        actuator = hardwareMap.get(Servo.class, "actuator");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        
        int x = 90;   // Distance for moving forward (inches)
        int y = 90;  // Distance for moving backward (inches)
        int degrees_left = 45;   // Degrees for left turn
        int degrees_right = 45;  // Degrees for right turn
        
        // Actuator positions
        double actuator_retracted = 0.0;  // Fully retracted position
        double actuator_extended = 1.0;   // Fully extended position
        
        // Calculate ticks for movement
        move_forward = (int)(50.31 * x);   // Forward movement
        move_backward = (int)(50.31 * y);  // Backward movement
        
        // Calculate ticks for turning
        int ticks_for_left_turn = (int)(5600 * degrees_left / 360);
        int ticks_for_right_turn = (int)(5600 * degrees_right / 360);
        turn_left = ticks_for_left_turn;
        turn_right = ticks_for_right_turn;
        
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
        
        // Initialize actuator to retracted position
        actuator.setPosition(actuator_retracted);
        
        // Initialize shooting mechanism
        isShooting = false;
        artifactstopper.setPosition(0.2);  // Block artifacts initially
        
        // Display movement variables
        telemetry.addData("Status", "Ready to move forward " + x + " inches, backward " + y + " inches");
        telemetry.addData("Forward Ticks", move_forward + " (for " + x + " inches)");
        telemetry.addData("Backward Ticks", move_backward + " (for " + y + " inches)");
        telemetry.addData("Turn Left Degrees", degrees_left + "° = " + turn_left + " ticks");
        telemetry.addData("Turn Right Degrees", degrees_right + "° = " + turn_right + " ticks");
        telemetry.addData("Left Turn Formula", "5600 * (" + degrees_left + "/360) = " + ticks_for_left_turn);
        telemetry.addData("Right Turn Formula", "5600 * (" + degrees_right + "/360) = " + ticks_for_right_turn);
        telemetry.addData("Ticks per inch", "50.31");
        telemetry.addData("Actuator Retracted", actuator_retracted);
        telemetry.addData("Actuator Extended", actuator_extended);
        telemetry.addData("Current Actuator Pos", actuator.getPosition());
        telemetry.addData("Shoot Power", shootPower);
        telemetry.addData("Artifact Stopper Pos", artifactstopper.getPosition());
        telemetry.update();
        
        // Wait for start
        waitForStart();
        
        if (opModeIsActive()) {
            
            // 1. Move forward x inches
            testTickMovement(move_forward, "1. Moving Forward (" + move_forward + " ticks = " + x + " inches)");
            sleep(1000);
            
            
            // 4. Turn left using turn_left variable
            testTurnMovement(-turn_left, turn_left, "2. Turning Left " + degrees_left + "°");
            sleep(1000);
            
            // 5. Shoot artifacts after positioning
            shootThreeArtifacts();
            sleep(1000);
            
            // 6. Move forward to second position
            testTickMovement(move_forward/2, "3. Moving to Second Position");
            sleep(1000);
            
            // 7. Deploy actuator to place second artifact
            deployActuator(actuator_extended, "4. Deploying Actuator for Second Artifact");
            sleep(2000);
            
            // 8. Retract actuator
            retractActuator(actuator_retracted, "5. Retracting Actuator");
            sleep(1000);
            
            testTurnMovement(turn_right, turn_right, "6. Turning Right " + degrees_left + "°");
            sleep(1000);
            
            testTickMovement(move_backward, "7. Moving Bckward (" + move_backward+ " ticks = " + y + " inches)");
            sleep(1000);
            
            telemetry.addData("=== MOVEMENT COMPLETE ===", "");
            telemetry.addData("Final Position Left", driveLeft.getCurrentPosition());
            telemetry.addData("Final Position Right", driveRight.getCurrentPosition());
            telemetry.addData("Final Actuator Position", actuator.getPosition());
            telemetry.addData("Artifacts Deployed", "2 artifacts successfully placed");
            telemetry.addData("Artifacts Shot", "3 artifacts successfully fired");
            telemetry.update();
            
            // Keep running to display results
            while (opModeIsActive()) {
                sleep(100);
            }
        }
    }
    
    // MAIN SHOOTING FUNCTION
    public void shoot(){
        // Don't move while shooting
        driveLeft.setPower(0);
        driveRight.setPower(0);
        isShooting = true;
        
        telemetry.addData("=== SHOOTING ARTIFACT ===", "");
        telemetry.addData("Status", "Shooting...");
        telemetry.addData("Shoot Power", shootPower);
        telemetry.update();
        
        // Let one artifact come through
        artifactstopper.setPosition(0);     // Open position - allows artifact through
        shootwheel.setPower(shootPower);    // Start spinning the shoot wheel
        sleep(250);                         // Give time for artifact to pass through
        
        // Stop the next artifact
        artifactstopper.setPosition(0.2);   // Close position - blocks next artifact
        sleep(200);                         // Brief pause
        shootwheel.setPower(0);             // Stop the shoot wheel
        sleep(1500);                        // Wait before allowing next shot
        
        // Allow for a new shot to be triggered
        isShooting = false;
        
        telemetry.addData("Status", "Shot complete!");
        telemetry.update();
    }

    // SHOOTING SEQUENCE FOR AUTONOMOUS MODE
    public void shootThreeArtifacts(){
        nArtifacts = 3;
        int shotCount = 0;
        
        telemetry.addData("=== STARTING SHOOTING SEQUENCE ===", "");
        telemetry.addData("Artifacts to shoot", nArtifacts);
        telemetry.update();
        
        while (opModeIsActive() && nArtifacts > 0) {
            if (!isShooting) {
                shotCount++;
                telemetry.addData("Firing artifact", shotCount + " of 3");
                telemetry.update();
                
                shoot();
                nArtifacts -= 1;
            }
            telemetry.addData("Artifacts remaining", nArtifacts);
            telemetry.update();
        }
        
        telemetry.addData("=== SHOOTING COMPLETE ===", "");
        telemetry.addData("All artifacts fired!", shotCount + " shots completed");
        telemetry.update();
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
    
    /**
     * Deploy actuator to extended position
     */
    private void deployActuator(double position, String actionName) {
        telemetry.addData("=== ACTUATOR ACTION ===", "");
        telemetry.addData("Action", actionName);
        telemetry.addData("Target Position", position);
        telemetry.addData("Current Position", actuator.getPosition());
        telemetry.update();
        
        actuator.setPosition(position);
        
        // Wait for servo to reach position
        sleep(1500);
        
        telemetry.addData("Actuator Deployed!", "");
        telemetry.addData("Final Position", actuator.getPosition());
        telemetry.update();
    }
    
    /**
     * Retract actuator to retracted position
     */
    private void retractActuator(double position, String actionName) {
        telemetry.addData("=== ACTUATOR ACTION ===", "");
        telemetry.addData("Action", actionName);
        telemetry.addData("Target Position", position);
        telemetry.addData("Current Position", actuator.getPosition());
        telemetry.update();
        
        actuator.setPosition(position);
        
        // Wait for servo to reach position
        sleep(1500);
        
        telemetry.addData("Actuator Retracted!", "");
        telemetry.addData("Final Position", actuator.getPosition());
        telemetry.update();
    }
}
