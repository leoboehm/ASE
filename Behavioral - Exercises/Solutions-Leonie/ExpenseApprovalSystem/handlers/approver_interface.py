from abc import abstractmethod

class Approver:
    def __init__(self, name, limit):
        self.name = name
        self.limit = limit
        self.next_approver = None

    def set_next(self, approver: "Approver"):
        self.next_approver = approver
        return approver

    def process_request(self, amount):
        if amount <= self.limit:
            print(f"{self.name} approved the expense of ${amount:.2f}.")
        else:
            print(f"{self.name} cannot approve ${amount:.2f}. Request is submitted to higher authority.")
            self.next_approver.process_request(amount)

    @abstractmethod
    def role(self):
        raise NotImplementedError
