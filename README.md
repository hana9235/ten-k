# ten-k
Code to implement the dice game 10,000.  Original "pseudocode" in python, to be turned into an Android app later.
Some rules have been altered for simplicity's sake and may be implemeted at a later time.

10,000 is a game played with 6 dice.  The goal is to be the first to get to 10,000 points.

The rules:
  To get on the board, a player must score at least 1000 points. From then on, the points stack.
  If a player chooses to, they may "hold" any of the dice (the smartest choices are scoring dice), and then only reroll the ones that did not score.
  Should a player score points with all 6 dice (held or rolled), they may continue rolling with all 6 dice.
  If a player rolls at any point during their turn and there are no scoring dice, then that player is "bust" and loses all points for that turn.
  
Scoring:
  If a player rolls three of any number, the points are calculated by:  number * 100.
  If a player rolls more than three of any number, the points are calculated by: the first three dice score the face value * 100, which is doubled for each additional die
  If a player does not have three of a particular number, then only 1's and 5's count for points.
    1's are worth 100 each, 5's are worth 50 each.
    
  Special cases:
    Three 1's are worth 1000 points.  Four are worth 2000 points.  
    # Three-pair and 1-6 Straight have yet to be implemented.
