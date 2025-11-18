from robot import Robot
from command_invoker import CommandInvoker

from commands.move_forward import MoveForwardCommand
from commands.move_backward import MoveBackwardCommand
from commands.turn_left import TurnLeftCommand
from commands.turn_right import TurnRightCommand
from commands.pickup import PickUpCommand
from commands.drop import DropCommand

def main():
    robot = Robot()
    invoker = CommandInvoker()

    # queue some commands
    invoker.add_command(MoveForwardCommand(robot, 10))
    invoker.add_command(TurnLeftCommand(robot, 90))
    invoker.add_command(PickUpCommand(robot))
    invoker.add_command(MoveBackwardCommand(robot, 5))
    invoker.add_command(DropCommand(robot))

    # execute queued commands
    invoker.run()

    # undo last action
    print("\n--- Undo last command ---")
    invoker.undo_last()

    # undo the action before
    print("\n--- Undo another command ---")
    invoker.undo_last()

if __name__ == "__main__":
    main()
