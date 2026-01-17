package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.navigation.TeleMecanumRobotController;
import org.firstinspires.ftc.teamcode.decode.FlyWheelController;
import org.firstinspires.ftc.teamcode.decode.IntakeController;

@TeleOp()
public class TeleDecode_OneOperator extends OpMode {
    //variables
    private TeleMecanumRobotController teleRobotController;
    private FlyWheelController flyWheelController;
    private IntakeController intakeController;
    private ElapsedTime runTimer = new ElapsedTime();

    @Override
    public void init() {
        // Controllers
        teleRobotController = new TeleMecanumRobotController(hardwareMap,gamepad1,telemetry);
        flyWheelController = new FlyWheelController(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeController(hardwareMap,gamepad1,telemetry);

        // Display status
        telemetry.addData("Status", "Alhamdulillah, Robot controllers initialized");
    }

    @Override
    public void start(){
        telemetry.addData("Status","Bismillah, starting now.");
        
        // Start the flywheel at the beginning. The operator can only change its speed. 
        // NOTE: The operator can't stop the flywheel anymore.
        flyWheelController.startWithDefaultVelocity();

        // Start the intake controller at the beginning. The operator, however, can start/stop and
        // change its direction.
        intakeController.start();
        
        runTimer.reset();
    }
    
    @Override
    public void loop() {
        double runtime = runTimer.seconds();//getRuntime();
        telemetry.addData("Remaining time",(120.0-runtime));            
        //*
        if(runtime>=120.0){
            telemetry.addData("Status","Alhamdulillah, done!");            
            requestOpModeStop();
        }//*/
            
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
