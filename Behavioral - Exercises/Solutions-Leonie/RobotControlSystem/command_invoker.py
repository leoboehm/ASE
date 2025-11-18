class CommandInvoker:
    def __init__(self):
        self.command_queue = []
        self.history = []

    # add command to queue
    def add_command(self, command):
        self.command_queue.append(command)

    # execute command queue
    def run(self):
        while self.command_queue:
            command = self.command_queue.pop(0)
            command.execute()
            self.history.append(command)

    # undo last command from history
    def undo_last(self):
        if not self.history:
            print("Nothing to undo.")
            return

        last_command = self.history.pop()
        last_command.undo()
