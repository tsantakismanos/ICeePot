package iceepotmobile.model;
import java.util.HashMap;
import java.util.List;
/**
 * Created by manos on 3/4/2016.
 */
public class Measurement {

    private long moment;
    private double value;

    public Measurement() {

    }

    public Measurement(long moment, double value) {
        this.moment = moment;
        this.value = value;
    }

    public long getMoment() {
        return moment;
    }

    public void setMoment(long moment) {
        this.moment = moment;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


    public static HashMap<Long, Double> getHashMap(List<Measurement> list){

        HashMap<Long, Double> map = new HashMap<Long, Double>(list.size());

        for(int i=0;i<list.size();i++){
            map.put(list.get(i).getMoment(), list.get(i).getValue());
        }

        return map;
    }

}
