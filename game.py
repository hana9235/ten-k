# Implements the game class for the 10000 dice game

from die import *
from player import *
from collections import Counter

# someone has to hit 1000 in a turn to get out, set their on_board = true
# three of one number = number * 100 (same for 4)
# every single 1 = 100
# every single 5 = 50
# no 1's, 5's, or trios, bust, player's current turn = 0, next player
# after every roll, ask to stop
    # if stopping, current score added to total score
    # if rolling again, ask if anything held (number dice)
# if all 6 dice have been held/score is still valid, keep current total, unhold all dice, keep going

class Game():

    def __init__(self, player_list):
        self.game_won = False
        self.players = player_list
        self.current_player = 0
        self.play()
        

    def play(self):
    
        while not self.game_won:

            print("\n\nPlayer {0}'s turn".format(self.current_player + 1))
            self.p = self.players[self.current_player]
            print("============")
            self.take_turn()
            self.next_player()
            
        print("Game over! Congratulations Player {0}!".format(self.current_player + 1) )
        return
        
        
    def take_turn(self, turn_total = 0):
        # controls the rolling of dice, holding of dice, and whether or not the 
        # user would like to keep going
        turn_total = turn_total
        self.p.roll_dice()
        roll_val, scoring_dice = self.calculate_roll_value()
        print("Total for this turn so far: ", turn_total)
        if roll_val == 0:
            print("Bust!")
            self.show_score()
            self.p.reset_dice()
            return
            
        # made some points, what do you want to do?
        go_again = input("Roll again? (y/n): ")
        if go_again.lower() == "y":
            # IF ALL DICE ARE SCORING THEN RESET AND START AGAIN
            if scoring_dice == len(self.p.dice):
                # all dice score some point value, reset and roll all 6 again
                self.p.reset_dice()
                
            else:    
                holds = input("Do you want to hold any dice? (y/n): ")
                if holds.lower() == "y":
                    # show dice that were rolled this turn
                    for i in range(len(self.p.dice)):
                        print("D{0}".format(i + 1), end = " ")
                    print("")
                    for d in self.p.dice:
                      print(" {0}".format(d.get_value()), end = " ")
                    
                    dice_to_hold = input("\nWhich dice do you want to hold? ")
                    dice_to_hold.strip()
                    holding = [int(index) - 1 for index in dice_to_hold.split(" ")]
                    
                    self.p.hold(holding)
                    turn_total += roll_val
                
                else: # didn't hold any scoring dice    
                    turn_total += 0

            self.take_turn(turn_total)

        else: # player is done, next player
            turn_total += roll_val
            if self.p.is_on_board(turn_total): 
                # only add points if player is on board, requires 1000+ points in one roll
                self.p.add_to_score(turn_total)

            self.p.reset_dice()
            if self.p.get_total_score() >= 10000:
                self.game_won = True
                print("YOU WIN!")
            self.show_score()
            return
        
    def next_player(self):
        # ensures that the game loops through player list
        if self.current_player < len(self.players) - 1:
            self.current_player += 1
        else:
            self.current_player = 0
        
       
    def calculate_roll_value(self):
        sum = 0
        scoring_dice = 0
        # see what was rolled
        rolled_dice = self.p.dice
        
        values = [d.get_value() for d in rolled_dice]
        counted_values = Counter(values)
        print("\nCOUNTED: ", counted_values)
        
        at_least_three = [d for d in counted_values if counted_values[d] > 2]
        less_than_three = [d for d in counted_values if counted_values[d] < 3]
        
        # three or more of a number
        for val in at_least_three:
            # 1 is a special case, three 1's = 1000
            if val == 1:
                sum += 1000
            else:
                if counted_values[val] > 3:
                    scoring_dice += counted_values[val]
                    # the first 3 are still the value * 100, every addition value doubles score
                    sum += (val * 100) * (2 * (counted_values[val] - 3))
                else:
                    scoring_dice += counted_values[val]
                    sum += (val * 100)
                    
        # one or two of a number, only 1 and 5 count
        for val in less_than_three:
            if val == 1:
                scoring_dice += counted_values[val]
                sum += 100 * counted_values[val]
            elif val == 5:
                scoring_dice += counted_values[val]
                sum += 50 * counted_values[val]
        print("Score for this roll: ", sum)
        return sum, scoring_dice
        
    def show_score(self):
        print("Player {0}'s score: ".format(self.current_player + 1), self.p.get_total_score())
        
