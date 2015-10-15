package iceepotmobile.exceptions;

/**
 * Created by manos on 14/10/2015.
 */
public class ValueMissingException extends  Exception {

    private String field;

    public ValueMissingException(String field){

        super("Field "+ field +"is missing");

        this.field = field;

    }

    public String getField() {
        return field;
    }
}
