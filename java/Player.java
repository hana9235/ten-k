import java.util.*;

public class Player {
    
    int total_score;
    boolean on_board;
    ArrayList<Die> dice_list, held_dice;
    
    public Player() {
        // constructor for the player class
        this.total_score = 0;
        this.on_board = false;
        dice_list = new ArrayList<Die>();
        held_dice = new ArrayList<Die>();
        
        for( int i = 0; i < 6; i++) {
            // populate dice list
            Die d = new Die();
            this.dice_list.add(d); 
        }
    }
    
    public int get_score() {
        return this.total_score;
    }
    
    public void add_to_score(int points) {
        this.total_score += points;
    }
    
    public void roll_dice() {
        Die d;
        for( int i = 0; i < this.dice_list.size(); i++ ){
            d = this.dice_list.get(i);
            d.roll();
        }
    }
    
    public void hold(ArrayList<Integer> dice_to_hold) {
        // move the selected dice from the dice_list to
        // the held_dice list
        for( int i = 0; i < dice_to_hold.size(); i++) {
            int index = dice_to_hold.get(i);
            this.held_dice.add(this.dice_list.get(index));
        }
        
        // now remove the held_dice from the dice_list
        // go backward so there are no index errors
        // check if only one die is selected first
        if (dice_to_hold.size() == 1) {
            this.dice_list.remove(dice_to_hold.get(0));
        }
        else {   // more than one die
            for(int i = dice_to_hold.size(); i > 0; i--) {
                int index = dice_to_hold.get(i - 1);
                this.dice_list.remove(index);
            }
        }
        // might have to add "trimToSize()" on the dice_list
    }
    public void reset_dice() {
        this.dice_list.clear();
        for( int i = 0; i < 6; i++) {
            // populate dice list
            Die d = new Die();
            this.dice_list.add(d); 
        }
        this.held_dice.clear();
    }
    
    public boolean is_on_board(int turn_points) {
        if (!this.on_board) {
            if (turn_points < 1000) {
                // didn't hit threshold
                System.out.println("Not enough to get on the board. Score = 0");
                return false;
            }
            else { // score at 1000+
                this.on_board = true;
                return true;
            }
        }
        else { // already on board
            return true;
        }
    }
    
    public ArrayList<Die> get_rolled_dice(){
        return this.dice_list;
    }
    
    
    public static void main(String[] args) {
        Player p = new Player();
        System.out.println(p.get_score());
        
        for(int i = 0; i < 4; i++) {
            p.roll_dice();
            System.out.println();
        }
        
    }
    
    
}