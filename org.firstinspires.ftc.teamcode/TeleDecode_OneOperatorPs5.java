package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.navigation.TeleMecanumRobotControllerPS5;
import org.firstinspires.ftc.teamcode.decode.FlyWheelControllerPS5;
import org.firstinspires.ftc.teamcode.decode.IntakeControllerPS5;

@TeleOp(name="PS5 Controller TeleOp")
public class TeleDecode_OneOperatorPs5 extends OpMode {
    //variables
    private TeleMecanumRobotControllerPS5 teleRobotController;
    private FlyWheelControllerPS5 flyWheelController;
    private IntakeControllerPS5 intakeController;
    private ElapsedTime runTimer = new ElapsedTime();
    
    @Override
    public void init() {
        // Controllers - PS5 versions
        teleRobotController = new TeleMecanumRobotControllerPS5(hardwareMap,gamepad1,telemetry);
        flyWheelController = new FlyWheelControllerPS5(hardwareMap,gamepad1,telemetry);
        intakeController = new IntakeControllerPS5(hardwareMap,gamepad1,telemetry);
        
        // Display status
        telemetry.addData("Status", "Alhamdulillah, PS5 Robot controllers initialized");
    }
    
    @Override
    public void start(){
        telemetry.addData("Status","Bismillah, starting now with PS5 controller.");
        
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
        double runtime = runTimer.seconds();
        telemetry.addData("Remaining time",(120.0-runtime));            
        
        if(runtime>=120.0){
            telemetry.addData("Status","Alhamdulillah, done!");            
            requestOpModeStop();
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
        telemetry.addData("Status ","PS5 Robot controllers stopped."); 
    }
    
}
