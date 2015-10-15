package iceepotmobile.exceptions;

/**
 * Created by manos on 14/10/2015.
 */
public class ValuesOrderException extends  Exception{

    private String valueGreater;
    private String valueLesser;

    public ValuesOrderException(String valueGreater, String valueLesser){


        super("Value " +  valueLesser + " is greater than " + valueGreater);

        this.valueGreater = valueGreater;
        this.valueLesser = valueLesser;

    }

    public String getValueGreater() {
        return valueGreater;
    }

    public String getValueLesser() {
        return valueLesser;
    }
}
