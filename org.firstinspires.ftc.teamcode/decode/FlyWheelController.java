package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.decode.IntakeController;

public class FlyWheelController {

    private DcMotor flywheel;
    private Gamepad gamepad;
    private CRServo servo;
    private Telemetry telemetry;
    private int maxVelocity = 2800;
    private int minVelocity = 1000;
    private int currentVelocity = 1600;
    private double WHEELS_INCHES_TO_TICKS = (28 * 5 * 3) / (3 * Math.PI);
    private boolean flyWheelStarted = false;
    private boolean rightBumperPressed = false;
    private boolean leftBumperPressed = false;
    private static final int rate = 50;
    private double closePower = -0.25;
    private double openPower = 0.6; // -1
    //private double a=0.027392, b=3.0918, c=1386.40407; Old Settings
    private double a=0.0657839, b=-1.03599, c=1473.18477; //On the line and small triangle
    //private double a=0.220436, b=-21.43438, c=2149.36458; //Inside Big Triangle?
    private double[] redLineCoefs = {5.71968,1339.26486};
    private double[] redInsideCoefs = {5.4326,1343.0535};
    private double[] redAwayCoefs = {9.52381,964.28571};

    private double[] blueLineCoefs = {5.71968,1339.26486};
    private double[] blueInsideCoefs = {5.4326,1343.0535};
    private double[] blueAwayCoefs = {9.52381,964.28571};

    private double[] lineCoefs = new double[2];
    private double[] insideCoefs = new double[2];
    private double[] awayCoefs = new double[2];
    private double bK = 0.95;

    public FlyWheelController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotor.Direction.REVERSE);

        servo = hardwareMap.get(CRServo.class, "servo");

        gamepad = pad;
        telemetry = tmetry;
        
        setRedCoef();
    }
    
    public void setRedCoef(){
        lineCoefs[0] = redLineCoefs[0];
        lineCoefs[1] = bK*redLineCoefs[1];
        
        insideCoefs[0] = redInsideCoefs[0];
        insideCoefs[1] = bK*redInsideCoefs[1];
        
        awayCoefs[0] = redAwayCoefs[0];
        awayCoefs[1] = redAwayCoefs[1];
    }

    public void setBlueCoef(){
        lineCoefs[0] = blueLineCoefs[0];
        lineCoefs[1] = bK*blueLineCoefs[1];
        
        insideCoefs[0] = blueInsideCoefs[0];
        insideCoefs[1] = 1.0*blueInsideCoefs[1];
        
        awayCoefs[0] = blueAwayCoefs[0];
        awayCoefs[1] = blueAwayCoefs[1];
    }

    public void setCurrentVelocity(int velocity){
        currentVelocity = velocity;
    }


    public void run() {
        // Update max speeds
        if(gamepad.right_bumper && flyWheelStarted){
            leftBumperPressed = false;
            if(!rightBumperPressed){
                rightBumperPressed = true;
                currentVelocity = Math.min(maxVelocity,currentVelocity + rate);
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", currentVelocity);
        }else if(gamepad.left_bumper && flyWheelStarted){//(gamepad.right_trigger==1.0){
            rightBumperPressed = false;
            if(!leftBumperPressed){
                leftBumperPressed = true;
                currentVelocity = Math.max(minVelocity,currentVelocity - rate);
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", currentVelocity);
        }else{
            rightBumperPressed = false;
            leftBumperPressed = false;
        }

        // Flywheel power - - -
        // Commented out to reduce an operator's load to start/stop the flywheel.
        /*
        if (gamepad.xWasPressed()) {
            if(!flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
                flyWheelStarted = true;
            }else{
                ((DcMotorEx) flywheel).setVelocity(0);
                flyWheelStarted = false;
            }
        }*/
        
        // Servo power
        if (gamepad.y){
             openDoor();
        }else{
            closeDoor();
        }
    }

    public void startWithDefaultVelocity(){
        ((DcMotorEx) flywheel).setVelocity(currentVelocity);
        flyWheelStarted = true;
    }
    
    public void setVelocity(int velocity){
        ((DcMotorEx) flywheel).setVelocity(velocity);
        currentVelocity = velocity;
    }

    public void convertToVelocity(double x){
        setVelocity((int)Math.min(2000, 1.0*(a*x*x + b*x + c)));
    }

    public void convertToLineVelocity(double x){
        setVelocity((int)Math.min(2200, lineCoefs[0]*x + lineCoefs[1]));
    }

    public void convertToInsideVelocity(double x){
        setVelocity((int)Math.min(2200, insideCoefs[0]*x + insideCoefs[1]));
    }
    
    public void convertToAwayVelocity(double x){
        setVelocity((int)Math.min(2200, awayCoefs[0]*x + awayCoefs[1]));
    }
    
    public void stopFlyWheel(){
        flywheel.setPower(0);
        flyWheelStarted = false;
    }
    
    public void openDoor(){
        servo.setPower(openPower);
    }

    public void closeDoor(){
        servo.setPower(closePower);
    }

    public void stop(){
        stopFlyWheel();
        closeDoor();
    }

}
