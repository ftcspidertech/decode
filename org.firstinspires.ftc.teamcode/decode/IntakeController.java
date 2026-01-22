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
    private boolean isRunning = false;
    private boolean isIntakeMode = true;

    public IntakeController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "coreHex");
        //intakeMotor.setDirection(DcMotor.Direction.REVERSE);

        gamepad = pad;
        telemetry = tmetry;
    }

    public void run(){
        if (gamepad.startWasPressed()) {
            if(!isRunning){
                setPower(currentPower);
            }else{
                if(isIntakeMode){ // intakeMotor.getPower()<0
                    stop();
                }else{
                    stop();
                    setPower(currentPower);
                }
            }
            isIntakeMode = true;
        }else if(gamepad.backWasPressed()){
            if(!isRunning){
                setPower(-currentPower);
            }else{
                if(isIntakeMode){ // intakeMotor.getPower()>0
                    stop();
                    setPower(-currentPower);
                }else{
                    stop();
                }
            }
            isIntakeMode = false;
        }

        // Start the motor for shoorting (if stalled before).
        if(gamepad.y && intakeMotor.getPower()==0){
            start();
        }
        
        // Check if the motor stalled for multiple balls.
        if(intakeMotor.getPower()!=0 && Math.abs(intakeMotor.getVelocity())<10){
            telemetry.addLine("Motor stalled!");
            stop();
        }
    }

    private void setPower(double power){
        intakeMotor.setPower(power);
        isRunning = true;
    }
    
    public void start(){
        if(isIntakeMode){
            setPower(currentPower);
        }else{
            setPower(-currentPower);
        }        
    } 

    public void stop(){
        intakeMotor.setPower(0.0);
        isRunning = false;
    }    
}
