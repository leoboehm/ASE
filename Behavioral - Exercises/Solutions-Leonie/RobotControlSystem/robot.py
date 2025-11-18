class Robot:
    def __init__(self):
        self.position = 0
        self.angle = 0
        self.holding_object = False

    # move
    def move_forward(self, distance: float):
        self.position += distance
        print(f"Robot moved {distance} units forward units. New position: {self.position}")

    def move_backward(self, distance: float):
        self.position -= distance
        print(f"Robot moved {distance} units backward. New position: {self.position}")

    # turn
    def turn_left(self, angle: float):
        self.angle = (self.angle - angle) % 360
        print(f"Robot turned {angle} degrees left. New angle: {self.angle}")

    def turn_right(self, angle: float):
        self.angle = (self.angle + angle) % 360
        print(f"Robot turned {angle} degrees right. New angle: {self.angle}")

    # pick up/drop object
    def pick_up(self):
        if not self.holding_object:
            self.holding_object = True
            print("Robot picked up the object at its current location.")
        else:
            print("Robot is already holding an object and cannot pick up another one.")

    def drop(self):
        if self.holding_object:
            self.holding_object = False
            print("Robot dropped the object at its current location.")
        else:
            print("Robot is not holding anything.")
