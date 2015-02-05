import java.io.*;
import java.util.*;


public class TenThousand {
    
    int num_players;
    private static ArrayList<Player> p_list;
    // each player gets their own dice list on Player() init
    // so don't need to init a dice list here
    
    public static void main(String[] args) {
        System.out.println("-={ Ten Thousand }=-");

        // ask for number of players, cast to int
        System.out.print("Enter the number of players: ");
        Scanner sc = new Scanner(System.in);
        int num_players = sc.nextInt();
        
        // init and fill ArrayList
        p_list = new ArrayList<Player>();
        
        for(int i = 0; i < num_players; i++) {
            Player p = new Player();
            p_list.add(p);
        }
                
        // create that list, pass it to Game();
        System.out.println("Let's play!");
        Game ten_k = new Game(p_list);
    }
}
