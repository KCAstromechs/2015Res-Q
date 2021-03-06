/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes.archive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Base Drive code
 * Blue side of field
 * far left corner
 */
public class AutoTestGyro extends LinearOpMode {
    float leftPower;
    float rightPower;
    float localRightPower;
    float localLeftPower;
    int stage = 0;
    int startHeading;
    int targetPos;// used for encoders

    //gyro stabilization variables
    int degErr;
    float proportionalConst = 0.05f;
    float correction;
    int lastDegErr;

    //Drive Constants
    private static final int kClicksPerRev = 1100;
    private static final int klongDrive = (int) (kClicksPerRev * 7.25);
    private static final int kClearWall = (int) (kClicksPerRev * 0.10);
    private static final int kSlowApproach =  (int) (kClicksPerRev * 1.25f);


    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

    Servo mjolnir;
    Servo grabber;
    Servo leftZipline;
    Servo rightZipline;
    Servo leftLock;
    Servo rightLock;

    GyroSensor gyro;


    @Override
    public void runOpMode() throws InterruptedException {
        //Motor init - use Config files
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        mjolnir = hardwareMap.servo.get("box");
        grabber=hardwareMap.servo.get("grabber");
        leftZipline=hardwareMap.servo.get("leftZipline");
        rightZipline=hardwareMap.servo.get("rightZipline");
        leftLock=hardwareMap.servo.get("leftLock");
        rightLock=hardwareMap.servo.get("rightLock");

        //reset encoders
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(500);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        sleep(500);

        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        //reverse Motors
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        //Sensor init
        gyro = hardwareMap.gyroSensor.get("gyro");
        //Add ODS to compensate for errors in longDrive
        gyro.calibrate();

        sleep(100);
        while (gyro.isCalibrating()) {
            sleep(50);
        }
        // check calibrate gyro
        System.out.println("gyro (PreCalibration" + gyro.getHeading());
        telemetry.addData("gyro (PreCalibration", gyro.getHeading());
        gyro.resetZAxisIntegrator();

        telemetry.addData("Stage", stage);
        telemetry.addData("gyro", gyro.getHeading());
        System.out.println("gyro (PostCalibration?" + gyro.getHeading());

        grabber.setPosition(0.1);
        leftZipline.setPosition(1.0);
        rightZipline.setPosition(0.4);
        mjolnir.setPosition(0.9);
        leftLock.setPosition(0.2);
        rightLock.setPosition(0.98);

        waitForStart();

        //gyro.resetZAxisIntegrator();
        //sleep(100);

        //clears wall
        /*
        rightPower =1;
        leftPower=1;
        motorFrontRight.setPower(rightPower);
        */

        //pivot turn - uses gyro to control length of turn (not encoders)
        stage = 1;
        telemetry.addData("Stage", stage);
        telemetry.addData("gyro (PostCalibration)", gyro.getHeading());
        leftPower = 0.5f;
        rightPower = 0;
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
        //motorFrontRight.setPower(rightPower);
        //motorBackRight.setPower(rightPower);
        while (gyro.getHeading() <= 35) {
            //motorFrontLeft.setPower(leftPower);
            //motorBackLeft.setPower(leftPower);
            //telemetry.addData("leftPower", leftPower);
            //telemetry.addData("rightPower", rightPower);
            //telemetry.addData("gyro",gyro.getHeading());
            //System.out.println("gyro="+gyro.getHeading());
            sleep(50);
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        // motorFrontRight.setPower(0);
        //motorBackRight.setPower(0);
        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.addData("gyro", gyro.getHeading());

        //sleep(10000);

        //Long straight drive - Main control is encoders on back right motor, us gyro for stabilization
        stage = 2;
        telemetry.addData("Stage", stage);
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(500);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        sleep(500);

        leftPower = 0.6f;
        rightPower = 0.6f;
      /*
    motorFrontRight.setTargetPosition(klongDrive);
    motorFrontLeft.setTargetPosition(klongDrive);
    motorBackRight.setTargetPosition(klongDrive);
    motorBackLeft.setTargetPosition(klongDrive);
    */
        targetPos = klongDrive;
        startHeading = 45;
        //gyro stabilization - PID

        stage = 3;
        telemetry.addData("Stage", stage);

        localLeftPower=leftPower;
        localRightPower=rightPower;

        telemetry.addData("codeLoc", "motors on!");
        degErr=0;

        while (motorBackRight.getCurrentPosition() < targetPos) {
            telemetry.addData("EncoderVal", motorBackRight.getCurrentPosition());
            System.out.println("Encoder= " +motorBackRight.getCurrentPosition());
            lastDegErr = degErr;
            degErr = gyro.getHeading() - startHeading;
            if (degErr != lastDegErr) {
                correction = degErr * proportionalConst;
                localLeftPower = Range.clip(leftPower - correction, -1.0f, 1.0f);
                localRightPower = Range.clip(rightPower + correction, -1.0f, 1.0f);

                motorFrontRight.setPower(localRightPower);
                motorFrontLeft.setPower(localLeftPower);
                motorBackRight.setPower(localRightPower);
                motorBackLeft.setPower(localLeftPower);

                /*
                telemetry.addData("motorFrontRight", motorFrontRight.getCurrentPosition());
                telemetry.addData("motorFrontLeft", motorFrontLeft.getCurrentPosition());
                telemetry.addData("motorBackRight", motorBackRight.getCurrentPosition());
                telemetry.addData("motorBackLeft", motorBackLeft.getCurrentPosition());
                */


            }
            sleep(50);
        }
        telemetry.addData("codeLoc", "motors turning off!");
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        telemetry.addData("codeLoc", "motors off!");

        sleep(1000);
        //turns towards beacon use gyro, not encoders
        stage = 4;
        telemetry.addData("Stage", stage);
        leftPower = 0.5f;
        startHeading = gyro.getHeading();
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
        while (gyro.getHeading() <= 80) {
            sleep(50);
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);

        sleep(1000);

        //slow approach - use light sensor to follow line
        stage = 5;
        telemetry.addData("Stage", stage);
        rightPower = 0.5f;
        leftPower = 0.5f;

        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(500);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        targetPos=kSlowApproach;
        startHeading = 90;
        stage = 3;
        telemetry.addData("Stage", stage);
        localLeftPower=leftPower;
        localRightPower=rightPower;
        degErr=0;

        while (motorBackRight.getCurrentPosition() < targetPos) {
            lastDegErr=degErr;
            degErr = gyro.getHeading() - startHeading;
            if (degErr!=lastDegErr) {
                correction = degErr * proportionalConst;
                localLeftPower = Range.clip(leftPower - correction, -1.0f, 1.0f);
                localRightPower = Range.clip(rightPower + correction, -1.0f, 1.0f);

                motorFrontRight.setPower(localRightPower);
                motorFrontLeft.setPower(localLeftPower);
                motorBackRight.setPower(localRightPower);
                motorBackLeft.setPower(localLeftPower);

                telemetry.addData("motorFrontRight", motorFrontRight.getCurrentPosition());
                telemetry.addData("motorFrontLeft", motorFrontLeft.getCurrentPosition());
                telemetry.addData("motorBackRight", motorBackRight.getCurrentPosition());
                telemetry.addData("motorBackLeft", motorBackLeft.getCurrentPosition());

            }

            sleep(50);
        }
        stage = 6;
        telemetry.addData("Stage", stage);

        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        sleep(500);

        //Deliver climbers, and potentially light sensors
        stage = 7;
        telemetry.addData("Stage", stage);

        mjolnir.setPosition(0.1);
    }
}