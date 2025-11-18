from commands.command_interface import Command

class PickUpCommand(Command):
    def __init__(self, robot):
        self.robot = robot

    def execute(self):
        self.robot.pick_up()
        print("[LOG] Executed PickUp")

    def undo(self):
        self.robot.drop()
        print("[LOG] Undone PickUp")
