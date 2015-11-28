package iceepotmobile.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import iceepot.iceepotmobile.R;
import iceepotlib.servergw.Measurement;
import iceepotlib.servergw.ServerTools;
import iceepotmobile.model.Pot;


/**
 * Created by manos on 28/10/2015.
 */
public class PotActivity extends AppCompatActivity{

    ProgressBar pgbLoading;
    GraphicalView grwGraph;
    LinearLayout lytGraph;
    LinearLayout lytFilters;
    Spinner sprFromMonth;
    Spinner sprFromYear;
    Spinner sprToMonth;
    Spinner sprToYear;

    Pot pot;

    private void initUI(){
        pgbLoading.setVisibility(View.INVISIBLE);
        lytGraph.setVisibility(View.INVISIBLE);
        lytFilters.setVisibility(View.VISIBLE);
    }

    private void loadingUI(){
        pgbLoading.setVisibility(View.VISIBLE);
        lytGraph.setVisibility(View.INVISIBLE);
        lytFilters.setVisibility(View.VISIBLE);
    }

    private void doneUI(){
        pgbLoading.setVisibility(View.INVISIBLE);
        lytGraph.setVisibility(View.VISIBLE);
        lytFilters.setVisibility(View.VISIBLE);
    }

    private void errorUI(){
        pgbLoading.setVisibility(View.INVISIBLE);
        lytGraph.setVisibility(View.INVISIBLE);
        lytFilters.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pot);

        pgbLoading = (ProgressBar) findViewById(R.id.pgbLoading);
        lytGraph = (LinearLayout)findViewById(R.id.lytGraph);
        lytFilters = (LinearLayout)findViewById(R.id.lytFilters);
        sprFromMonth = (Spinner)findViewById(R.id.sprFromMonth);
        sprFromYear = (Spinner)findViewById(R.id.sprFromYear);
        sprToMonth = (Spinner)findViewById(R.id.sprToMonth);
        sprToYear = (Spinner)findViewById(R.id.sprToYear);

        Intent i = getIntent();
        pot = (Pot)i.getSerializableExtra("pot");

        initUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.pot_get){

            loadData();
        }
        return super.onOptionsItemSelected(item);

    }

    private void loadData(){

        Calendar from = Calendar.getInstance();
        from.set(Calendar.MONTH, sprFromMonth.getSelectedItemPosition());
        from.set(Calendar.YEAR, Integer.parseInt((String) sprFromYear.getSelectedItem()));
        from.set(Calendar.DAY_OF_MONTH, 1);
        from.set(Calendar.HOUR,0);
        from.set(Calendar.MINUTE,0);
        from.set(Calendar.SECOND,0);
        from.set(Calendar.MILLISECOND,0);

        Calendar to = Calendar.getInstance();
        to.set(Calendar.MONTH, sprToMonth.getSelectedItemPosition());
        to.set(Calendar.YEAR, Integer.parseInt((String) sprToYear.getSelectedItem()));
        to.set(Calendar.DAY_OF_MONTH, 1);
        to.set(Calendar.HOUR,0);
        to.set(Calendar.MINUTE,0);
        to.set(Calendar.SECOND,0);
        to.set(Calendar.MILLISECOND,0);

        ServerTask task = new ServerTask(pot, from.getTime(), to.getTime(), this);
        task.execute(pot);
    }

    public class ServerTask extends AsyncTask<Pot, Void, ArrayList<Measurement>>{

        Date from;
        Date to;
        Exception ex;
        Pot p;
        Context context;

        private HashMap<Long, Double> getMaxHashMap(){

            HashMap<Long, Double> hashMap = new HashMap<Long, Double>();

            hashMap.put(Long.valueOf(from.getTime()), Double.valueOf(p.getMaxMoistVal()));
            hashMap.put(Long.valueOf(to.getTime()), Double.valueOf(p.getMaxMoistVal()));

            return  hashMap;
        }

        private HashMap<Long, Double> getMinHashMap() {

            HashMap<Long, Double> hashMap = new HashMap<Long, Double>();

            hashMap.put(Long.valueOf(from.getTime()), Double.valueOf(p.getMinMoistVal()));
            hashMap.put(Long.valueOf(to.getTime()), Double.valueOf(p.getMinMoistVal()));

            return  hashMap;
        }

        public ServerTask(Pot p, Date from, Date to, Context context) {
            this.p = p;
            this.from = from;
            this.to = to;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            ex = null;

            loadingUI();
        }

        @Override
        protected void onPostExecute(ArrayList<Measurement> measurements) {
            super.onPostExecute(measurements);

            if(ex != null){
                GenericDialog.createFromException(PotActivity.this, ex).show();
                errorUI();
            }else {

                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                dataset.addSeries(mapToTimeSeries(Measurement.getHashMap(measurements)));
                dataset.addSeries(mapToTimeSeries(getMaxHashMap()));
                dataset.addSeries(mapToTimeSeries(getMinHashMap()));

                XYMultipleSeriesRenderer renderer  = new XYMultipleSeriesRenderer();
                XYSeriesRenderer actual = new XYSeriesRenderer();
                XYSeriesRenderer min = new XYSeriesRenderer();
                XYSeriesRenderer max = new XYSeriesRenderer();
                min.setColor(getResources().getColor(R.color.Limits));
                max.setColor(getResources().getColor(R.color.Limits));
                actual.setShowLegendItem(false);
                min.setShowLegendItem(false);
                max.setShowLegendItem(false);

                renderer.addSeriesRenderer(actual);
                renderer.addSeriesRenderer(max);
                renderer.addSeriesRenderer(min);

                renderer.setPanEnabled(false);
                renderer.setYAxisMin(0);
                renderer.setYAxisMax(900);
                renderer.setMarginsColor(getResources().getColor(R.color.GraphBackground));
                renderer.setAxesColor(getResources().getColor(R.color.Axes));
                renderer.setXLabelsColor(getResources().getColor(R.color.Axes));


                grwGraph = ChartFactory.getTimeChartView(PotActivity.this,dataset, renderer,null);

                lytGraph.removeAllViews();
                lytGraph.addView(grwGraph);
                lytGraph.setBackgroundColor(getResources().getColor(R.color.GraphBackground));

                doneUI();
            }
        }

        @Override
        protected ArrayList<Measurement> doInBackground(Pot... pots) {

            ArrayList<Measurement> measurements = new ArrayList<Measurement>();

            Calendar idx = Calendar.getInstance();
            idx.setTimeInMillis(from.getTime());

            Calendar last = Calendar.getInstance();
            last.setTimeInMillis(to.getTime());

            try{
                //for all months in range
                while(idx.before(last)){
                    measurements.addAll(ServerTools.GetMeasurements(idx.get(Calendar.MONTH)+1, idx.get(Calendar.YEAR), pots[0].getId(), getHost(context), getPort(context), getTimeout(context)));
                    idx.add(Calendar.MONTH, 1);
                }

            }catch(Exception e){
                ex = e;
            }

            return measurements;
        }
    }

    private TimeSeries mapToTimeSeries(HashMap<Long, Double> map) {

        TimeSeries series = new TimeSeries("");
        int index = 0;

        Iterator i = map.keySet().iterator();
        while(i.hasNext()){
            Object key = i.next();

            long moment = (long)key;
            double value = map.get(key);

            series.add(new Date(moment), value);

        }

        return series;

    }

    private String getHost(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String host = sharedPreferences.getString(SettingsActivity.KEY_PREF_HOST, "");

        return host;
    }
    private int getPort(Context context){
        int port = 0;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            port = Integer.parseInt(sharedPreferences.getString(SettingsActivity.KEY_PREF_PORT, "0"));
        }catch (NumberFormatException ex){

        }

        return port;
    }
    private int getTimeout(Context context){
        int timeout = 30;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        try {
            timeout = Integer.parseInt(sharedPreferences.getString(SettingsActivity.KEY_PREF_TIMEOUT, "30"));
        }catch (NumberFormatException ex){

        }
        return timeout*1000;
    }
}
