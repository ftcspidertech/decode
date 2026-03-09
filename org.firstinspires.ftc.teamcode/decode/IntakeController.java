package org.firstinspires.ftc.teamcode.decode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

public class IntakeController {

    private DcMotorEx intakeMotor;
    private Gamepad gamepad;
    public double regularPower = 1.0;//POSSIBLE CHNAGES
    public double shootPower = 1.0;
    private Telemetry telemetry;
    private boolean isRunning = false;
    private boolean isIntakeMode = true;
    private ElapsedTime stallTimer =  new ElapsedTime();
    public int MIN_VELOCITY = 250; // 250 ticks
    private final double MIN_WAIT = 1.0; // seconds
    //public double minVel = 0.6;
    private double velocity,maxVelocity=0;

    public IntakeController(HardwareMap hardwareMap, Gamepad pad, Telemetry tmetry){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        //intakeMotor.setDirection(DcMotor.Direction.REVERSE);

        gamepad = pad;
        telemetry = tmetry;
    }

    public void run(){
        velocity = intakeMotor.getVelocity();
        if(velocity>maxVelocity){
            maxVelocity = velocity;//(intakeMotor.getVelocity() + maxVelocity)/2.0;
        }
        //telemetry.addData("Max intake velocity",maxVelocity);
        //telemetry.addData("Current intake velocity",velocity);
        if (gamepad.startWasPressed()) {
            if(!isRunning){
                setPower(regularPower);
            }else{
                if(isIntakeMode){ // intakeMotor.getPower()<0
                    stop();
                }else{
                    stop();
                    setPower(regularPower);
                }
            }
            isIntakeMode = true;
        }else if(gamepad.backWasPressed()){
            if(!isRunning){
                setPower(-regularPower);
            }else{
                if(isIntakeMode){ // intakeMotor.getPower()>0
                    stop();
                    setPower(-regularPower);
                }else{
                    stop();
                }
            }
            isIntakeMode = false;
        }

        // Start the motor for shoorting (if stalled before).
        restartIfStalled(gamepad.y);
        //*
        if(gamepad.y){
            setShootPower();
        }else{
            if(intakeMotor.getPower()!=0 && intakeMotor.getPower()!=regularPower){
                resetPower();
            }
        }//*/

        // Check if the motor stalled for multiple balls.
        //stopIfStalled();
        releaseBallIfStalled();
    }

    private void setPower(double power){
        if(power==intakeMotor.getPower()){
            return;
        }
        intakeMotor.setPower(power);
        isRunning = true;
        stallTimer.reset();
    }
    
    public void start(){
        if(isIntakeMode){
            setPower(regularPower);
        }else{
            setPower(-regularPower);
        }       
    } 

    public void stop(){
        intakeMotor.setPower(0.0);
        isRunning = false;
    }
    
    public void releaseBallIfStop(){
        if(intakeMotor.getPower()==0){
            releaseBall(500);
        }
    }
    
    public void stopIfStalled(){
        if(stallTimer.seconds()>MIN_WAIT && intakeMotor.getPower()!=0 && 
           Math.abs(intakeMotor.getVelocity())<MIN_VELOCITY){
            // First, set opposite motion to release the pressure on the exit door.
            if(isIntakeMode){
                setPower(-regularPower);
                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    telemetry.addData("Error: ", e.getMessage());
                }                
            }        
            // Next, stop the motion.
            stop();
        }
    }

    public void releaseBallIfStalled(){
        if(stallTimer.seconds()>MIN_WAIT && intakeMotor.getPower()!=0 && 
           Math.abs(intakeMotor.getVelocity())<MIN_VELOCITY){
               
               stop();
               releaseBall(600);
               
        }
    }
    
    private void releaseBall(int waitTime){
        // Run the motor to reverse direction   
        setPower(-regularPower);
        // Wait enough to release one ball
        try{
            Thread.sleep(waitTime);
        }catch(InterruptedException e){
            telemetry.addData("Error: ", e.getMessage());
        }
        // Reset the motor to its regular motion direction.
        setPower(regularPower);
    }
    
    public void restartIfStalled(boolean youCanRestart){
        if(youCanRestart && intakeMotor.getPower()==0){
            start();
        }
    }
    
    public void setShootPower(){
        setPower(shootPower);
    }

    public void setShootPower(boolean youCanChange){
        if(youCanChange){
            setPower(shootPower);
        }
    }
    
    public void resetPower(){
        start();
    }
    
}
