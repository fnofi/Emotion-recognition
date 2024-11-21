package com.example.face_test.ui.notifications;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;

public class TimeAxisValueFormatter extends ValueFormatter {

    private SimpleDateFormat mDateFormat;

    public TimeAxisValueFormatter(SimpleDateFormat dateFormat) {
        this.mDateFormat = dateFormat;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        // Преобразуйте значение (временную метку в миллисекундах) в формат даты и часа
        return mDateFormat.format(value);
    }
}
