package eco.org.greenapp.eco.org.greenapp.classes;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by danan on 6/25/2018.
 */
class IntegerValueFormatter implements ValueFormatter {
    private DecimalFormat mFormat;

    public IntegerValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0");
    }


    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format(value);
    }
}