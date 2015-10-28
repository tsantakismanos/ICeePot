package iceepotmobile.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;

import iceepot.iceepotmobile.R;

/**
 * Created by manos on 28/10/2015.
 */
public class PotActivity extends Activity {

    ProgressBar pgbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pot);
    }
}
