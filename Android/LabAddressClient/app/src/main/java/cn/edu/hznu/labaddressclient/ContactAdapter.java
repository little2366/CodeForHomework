package cn.edu.hznu.labaddressclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus on 2017/11/23.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {
    private int resourceId;

    public ContactAdapter(Context context, int textViewResourceId, List<Contact> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final Contact contact = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contactName = (TextView)view.findViewById(R.id.contact_name);
            viewHolder.contactMobile = (TextView)view.findViewById(R.id.contact_mobile);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.contactName.setText(contact.getName());
        viewHolder.contactMobile.setText(contact.getMobile());
        return view;
    }

    class ViewHolder {
        TextView contactName;
        TextView contactMobile;
    }
}
