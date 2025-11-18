from handlers.approver_interface import Approver

class Director(Approver):
    def role(self):
        return "Director"
