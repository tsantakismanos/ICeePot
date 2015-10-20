package iceepotmobile.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import iceepot.iceepotmobile.R;

/**
 * Created by manos on 20/10/2015.
 */
public class GenericDialog extends AlertDialog {

    public GenericDialog(Context context) {
        super(context);
    }

    public static AlertDialog createFromException(Context ctx, Exception ex){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setPositiveButton(R.string.new_pot_warning, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setMessage(ex.getMessage());
        return builder.create();
    }
}
