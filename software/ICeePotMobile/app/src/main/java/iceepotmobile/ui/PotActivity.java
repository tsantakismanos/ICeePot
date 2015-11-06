package iceepotmobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import iceepotlib.servergw.ServerService;
import iceepotlib.servergw.ServerTools;
import iceepotmobile.model.Pot;


/**
 * Created by manos on 28/10/2015.
 */
public class PotActivity extends Activity{

    ProgressBar pgbLoading;
    GraphicalView grwGraph;
    LinearLayout lytGraph;

    Calendar from;
    Calendar to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pot);

        pgbLoading = (ProgressBar) findViewById(R.id.pgbLoading);
        lytGraph = (LinearLayout)findViewById(R.id.lytGraph);

        Intent i = getIntent();
        Pot p = (Pot)i.getSerializableExtra("pot");

        from = Calendar.getInstance();
        from.set(Calendar.MONTH, 1);
        from.set(Calendar.YEAR, 2013);
        from.set(Calendar.DAY_OF_MONTH, 1);
        to = Calendar.getInstance();
        to.set(Calendar.MONTH, 2);
        to.set(Calendar.YEAR, 2013);
        to.set(Calendar.DAY_OF_MONTH, 1);

        ServerTask task = new ServerTask(p);
        task.execute(p);
    }

    public class ServerTask extends AsyncTask<Pot, Void, ArrayList<Measurement>>{

        Exception ex;
        Pot p;

        private HashMap<Long, Double> getMaxHashMap(){

            HashMap<Long, Double> hashMap = new HashMap<Long, Double>();

            hashMap.put(Long.valueOf(from.getTimeInMillis()), Double.valueOf(p.getMaxMoistVal()));
            hashMap.put(Long.valueOf(to.getTimeInMillis()), Double.valueOf(p.getMaxMoistVal()));

            return  hashMap;
        }

        private HashMap<Long, Double> getMinHashMap() {

            HashMap<Long, Double> hashMap = new HashMap<Long, Double>();

            hashMap.put(Long.valueOf(from.getTimeInMillis()), Double.valueOf(p.getMinMoistVal()));
            hashMap.put(Long.valueOf(to.getTimeInMillis()), Double.valueOf(p.getMinMoistVal()));

            return  hashMap;
        }

        public ServerTask(Pot p) {
            this.p = p;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            lytGraph.setVisibility(View.INVISIBLE);
            pgbLoading.setVisibility(View.VISIBLE);

            ex = null;
        }

        @Override
        protected void onPostExecute(ArrayList<Measurement> measurements) {
            super.onPostExecute(measurements);

            if(ex != null){
                GenericDialog.createFromException(PotActivity.this, ex).show();
                pgbLoading.setVisibility(View.INVISIBLE);
            }else {

                XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
                dataset.addSeries(mapToTimeSeries(Measurement.getHashMap(measurements)));
                dataset.addSeries(mapToTimeSeries(getMaxHashMap()));
                dataset.addSeries(mapToTimeSeries(getMinHashMap()));

                XYMultipleSeriesRenderer renderer  = new XYMultipleSeriesRenderer();
                renderer.addSeriesRenderer(new XYSeriesRenderer());
                renderer.addSeriesRenderer(new XYSeriesRenderer());
                renderer.addSeriesRenderer(new XYSeriesRenderer());

                renderer.setPanEnabled(false);
                //renderer.setXAxisMin(from.getTimeInMillis());
                //renderer.setXAxisMax(to.getTimeInMillis());
                renderer.setYAxisMin(0);
                renderer.setYAxisMax(900);

                grwGraph = ChartFactory.getTimeChartView(PotActivity.this,dataset, renderer,null);
                lytGraph.addView(grwGraph);

                lytGraph.setVisibility(View.VISIBLE);
                pgbLoading.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected ArrayList<Measurement> doInBackground(Pot... pots) {

            ArrayList<Measurement> measurements = new ArrayList<Measurement>();

            Calendar idx = Calendar.getInstance();
            idx.setTimeInMillis(from.getTimeInMillis());

            try{
                //for all months in range
                while((idx.get(Calendar.MONTH) != to.get(Calendar.MONTH))
                        || (idx.get(Calendar.YEAR) != to.get(Calendar.YEAR))){
                    measurements.addAll(ServerTools.GetMeasurements(idx, pots[0].getId(), "homeplants.ddns.net", 3629, 20000));
                    idx.add(Calendar.MONTH, 1);
                }

                measurements.addAll(ServerTools.GetMeasurements(to, pots[0].getId(), "homeplants.ddns.net", 3629, 20000));

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
}
