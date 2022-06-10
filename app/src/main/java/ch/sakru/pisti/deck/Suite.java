package ch.sakru.pisti.deck;

public class Suite {
    private String name;
    private int weight=1;

    public Suite(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public int getWeight(){
        return weight;
    }
}
