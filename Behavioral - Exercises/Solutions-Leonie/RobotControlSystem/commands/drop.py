from commands.command_interface import Command

class DropCommand(Command):
    def __init__(self, robot):
        self.robot = robot

    def execute(self):
        self.robot.drop()
        print("[LOG] Executed Drop")

    def undo(self):
        self.robot.pick_up()
        print("[LOG] Undone Drop")
