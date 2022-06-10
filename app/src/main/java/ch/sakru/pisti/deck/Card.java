package ch.sakru.pisti.deck;

public class Card {
    private Integer cardNumber; // It's a sequential Number in the unsorted Deck.
    private String name;
    private Integer weight;
    private Integer point=0;
    String suite;

    public Card(Integer cardNumber, Suite suite, String name, int weight){
        this.cardNumber = cardNumber;
        //this.suite = suite;
        this.name = name;
        this.weight= weight;
    }

    public Card(Integer cardNumber){
        this.cardNumber = cardNumber;
        this.point = 0;
        this.weight = 0;
    }

    public void setWeight(int weight){
        this.weight=weight;
    }

    public int getWeight(){
        return weight;
    }

    public Integer getCardNumber(){
        return cardNumber;
    }

    // public void setSuite(Suite suite){
    //    this.suite=suite;
    // }

    public void setPoint(Integer point){
        this.point = point;
    }

    public Integer getPoint(){
        return point;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
       return name;
    }

    public void setSuite(String suite){
        this.suite = suite;
    }

    public String getSuite(){
        return this.suite;
    }
}
