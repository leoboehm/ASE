from commands.command_interface import Command

class MoveBackwardCommand(Command):
    def __init__(self, robot, distance):
        self.robot = robot
        self.distance = distance

    def execute(self):
        self.robot.move_backward(self.distance)
        print(f"[LOG] Executed MoveBackward({self.distance})")

    def undo(self):
        self.robot.move_forward(self.distance)
        print(f"[LOG] Undone MoveBackward({self.distance})")
