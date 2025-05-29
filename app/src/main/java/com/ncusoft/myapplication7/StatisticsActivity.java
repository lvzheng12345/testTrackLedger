// main/java/com/ncusoft/myapplication7/StatisticsActivity.java
package com.ncusoft.myapplication7;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        calendarView = findViewById(R.id.calendarView);
        lineChart = findViewById(R.id.lineChart);

        // 模拟折线图数据
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 30));
        entries.add(new Entry(3, 25));
        entries.add(new Entry(4, 35));

        LineDataSet dataSet = new LineDataSet(entries, "交易统计");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // 刷新图表
    }
}