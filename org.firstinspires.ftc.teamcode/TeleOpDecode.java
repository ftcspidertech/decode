package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.navigation.TeleRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;

@TeleOp()
public class TeleOpDecode extends OpMode {
    //variables
    private TeleRobotController teleRobotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;

    @Override
    public void init() {
        // Controllers
        teleRobotController = new TeleRobotController(hardwareMap,gamepad1,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);

        // Display status
        telemetry.addData("Status", "Robot controllers initialized");
    }

    @Override
    public void start(){
        telemetry.addData("Status","Bismillah, starting now."); 
    }
    
    @Override
    public void loop() {
        if(getRuntime()>=2.0){
            telemetry.addData("Status","Alhamdulillah, done!");            
            requestOpModeStop();
            idle();
        }
            
        teleRobotController.run();
        flyWheelController.run();
        intakeController.run();
    }

    @Override
    public void stop(){
        teleRobotController.stop();
        flyWheelController.stop();
        intakeController.stop();
        telemetry.addData("Status ","Robot controllers stopped."); 
    }
    
}
