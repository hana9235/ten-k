# python 3.4 implementation of 10,000 (dice game)

from player import *
from die import *
from game import *

def main():
    # list of players
    num = input("Enter the number of players: ")
    num = int(num)
    
    # generate 6 dice
    dice_list = [Die() for i in range(6)]
    
    player_list = []
    for i in range(num):
        player_list.append(Player(dice_list.copy()))

    print("Let's play!\n")
    
    game = Game(player_list)
    

main()