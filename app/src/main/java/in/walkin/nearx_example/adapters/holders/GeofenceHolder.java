package in.walkin.nearx_example.adapters.holders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import in.walkin.nearx_example.model.GeofencePojo;
import in.walkin.nearx_example.R;


public class GeofenceHolder extends BaseViewHolder {

    TextView name;
    Context context;
    View.OnClickListener listener;

    public GeofenceHolder(Context context, View view, View.OnClickListener listener) {
        super(view);
        this.context=context;
        this.listener=listener;
        name = view.findViewById(R.id.tv_location);

    }



    public void setData(final Context context, final GeofencePojo geofencePojo){

        if(geofencePojo != null){
            name.setText(geofencePojo.getLocationName());


//            name.setTag(geofencePojo);

        }
    }
}
