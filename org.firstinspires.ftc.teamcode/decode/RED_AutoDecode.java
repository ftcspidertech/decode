package org.firstinspires.ftc.teamcode.decode;

import org.firstinspires.ftc.teamcode.decode.AutoDecode;

public abstract class RED_AutoDecode extends AutoDecode{

    protected void initAutoDecode(){
        goalID = 24;
        goalPipelineID = 2;
        //correctionTurnSpeed = 0.1;
        //collectionMoveSpeed = 0.125;
        
        flyWheelController.setRedCoef();
    }
    
}
