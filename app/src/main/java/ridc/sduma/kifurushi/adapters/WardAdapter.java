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
import ridc.sduma.kifurushi.models.Ward;

import java.util.List;

public class WardAdapter extends ArrayAdapter<Ward> {

    LayoutInflater inflater;

    List<Ward> regionList;

    public WardAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Ward> objects) {
        super(context, resource, textViewResourceId, objects);
        this.regionList = objects;
    }


    @Nullable
    @Override
    public Ward getItem(int position) {
        return (Ward) regionList.get(position);
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
        Ward rowItem = getItem(position);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.spinner_id);
        txtTitle.setText(rowItem.getWardName());
        return convertView;
    }


    private View rowview(View convertView , int position){

        Ward rowItem = getItem(position);

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

        holder.txtTitle.setText(rowItem.getWardName());

        return rowview;
    }

    private class viewHolder{
        TextView txtTitle;
    }


}
