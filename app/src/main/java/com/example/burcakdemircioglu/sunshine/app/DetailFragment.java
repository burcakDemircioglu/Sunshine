package com.example.burcakdemircioglu.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.burcakdemircioglu.sunshine.app.data.WeatherContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String mForecast;
    private Uri mUri;
    private static final String LOG_TAG=DetailFragment.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG=" #SunshineApp";
    private ShareActionProvider mShareActionProvider;
    static final String DETAIL_URI = "URI";

    private static final int DETAIL_LOADER=0;
    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY= 5;
    public static final int COL_WEATHER_PRESSURE= 6;
    public static final int COL_WEATHER_WIND_SPEED= 7;
    public static final int COL_WEATHER_DEGREES= 8;
    public static final int COL_WEATHER_CONDITION_ID= 9;

    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
    private CompassView mCompassView;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args){
        Log.v(LOG_TAG, "In onCreateLoader");
//        Intent intent=getActivity().getIntent();
//        if(intent==null||intent.getData()==null){
//            return null;
//        }
//        return new CursorLoader(
//                getActivity(),
//                intent.getData(),
//                DETAIL_COLUMNS,
//                null,
//                null,
//                null
//        );
        if(null!=mUri){
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Intent intent = getActivity().getIntent();
        Bundle arguments=getArguments();
        if(arguments!=null){
            mUri= arguments.getParcelable(DetailFragment.DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//        if (intent != null) {
//            mForecastStr = intent.getDataString();
//        }
//        if(null!=mForecastStr){
//
//            ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecastStr);
//        }

        mIconView=(ImageView)rootView.findViewById(R.id.detail_icon);
        mFriendlyDateView=(TextView)rootView.findViewById(R.id.detail_day_textview);
        mDateView=(TextView)rootView.findViewById(R.id.detail_date_textview);
        mDescriptionView=(TextView)rootView.findViewById(R.id.detail_description_textview);
        mHighTempView=(TextView)rootView.findViewById(R.id.detail_high_textview);
        mLowTempView=(TextView)rootView.findViewById(R.id.detail_low_textview);
        mHumidityView=(TextView)rootView.findViewById(R.id.detail_humidity_textview);
        mWindView=(TextView)rootView.findViewById(R.id.detail_wind_textview);
        mPressureView=(TextView)rootView.findViewById(R.id.detail_pressure_textview);
        mCompassView=(CompassView)rootView.findViewById(R.id.compass_view);
        return rootView;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        if(data != null && data.moveToFirst()){
            boolean isMetric=Utility.isMetric(getActivity());

            int weatherId=data.getInt(COL_WEATHER_CONDITION_ID);
            mIconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

            long date=data.getLong(COL_WEATHER_DATE);
            String friendlyDateText=Utility.getDayName(getActivity(), date);
            String dateText=Utility.getFormattedMonthDay(getActivity(), date);
            mFriendlyDateView.setText(friendlyDateText);
            mDateView.setText(dateText);

            double high=data.getDouble(COL_WEATHER_MAX_TEMP);
            mHighTempView.setText(Utility.formatTemperature(getActivity(), high));
            mHighTempView.setContentDescription(getActivity().getString(R.string.max_temperature)+
                    Utility.formatTemperature(getActivity(), high));
            double low=data.getDouble(COL_WEATHER_MIN_TEMP);
            mLowTempView.setText(Utility.formatTemperature(getActivity(), low));
            mLowTempView.setContentDescription(getActivity().getString(R.string.min_temperature) +
                    Utility.formatTemperature(getActivity(), low));

            String description=data.getString(COL_WEATHER_DESC);
            mDescriptionView.setText(description);
            mIconView.setContentDescription(description);

            float humidity=data.getFloat(COL_WEATHER_HUMIDITY);
            mHumidityView.setText(getActivity().getString(R.string.format_humidity, humidity));

            float pressure=data.getFloat(COL_WEATHER_PRESSURE);
            mPressureView.setText(getActivity().getString(R.string.format_pressure, pressure));

            float windSpeed=data.getFloat(COL_WEATHER_WIND_SPEED);
            float windDirection=data.getFloat(COL_WEATHER_DEGREES);
            mWindView.setText(Utility.getFormattedWind(getActivity(), windSpeed, windDirection));

            mForecast=String.format("%s - %s - %s/%s", dateText, description, high, low);
            mCompassView.setDirection(Utility.getWindDirection(getActivity(), windDirection));
            mCompassView.setWind(Utility.getWindSpeed(getActivity(),windSpeed));

            if (mShareActionProvider != null)
                mShareActionProvider.setShareIntent(createShareForecastIntent());

        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader){

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detailfragment, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (mForecast != null)
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        else
            Log.d(LOG_TAG, "Share Action Provider is null?");
    }
    private Intent createShareForecastIntent(){
        Intent shareIntent=new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    public void onLocationChanged(String newLocation) {
        Uri uri= mUri;
        if(null!=uri){
            long date= WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri= WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation,date);
            mUri=updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER,null,this);
        }
    }
}
