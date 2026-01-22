package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeController {

    private DcMotorEx intakeMotor;
    private Gamepad gamepad;
    private double currentPower = 0.75;
    private Telemetry telemetry;
    private boolean isRunning=false;
    private boolean isIntakeMode;

    public IntakeController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "coreHex");
        //intakeMotor.setDirection(DcMotor.Direction.REVERSE);

        gamepad = pad;
        telemetry = tmetry;
    }

    public void run(){
        if (gamepad.startWasPressed()) {
            if(!isRunning){
                intakeMotor.setPower(currentPower);
                isRunning = true;
            }else{
                if (intakeMotor.getPower()<0){
                    intakeMotor.setPower(0.0);
                    intakeMotor.setPower(currentPower);
                }else{
                    intakeMotor.setPower(0.0);
                    isRunning = false;
                }
            }
            isIntakeMode = true;
        }else if(gamepad.backWasPressed()){
            if(!isRunning){
                intakeMotor.setPower(-currentPower);
                isRunning = true;
            }else{
                if (intakeMotor.getPower()>0){
                    intakeMotor.setPower(0.0);
                    intakeMotor.setPower(-currentPower);
                }else{
                    intakeMotor.setPower(0.0);
                    isRunning = false;
                }
            }
            isIntakeMode = false;
        }

        if(gamepad.y && intakeMotor.getPower()==0){
            if(isIntakeMode){
                intakeMotor.setPower(currentPower);
            }else{
                intakeMotor.setPower(-currentPower);
            }
        }
        
        // Check if the motor stalled.
        if(intakeMotor.getPower()!=0 && Math.abs(intakeMotor.getVelocity())<10){
            telemetry.addLine("Motor stalled!");
            stop();
        }
    }

    public void start(){
        intakeMotor.setPower(currentPower);
        isIntakeMode = true;
    } 
    
    public void stop(){
        intakeMotor.setPower(0.0);
    }    
}
