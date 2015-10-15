package iceepotmobile.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import iceepot.iceepotmobile.R;
import iceepotmobile.application.DbHelper;
import iceepotmobile.exceptions.ValueMissingException;
import iceepotmobile.exceptions.ValuesOrderException;
import iceepotmobile.model.Pot;

/**
 * Created by manos on 11/10/2015.
 */
public class NewPotActivity extends AppCompatActivity{

    EditText etxDescription;
    EditText etxId;
    TextView txtMinMoistVal;
    TextView txtMaxMoistVal;
    TextView txtMinMoisture;
    TextView txtMaxMoisture;
    SeekBar skbMinMoisture;
    SeekBar skbMaxMoisture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_pot);

        etxDescription = (EditText)findViewById(R.id.etxDescription);
        etxId = (EditText)findViewById(R.id.etxId);

        txtMinMoistVal = (TextView)findViewById(R.id.txtMinMoistVal);
        txtMaxMoistVal = (TextView)findViewById(R.id.txtMaxMoistVal);
        txtMinMoisture = (TextView)findViewById(R.id.txtMinMoisture);
        txtMaxMoisture = (TextView)findViewById(R.id.txtMaxMoisture);

        skbMinMoisture = (SeekBar)findViewById(R.id.skbMinMoisture);
        skbMaxMoisture = (SeekBar)findViewById(R.id.skbMaxMoisture);

        skbMinMoisture.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtMinMoistVal.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skbMaxMoisture.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtMaxMoistVal.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_new_pot, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.new_pot_save) {

            try {
                validateValues();

                Pot p = new Pot(Integer.parseInt(etxId.getText().toString()), etxDescription.getText().toString(), Double.valueOf(txtMinMoistVal.getText().toString()), Double.valueOf(txtMaxMoistVal.getText().toString()));
                p.insert(DbHelper.getIntance(this));
                this.finish();

            }catch (Exception ex) {
                Log.e("iceepotmobile", "NewPotActivity: " + ex.getLocalizedMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton(R.string.new_pot_warning, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setMessage(ex.getMessage());

                builder.create().show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateValues() throws Exception{
        if(etxDescription.getText() == null || "".equals(etxDescription.getText().toString()))
            throw new ValueMissingException("Pot Description");
        if(etxId.getText() == null || "".equals(etxId.getText().toString()))
            throw new ValueMissingException("Pot ID");


        if(Integer.parseInt(txtMinMoistVal.getText().toString()) > Integer.parseInt(txtMaxMoistVal.getText().toString())){
            throw  new ValuesOrderException(txtMaxMoisture.getText().toString(), txtMinMoisture.getText().toString());
        }
    }
}
