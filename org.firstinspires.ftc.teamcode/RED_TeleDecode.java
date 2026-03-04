package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.decode.TeleDecode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp()
public class RED_TeleDecode extends TeleDecode{

    protected void initTeleDecode(){
        goalID = 24;
        kCorrLeft = 0.7;
        kCorrRight = 0.7;
        
        flyWheelController.setRedCoef();
    }

}
