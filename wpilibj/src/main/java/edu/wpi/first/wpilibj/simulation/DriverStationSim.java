/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.simulation;

import edu.wpi.first.hal.simulation.DriverStationDataJNI;
import edu.wpi.first.hal.simulation.NotifyCallback;

public class DriverStationSim {
  public CallbackStore registerEnabledCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerEnabledCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelEnabledCallback);
  }
  public boolean getEnabled() {
    return DriverStationDataJNI.getEnabled();
  }
  public void setEnabled(boolean enabled) {
    DriverStationDataJNI.setEnabled(enabled);
  }

  public CallbackStore registerAutonomousCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerAutonomousCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelAutonomousCallback);
  }
  public boolean getAutonomous() {
    return DriverStationDataJNI.getAutonomous();
  }
  public void setAutonomous(boolean autonomous) {
    DriverStationDataJNI.setAutonomous(autonomous);
  }

  public CallbackStore registerTestCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerTestCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelTestCallback);
  }
  public boolean getTest() {
    return DriverStationDataJNI.getTest();
  }
  public void setTest(boolean test) {
    DriverStationDataJNI.setTest(test);
  }

  public CallbackStore registerEStopCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerEStopCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelEStopCallback);
  }
  public boolean getEStop() {
    return DriverStationDataJNI.getEStop();
  }
  public void setEStop(boolean eStop) {
    DriverStationDataJNI.setEStop(eStop);
  }

  public CallbackStore registerFmsAttachedCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerFmsAttachedCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelFmsAttachedCallback);
  }
  public boolean getFmsAttached() {
    return DriverStationDataJNI.getFmsAttached();
  }
  public void setFmsAttached(boolean fmsAttached) {
    DriverStationDataJNI.setFmsAttached(fmsAttached);
  }

  public CallbackStore registerDsAttachedCallback(NotifyCallback callback, boolean initialNotify) {
    int uid = DriverStationDataJNI.registerDsAttachedCallback(callback, initialNotify);
    return new CallbackStore(uid, DriverStationDataJNI::cancelDsAttachedCallback);
  }
  public boolean getDsAttached() {
    return DriverStationDataJNI.getDsAttached();
  }
  public void setDsAttached(boolean dsAttached) {
    DriverStationDataJNI.setDsAttached(dsAttached);
  }
  public void notifyNewData() {
    DriverStationDataJNI.notifyNewData();
  }

  /**
   * Toggles suppression of DriverStation.reportError and reportWarning messages.
   *
   * @param shouldSend If false then messages will will be suppressed.
   */
  public void setSendError(boolean shouldSend) {
    DriverStationDataJNI.setSendError(shouldSend);
  }

  public void resetData() {
    DriverStationDataJNI.resetData();
  }
}