package iceepotmobile.application;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import iceepotmobile.model.Pot;

/**
 * Created by manos on 2/10/2015.
 */
public class PotsLoader extends AsyncTaskLoader<List<Pot>>{

    private DbHelper dbHelper;
    private List<Pot> mPots;

    public PotsLoader(Context context) {
        super(context);

        dbHelper = DbHelper.getIntance(context);

    }

    @Override
    public List<Pot> loadInBackground() {

        List<Pot> pots = Pot.listPots(dbHelper);

        return pots;
    }

    @Override
    public void deliverResult(List<Pot> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (data != null) {
                onReleaseResources(data);
            }
        }
        List<Pot> oldPots = mPots;
        mPots = data;

        if (isStarted()) {

            super.deliverResult(data);
        }


        if (oldPots != null) {
            onReleaseResources(oldPots);
        }

    }


    protected void onReleaseResources(List<Pot> apps) {

    }

    @Override
    protected void onStartLoading() {
        if (mPots != null) {

            deliverResult(mPots);
        }
        if(takeContentChanged() || mPots ==null){
            forceLoad();
        }

    }

    @Override
    public void stopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mPots != null) {
            onReleaseResources(mPots);
            mPots = null;
        }
    }
}
