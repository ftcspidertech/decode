package org.firstinspires.ftc.teamcode.biobuzz.navigation;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.biobuzz.navigation.NavigationType;
import org.firstinspires.ftc.teamcode.biobuzz.navigation.TurnMethod;

import java.util.concurrent.TimeUnit;

public class MecanumRobotController {
    // Motors
    private DcMotor frontLeftMotor = null;
    private DcMotor backRightMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;

    private double frontLeftMotorTicksPerRotation;
    private double backLeftMotorTicksPerRotation;
    private double frontRightMotorTicksPerRotation;
    private double backRightMotorTicksPerRotation;

    private double frontLeftMotorTicksPerInch, frontLeftMotorTicksPerMM;
    private double backLeftMotorTicksPerInch, backLeftMotorTicksPerMM;
    private double frontRightMotorTicksPerInch, frontRightMotorTicksPerMM;
    private double backRightMotorTicksPerInch, backRightMotorTicksPerMM;

    private static final double WHEEL_DIAMETER_INCH = 4.09449;//2.95276;
    private static final double WHEEL_DIAMETER_MM = 104;//75;
    private IMU imu;
    private Telemetry telemetry;
    private boolean robotMoving = false;
    public double leftCalibrationFactor = 1.0;
    public double rightCalibrationFactor = 1.0;

    private ElapsedTime timer = null;
    private ElapsedTime timeoutTimer = null;

    private boolean loopExit = false;
    private int numCyclesToIgnore = 1;

    public int timeout = 5000;
    public double tickTolerance = 3; // tick percentage
    private double a, b=10.0, c;

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
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft"); //Motor 3
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft"); //Motor 4
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight"); //Motor 1
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight"); //Motor 2

        // Telemetry
        telemetry = tmetry;

        // Direction
        DcMotor.Direction reverseDirection = DcMotorSimple.Direction.REVERSE;
        DcMotor.Direction forwardDirection = DcMotorSimple.Direction.FORWARD;
        frontLeftMotor.setDirection(forwardDirection);
        backRightMotor.setDirection(forwardDirection);

        frontRightMotor.setDirection(forwardDirection);
        backLeftMotor.setDirection(reverseDirection);

        // Run mode encoder settings.
        resetAndRunWithEncoder();

        // Stop mode break settings.
        DcMotor.ZeroPowerBehavior stopMode = DcMotor.ZeroPowerBehavior.BRAKE;
        frontLeftMotor.setZeroPowerBehavior(stopMode);
        backRightMotor.setZeroPowerBehavior(stopMode);
        frontRightMotor.setZeroPowerBehavior(stopMode);
        backLeftMotor.setZeroPowerBehavior(stopMode);

        // Ticks
        frontLeftMotorTicksPerRotation = frontLeftMotor.getMotorType().getTicksPerRev();
        backLeftMotorTicksPerRotation = backRightMotor.getMotorType().getTicksPerRev();
        frontRightMotorTicksPerRotation = frontRightMotor.getMotorType().getTicksPerRev();
        backRightMotorTicksPerRotation = backLeftMotor.getMotorType().getTicksPerRev();
        //telemetry.addData("Front left motor ticks per rotation = ",frontLeftMotorTicksPerRotation);

        frontLeftMotorTicksPerInch = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        backLeftMotorTicksPerInch = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        frontRightMotorTicksPerInch = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        backRightMotorTicksPerInch = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_INCH*Math.PI);
        //telemetry.addData("Front left motor ticks per inch = ",frontLeftMotorTicksPerInch);

        frontLeftMotorTicksPerMM = frontLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        backLeftMotorTicksPerMM = backLeftMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        frontRightMotorTicksPerMM = frontRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);
        backRightMotorTicksPerMM = backRightMotorTicksPerRotation/(WHEEL_DIAMETER_MM*Math.PI);

        // IMU
        imu = hardwareMap.get(IMU.class, "imu");
        RevHubOrientationOnRobot revOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.DOWN,
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
        imu.initialize(new IMU.Parameters(revOrientation));
        imu.resetYaw();

        // Timer
        timer = new ElapsedTime();
        timeoutTimer = new ElapsedTime();
    }

    private void setFrontLeftMotorPower(double power){
        frontLeftMotor.setPower(power);
    }
    private void setBackLeftMotorPower(double power){
        backRightMotor.setPower(power);
    }
    private void setFrontRightMotorPower(double power){

        frontRightMotor.setPower(power);
    }
    private void setBackRightMotorPower(double power){
        backLeftMotor.setPower(power);
    }
    public void setPower(double frontLeftPower, double backLeftPower,
                         double frontRightPower, double backRightPower){

        frontRightMotor.setPower(rightCalibrationFactor*frontRightPower);
        frontLeftMotor.setPower(leftCalibrationFactor*frontLeftPower);

        backLeftMotor.setPower(rightCalibrationFactor*backRightPower);
        backRightMotor.setPower(leftCalibrationFactor*backLeftPower);

        robotMoving = true;
    }

    private void setPower(double drive, double strafe, double turn){
        double frontLeftPower = drive + strafe + turn;
        double backLeftPower = drive + strafe - turn;
        double frontRightPower = drive - strafe - turn;
        double backRightPower = drive - strafe + turn;
        double denominator = Math.max(Math.abs(frontLeftPower),Math.abs(backLeftPower));
        denominator = Math.max(denominator, Math.abs(frontRightPower));
        denominator = Math.max(denominator, Math.abs(backRightPower));
        denominator = Math.max(denominator, 1.0);

        setPower(frontLeftPower/denominator, backLeftPower/denominator,
                frontRightPower/denominator, backRightPower/denominator);

        robotMoving = true;
    }


    public void slideLeft(double speed){
        setPower(-speed, -speed, speed, speed);
    }

    public void slideLeft(double speed, double distanceInch){
        goToPosition(speed,distanceInch, DistanceUnit.INCH, NavigationType.SLIDELEFT);
    }

    public void slideRight(double speed, double distanceInch){
        goToPosition(speed,distanceInch,DistanceUnit.INCH,NavigationType.SLIDERIGHT);
    }

    public void slideLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void slideRight(double speed){
        setPower(speed, speed, -speed, -speed);
    }

    public void slideRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void slideTopLeft(double speed){

        setPower(0, 0, speed, speed);
    }

    public void slideTopLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideTopLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void slideTopRight(double speed){
        setPower(speed, speed, 0, 0);
    }

    public void slideTopRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideTopRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }


    public void slideBottomLeft(double speed){

        setPower(-speed, -speed, 0, 0);
    }

    //*
    public void slideBottomLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideBottomLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }//*/

    public void slideBottomRight(double speed){

        setPower(0, 0, -speed, -speed);
    }

    public void slideBottomRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        slideBottomRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    private void driveMotor(double speed){
        setPower(speed, speed, speed, speed);
    }

    public void goForward(double speed, double runTime, TimeUnit unit){
        timer.reset();
        goForward(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void goBackward(double speed, double runTime, TimeUnit unit){
        timer.reset();
        goBackward(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void turnLeft(double speed, double runTime, TimeUnit unit){
        timer.reset();
        turnLeft(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
        stop();
    }

    public void turnRight(double speed, double runTime, TimeUnit unit){
        timer.reset();
        turnRight(speed);
        if(unit==TimeUnit.MILLISECONDS){
            runTime = runTime/1000.0;
        }
        loopExit = false;
        while(!loopExit && timer.seconds()<runTime);
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
        loopExit = true;

        driveMotor(0);

        robotMoving = false;
    }

    private void turnMotor(double speed){
        setPower(-speed, speed, speed, -speed);
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
        goToPosition(speed,distance,unit,NavigationType.GOFORWARD);
    }

    private void resetAndRunWithEncoder(){
        resetEncoder();
        runWithEncoder();
    }

    private void resetEncoder(){
        frontLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    private void runWithEncoder(){
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private void goToPosition(double speed, double distance, DistanceUnit unit, NavigationType navType) {
        int targetPosition;
        long waitTime = 10; // milliseconds
        double currSpeed;

        resetAndRunWithEncoder();

        // Calculate target tick number.
        targetPosition = getTargetTickNumber(distance, unit);
        a = Math.abs(targetPosition)/2.0;
        c = Math.abs(targetPosition)/2.0;
        int tolerance = Math.max(10,(int)(targetPosition*tickTolerance/100.0));

        // Set power and move the robot.
        currSpeed = speed*gbell(0,a,b,c);
        try{
            // Loop until within tolerance
            loopExit = false;
            timeoutTimer.reset();
            while(!loopExit && notWithinTolerance(targetPosition,tolerance) && timeoutTimer.milliseconds()<=timeout){
                if(navType==NavigationType.GOFORWARD){
                    goForward(currSpeed);
                }else if(navType==NavigationType.GOBACKWARD){
                    goBackward(currSpeed);
                }else if(navType==NavigationType.SLIDERIGHT){
                    slideRight(currSpeed);
                }else{
                    slideLeft(currSpeed);
                }
                Thread.sleep(waitTime);
                currSpeed = speed*gbell(getMeanPosition(),a,b,c);
            }
        } catch (Exception e) {
            telemetry.addData("Error: ", e.getMessage());
            telemetry.update();
        }

        // Stop motors.
//        oppositePush(currSpeed,navType);
        stop();

    }

    private void oppositePush(double speed, NavigationType navType){
        if(navType==NavigationType.GOFORWARD){
            goBackward(speed);
        }else if(navType==NavigationType.GOBACKWARD){
            goForward(speed);
        }else if(navType==NavigationType.SLIDERIGHT){
            slideLeft(speed);
        }else{
            slideRight(speed);
        }
    }

    public void goBackward(double speed, double distanceInInch){
        goBackward(speed,distanceInInch,DistanceUnit.INCH);
    }

    public void goBackward(double speed, double distance, DistanceUnit unit){
        goToPosition(speed,Math.abs(distance),unit,NavigationType.GOBACKWARD);
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
        return targetPosition/4;
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
        int numCycles = 0;

        // Extra caution: Stop robot, reset encoder, and set regular mode
        stop();

        runWithEncoder();

        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;

        // Target heading
        targetHeading = currentHeading + (Math.abs(angle)%divisor);

        robotMoving = true;
        loopExit = false;
        while(!loopExit && currentHeading<targetHeading){
            numCycles = numCycles +1;
            turnLeft(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
                telemetry.addData("Error: ", e.getMessage());
            }
            if(numCycles>numCyclesToIgnore){
                currentHeading = getHeading(AngleUnit.DEGREES);
                if(currentHeading<-noiseThreshold && currentHeading<initialHeading){
                    currentHeading = divisor + currentHeading;
                }
            }
        }
        stop();
        //telemetry.addData("numCycles",numCycles);

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
        int numCycles = 0;

        // Extra caution: Stop robot, reset encoder, and set regular mode
        stop();

        runWithEncoder();

        // With Yaw reset, current heading is always close to 0.
        imu.resetYaw();
        currentHeading = getHeading(AngleUnit.DEGREES);
        initialHeading = currentHeading;

        targetHeading = currentHeading - (Math.abs(angle)%divisor);

        robotMoving = true;
        loopExit = false;
        while(!loopExit && currentHeading>targetHeading){
            numCycles = numCycles + 1;
            turnRight(speed);
            try{
                Thread.sleep(waitTime);
            }catch(InterruptedException e){
                telemetry.addData("Error: ",e.getMessage());
            }
            if(numCycles>numCyclesToIgnore){
                currentHeading = getHeading(AngleUnit.DEGREES);
                if(currentHeading>noiseThreshold && currentHeading>initialHeading){
                    currentHeading = currentHeading - divisor;
                }
            }
            //else{
            //    telemetry.addData("numCycles",numCycles);
            //}
        }
        stop();
        //telemetry.addData("numCycles",numCycles);

        robotMoving = false;
    }

    public void splitStickArcadeDrive(double drive, double strafe, double turn) {
        setPower(drive, strafe, turn);
    }

    private double getHeading(AngleUnit angleUnit) {
        return imu.getRobotYawPitchRollAngles().getYaw(angleUnit); // AngleUnit.DEGREES
    }

    public boolean isRobotMoving(){
        return robotMoving;
    }

    private boolean isMotorBusy() {
        return frontLeftMotor.isBusy() || backRightMotor.isBusy() ||
                frontRightMotor.isBusy() || backLeftMotor.isBusy();
    }

    private boolean notWithinTolerance(int targetPosition, double tolerance){
        return Math.abs(getMeanPosition()-Math.abs(targetPosition))>Math.abs(tolerance);
    }

    private double getMeanPosition(){
        int frontLeftPose, backLeftPose, frontRightPose, backRightPose;
        double averagePose;

        frontLeftPose = Math.abs(frontLeftMotor.getCurrentPosition());
        backLeftPose = Math.abs(backRightMotor.getCurrentPosition());
        frontRightPose = Math.abs(frontRightMotor.getCurrentPosition());
        backRightPose = Math.abs(backLeftMotor.getCurrentPosition());

        averagePose = (frontLeftPose+backLeftPose+frontRightPose+backRightPose)/4.0;

        return averagePose;
    }

    public void setCalibrationFactors(double left, double right){
        leftCalibrationFactor = left;
        rightCalibrationFactor = right;
    }

    private double findMax(double a, double b, double c, double d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    private double findMin(double a, double b, double c, double d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    private double gbell(double x, double a, double b, double c) {
/*
        Generalized bell shape function to smooth out speed.
        Return Output in [0 1] for input x and parameters a, b, and c.
*/

        // Prevent division by zero if 'a' is 0
        if (a == 0) {
            return x == c ? 1.0 : 0.0;
        }

        double core = Math.abs((x - c) / a);
        double denominator = 1.0 + Math.pow(core, 2.0 * b);

        return 1.0 / denominator;
    }

}
