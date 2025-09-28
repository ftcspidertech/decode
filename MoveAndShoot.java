public class MyFIRSTJavaOpMode extends LinearOpMode {
    // Hardware components
    DcMotor driveLeft;
    DcMotor driveRight;
    DcMotor shootwheel;
    Servo artifactstopper;
    ColorSensor color1;
    DistanceSensor distance1;
    BNO055IMU imu;

    // Variables for shooting control
    double shootPower;
    boolean isShooting;
    int nArtifacts;

    // INITIALIZATION
    public void inititalSetup(){
        isShooting = false;
        // Holds back artifacts until we start shooting
        artifactstopper.setPosition(0.2);  // Closed position - blocks artifacts
        
        // Set motor direction
        driveLeft.setDirection(DcMotor.Direction.REVERSE);
    }

    // MAIN SHOOTING FUNCTION
    public void shoot(){
        // Don't move while shooting
        driveLeft.setPower(0);
        driveRight.setPower(0);
        isShooting = true;
        
        telemetry.addData("Status", "Shooting...");
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
        while (opModeIsActive() && nArtifacts > 0) {
            if (!isShooting) {
                shoot();
                nArtifacts -= 1;
            }
            telemetry.addData("Artifacts remaining", nArtifacts);
            telemetry.update();
        }
    }

    // SIMPLE MOVEMENT FUNCTIONS
    public void moveForward(double power, int milliseconds) {
        driveLeft.setPower(power);
        driveRight.setPower(power);
        sleep(milliseconds);
        driveLeft.setPower(0);
        driveRight.setPower(0);
    }

    public void turnRight(double power, int milliseconds) {
        driveLeft.setPower(power);
        driveRight.setPower(-power);
        sleep(milliseconds);
        driveLeft.setPower(0);
        driveRight.setPower(0);
    }

    public void turnLeft(double power, int milliseconds) {
        driveLeft.setPower(-power);
        driveRight.setPower(power);
        sleep(milliseconds);
        driveLeft.setPower(0);
        driveRight.setPower(0);
    }

    // YOUR REQUESTED SEQUENCE
    public void executeMovementSequence() {
        telemetry.addData("Sequence", "Starting movement sequence");
        telemetry.update();
        
        // Forward 2 revolutions (approximate timing - adjust as needed)
        telemetry.addData("Step", "Forward 2 revolutions");
        telemetry.update();
        moveForward(0.6, 3000);  // Adjust time as needed
        sleep(500);
        
        // Turn right 90 degrees
        telemetry.addData("Step", "Turn right 90 degrees");
        telemetry.update();
        turnRight(0.5, 800);  // Adjust time as needed
        sleep(500);
        
        // Forward 1 revolution
        telemetry.addData("Step", "Forward 1 revolution");
        telemetry.update();
        moveForward(0.6, 1500);  // Adjust time as needed
        sleep(500);
        
        // Turn left 90 degrees
        telemetry.addData("Step", "Turn left 90 degrees");
        telemetry.update();
        turnLeft(0.5, 800);  // Adjust time as needed
        sleep(500);
        
        // Shoot
        telemetry.addData("Step", "Shooting!");
        telemetry.update();
        shoot();
        
        telemetry.addData("Sequence", "Complete!");
        telemetry.update();
    }

    // MANUAL CONTROL LOOP
    public void manualControl() {
        while (opModeIsActive()) {
            // Gamepad driving
            double drive = -gamepad1.left_stick_y;
            double turn = gamepad1.right_stick_x;
            
            driveLeft.setPower(drive + turn);
            driveRight.setPower(drive - turn);
            
            // Shooting trigger
            if (gamepad1.a && !isShooting) {
                shoot();
            }
            
            // Display controls
            telemetry.addData("Controls", "Left stick = drive, Right stick = turn");
            telemetry.addData("Shooting", "Press A button to shoot");
            telemetry.addData("Drive Power", "%.2f", drive);
            telemetry.addData("Turn Power", "%.2f", turn);
            telemetry.update();
            
            sleep(50);
        }
    }

    @Override
    public void runOpMode() {
        // HARDWARE MAPPING
        driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
        driveRight = hardwareMap.get(DcMotor.class, "driveRight");
        shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
        artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
        color1 = hardwareMap.get(ColorSensor.class, "color1");
        distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        
        // Initialize variables
        shootPower = 0.8;  // Set shooting power to 80%
        
        // Setup robot
        inititalSetup();
        
        telemetry.addData("Status", "Robot Ready!");
        telemetry.addData("Mode", "Will execute movement sequence, then manual control");
        telemetry.addData("Press", "START to begin");
        telemetry.update();
        
        waitForStart();
        
        if (opModeIsActive()) {
            // Execute your requested sequence
            executeMovementSequence();
            
            // Then switch to manual control
            telemetry.addData("Sequence complete!", "Switching to manual control");
            telemetry.update();
            sleep(2000);
            
            manualControl();
        }
    }
}
