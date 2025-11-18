from handlers.approver_interface import Approver

class TeamLead(Approver):
    def role(self):
        return "Team Lead"
