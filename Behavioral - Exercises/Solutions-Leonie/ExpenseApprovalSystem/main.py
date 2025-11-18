from handlers.team_lead import TeamLead
from handlers.dept_manager import DeptManager
from handlers.director import Director
from handlers.ceo import CEO

def main():
    # create approver authorities with limits
    team_lead = TeamLead("Team Lead", 500)
    manager = DeptManager("Department Manager", 2000)
    director = Director("Director", 10000)
    ceo = CEO("CEO", float("inf"))

    # build approval chain
    team_lead.set_next(manager).set_next(director).set_next(ceo)

    expenses = [250, 1200, 7500, 20000]

    for amount in expenses:
        print("\nSubmitting expense request:", amount)
        team_lead.process_request(amount)

if __name__ == "__main__":
    main()
