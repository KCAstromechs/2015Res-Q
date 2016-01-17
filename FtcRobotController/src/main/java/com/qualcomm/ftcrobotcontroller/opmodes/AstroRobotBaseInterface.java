package com.qualcomm.ftcrobotcontroller.opmodes;

import android.hardware.Camera;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Kevin on 1/7/2016.
 */
public interface AstroRobotBaseInterface {
    void initializeVariables(HardwareMap hardwareMap);

    void setDriveReverse();

    void setDriveForward();

    void snapPic();

    void cameraSetup() throws InterruptedException;

    Camera.PictureCallback getPicCallback();

    void calibrateGyro()throws InterruptedException;

    void setGrabberUp();

    void setGrabberMiddle();

    void setGrabberDown();

    void setLeftZiplineUp();

    void setLeftZiplineDown();

    void setRightZiplineUp();

    void setRightZiplineDown();

    void setMjolnirDown();

    void setMjolnirUp();

    void hammerTime() throws InterruptedException;

    void setLeftLockOpen();

    void setRightLockOpen();

    void setLeftLockClosed();

    void setRightLockClosed();

    void initializeServos();

    void setRightPower(double rightPower);

    void setLeftPower(double leftPower);

    void turn(int turnHeading, double power)throws InterruptedException;

    void driveStraight(double inches, double power, int heading, float direction)throws InterruptedException;

    void driveStraightEncoder(int dist, double power, int heading, float direction)throws InterruptedException;

    void setLeftHookPosition(double position);

    void setRightHookPosition(double position);

    void updateWinchAndDrawerSlide(float winch, float DrawerSlide);
}
