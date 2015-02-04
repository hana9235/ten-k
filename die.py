# implements the Die class for the 10000 dice game

from random import randint

class Die():
    def __init__(self):
        self.value = 1
        
    def roll(self):
        new_value = randint(1, 6)
        self.value = new_value
        print(self.value, end = " ")
            
    def get_value(self):
        return self.value
        
