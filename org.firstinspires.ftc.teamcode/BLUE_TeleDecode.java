package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.decode.TeleDecode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class BLUE_TeleDecode extends TeleDecode{

    protected void initTeleDecode(){
        goalID = 20;
        kCorrLeft = 0.7;
        kCorrRight = 0.7;
        
        flyWheelController.setBlueCoef();
    }

}
