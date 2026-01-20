package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeController {

    private DcMotor coreHex;
    private Gamepad gamepad;
    private double currentPower = -0.9;
    private Telemetry telemetry;
    private boolean isRunning=false;

    public IntakeController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        coreHex = hardwareMap.get(DcMotor.class, "coreHex");
        coreHex.setDirection(DcMotor.Direction.REVERSE);

        gamepad = pad;
        telemetry = tmetry;
    }

    public void run(){
        if (gamepad.startWasPressed()) {
            if(!isRunning){
                coreHex.setPower(currentPower);
                isRunning = true;
            }else{
                if (coreHex.getPower()<0){
                    coreHex.setPower(0.0);
                    coreHex.setPower(currentPower);
                }else{
                    coreHex.setPower(0.0);
                    isRunning = false;
                }
            }
        }else if(gamepad.backWasPressed()){
            if(!isRunning){
                coreHex.setPower(-currentPower);
                isRunning = true;
            }else{
                if (coreHex.getPower()>0){
                    coreHex.setPower(0.0);
                    coreHex.setPower(-currentPower);
                }else{
                    coreHex.setPower(0.0);
                    isRunning = false;
                }
            }
        }
    }

    public void start(){
        coreHex.setPower(currentPower);
    } 
    
    public void stop(){
        coreHex.setPower(0.0);
    }    
}
