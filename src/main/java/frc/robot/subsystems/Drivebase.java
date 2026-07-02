// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.system.plant.DCMotor;

import com.revrobotics.ResetMode;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.PersistMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkMax;

public class Drivebase extends SubsystemBase {
  
  // 1. The 4 motors of the base (Always SparkMax, real or sim!)
  SparkMax frontLeftMotor = new SparkMax(1, MotorType.kBrushless);
  SparkMax backLeftMotor = new SparkMax(2, MotorType.kBrushless);
  SparkMax frontRightMotor = new SparkMax(3, MotorType.kBrushless);
  SparkMax backRightMotor = new SparkMax(4, MotorType.kBrushless);

  // 2. Declare the simulation wrappers separately
  SparkMaxSim frontLeftSim;
  SparkMaxSim backLeftSim;
  SparkMaxSim frontRightSim;
  SparkMaxSim backRightSim;

  /** Creates a new Drivebase subsystem, configures motors */
  public Drivebase() {
    
    // 3. Only initialize the wrappers if we are in Sim mode
    if (RobotBase.isSimulation()) {
        // You pass the real motor in, plus the physical gearbox/motor model (e.g., 1 NEO)
        frontLeftSim = new SparkMaxSim(frontLeftMotor, DCMotor.getNEO(1));
        backLeftSim = new SparkMaxSim(backLeftMotor, DCMotor.getNEO(1));
        frontRightSim = new SparkMaxSim(frontRightMotor, DCMotor.getNEO(1));
        backRightSim = new SparkMaxSim(backRightMotor, DCMotor.getNEO(1));
    }

    // Creates a standard configuration
    SparkMaxConfig config = new SparkMaxConfig();
    config.idleMode(IdleMode.kBrake);
    config.smartCurrentLimit(40); // 40A is a great, safe choice to chillax with!

    // 4. FIX: Create a NEW config object for the inverted side
    SparkMaxConfig invertConfig = new SparkMaxConfig();
    invertConfig.apply(config);  // Copy the brake/current limit settings over
    invertConfig.inverted(true); // Now invert ONLY this config

    // Apply the configurations
    frontLeftMotor.configure(invertConfig, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    
    SparkMaxConfig followConfig = new SparkMaxConfig();
    followConfig.apply(config);
    backLeftMotor.configure(followConfig.follow(frontLeftMotor), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);

    frontRightMotor.configure(config, ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
    backRightMotor.configure(followConfig.follow(frontRightMotor), ResetMode.kNoResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void move(double leftPower, double rightPower) {
    frontLeftMotor.set(leftPower);
    frontRightMotor.set(rightPower);
  }

  public void stop() {
      frontLeftMotor.set(0);
      frontRightMotor.set(0);
  }
}