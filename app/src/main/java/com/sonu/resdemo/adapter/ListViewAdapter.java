package com.sonu.resdemo.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sonu.resdemo.R;
import com.sonu.resdemo.model.TypeModel;
import com.sonu.resdemo.utils.Preferences;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<TypeModel> {
  private List<TypeModel> myFriends;
  private Activity activity;
  private int selectedPosition = -1;
  public ListViewAdapter(Activity context, int resource, List<TypeModel> objects) {
    super(context, resource, objects);

    this.activity = context;
    this.myFriends = objects;
  }
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    // If holder not exist then locate all view from UI file.
    if (convertView == null) {
      // inflate UI from XML file
      convertView = inflater.inflate(R.layout.item_listview, parent, false);
      // get all UI view
      holder = new ViewHolder(convertView);
      // set tag for holder
      convertView.setTag(holder);
    } else {
      // if holder created, get tag from view
      holder = (ViewHolder) convertView.getTag();
    }

    holder.checkBox.setTag(position); // This line is important.

    holder.friendName.setText(getItem(position).getType_name());
    if (position == selectedPosition) {
      holder.checkBox.setChecked(true);
    } else holder.checkBox.setChecked(false);

    holder.checkBox.setOnClickListener(onStateChangedListener(holder.checkBox, position));

    return convertView;
  }

  private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
    return new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (checkBox.isChecked()) {
          selectedPosition = position;
          Preferences pref=new Preferences(activity);
          pref.storeString("check",getItem(position).getType_name());
        } else {
          selectedPosition = -1;
        }
        notifyDataSetChanged();
      }
    };
  }

  private static class ViewHolder {
    private TextView friendName;
    private CheckBox checkBox;

    public ViewHolder(View v) {
      checkBox = (CheckBox) v.findViewById(R.id.check);
      friendName = (TextView) v.findViewById(R.id.name);
    }
  }
}
