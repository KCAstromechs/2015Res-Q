package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Kevin on 1/7/2016.
 */
public interface AstroRobotBaseInterface {
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

    void setLeftHookPosition(double position);

    void setRightHookPosition(double position);
}
