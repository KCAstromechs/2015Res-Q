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

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

/**
 * Base Drive code
 * Blue side of field
 */
public class AutoTestGyro extends LinearOpMode {
    float leftPower;
    float rightPower;
    int stage = 0;
    float startHeading;
    int targetPos;
    float degErr;
    float proportionalConst = 0.1f;
    float correction;

    private static final int kClicksPerRev = 1100;
    private static final int klongDrive = (int) (kClicksPerRev * 3.75);
    private static final int kSlowApproach = kClicksPerRev * 2;


    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

    GyroSensor gyro;


    @Override
    public void runOpMode() throws InterruptedException {
        //Motor init - use Config files
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");

        //reset encoders
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(100);

        // motorFrontRight.setMode();
        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        //reverse Motors
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);

        //Sensor init
        gyro = hardwareMap.gyroSensor.get("gyro");
        gyro.calibrate();

        sleep(100);
        while (gyro.isCalibrating()) {
            sleep(50);
        }

        System.out.println("gyro (PreCalibration" + gyro.getHeading());
        telemetry.addData("gyro (PreCalibration", gyro.getHeading());
        gyro.resetZAxisIntegrator();

        telemetry.addData("Stage", stage);
        telemetry.addData("gyro", gyro.getHeading());
        System.out.println("gyro (PostCalibration?" + gyro.getHeading());

        waitForStart();

        //gyro.resetZAxisIntegrator();
        //sleep(100);

        //turns away from wall - uses gyro to control length of turn (not encoders)
        stage = 1;
        telemetry.addData("Stage", stage);
        telemetry.addData("gyro (PostCalibration)", gyro.getHeading());
        sleep(5000);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        leftPower = 1;
        rightPower = 0;
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
        while (gyro.getHeading() <= 45) {
            //motorFrontLeft.setPower(leftPower);
            //motorBackLeft.setPower(leftPower);
            //telemetry.addData("leftPower", leftPower);
            //telemetry.addData("rightPower", rightPower);
            //telemetry.addData("gyro",gyro.getHeading());
            //System.out.println("gyro="+gyro.getHeading());
            sleep(1);
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);
        telemetry.addData("leftPower", leftPower);
        telemetry.addData("rightPower", rightPower);
        telemetry.addData("gyro", gyro.getHeading());

        //sleep(10000);

        //Long straight drive - Main control is encoders on front right motor, us gyro for stabilization
        stage = 2;
        telemetry.addData("Stage", stage);
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(100);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

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

        float localLeftPower=leftPower;
        float localRightPower=rightPower;

        while (motorFrontRight.getCurrentPosition() < targetPos &&
                motorFrontLeft.getCurrentPosition() < targetPos &&
                motorBackRight.getCurrentPosition() < targetPos &&
                motorBackLeft.getCurrentPosition() < targetPos) {


            degErr = gyro.getHeading() - startHeading;
            correction = degErr * proportionalConst;
            localLeftPower= Range.clip(leftPower-correction,-1.0f,1.0f);
            localRightPower=Range.clip(rightPower+correction,-1.0f,1.0f);

                motorFrontRight.setPower(localRightPower);
                motorFrontLeft.setPower(localLeftPower);
                motorBackRight.setPower(localRightPower);
                motorBackLeft.setPower(localLeftPower);

            System.out.println(gyro.getHeading()+" correction:"+correction);

            sleep(1);
        }


        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        //turns towards beacon use gyro, not encoders
        stage = 4;
        telemetry.addData("Stage", stage);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        startHeading = gyro.getHeading();
        while (gyro.getHeading() <= 60 - startHeading) {
            motorFrontLeft.setPower(leftPower);
            motorBackLeft.setPower(leftPower);
        }
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);

        //slow approach - use light sensor to follow line
        stage = 5;
        telemetry.addData("Stage", stage);
        rightPower = 0.5f;
        leftPower = 0.5f;

        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        sleep(100);
        motorFrontRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorFrontRight.setTargetPosition(kSlowApproach);
        motorFrontLeft.setTargetPosition(kSlowApproach);
        motorBackRight.setTargetPosition(kSlowApproach);
        motorBackLeft.setTargetPosition(kSlowApproach);
        //gyro stabilization - PID
        stage = 6;
        telemetry.addData("Stage", stage);

        motorFrontRight.setPower(rightPower);
        motorFrontLeft.setPower(leftPower);
        motorBackRight.setPower(rightPower);
        motorBackLeft.setPower(leftPower);


        motorFrontRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);

        //Deliver climbers, and potentially light sensors
        stage = 7;
        telemetry.addData("Stage", stage);
    }
}

