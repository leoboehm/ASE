from commands.command_interface import Command

class TurnLeftCommand(Command):
    def __init__(self, robot, angle):
        self.robot = robot
        self.angle = angle

    def execute(self):
        self.robot.turn_left(self.angle)
        print(f"[LOG] Executed TurnLeft({self.angle})")

    def undo(self):
        self.robot.turn_right(self.angle)
        print(f"[LOG] Undone TurnLeft({self.angle})")
