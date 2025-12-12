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
        //coreHex.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
                coreHex.setPower(0.0);
                isRunning = false;
            }
        }
    }

    public void stop(){
        coreHex.setPower(0.0);
    }    
}
