package org.firstinspires.ftc.teamcode.navigation;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.navigation.MecanumRobotController;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TeleMecanumRobotController {
    public double kMove = 1.0;
    public double kTurn = 1.0;
    
    private MecanumRobotController robot;
    private Gamepad gamepad;
    private double moveSpeed;
    private double turnSpeed;
    private Telemetry telemetry;
    
    public TeleMecanumRobotController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry) {
        
        robot = new MecanumRobotController(hardwareMap,tmetry,1.0,1.0);
        gamepad = pad;
        
        moveSpeed = 0.5;
        turnSpeed = 0.25;

        telemetry = tmetry;
    }
    
    public void run(){
        // Control robot with joystick.
        float y = -gamepad.left_stick_y;
        float x = gamepad.left_stick_x;
        float rx = gamepad.right_stick_x;
        if (y!=0 || x!=0 || rx!=0){
            robot.splitStickArcadeDrive(y*kMove,x*kMove,rx*kTurn);            
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
            robot.turnLeft(turnSpeed);
        }else if(gamepad.dpad_right){
            robot.turnRight(turnSpeed);
        }else if(gamepad.dpad_up){
            robot.goForward(moveSpeed);
        }else if(gamepad.dpad_down){
            robot.goBackward(moveSpeed);
        }else{
            robot.stop();
        }
        
    }
    
    public void turnLeft(double speed, double angle){
        robot.turnLeft(speed,angle);
    }

    public void turnRight(double speed, double angle){
        robot.turnRight(speed,angle);
    }

    public void stop(){
        robot.stop();
    }
}
