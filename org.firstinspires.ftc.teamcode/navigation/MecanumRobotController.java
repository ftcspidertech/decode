package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.navigation.NavigationType;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;


public class MecanumRobotController {

    // Motors
    private DcMotor frontLeftMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backRightMotor = null;

    private double frontLeftMotorTicksPerRotation;
    private double backLeftMotorTicksPerRotation;	
    private double frontRightMotorTicksPerRotation;
    private double backRightMotorTicksPerRotation;
	
    private double frontLeftMotorTicksPerInch, frontLeftMotorTicksPerMM;
    private double backLeftMotorTicksPerInch, backLeftMotorTicksPerMM;
    private double frontRightMotorTicksPerInch, frontRightMotorTicksPerMM;
    private double backRightMotorTicksPerInch, backRightMotorTicksPerMM;
	
    private static final double WHEEL_DIAMETER_INCH = 2.95276;
    private static final double WHEEL_DIAMETER_MM = 75;
    private IMU imu;
    private Telemetry telemetry;	

	  public MecanumDrive(HardwareMap hardwareMap){
		private RunMode runMode = null;
		private Direction direction = null;
	
		// Initialize motors.
    frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeftMotor");
    backLeftMotor  = hardwareMap.get(DcMotor.class, "backLeftMotor");
    frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
    backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
		
		// Direction
		direction = Direction.REVERSE;
    frontLeftMotor.setDirection(direction);
    backLeftMotor.setDirection(direction);
		direction = Direction.FORWARD;
    frontRightMotor.setDirection(direction);
    backRightMotor.setDirection(direction);
		
		// Run mode encoder settings.
		runMode = RunMode.STOP_AND_RESET_ENCODER;
    frontLeftMotor.setMode(runMode);
    backLeftMotor.setMode(runMode);
    frontRightMotor.setMode(runMode);
    backRightMotor.setMode(runMode);
		
		runMode = RunMode.RUN_WITH_ENCODER;
    frontLeftMotor.setMode(runMode);
    backLeftMotor.setMode(runMode);
    frontRightMotor.setMode(runMode);
    backRightMotor.setMode(runMode);
		
		// Stop mode break settings.
		stopMode = ZeroPowerBehavior.BRAKE;
    frontLeftMotor.setZeroPowerBehavior(stopMode);
    backLeftMotor.setZeroPowerBehavior(stopMode);
    frontRightMotor.setZeroPowerBehavior(stopMode);
    backRightMotor.setZeroPowerBehavior(stopMode);		
				
		// Ticks		
    frontLeftMotorTicksPerRotation = frontLeftMotor.getMotorType().getTicksPerRev();
    backLeftMotorTicksPerRotation = backLeftMotor.getMotorType().getTicksPerRev();
    frontRightMotorTicksPerRotation = frontRightMotor.getMotorType().getTicksPerRev();
    backRightMotorTicksPerRotation = backRightMotor.getMotorType().getTicksPerRev();
		
    frontLeftMotorTicksPerInch = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
    backLeftMotorTicksPerInch = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
    frontRightMotorTicksPerInch = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
    backRighttMotorTicksPerInch = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);

    frontLeftMotorTicksPerMM = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
    backLeftMotorTicksPerMM = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
    frontRightMotorTicksPerMM = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
    backRighttMotorTicksPerMM = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);	

    // IMU
    imu = hardwareMap.get(IMU.class, "imu");
    RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.LEFT, 
            RevHubOrientationOnRobot.UsbFacingDirection.UP);
    imu.initialize(new IMU.Parameters(revOrientation));
    imu.resetYaw();		
	}

	public void setPower(double frontLeftPower, double backLeftPower, 
	                     double frontRightPower, double backRightPower){
						 
		double denominator = Math.max(Math.abs(frontLeftPower),Math.abs(backLeftPower));
		denominator = Math.max(denominator, Math.abs(frontRightPower));
		denominator = Math.max(denominator, Math.abs(backRightPower));
		denominator = Math.max(denominator, 1.0);

		frontLeftMotor.setPower(frontLeftPower / denominator);
		backLeftMotor.setPower(backLeftPower / denominator);
		frontRightMotor.setPower(frontRightPower / denominator);
		backRightMotor.setPower(backRightPower / denominator);
	}

	public void setPower(double drive, double strafe, double turn){
		double frontLeftPower = drive + strafe + turn;
		double backLeftPower = drive - strafe + turn;
		double frontRightPower = drive - strafe - turn;
		double backRightPower = drive + strafe - turn;

		setPower(frontLeftPower, backLeftPower, 
		         frontRightPower, backRightPower);
	}

}
 
