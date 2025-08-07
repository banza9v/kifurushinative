package ridc.sduma.kifurushi.Utils;

import java.util.Comparator;

public class CustomComparator implements Comparator<AppDetails> {


    @Override
    public int compare(AppDetails appDetails, AppDetails t1) {
        return Long.compare(appDetails.getTotalInBytes(), t1.getTotalInBytes());
    }

    @Override
    public Comparator<AppDetails> reversed() {
        return Comparator.super.reversed();
    }
}
