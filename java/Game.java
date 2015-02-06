import java.io.*;
import java.util.*;

public class Game {
    
    boolean game_won;
    ArrayList<Player> player_list;
    int current_player;
    static int GAME_LIMIT = 10000;
    
    public Game(ArrayList<Player> p_list) {
        this.player_list = p_list;
        this.game_won = false;
        
        // an index to be used for the player_list
        this.current_player = 0;
        play();
    }
    

    public void play() {
        while (!this.game_won) {
            System.out.println("\nPlayer " + (this.current_player + 1) + "'s turn");
            Player p = this.player_list.get(this.current_player);
            System.out.println("============");
            take_turn(p);
            next_player();
        }
        System.out.println("Game over, congratulations Player " + (this.current_player + 1) + "!");
        return;
    }
    
    public void take_turn(Player p) {
        boolean is_rolling = true;
        int turn_total = 0;
        while (is_rolling) {
            p.roll_dice();
            ArrayList<Integer> roll_results = calculate_roll_value(p);
            int roll_score = roll_results.get(0);
            int scoring_dice = roll_results.get(1);

            // check for no points
            if (roll_score == 0) {
                // bust
                is_rolling = false;
                System.out.println("Bust!");
                p.get_score();
                this.show_score(p);
                p.reset_dice();
                break;
            }
            
            // player scored something, ask what to do next
            Scanner sc = new Scanner(System.in);
            System.out.print("Roll again? (y/n): ");
            String go_again = sc.next().toLowerCase();
            int y_or_n = go_again.compareTo("y");
            
            if(y_or_n == 0) { // player said yes
                if(scoring_dice == 6) {
                    // all dice score some value, reset and reroll all
                    System.out.println("resetting dice...");
                    p.reset_dice();
                }
                else {
                    // at least one didn't score, so offer to hold the ones that did
                    System.out.print("Hold any dice? (y/n): ");
                    String holding = sc.next().toLowerCase();
                    int hold_y_n = holding.compareTo("y");
                    if (hold_y_n == 0) { // yes to hold some
                        System.out.println();
                        // show dice and ask player to choose which
                        ArrayList<Die> rolled = p.get_rolled_dice();
                        for(int i = 0; i < rolled.size(); i++) {
                            System.out.print("D" + (i + 1) + " ");
                        }
                        
                        System.out.println();
                        for(Die d : rolled) {
                            System.out.print(" " + d.get_value() + " ");
                        }
                        System.out.println();
                        
                        // choose which
                        System.out.println("\nWhich dice do you want to hold?");
                        System.out.print("Enter the numbers (D1 = 1) separated only by commas: ");
                        
                        ArrayList<Integer> dice_to_hold = new ArrayList<Integer>();
                        String dice_indexes = sc.next();
                        
                        for (int i = 0; i < dice_indexes.length(); i++) {
                            String die_num = Character.toString(dice_indexes.charAt(i));
                            int is_comma = die_num.compareTo(",");
                            if (is_comma == 0) {
                                continue;
                            }
                            else {
                                // add the values to the hold list, adjusted for 0-index
                                dice_to_hold.add(Integer.parseInt(die_num) - 1);
                                p.hold(dice_to_hold);
                                turn_total += roll_score;
                            }
                        }

                        System.out.println("\n\n DICETOHOLD = " + dice_to_hold + "\n\n");
                    }
                    else {
                        // didn't hold any scoring dice, so lose their point values
                        turn_total += 0;
                    }
                }
            }
            else { // player said no to roll again
                turn_total += roll_score;
                if (p.is_on_board(turn_total)) {
                    // only add points if player is on board
                    // requires 1000+ in one roll
                    p.add_to_score(turn_total);
                }
                
                p.reset_dice();
                if (p.get_score() >= GAME_LIMIT) {
                    this.game_won = true;
                }
                this.show_score(p);
                break;
            }
        }
        return;
    }
    
    public ArrayList<Integer> calculate_roll_value(Player p) {
        ArrayList<Integer> results = new ArrayList<Integer>();
        // result will have two values, the point value and the number of scoring dice
        
        int sum = 0;
        int scoring_dice = 0;
        ArrayList<Die> rolled_dice = p.get_rolled_dice();
        
        // count occurrences
        Map<Integer, Integer> counted = new HashMap<Integer, Integer>();
        for (int i = 0; i < rolled_dice.size(); i++) {
            int die_face_val = rolled_dice.get(i).get_value();
            if(!counted.containsKey(die_face_val)) {
                // value is not yet in the map, add it
                counted.put(die_face_val, 1);
            }
            else { // value exists
                int old_count = (int) counted.get(die_face_val);
                counted.put(die_face_val, old_count + 1);
            }
        }
        
        // counted should contain something like:
        // {1=3, 4=2, 6=1}
        
        // rules vary for scoring if at least 3 of a value has been rolled
        ArrayList<Integer> at_least_three = new ArrayList<Integer>();
        ArrayList<Integer> less_than_three = new ArrayList<Integer>();
        
        for( Map.Entry<Integer, Integer> entry : counted.entrySet()) {
            int face_val = entry.getKey();
            int occurrences = entry.getValue();
            
            if (occurrences < 3) {
                less_than_three.add(face_val);
            }
            else {
                at_least_three.add(face_val);
            }
        }
        // the roll values are properly sorted between the lists
        
        // first take care of special cases
        if (less_than_three.size() == 6) {
            // rolled a straight
            sum = 1500;
            scoring_dice = 6;
            results.add(sum);
            results.add(scoring_dice);
            System.out.println("Straight");
            return results;
        }

        if (less_than_three.size() == 3 && at_least_three.size() == 0
            && rolled_dice.size() == 6) {
            // possibly rolled three pairs
            boolean is_three_pair = true;
            for(int i = 0; i < 3; i++) {
                int key = less_than_three.get(i);
                int occurrences = counted.get(key);
                if (occurrences != 2) {
                    // if one isn't a pair, three pair fails
                    is_three_pair = false;
                }
            }
            if(is_three_pair) {
                // if it got to here, then it must be three pair
                sum = 1500;
                scoring_dice = 6;
                System.out.println("Three pairs");
                results.add(sum);
                results.add(scoring_dice);
                return results;
            }
        }
        
        // special cases didn't happen, calculate point values of roll
        // three or more of a number
        for(int val : at_least_three) {
            int occurrences = counted.get(val);
            if(occurrences > 3) {
                if (val == 1) {
                    // special case, 3 1's == 1000
                    sum += 1000 * (2 * (occurrences - 3));
                }
                else { // not 1's, no special case
                    sum += (val * 100) * (2 * (occurrences - 3));   
                }
                scoring_dice += occurrences;
            }
            else { // exactly 3 of the same value rolled
                if (val == 1) {
                    sum += 1000;
                }
                else {
                    sum += (val * 100);
                }
                scoring_dice += occurrences;
            }
        }
        
        // for 1 or 2 of a number, only 1's and 5's count for points
        for (int val : less_than_three) {
            int occurrences = counted.get(val);
            if (val == 1) {
                sum += 100 * occurrences;
                scoring_dice += occurrences;
            }
            else if (val == 5) {
                sum += 50 * occurrences;
                scoring_dice += occurrences;
            }
        }
        
        System.out.println("score: " + sum);
        
        results.add(sum);
        results.add(scoring_dice);
        return results;
    }
    
    public void next_player() {
        // get next player from list, or loop back to player 1
        if(this.current_player < this.player_list.size() - 1) {
            this.current_player++;
        }
        else {
            this.current_player = 0;
        }
    }
    
    public void show_score(Player p) {
        System.out.print("Your current score is: ");
        System.out.println(p.get_score());
    }
    
    public static void main(String[] args) {
        System.out.println("Game running.");
    }
}
