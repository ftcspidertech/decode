package org.firstinspires.ftc.teamcode.decode;

import org.firstinspires.ftc.teamcode.decode.AutoDecode;

public abstract class BLUE_AutoDecode extends AutoDecode{

    protected void initAutoDecode(){
        goalID = 20;
        goalPipelineID = 0;
        //correctionTurnSpeed = 0.1;
        //collectionMoveSpeed = 0.125;
        
        flyWheelController.setBlueCoef();
    }
    
}
