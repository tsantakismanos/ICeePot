package iceepotmobile.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import iceepot.iceepotmobile.R;
import iceepotmobile.application.PotsLoader;
import iceepotmobile.model.Pot;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Pot>>{

    private PotsAdapter potsAdapter;
    private ListView lvwPots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        lvwPots = (ListView)findViewById(R.id.lvwPots);

        lvwPots.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pot p = potsAdapter.getItem(i);
                Intent p_i = new Intent(MainActivity.this, PotActivity.class);
                p_i.putExtra("pot",p);
                startActivity(p_i);
            }
        });

        potsAdapter = new PotsAdapter();

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent iSettings = new Intent(this, SettingsActivity.class);
            startActivity(iSettings);
        }
        if(id == R.id.add_pot){
            Intent i = new Intent(this, NewPotActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Pot>> onCreateLoader(int i, Bundle bundle) {
        return new PotsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Pot>> loader, List<Pot> pots) {
        potsAdapter.setPots(pots);
        lvwPots.setAdapter(potsAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Pot>> loader) {
        potsAdapter.setPots(null);
    }


    public class PotsAdapter extends BaseAdapter {

        List<Pot> pots = null;

        public PotsAdapter() {

        }

        public void setPots(List<Pot> pots) {
            this.pots = pots;
        }

        @Override
        public int getCount() {
            return pots.size();
        }

        @Override
        public Pot getItem(int i) {
            return pots.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View v = View.inflate(getApplicationContext(), R.layout.pots_item, null);

            TextView txtPotDescr = (TextView)v.findViewById(R.id.txtPotDescr);
            TextView txtPotId = (TextView)v.findViewById(R.id.txtPotId);

            txtPotDescr.setText(pots.get(i).getDescr());
            txtPotId.setText(String.valueOf(pots.get(i).getId()));

            return v;
        }
    }
}
