from handlers.approver_interface import Approver

class DeptManager(Approver):
    def role(self):
        return "Department Manager"
