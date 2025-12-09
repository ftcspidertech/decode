package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RobotController {
    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private double leftMotorTicksPerRotation;
    private double rightMotorTicksPerRotation;
    private double avgTicksPerRotation;
    private double ticksPerInch, ticksPerMM;
    private static final double WHEEL_DIAMETER_INCH = 3.54331;
    private static final double WHEEL_DIAMETER_MM = 90;
    private IMU imu;
    private Telemetry telemetry;
    private boolean robotMoving = false;
    
    public RobotController(HardwareMap hardwareMap, Telemetry tmetry) {
        // Telemetry
        telemetry = tmetry;
        
        // Left motor
        leftMotor = hardwareMap.get(DcMotor.class, "leftDrive");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftMotorTicksPerRotation = leftMotor.getMotorType().getTicksPerRev();
        //telemetry.addData("leftMotorTicksPerRotation = ",leftMotorTicksPerRotation);
        
        // Right motor
        rightMotor = hardwareMap.get(DcMotor.class, "rightDrive");
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotorTicksPerRotation = rightMotor.getMotorType().getTicksPerRev();
        //telemetry.addData("rightMotorTicksPerRotation = ",rightMotorTicksPerRotation);
        
        avgTicksPerRotation = (leftMotorTicksPerRotation+rightMotorTicksPerRotation)/2.0;
        ticksPerInch = avgTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        ticksPerMM = avgTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        //telemetry.addData("ticksPerInch = ",ticksPerInch);
        //telemetry.addData("ticksPerMM = ",ticksPerMM);
        
        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT, 
                RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu.initialize(new IMU.Parameters(revOrientation));
        imu.resetYaw();
    }
    
    public void driveMotorCurveLeft(double speed, double ratio){
        double leftSpeed = speed;
        double rightSpeed = (1-ratio)*speed;
        
        leftMotor.setPower(leftSpeed);
        rightMotor.setPower(rightSpeed);
        
        robotMoving = true;
    }

    public void driveMotorCurveRight(double speed, double ratio){
        leftMotor.setPower(-(1-ratio)*speed);
        rightMotor.setPower(speed);
        
        robotMoving = true;
    }

    private void driveMotor(double speed){
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
    }
    
    public void goForward(double speed){
        //telemetry.addData("Going forward: ",speed);
        driveMotor(speed);
        
        robotMoving = true;
    }

    public void goBackward(double speed){
        driveMotor(-speed);
        
        robotMoving = true;
    }

    public void stop(){
        stopMotor();
    }

    public void stopMotor(){
        driveMotor(0);
        
        robotMoving = false;
    }

    private void turnMotor(double speed){
        leftMotor.setPower(-speed);
        rightMotor.setPower(speed);
    }

    public void turnLeft(double speed){
        turnMotor(-speed);
        
        robotMoving = true;
    }

    public void turnRight(double speed){
        turnMotor(speed);
        
        robotMoving = true;
    }
    
    public void goForward(double speed, double distance, DistanceUnit unit) {
        goToPosition(speed, Math.abs(distance),unit);
    }

    public void goToPosition(double speed, double distance, DistanceUnit unit) {
        int targetPosition;
        long waitTime = 10; // milliseconds
        
        // Get target tick number
        if(unit==DistanceUnit.INCH){
            targetPosition = (int)(distance * ticksPerInch);
        }else if(unit==DistanceUnit.MM){
            targetPosition = (int)(distance * ticksPerMM);        
        }else if(unit==DistanceUnit.CM){
            targetPosition = (int)(distance * ticksPerMM * 10.0);        
        }else{ //if(unit==DistanceUnit.M){
            targetPosition = (int)(distance * ticksPerMM * 1000.0);        
        }
        //telemetry.addData("Ticks to move: ", targetPosition);
        
        // Set motor options
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // Set target positions
        //telemetry.addData("leftMotor.CurrentPosition = ",leftMotor.getCurrentPosition());
        //telemetry.addData("rightMotor.CurrentPosition = ",rightMotor.getCurrentPosition());
        
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + targetPosition);
        rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + targetPosition);
        
        // Set power and move the robot. Wait until it's done.
        speed = Math.abs(speed);
        try{
            if(distance>0){
                //telemetry.addData("Going forward ...", "");
                goForward(speed);
            }else{
                //telemetry.addData("Going backward ...", "");
                goBackward(speed);
            }
            while ((leftMotor.isBusy() || rightMotor.isBusy())) {
                Thread.sleep(waitTime);
            }
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }
        
        // Stop motors
        stopMotor();
        
        // Switch back to normal mode
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        
        //telemetry.addData("Movement Complete!", "");
    }
    
    public void goBackward(double speed, double distance, DistanceUnit unit){
        goToPosition(speed, -Math.abs(distance),unit);        
    }
    
    public void turnLeft(double speed, double angle){
        turnLeftToAngleWithIMUReset(speed, angle);
    }

    private void turnLeftToAngleWithIMUReset(double speed, double angle){
        double currentHeading,initialHeading;
        double targetHeading;
        int id;
        long waitTime = 0; // 10 milliseconds
        double divisor = 360.0;
        
        telemetry.addData("Turn left (deg):",angle);
        
        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;
        //telemetry.addData("    Initial heading (deg):",initialHeading);

        // Target heading
        targetHeading = currentHeading + (Math.abs(angle)%divisor);
        telemetry.addData("    target heading (deg):",targetHeading);
        telemetry.addData("    Motor turning left with power:",speed);

        robotMoving = true;
        
        while(currentHeading<targetHeading){
            turnLeft(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
                telemetry.addData("Error: ", e.getMessage());
            }
            currentHeading = getHeading(AngleUnit.DEGREES);
            if(currentHeading<-0.1 && currentHeading<initialHeading){
                currentHeading = divisor + currentHeading;
            }
        }
        stopMotor();

        robotMoving = false;
        telemetry.addData("    current heading (deg):",currentHeading);
    }

    public void turnRight(double speed, double angle){
        turnRightToAngleWithIMUReset(speed, angle);
    }

    private void turnRightToAngleWithIMUReset(double speed, double angle){
        double currentHeading,initialHeading;
        double targetHeading;
        int id;
        long waitTime = 0; // 10 milliseconds
        double divisor = 360.0;
        
        telemetry.addData("Turn right (deg):",angle);
        
        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;
        //telemetry.addData("    Initial heading (deg):",initialHeading);

        targetHeading = currentHeading - (Math.abs(angle)%divisor);
        //telemetry.addData("    target heading (deg):",targetHeading);
        //telemetry.addData("    Motor turning right with power:",speed);

        robotMoving = true;
        
        while(currentHeading>targetHeading){
            turnRight(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
        
            }
            currentHeading = getHeading(AngleUnit.DEGREES);
            if(currentHeading>0.1 && currentHeading>initialHeading){
                currentHeading = currentHeading - divisor;
            }
        }
        stopMotor();

        robotMoving = false;
        //telemetry.addData("    current heading (deg):",currentHeading);
    }

    public void splitStickArcadeDrive(double leftSpeed, double rightSpeed) {
        leftMotor.setPower(leftSpeed);//Y - X);
        rightMotor.setPower(rightSpeed);//Y + X);
    }

    public double getHeading(AngleUnit angleUnit) {
        return imu.getRobotYawPitchRollAngles().getYaw(angleUnit); // AngleUnit.DEGREES
    }    
    
    public boolean isRobotMoving(){
        return robotMoving;
    }

    public boolean isBusy() {
        return leftMotor.isBusy() || rightMotor.isBusy();
    }
}
