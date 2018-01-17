package cn.edu.hznu.labdataforsqlite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by asus on 2017/11/15.
 */

public class ContactAdapter extends ArrayAdapter<contact> {
    private int resourceId;

    public ContactAdapter(Context context, int textViewResourceId, List<contact> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final contact contact = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.contactCheck = (CheckBox)view.findViewById(R.id.contact_check);
            viewHolder.contactName = (TextView)view.findViewById(R.id.contact_name);
            viewHolder.contactMobile = (TextView)view.findViewById(R.id.contact_mobile);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.contactName.setText(contact.getName());
        viewHolder.contactMobile.setText(contact.getMobile());

        viewHolder.contactCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    contact.setCheck(true);
                } else {
                    contact.setCheck(false);
                }
            }
        });
        return view;
    }

    class ViewHolder {
        CheckBox contactCheck;
        TextView contactName;
        TextView contactMobile;
    }
}
