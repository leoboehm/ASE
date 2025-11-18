from commands.command_interface import Command

class MoveForwardCommand(Command):
    def __init__(self, robot, distance):
        self.robot = robot
        self.distance = distance

    def execute(self):
        self.robot.move_forward(self.distance)
        print(f"[LOG] Executed MoveForward({self.distance})")

    def undo(self):
        self.robot.move_backward(self.distance)
        print(f"[LOG] Undone MoveForward({self.distance})")
