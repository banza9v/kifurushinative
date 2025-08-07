package ridc.sduma.kifurushi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ridc.sduma.kifurushi.R;
import ridc.sduma.kifurushi.models.Region;

import java.util.List;

public class RegionsAdapter extends ArrayAdapter<Region> {

    LayoutInflater inflater;

    List<Region> regionList;

    public RegionsAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Region> objects) {
        super(context, resource, textViewResourceId, objects);
        this.regionList = objects;
    }


    @Nullable
    @Override
    public Region getItem(int position) {
        return (Region) regionList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return rowview(convertView, position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.spinner_item,parent, false);
        }
        Region rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.spinner_id);

        if (rowItem != null && rowItem.getRegionalName() != null){
            if (rowItem.getRegionalName().contains(" Region"))
                txtTitle.setText(rowItem.getRegionalName().replace(" Region", ""));
            else
                txtTitle.setText(rowItem.getRegionalName().replace(" region", ""));
        }
        return convertView;
    }


    private View rowview(View convertView , int position){

        Region rowItem = getItem(position);

        viewHolder holder ;
        View rowview = convertView;
        if (rowview==null) {

            holder = new viewHolder();
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = inflater.inflate(R.layout.spinner_item, null, false);

            holder.txtTitle = rowview.findViewById(R.id.spinner_id);
            rowview.setTag(holder);
        }else{
            holder = (viewHolder) rowview.getTag();
        }

        if (rowItem != null && rowItem.getRegionalName() != null){
            if (rowItem.getRegionalName().contains(" Region"))
                holder.txtTitle.setText(rowItem.getRegionalName().replace(" Region", ""));
            else
                holder.txtTitle.setText(rowItem.getRegionalName().replace(" region", ""));
        }

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
    }


}
