package org.firstinspires.ftc.teamcode.biobuzz.tests;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveOdometry;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveWheelSpeeds;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveKinematics;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

@TeleOp(name = "FTCLib Odometry")
public class EncoderImuAutonomous extends LinearOpMode {

    // Drivebase & Tracker
    private Motor frontLeft, frontRight, backLeft, backRight;
    private MecanumDrive drivebase;
    private MecanumDriveOdometry odometry;
    private IMU imu;

    // --- ENCODER MATH CONSTANTS ---
    // Update these to match your exact physical chassis dimensions!
    private final double COUNTS_PER_MOTOR_REV = 28; //  REV HD Ultra
    private final double WHEEL_DIAMETER_M = 0.104; // meter Mecanum wheels
    private final double GEAR_RATIO = 5.23; // 1:1 if direct drive from motor to wheel

    private final double M_PER_TICK =
            (WHEEL_DIAMETER_M * Math.PI) / (COUNTS_PER_MOTOR_REV * GEAR_RATIO);

    // Track width: Physical distance between left and right wheels across the center (in inches)
    // Wheel base: Physical distance between front and back wheels along the side (in inches)
    private final double TRACK_WIDTH = 0.4318; // meter Y
    private final double WHEEL_BASE = 0.4064; // meter  X

    // PID Controllers for X, Y, and Heading
    private final PIDController xController = new PIDController(0.5, 0.0, 0.00);
    private final PIDController yController = new PIDController(0.00, 0.0, 0.00);
    private final PIDController headingController = new PIDController(0.00, 0.0, 0.00);

    private final double POSITION_TOLERANCE = 0.01; // meter Increased slightly due to wheel slippage
    private final double HEADING_TOLERANCE = Math.toRadians(1.5);

    private final double MAX_FORWARD_SPEED = 0.3; // m/s
    private final double MAX_STRAFE_SPEED = 0.3; // m/s
    private final double MAX_TURN_SPEED = 0.15; // m/s
    private GamepadEx driverGamepad;
    private double preRunTime=0, preFLTick=0, preFRTick=0, preBLTick=0, preBRTick=0;


    @Override
    public void runOpMode() throws InterruptedException {
        // 1. Initialize Motors
        frontLeft  = new Motor(hardwareMap, "frontLeft");
        frontRight = new Motor(hardwareMap, "frontRight");
        backLeft   = new Motor(hardwareMap,"backLeft");
        backRight  = new Motor(hardwareMap, "backRight");

        frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        // Normalize your drivetrain directions
        // Left pair
        frontLeft.setInverted(false);
        backRight.setInverted(true);
        // Right pair
        frontRight.setInverted(true);
        backLeft.setInverted(true);

        // Reset encoder tick counts to zero at start
        frontLeft.resetEncoder();
        frontRight.resetEncoder();
        backLeft.resetEncoder();
        backRight.resetEncoder();

        // Set distance type
        frontLeft.setDistancePerPulse(M_PER_TICK);
        frontRight.setDistancePerPulse(M_PER_TICK);
        backLeft.setDistancePerPulse(M_PER_TICK);
        backRight.setDistancePerPulse(M_PER_TICK);

        drivebase = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);

        // 2. Initialize Control Hub IMU
        imu = hardwareMap.get(IMU.class, "imu");
        // Adjust the orientation parameter below to match how your hub is mounted!
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
        ));
        imu.initialize(parameters);
        imu.resetYaw();

        // 3. Initialize FTCLib's Kinematics-based Odometry
        // Note: FTCLib takes distance values, but you must pass the track width / wheel base setup
        // to help it calculate internal calculations.
        double xOffset = WHEEL_BASE / 2.0;
        double yOffset = TRACK_WIDTH / 2.0;

        Translation2d frontLeftLocation  = new Translation2d(xOffset, yOffset);
        Translation2d frontRightLocation = new Translation2d(xOffset, -yOffset);
        Translation2d backLeftLocation   = new Translation2d(-xOffset, yOffset);
        Translation2d backRightLocation  = new Translation2d(-xOffset, -yOffset);

        MecanumDriveKinematics kinematics = new MecanumDriveKinematics(frontLeftLocation,
                frontRightLocation, backLeftLocation, backRightLocation);
        odometry = new MecanumDriveOdometry(
                kinematics,
                new Rotation2d(0),
                new Pose2d(0, 0, new Rotation2d(0))
        );

        driverGamepad = new GamepadEx(gamepad1);
        waitForStart();
        if (isStopRequested()) return;

        // Path Execution
        goToPoint(new Pose2d(0.3048, 0, Rotation2d.fromDegrees(0.0)));
//        goToPoint(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0)));
    }

    public void goToPoint(Pose2d targetPose) {
        xController.setSetPoint(targetPose.getX());
        yController.setSetPoint(targetPose.getY());
        headingController.setSetPoint(targetPose.getHeading());

        while (opModeIsActive() && !isStopRequested()) {

            // Update localization readings
            MecanumDriveWheelSpeeds wheelSpeed = new MecanumDriveWheelSpeeds(
                    frontLeft.getRate(), frontRight.getRate(), backLeft.getRate(), backRight.getRate()
            );

            odometry.updateWithTime (getRuntime(), getHeading(), wheelSpeed);
            Pose2d currentPose = odometry.getPoseMeters();

            // --- MANUALLY CALCULATE AND WRAP HEADING ERROR ---
            double xError = targetPose.getX() - currentPose.getX();
            double yError = targetPose.getY() - currentPose.getY();

            // Calculate raw angular difference
            double headingError = targetPose.getHeading() - currentPose.getHeading();

            // Normalize the angle so it stays strictly bounded between -PI and +PI
            // This forces the robot to take the absolute shortest turning path!
            while (headingError > Math.PI)  headingError -= 2 * Math.PI;
            while (headingError < -Math.PI) headingError += 2 * Math.PI;

            // Exit the method if we are inside our target zone tolerances
            if (Math.abs(xError) < POSITION_TOLERANCE &&
                    Math.abs(yError) < POSITION_TOLERANCE &&
                    Math.abs(headingError) < HEADING_TOLERANCE) {
                break;
            }

            // Calculate world-frame motor corrections
            double worldXPower = xController.calculate(currentPose.getX());
            double worldYPower = yController.calculate(currentPose.getY());

            // Pass the already wrapped heading error into the controller instead of the raw heading
            // Because the setpoint is 0, passing the error directly yields the correct correction.
            double turnPower = headingController.calculate(-headingError);

            // Vector translation to Field Centric coordinates
            double robotHeading = currentPose.getHeading();
            double robotStrafe  = worldXPower * Math.cos(-robotHeading) - worldYPower * Math.sin(-robotHeading);
            double robotForward = worldXPower * Math.sin(-robotHeading) + worldYPower * Math.cos(-robotHeading);

            // Cap speeds safely
            robotStrafe  = Math.max(-MAX_STRAFE_SPEED, Math.min(MAX_STRAFE_SPEED, robotStrafe));
            robotForward = Math.max(-MAX_FORWARD_SPEED, Math.min(MAX_FORWARD_SPEED, robotForward));
            turnPower    = Math.max(-MAX_TURN_SPEED, Math.min(MAX_TURN_SPEED, turnPower));

//            drivebase.driveRobotCentric(robotStrafe, robotForward, turnPower);

            drivebase.driveRobotCentric(
                    MAX_STRAFE_SPEED*driverGamepad.getLeftX(),   // Left stick X handles strafing sideways
                    MAX_FORWARD_SPEED*driverGamepad.getLeftY(),   // Left stick Y handles forward/backward
                    MAX_TURN_SPEED*driverGamepad.getRightX()   // Right stick X handles rotation
            );

            telemetry.addData("robotStrafe", robotStrafe);
            telemetry.addData("robotForward", robotForward);
            telemetry.addData("turnPower", turnPower);

            telemetry.addData("X", 39.3701*currentPose.getX());
            telemetry.addData("Y", 39.3701*currentPose.getY());
            telemetry.addData("Heading Error (Deg)", Math.toDegrees(headingError));
            telemetry.update();

        }

        drivebase.stop();
    }

    private Rotation2d getHeading(){
        double headingRad = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        return new Rotation2d(headingRad);
    }
}

