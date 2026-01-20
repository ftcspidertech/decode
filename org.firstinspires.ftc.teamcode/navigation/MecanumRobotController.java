package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.concurrent.TimeUnit;
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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.navigation.NavigationType;
import org.firstinspires.ftc.teamcode.navigation.TurnMethod;
import java.util.concurrent.TimeUnit;


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
    private boolean robotMoving = false;
    private double leftCalibrationFactor = 1.0;
    private double rightCalibrationFactor = 1.0;
    
    private ElapsedTime timer = null;
    private boolean useSingleWheelRef = false;

    public MecanumRobotController(HardwareMap hardwareMap, Telemetry tmetry) {
        initRobotController(hardwareMap,tmetry);
    }

    public MecanumRobotController(HardwareMap hardwareMap, Telemetry tmetry, double leftCalib, double rightCalib) {
        initRobotController(hardwareMap,tmetry);
        leftCalibrationFactor = Math.abs(leftCalib);
        rightCalibrationFactor = Math.abs(rightCalib);
    }

    private void initRobotController(HardwareMap hardwareMap, Telemetry tmetry){
        // Initialize motors.
        frontLeftMotor  = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor  = hardwareMap.get(DcMotor.class, "backLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        
        // Telemetry
        telemetry = tmetry;

        // Direction
        DcMotor.Direction direction = Direction.REVERSE;
        frontLeftMotor.setDirection(direction);
        backLeftMotor.setDirection(direction);
        direction = Direction.FORWARD;
        frontRightMotor.setDirection(direction);
        backRightMotor.setDirection(direction);

        // Run mode encoder settings.
        DcMotor.RunMode runMode = RunMode.STOP_AND_RESET_ENCODER;
        frontLeftMotor.setMode(runMode);
        backLeftMotor.setMode(runMode);
        frontRightMotor.setMode(runMode);
        backRightMotor.setMode(runMode);

        runMode = RunMode.RUN_USING_ENCODER;
        frontLeftMotor.setMode(runMode);
        backLeftMotor.setMode(runMode);
        frontRightMotor.setMode(runMode);
        backRightMotor.setMode(runMode);

        // Stop mode break settings.
        DcMotor.ZeroPowerBehavior stopMode = ZeroPowerBehavior.BRAKE;
        frontLeftMotor.setZeroPowerBehavior(stopMode);
        backLeftMotor.setZeroPowerBehavior(stopMode);
        frontRightMotor.setZeroPowerBehavior(stopMode);
        backRightMotor.setZeroPowerBehavior(stopMode);        

        // Ticks        
        frontLeftMotorTicksPerRotation = frontLeftMotor.getMotorType().getTicksPerRev();
        backLeftMotorTicksPerRotation = backLeftMotor.getMotorType().getTicksPerRev();
        frontRightMotorTicksPerRotation = frontRightMotor.getMotorType().getTicksPerRev();
        backRightMotorTicksPerRotation = backRightMotor.getMotorType().getTicksPerRev();
        //telemetry.addData("Front left motor ticks per rotation = ",frontLeftMotorTicksPerRotation);

        frontLeftMotorTicksPerInch = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        backLeftMotorTicksPerInch = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        frontRightMotorTicksPerInch = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        backRightMotorTicksPerInch = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);

        frontLeftMotorTicksPerMM = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        backLeftMotorTicksPerMM = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        frontRightMotorTicksPerMM = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        backRightMotorTicksPerMM = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);    

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT, 
                RevHubOrientationOnRobot.UsbFacingDirection.UP);
        imu.initialize(new IMU.Parameters(revOrientation));
        imu.resetYaw();
        
        // Timer
        timer = new ElapsedTime();
    }

    public void setPower(double frontLeftPower, double backLeftPower, 
            double frontRightPower, double backRightPower){

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);

        robotMoving = true;
    }

    public void setPower(double drive, double strafe, double turn){
        double frontLeftPower = drive + strafe + turn;
        double backLeftPower = drive - strafe + turn;
        double frontRightPower = drive - strafe - turn;
        double backRightPower = drive + strafe - turn;
        double denominator = Math.max(Math.abs(frontLeftPower),Math.abs(backLeftPower));
        denominator = Math.max(denominator, Math.abs(frontRightPower));
        denominator = Math.max(denominator, Math.abs(backRightPower));
        denominator = Math.max(denominator, 1.0);

        setPower(frontLeftPower/denominator, backLeftPower/denominator, 
                frontRightPower/denominator, backRightPower/denominator);

        robotMoving = true;
    }
    
    
    public void slideLeft(double speed){
        setPower(-speed, speed, speed, -speed);
    }

    public void slideLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void slideRight(double speed){
        setPower(speed, -speed, -speed, speed);
    }

    public void slideRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void slideTopLeft(double speed){
        setPower(0, speed, speed, 0);
    }

    public void slideTopLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideTopLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void slideTopRight(double speed){
        setPower(speed, 0, 0, speed);
    }

    public void slideTopRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideTopRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }


    public void slideBottomLeft(double speed){
        setPower(-speed, 0, 0, -speed);
    }

    //*
    public void slideBottomLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideBottomLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }//*/

    public void slideBottomRight(double speed){
        setPower(0, -speed, -speed, 0);
    }

    public void slideBottomRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideBottomRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    private void driveMotor(double speed){
        double leftSpeed = Math.max(Math.min(speed*leftCalibrationFactor,1),-1);
        double rightSpeed = Math.max(Math.min(speed*rightCalibrationFactor,1),-1);
        
        setPower(leftSpeed, leftSpeed, rightSpeed, rightSpeed);
    }
    
    public void goForward(double speed, double runTime, TimeUnit unit){
        timer.reset();
        goForward(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void goBackward(double speed, double runTime, TimeUnit unit){
        timer.reset();
        goBackward(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void turnLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        turnLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }

    public void turnRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        turnRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        while(timer.seconds()<runTime);
        stop();
    }
    
    public void goForward(double speed){
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
        double leftSpeed = Math.max(Math.min(speed*leftCalibrationFactor,1),-1);
        double rightSpeed = Math.max(Math.min(speed*rightCalibrationFactor,1),-1);
        
        setPower(-leftSpeed, -leftSpeed, rightSpeed, rightSpeed);
    }

    public void turnLeft(double speed){
        turnMotor(speed);
        
        robotMoving = true;
    }

    public void turnRight(double speed){
        turnMotor(-speed);
        
        robotMoving = true;
    }
    
    public void goForward(double speed, double distanceInInch) {
        goForward(speed, distanceInInch, DistanceUnit.INCH);
    }
    
    public void goForward(double speed, double distance, DistanceUnit unit) {
        goToPosition(speed, Math.abs(distance),unit);
    }

    public void goForward(double speed, double distanceInInch, boolean useSpeedCorrection) {
        goForward(speed, distanceInInch, DistanceUnit.INCH, useSpeedCorrection);
    }
    
    public void goForward(double speed, double distance, DistanceUnit unit, boolean useSpeedCorrection) {
        if(useSpeedCorrection){
            goToPositionWithSpeedCorrection(speed, Math.abs(distance), unit);
        }else{
            goForward(speed,distance,unit);
        }
    }
    
    private void goToPosition(double speed, double distance, DistanceUnit unit) {
        int targetPosition;
        long waitTime = 0; // milliseconds
        int tolerance = 10;
        int leftDiff=tolerance+1,rightDiff=tolerance+1;
        
        // Reset motor encoders.
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
if(!useSingleWheelRef){
backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);          
}        
        // Calculate target tick number.
        targetPosition = getTargetTickNumber(distance, unit);

        // Set target positions
        frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() + targetPosition);

if(!useSingleWheelRef){
backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() + targetPosition);
        frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() + targetPosition);
        backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() + targetPosition);
}

        // Set motor options
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
if(!useSingleWheelRef){
backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
}
        
        // Set power and move the robot. Wait until it's done.
        speed = Math.abs(speed);
        try{            
            // Loop until within tolerance
            //while(notWithinTolerance(targetPosition,tolerance)){
            while(isMotorBusy()){
            //while (leftDiff>tolerance || rightDiff>tolerance) {
                if(distance>0){
                    goForward(speed);
                }else{
                    goBackward(speed);
                }
                Thread.sleep(waitTime);
                
                leftDiff = (Math.abs(Math.abs(frontLeftMotor.getCurrentPosition())-Math.abs(targetPosition)) + 
                           Math.abs(Math.abs(backLeftMotor.getCurrentPosition())-Math.abs(targetPosition)))/2;
                rightDiff = (Math.abs(Math.abs(frontRightMotor.getCurrentPosition())-Math.abs(targetPosition)) + 
                           Math.abs(Math.abs(backRightMotor.getCurrentPosition())-Math.abs(targetPosition)))/2;
            }                        
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
        }
        
        // Stop motors.
        stop();
        
        // Switch back to normal mode
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
if(!useSingleWheelRef){
backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
}
    }

    private void goToPosition(double speed, double distance, DistanceUnit unit, NavigationType navType) {
        int targetPosition;
        long waitTime = 10; // milliseconds
        int tolerance = 10;
        int leftDiff=tolerance+1,rightDiff=tolerance+1;
        
        // Reset motor encoders.
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        
        // Calculate target tick number.
        targetPosition = getTargetTickNumber(distance, unit);

        // Set target positions
        if(navType==NavigationType.GOFORWARD){
            frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() + targetPosition);
            backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() + targetPosition);
            frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() + targetPosition);
            backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() + targetPosition);
        }else if(navType==NavigationType.GOBACKWARD){
            frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() - targetPosition);
            backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() - targetPosition);
            frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() - targetPosition);
            backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() - targetPosition);
        }else if(navType==NavigationType.TURNLEFT){
            frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() - targetPosition);
            backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() - targetPosition);
            frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() + targetPosition);
            backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() + targetPosition);
        }else{//navType==NavigationType.TURNRIGHT
            frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() + targetPosition);
            backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() + targetPosition);
            frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() - targetPosition);
            backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() - targetPosition);
        }
        
        // Set motor options
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // Set power and move the robot. Wait until it's done.
        speed = Math.abs(speed);
        try{            
            // Loop until within tolerance
            //while(notWithinTolerance(targetPosition,tolerance)){
            while(isMotorBusy()){
            //while (leftDiff>tolerance || rightDiff>tolerance) {
                if(navType==NavigationType.GOBACKWARD){
                    goBackward(speed);
                }else{
                    goForward(speed);
                }
                Thread.sleep(waitTime);
                
                leftDiff = (Math.abs(Math.abs(frontLeftMotor.getCurrentPosition())-Math.abs(targetPosition)) + 
                           Math.abs(Math.abs(backLeftMotor.getCurrentPosition())-Math.abs(targetPosition)))/2;
                rightDiff = (Math.abs(Math.abs(frontRightMotor.getCurrentPosition())-Math.abs(targetPosition)) + 
                           Math.abs(Math.abs(backRightMotor.getCurrentPosition())-Math.abs(targetPosition)))/2;
            }                        
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
            telemetry.update();
        }
        
        // Stop motors.
        stop();

        // Switch back to normal mode
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void goBackward(double speed, double distanceInInch){
        goBackward(speed,distanceInInch,DistanceUnit.INCH);    
    }
    
    public void goBackward(double speed, double distance, DistanceUnit unit){
        goToPosition(speed, -Math.abs(distance),unit);
    }

    public void goBackward(double speed, double distanceInInch, boolean useSpeedCorrection) {
        goBackward(speed, distanceInInch, DistanceUnit.INCH, useSpeedCorrection);
    }
    
    public void goBackward(double speed, double distance, DistanceUnit unit, boolean useSpeedCorrection) {
        if(useSpeedCorrection){
            goToPositionWithSpeedCorrection(speed, -Math.abs(distance), unit);
        }else{
            goBackward(speed,distance,unit);
        }
    }

    private void goToPositionWithSpeedCorrection(double speed, double distance, DistanceUnit unit) {
        if(distance==0){
            return;
        }
        int targetPosition, leftPose, rightPose, leftDist, rightDist;
        long waitTime = 0; // milliseconds
        int tolerance = 10;
        double ratio, k=0.75, preLeftCalib, preRightCalib;
        int tickTh = 100;
        
        // Reset motor encoders.
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);     
        
        // Calculate target tick number.
        targetPosition = getTargetTickNumber(distance, unit);

        // Set target positions
        frontLeftMotor.setTargetPosition(frontLeftMotor.getCurrentPosition() + targetPosition);
        backLeftMotor.setTargetPosition(backLeftMotor.getCurrentPosition() + targetPosition);
        frontRightMotor.setTargetPosition(frontRightMotor.getCurrentPosition() + targetPosition);
        backRightMotor.setTargetPosition(backRightMotor.getCurrentPosition() + targetPosition);

        // Set motor options
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        
        // Set power and move the robot. Wait until it's done.
        speed = Math.abs(speed);

        // Store current calibration factprs.
        preLeftCalib = leftCalibrationFactor;
        preRightCalib = rightCalibrationFactor;

        // Move to the target distance.
        try{            
            // Loop until within tolerance
            leftDist = tolerance + 1;
            rightDist = leftDist;
            ratio = 1.0;
            //while(notWithinTolerance(targetPosition,tolerance)){
            while(isMotorBusy()){
            //while (leftDist>tolerance || rightDist>tolerance) {
                if(ratio>1){
                    leftCalibrationFactor = 1.0/ratio;
                    rightCalibrationFactor = 1.0;
                }else if(ratio<1){
                    leftCalibrationFactor = 1.0;
                    rightCalibrationFactor = ratio;
                }else{
                    leftCalibrationFactor = 1.0;
                    rightCalibrationFactor = 1.0;
                }
                if(distance>0){
                    goForward(speed);
                }else{
                    goBackward(speed);
                }
                Thread.sleep(waitTime);

                leftPose = (frontLeftMotor.getCurrentPosition() + backLeftMotor.getCurrentPosition())/2;
                leftPose = (leftPose==0)?1:leftPose;
                rightPose = (frontRightMotor.getCurrentPosition() + backRightMotor.getCurrentPosition())/2;
                rightPose = (rightPose==0)?1:rightPose;
                ratio = Math.abs((double)Math.pow(leftPose,1)/(double)Math.pow(rightPose,1));
                if(ratio<1){
                    if(Math.abs(leftPose)>tickTh || Math.abs(rightPose)>tickTh){
                        ratio = ratio*k;
                    }else{
                        ratio = 1.0;
                    }
                }
                leftDist = Math.abs(Math.abs(leftPose)-Math.abs(targetPosition));
                rightDist = Math.abs(Math.abs(rightPose)-Math.abs(targetPosition));
            } // while loop ends
        } catch (Exception e) {
            telemetry.addData("Error", e.getMessage());
            telemetry.update();
        }

        // Stop motors.
        stop();

        // Store current calibration factprs.
        leftCalibrationFactor = preLeftCalib;
        rightCalibrationFactor = preRightCalib;
        
        // Switch back to normal mode
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private int getTargetTickNumber(double distance, DistanceUnit unit){
        int targetPosition;
        if(unit==DistanceUnit.INCH){
            targetPosition = (int)(distance * frontRightMotorTicksPerInch);
        }else if(unit==DistanceUnit.MM){
            targetPosition = (int)(distance * frontRightMotorTicksPerMM);        
        }else if(unit==DistanceUnit.CM){
            targetPosition = (int)(distance * frontRightMotorTicksPerMM * 10.0);        
        }else{ //if(unit==DistanceUnit.M){
            targetPosition = (int)(distance * frontRightMotorTicksPerMM * 1000.0);        
        }
        return targetPosition;
    }
    
    public void turnLeft(double speed, double angle, TurnMethod turnMethod){
        if(turnMethod==TurnMethod.IMU){
            turnLeft(speed, angle);            
        }else{//turnMethod==TurnMethod.ODOMETRY
            double distance = angle*(Math.PI*15)/360.0;
            goToPosition(speed, distance, DistanceUnit.INCH, NavigationType.TURNLEFT);
        }
    }

    public void turnRight(double speed, double angle, TurnMethod turnMethod){
        if(turnMethod==TurnMethod.IMU){
            turnRight(speed, angle);            
        }else{//turnMethod==TurnMethod.ODOMETRY
            double distance = angle*(Math.PI*15)/360.0;
            goToPosition(speed, distance, DistanceUnit.INCH, NavigationType.TURNRIGHT);
        }
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
        double noiseThreshold = 1.0; //degree
        
        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;

        // Target heading
        targetHeading = currentHeading + (Math.abs(angle)%divisor);

        robotMoving = true;
        
        while(currentHeading<targetHeading){
            turnLeft(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
                telemetry.addData("Error: ", e.getMessage());
            }
            currentHeading = getHeading(AngleUnit.DEGREES);
            if(currentHeading<-noiseThreshold && currentHeading<initialHeading){
                currentHeading = divisor + currentHeading;
            }
        }
        stop();

        robotMoving = false;
    }

    public void turnRight(double speed, double angle){
        turnRightToAngleWithIMUReset(speed, angle);
    }

    private void turnRightToAngleWithIMUReset(double speed, double angle){
        double currentHeading,initialHeading;
        double targetHeading;
        int id;
        long waitTime = 0; // milliseconds
        double divisor = 360.0;
        double noiseThreshold = 1.0; // degree
        
        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;

        targetHeading = currentHeading - (Math.abs(angle)%divisor);

        robotMoving = true;
        
        while(currentHeading>targetHeading){
            turnRight(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
                telemetry.addData("Error: ",e.getMessage());
            }
            currentHeading = getHeading(AngleUnit.DEGREES);
            if(currentHeading>noiseThreshold && currentHeading>initialHeading){
                currentHeading = currentHeading - divisor;
            }
        }
        stop();

        robotMoving = false;
    }

    public void splitStickArcadeDrive(double drive, double strafe, double turn) {
        setPower(drive, strafe, turn);
    }

    public double getHeading(AngleUnit angleUnit) {
        return imu.getRobotYawPitchRollAngles().getYaw(angleUnit); // AngleUnit.DEGREES
    }    
    
    public boolean isRobotMoving(){
        return robotMoving;
    }

    public boolean isMotorBusy() {
        return frontLeftMotor.isBusy() || backLeftMotor.isBusy() || 
               frontRightMotor.isBusy() || backRightMotor.isBusy();
    }

    public boolean notWithinTolerance(int targetPosition, double tolerance){
        int frontLeftPose, backLeftPose, frontRightPose, backRightPose;
        
        targetPosition = Math.abs(targetPosition);
        tolerance = Math.abs(tolerance);
        
        frontLeftPose = Math.abs(frontLeftMotor.getCurrentPosition());
        backLeftPose = Math.abs(backLeftMotor.getCurrentPosition());
        frontRightPose = Math.abs(frontRightMotor.getCurrentPosition());
        backRightPose = Math.abs(backRightMotor.getCurrentPosition());

        return Math.abs(frontLeftPose-targetPosition)>tolerance ||
               Math.abs(backLeftPose-targetPosition)>tolerance ||
               Math.abs(frontRightPose-targetPosition)>tolerance ||
               Math.abs(backRightPose-targetPosition)>tolerance;
    }
    
    public void setCalibrationFactors(double left, double right){
        leftCalibrationFactor = left;
        rightCalibrationFactor = right;
    }
}
