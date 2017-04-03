package com.example.qy.q233;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Xu Yining on 2017/4/3.
 */

public class DrawLinechart {
    public LineChart mLineChart;
    public boolean isChart = false;

//    if(!isChart){
//        initChart();
//        return;
//    }

    public void initChart(LineChart mLineChart, float savedTime, ArrayList<Entry> yVals) {
        if (mLineChart == null) {
            return;
        }
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            savedTime += 0.002;

        } else {
            long currentTime = 0;
            currentTime = new Date().getTime();
            mLineChart.setViewPortOffsets(50, 20, 5, 60);
            // no description text
            mLineChart.setDescription(new Description());
            // enable touch gestures
            mLineChart.setTouchEnabled(true);
            // enable scaling and dragging
            mLineChart.setDragEnabled(false);
            mLineChart.setScaleEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mLineChart.setPinchZoom(false);
            mLineChart.setDrawGridBackground(false);
            //mChart.setMaxHighlightDistance(400);
            XAxis x = mLineChart.getXAxis();
            x.setLabelCount(8, false);
            x.setEnabled(true);
//                x.setTypeface(tf);
            x.setTextColor(Color.BLUE);
            x.setPosition(XAxis.XAxisPosition.BOTTOM);
            x.setDrawGridLines(true);
            x.setAxisLineColor(Color.BLUE);
            YAxis y = mLineChart.getAxisLeft();
            y.setLabelCount(6, false);
            y.setTextColor(Color.BLUE);
            y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            y.setDrawGridLines(false);
            y.setAxisLineColor(Color.BLUE);
            y.setAxisMinValue(0);
            y.setAxisMaxValue(20);
            mLineChart.getAxisRight().setEnabled(true);
            yVals = new ArrayList<Entry>();
            yVals.add(new Entry(0, 0));
            LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.02f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setCircleColor(Color.BLUE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.BLUE);
            set1.setFillColor(Color.BLUE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);

//            set1.setFillFormatter(new IFillFormatter() {
//                @Override
//                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
//                    return 100;
//                }
//            });
            LineData data;
            if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
                data = mLineChart.getLineData();
                data.clearValues();
                data.removeDataSet(0);
                data.addDataSet(set1);
            } else {
                data = new LineData(set1);
            }
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mLineChart.setData(data);
            mLineChart.getLegend().setEnabled(false);
            mLineChart.animateXY(2000, 2000);
            // dont forget to refresh the drawing
            mLineChart.invalidate();
        }
    }


    public void updateData(LineChart mLineChart, float val, ArrayList<Entry> yVals, float savedTime) {
        if (mLineChart == null) {
            return;
        }
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            LineDataSet set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            Entry entry = new Entry(savedTime, val);
            set1.addEntry(entry);
            if (set1.getEntryCount() > 200) {
                set1.removeFirst();
                set1.setDrawFilled(false);
            }
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
            savedTime += 0.002;
        }
    }
}
