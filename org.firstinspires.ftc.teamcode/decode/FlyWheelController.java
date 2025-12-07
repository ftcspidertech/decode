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
    private static final int bankVelocity = 1300;
    private static final int farVelocity = 1900;
    private static final int maxVelocity = 2200;
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
        //if (gamepad.options) {
        //    flywheel.setPower(-0.5);
        //} else 
        //if (gamepad.left_bumper) {
        //    FAR_POWER_AUTO();
        //} else if (gamepad.right_bumper) {
        //    BANK_SHOT_AUTO();
        //} else 
        if (gamepad.xWasPressed()) {
            if(!flyWheelStarted){
                ((DcMotorEx) flywheel).setVelocity(maxVelocity);
                flyWheelStarted = true;
            }else{
                ((DcMotorEx) flywheel).setVelocity(0);
                flyWheelStarted = false;
            }
        } /*else if (gamepad.b) {
            ((DcMotorEx) flywheel).setVelocity(bankVelocity);
        } else {
            ((DcMotorEx) flywheel).setVelocity(0);
            //coreHex.setPower(0);
            // The check below is in place to prevent stuttering with the servo. It checks if the servo is under manual control!
            if (!gamepad.dpad_right && !gamepad.dpad_left) {
                // servo.setPower(0);
            }
        }*/
        
        //if (gamepad.left_trigger==1.0 || gamepad.left_bumper){
        if (gamepad.y){
            servo.setPower(-1);
        }else{
            servo.setPower(-0.25);
        }
        
    }

    private void BANK_SHOT_AUTO() {
        ((DcMotorEx) flywheel).setVelocity(bankVelocity);
        //servo.setPower(-1);
        //if (((DcMotorEx) flywheel).getVelocity() >= bankVelocity - 100) {
        //    coreHex.setPower(1);
        //} else {
        //    coreHex.setPower(0);
        //}
    }

    private void FAR_POWER_AUTO() {
        ((DcMotorEx) flywheel).setVelocity(farVelocity);
        //servo.setPower(-1);
        //if (((DcMotorEx) flywheel).getVelocity() >= farVelocity - 100) {
        //    coreHex.setPower(1);
        //} else {
        //    coreHex.setPower(0);
        //}
    }

}