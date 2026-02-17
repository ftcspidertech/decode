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
    private double a=0.027392, b=3.0918, c=1386.40407;


    public FlyWheelController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotor.Direction.REVERSE);

        servo = hardwareMap.get(CRServo.class, "servo");

        gamepad = pad;
        telemetry = tmetry;
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
        //*
        if (gamepad.xWasPressed()) {
            if(!flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
                flyWheelStarted = true;
            }else{
                ((DcMotorEx) flywheel).setVelocity(0);
                flyWheelStarted = false;
            }
        }//*/
        
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
        setVelocity((int)Math.min(2150, 0.97*(a*x*x + b*x + c)));
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
