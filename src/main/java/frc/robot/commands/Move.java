package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

public class Move extends Command {
    private Drivebase driveSubsystem;

    double leftPower;
    double rightPower;

    public Move(Drivebase driveSubsystem, double leftY, double rightX) {
        this.driveSubsystem = driveSubsystem;

        leftPower = leftY - rightX;
        rightPower = leftY + rightX;

        // lower values bc im scared yah yah
        leftPower /= 10;
        rightPower /= 10;
    }

    public void initialize() {
        driveSubsystem.move(leftPower, rightPower);
    }
}
