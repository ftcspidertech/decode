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
        }/*else if (gamepad.back){
            coreHex.setPower(0.0);
        }else if(gamepad.right_trigger==1.0 || gamepad.right_bumper){
            telemetry.addData("right trigger || right_bumper","");
            if (isRunning){
                currentPower = -coreHex.getPower();
                coreHex.setPower(0);
                coreHex.setPower(currentPower);
                // try{
                //     Thread.sleep(100);
                // } catch (Exception e){
                //     telemetry.addData("Error: ", e.getMessage());
                // }
                telemetry.addData("Power set to: ",currentPower);
            }
        }*/
    }
    
}