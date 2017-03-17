
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class RCMFunction
 * Created by dantedg on 3/14/2017.
 */
public class RCMFunction{
    private double capacity;
    private double weight;
    private USMoney maxMoney;
    private USMoney currentMoney;
    private String location;
    private String ID;
    private int numItems;
    private ArrayList<RecyclableItem> activeRecyclableItems;
    private HashMap<String, USMoney> recyclableItemPrices;

    public RCMFunction(String location, String ID, int capacity) {
        this.location = location;
        this.ID = ID;
        this.capacity = capacity;
        weight = 0;
        numItems = 0;
        this.maxMoney = new USMoney(200, 0);
        currentMoney = maxMoney;
        activeRecyclableItems = new ArrayList<RecyclableItem>();
        recyclableItemPrices = new HashMap<String, USMoney>();
    }

    /////////Getters and Setters\\\\\\\\\
    public int getNumItems() {
        return numItems;
    }

    public int availableItems() {
        return activeRecyclableItems.size();
    }

    public String getItemNameByIndex(int i) {
        return activeRecyclableItems.get(i).getMaterialType();
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public USMoney getMaxMoney(){ return maxMoney; }

    public USMoney getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(USMoney currentMoney) {
        this.currentMoney = currentMoney;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String place) {
        this.location = place;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "RCM: " + ID + " in " + location;
    }

    public void setRecyclableItemPrices(String key, USMoney price){
        recyclableItemPrices.get(key).setDollars(price.getDollars());
        recyclableItemPrices.get(key).setCents(price.getCents());
    }

    public HashMap<String,USMoney> getRecyclableItemPrices() { return recyclableItemPrices; }

    ////////////////////// OPERATION METHODS //////////////////////
    public void empty(){
        weight = 0;
    }

    public void refillMoney() { currentMoney = maxMoney; }

    //What gets called when an item gets recycled
    public USMoney recycle(ArrayList<RecyclableItem> recyclables) {
        double tmpWeight;
        double sumWeight = 0;
        USMoney tmpCost;
        USMoney sumCost = new USMoney(0, 0);
        for(RecyclableItem tmp : recyclables){
            tmpWeight = tmp.getWeight();

            tmpCost = recyclableItemPrices.get(tmp.getMaterialType()).calculateCost(tmpWeight);

            if(currentMoney.getDouble() - tmpCost.getDouble() < 0){
                break;
            }
            RCMTransaction.post(tmpCost.toString(), ID, tmpWeight, tmp.getMaterialType(), 0);
            sumWeight += tmpWeight;
            sumCost.addTo(tmpCost.getDollars(), tmpCost.getCents());
            numItems++;
        }
        weight += sumWeight;
        double val = (currentMoney.getDouble() - sumCost.getDouble()) * 100;
        currentMoney.setDollars(0);
        currentMoney.setCents((int)val);
        return sumCost;
    }

    public void addItem(RecyclableItem x, USMoney value) {
        activeRecyclableItems.add(x);
        recyclableItemPrices.put(x.getMaterialType(),value);
    }
/*
    public void logTransaction(ArrayList<RecyclableItem> items, ArrayList<String> sales) {
        RecyclableItem item;
        String sale;
        for(int i=0;i<items.size();i++) {
            item = items.get(i);
            sale = sales.get(i);
            RCMTransaction.post("90",ID,item.getWeight(),item.getMaterialType(),0);
        }
    }*/
}
