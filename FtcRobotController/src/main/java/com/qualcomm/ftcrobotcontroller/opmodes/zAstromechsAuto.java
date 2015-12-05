package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kevin on 11/19/2015.
 */
public abstract class zAstromechsAuto extends LinearOpMode {

    DcMotor motorFrontRight;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackLeft;
    Servo scythe;


    GyroSensor Gyro;
    int xVal, yVal, zVal = 0;
    int heading = 0;
    long watchdogTimer = 0;

    public void driveStraight (long targetDistance, int headingTarget, double drivePower) throws InterruptedException {

        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        sleep(50);

        while(motorFrontRight.getCurrentPosition()<targetDistance) {
            int variation = headingTarget - heading;

            if(variation>20){
                variation = variation - 360;
            }
            if(variation<-20){
                variation = variation + 360;
            }

            motorBackLeft.setPower(drivePower+(variation*0.02));
            motorFrontLeft.setPower(drivePower+(variation*0.02));
            motorBackRight.setPower(drivePower+(variation*-0.02));
            motorFrontRight.setPower(drivePower+(variation*-0.02));
            sleep(5);
        }

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        sleep(50);

    }

    public void pivotTurn (int headingTarget) throws InterruptedException{
        int variation = Math.abs(headingTarget-heading);
        boolean turnDirection = calcTurnDirection(headingTarget, variation);

        while (headingTarget != heading) {
            if (turnDirection) {
                motorBackLeft.setPower(0.5);
                motorFrontLeft.setPower(0.5);
                motorBackRight.setPower(-0.5);
                motorFrontRight.setPower(-0.5);
            } else {
                motorBackLeft.setPower(-0.5);
                motorFrontLeft.setPower(-0.5);
                motorBackRight.setPower(0.5);
                motorFrontRight.setPower(0.5);
            }
        }

        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        sleep(50);

    }

    private boolean calcTurnDirection (int target, int variation)throws InterruptedException {

        if(target>180){
            if(heading>180){
                if(heading>target){
                    return false;
                } else {
                    return true;
                }
            } else {
                if (variation>180){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (heading>180){
                if(variation>180){
                    return false;
                } else {
                    return true;
                }
            } else {
                if (heading>target){
                    return false;
                } else {
                    return true;
                }
            }
        }

    }

    public void watchdog () throws InterruptedException {
        if (watchdogTimer<7500){
            sleep(100);
            watchdogTimer = watchdogTimer + 100;
        } else {
            System.exit(-1);
        }
    }

    public void kickDaDog () {
        watchdogTimer = 0;
    }

    public void updateHeading () throws InterruptedException {

        while(true) {
            heading = Gyro.getHeading();
            sleep(5);
        }
    }

    public void reapClimbers () throws InterruptedException {
        scythe.setPosition(1);
        sleep(50);
    }

}
