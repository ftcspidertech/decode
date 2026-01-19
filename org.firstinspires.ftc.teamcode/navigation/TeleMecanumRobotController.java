package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleMecanumRobotController {
    private MecanumRobotController robot;
    private Gamepad gamepad;
    private double moveSpeed;
    //private double turnSpeed;
    private Telemetry telemetry;
    
    public TeleMecanumRobotController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry) {
        
        robot = new MecanumRobotController(hardwareMap,tmetry,1.0,1.0);
        gamepad = pad;
        
        moveSpeed = 0.75;
        //turnSpeed = 0.5;

        telemetry = tmetry;
    }
    
    public void run(){
        // Control robot with joystick.
        float y = -gamepad.left_stick_y;
        float x = gamepad.left_stick_x;
        float rx = gamepad.right_stick_x;
        if (y!=0 || x!=0 || rx!=0){
            robot.splitStickArcadeDrive(y,x,rx);            
            return;
        }
        
        // Update max speeds
        double rate = 0.001;
        if(gamepad.right_trigger==1.0){
            moveSpeed = Math.min(1,(1.0+rate)*moveSpeed);
            telemetry.addData("Max move speed: ", moveSpeed);
        }else if(gamepad.left_trigger==1.0){
            moveSpeed = Math.max(0.5,(1-rate)*moveSpeed);
            telemetry.addData("Max move speed: ", moveSpeed);
        }
        
        // Control robot with arrow keys.
        if(gamepad.dpad_left){
            robot.slideLeft(moveSpeed);
        }else if(gamepad.dpad_right){
            robot.slideRight(moveSpeed);
        }else if(gamepad.dpad_up){
            robot.goForward(moveSpeed);
        }else if(gamepad.dpad_down){
            robot.goBackward(moveSpeed);
        }else if(gamepad.b){
            robot.slideTopRight(moveSpeed);
        }else if(gamepad.a){
            robot.slideBottomLeft(moveSpeed);
        }else{
            robot.stop();
        }
        
    }
    
    public void stop(){
        robot.stop();
    }
}
