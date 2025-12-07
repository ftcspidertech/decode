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
    //private static final int bankVelocity = 1300;
    //private static final int farVelocity = 1900;
    private int maxVelocity = 1700;//2200;
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
        if(gamepad.right_bumper){
            //maxVelocity = ;
            maxVelocity = Math.min(2200,maxVelocity + 1);
            if(flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(maxVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", maxVelocity);
        }else if(gamepad.right_trigger==1.0){
            maxVelocity = Math.max(1700,maxVelocity - 1);
            if(flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(maxVelocity);
            }
            telemetry.addData("Fly wheel velocity: ", maxVelocity);
        }

        // Flywheel power
        if (gamepad.xWasPressed()) {
            if(!flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(maxVelocity);
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

}
