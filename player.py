# implements the Player class for the 10000 dice game

class Player():

    def __init__(self, dice_list):
        self.total_score = 0
        self.on_board = False
        self.dice = dice_list
        self.held_dice = []

    def get_total_score(self):
        return self.total_score
        
    def add_to_score(self, points):
        self.total_score += points
        
    def roll_dice(self):
        if len(self.dice) == 0: #all are held
            tmp = []
            self.dice = self.held_dice
            self.held_dice = tmp
        for d in self.dice:
            d.roll()
            
    def hold(self, dice_to_hold):
        # gets a list of indices, moves them to the held_dice
        for i in dice_to_hold:
            self.held_dice.append(self.dice[i])
        
        # now safely remove held from .dice
        dice_to_hold.reverse()
        for i in dice_to_hold:
            self.dice.pop(i)
        
    def reset_dice(self):
        for d in self.held_dice:
            self.dice.append(d)
        self.held_dice = []
        
    def is_on_board(self, turn_points):
        if not self.on_board: # need 1000 to get on board and score
            if turn_points < 1000: # didn't hit threshold
                print("Not enough to get on the board. Score = 0")
                return False
            else: # score at 1000+
                self.on_board = True
                return True
        else: 
            #already on board
            return True
            
