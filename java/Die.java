import java.util.*;
import java.io.*;

public class Die {
    
    int value;
    Random random;
    
    public Die() {
        this.value = 1;
        random = new Random();
        
    }
    
    public void roll() { // probably change return type
        int new_value = random.nextInt(6) + 1;
        this.value = new_value;
        System.out.print(this.value + " ");
    }
    
    public int get_value() {
        return this.value;
    }
    
    public static void main(String[] args) {
        Die d = new Die();
        for (int i = 0; i < 6; i++) {
            d.roll();
        }
    }
}
