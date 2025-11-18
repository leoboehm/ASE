from handlers.approver_interface import Approver

class CEO(Approver):
    def role(self):
        return "CEO"
