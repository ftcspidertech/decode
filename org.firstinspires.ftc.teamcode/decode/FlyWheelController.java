package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlyWheelController {

    private DcMotor flywheel;
    private Gamepad gamepad;
    private CRServo servo;
    private Telemetry telemetry;
    private int maxVelocity = 2200;
    private int minVelocity = 1500;
    private int currentVelocity = 1700;
    private double WHEELS_INCHES_TO_TICKS = (28 * 5 * 3) / (3 * Math.PI);
    private boolean flyWheelStarted = false;

    public FlyWheelController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        flywheel = hardwareMap.get(DcMotor.class, "flywheel");
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotor.Direction.REVERSE);
        
        servo = hardwareMap.get(CRServo.class, "servo");

        gamepad = pad;
        telemetry = tmetry;
    }

    public void run() {
        // Update max speeds
        int rate = 1;
        if(gamepad.right_bumper){
            currentVelocity = Math.min(maxVelocity,currentVelocity + rate);
            if(flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", currentVelocity);
        }else if(gamepad.right_trigger==1.0){
            currentVelocity = Math.max(minVelocity,currentVelocity - rate);
            if(flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", currentVelocity);
        }

        // Flywheel power
        if (gamepad.xWasPressed()) {
            if(!flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(currentVelocity);
                flyWheelStarted = true;
            }else{
                ((DcMotorEx) flywheel).setVelocity(0);
                flyWheelStarted = false;
            }
        }
        
        // Servo power
        if (gamepad.y){
            servo.setPower(-1);
        }else{
            servo.setPower(-0.25);
        }
    }

    public void stop(){
        flywheel.setPower(0);
        servo.setPower(0);
    }

}
