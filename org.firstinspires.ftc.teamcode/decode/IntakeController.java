package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class IntakeController {

    private DcMotor coreHex;
    private Gamepad gamepad;
    private double currentPower;
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
                coreHex.setPower(1.0);
                isRunning = true;
            }else{
                if (coreHex.getPower()<0){
                    coreHex.setPower(0.0);
                    coreHex.setPower(1.0);
                }else{
                    coreHex.setPower(0.0);
                    isRunning = false;
                }
            }
        }else if(gamepad.backWasPressed()){
            if(!isRunning){
                coreHex.setPower(-1.0);
                isRunning = true;
            }else{
                if (coreHex.getPower()>0){
                    coreHex.setPower(0.0);
                    coreHex.setPower(-1.0);
                }else{
                    coreHex.setPower(0.0);
                    isRunning = false;
                }
            }
        }
    }

    public void start(){
        coreHex.setPower(1.0);
    } 
    
    public void stop(){
        coreHex.setPower(0.0);
    }    
}
