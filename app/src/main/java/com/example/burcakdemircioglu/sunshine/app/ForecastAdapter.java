package com.example.burcakdemircioglu.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    private final int VIEW_TYPE_TODAY=0;
    private boolean mUseTodayLayout;
    private final int VIEW_TYPE_FUTURE_DAY=1;
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public void setUseTodayLayout(boolean useTodayLayout){
        mUseTodayLayout=useTodayLayout;
    }
    @Override
    public int getItemViewType(int position){
        return (position==0 && mUseTodayLayout)? VIEW_TYPE_TODAY: VIEW_TYPE_FUTURE_DAY;
    }
    @Override
    public int getViewTypeCount(){
        return 2;
    }
    /**
     * Prepare the weather high/lows for presentation.
     *
    private String formatHighLows(double high, double low) {
        boolean isMetric = com.example.burcakdemircioglu.sunshine.app.Utility.isMetric(mContext);
        String highLowStr = com.example.burcakdemircioglu.sunshine.app.Utility.formatTemperature(high, isMetric) + "/" + com.example.burcakdemircioglu.sunshine.app.Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }


//        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
//        string.

    private String convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor

        String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    *
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType=getItemViewType(cursor.getPosition());
        int layoutId=-1;

        if (viewType==VIEW_TYPE_TODAY)
            layoutId=R.layout.list_item_forecast_today;
        else if (viewType==VIEW_TYPE_FUTURE_DAY)
            layoutId=R.layout.list_item_forecast;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder=(ViewHolder) view.getTag();
        int viewType=getItemViewType(cursor.getPosition());
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        // Read weather icon ID from cursor
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        switch (viewType){
            case VIEW_TYPE_TODAY:{
                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

                break;
            }
            case VIEW_TYPE_FUTURE_DAY:{
                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(weatherId));

                break;
            }
        }
        // Use placeholder image for now
        //viewHolder.iconView.setImageResource(R.drawable.art_rain);

        // TODO Read date from cursor
        long dateInMillis=cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        viewHolder.dateView.setText(Utility.getFriendlyDayString(context, dateInMillis));

        // TODO Read weather forecast from cursor
        String description=cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descriptionView.setText(description);
        viewHolder.iconView.setContentDescription(description);
        // Read user preference for metric or imperial temperature units
        boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        viewHolder.highTempView.setText(Utility.formatTemperature(context, high));
        viewHolder.highTempView.setContentDescription(context.getString(R.string.max_temperature) + Utility.formatTemperature(context, high));
        // TODO Read low temperature from cursor
        double low=cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        viewHolder.lowTempView.setText(Utility.formatTemperature(context, low));
        viewHolder.lowTempView.setContentDescription(context.getString(R.string.min_temperature)+Utility.formatTemperature(context, low));
    }

    public static class ViewHolder{
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView=(TextView)view.findViewById(R.id.list_item_date_textview);
            descriptionView=(TextView)view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView)view.findViewById(R.id.list_item_high_textview);
            lowTempView=(TextView)view.findViewById(R.id.list_item_low_textview);
        }
    }

}