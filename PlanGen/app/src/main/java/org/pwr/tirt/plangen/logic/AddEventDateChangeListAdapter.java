package org.pwr.tirt.plangen.logic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.pwr.tirt.plangen.R;

public class AddEventDateChangeListAdapter extends ArrayAdapter<String[]> {
    private static class DateChangeHolder
    {
        private EditText editTextFrom;
        private EditText editTextTo;
        private Button buttonClear;
        private int tableIndex;
    }

    private Context context;
    private int layoutResourceId;
    private String data[][] = null;
    private IAddEventDateChangeListAdapterListener listener;

    public AddEventDateChangeListAdapter(Context context, int layoutResourceId, String[][] data, IAddEventDateChangeListAdapterListener listener) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final DateChangeHolder holder= new DateChangeHolder();
        final String from = data[position][0];
        String to = data[position][1];

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder.editTextFrom = (EditText) row.findViewById(R.id.editTextDateFrom);
        holder.editTextTo = (EditText) row.findViewById(R.id.editTextDateTo);
        holder.buttonClear = (Button) row.findViewById(R.id.buttonDeleteDateChangeItem);
        holder.tableIndex = position;
        row.setTag(holder);

        holder.editTextFrom.setText(from);
        holder.editTextTo.setText(to);

        holder.editTextFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    listener.changeDate(holder.tableIndex, 0);
                }
            }
        });
        holder.editTextTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    listener.changeDate(holder.tableIndex, 1);
                }
            }
        });
        holder.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.itemDeleted(holder.tableIndex);
            }
        });
        return row;
    }
}
