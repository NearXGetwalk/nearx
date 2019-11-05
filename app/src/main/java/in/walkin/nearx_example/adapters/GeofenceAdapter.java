package in.walkin.nearx_example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.walkin.nearx_example.adapters.holders.BaseViewHolder;
import in.walkin.nearx_example.adapters.holders.GeofenceHolder;
import in.walkin.nearx_example.model.GeofencePojo;
import in.walkin.nearx_example.R;

public class GeofenceAdapter extends BaseAdapter<GeofencePojo> {
    private Context context;
    View.OnClickListener listener;

    public GeofenceAdapter(Context context, View.OnClickListener listener){
        this.context = context;
        this.listener = listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected BaseViewHolder createHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected BaseViewHolder createItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card, parent, false);
        return new GeofenceHolder(context,view,listener);
        //return null;
    }

    @Override
    protected BaseViewHolder createFooterViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindHeaderViewHolder(BaseViewHolder viewHolder) {

    }

    @Override
    protected void bindItemViewHolder(BaseViewHolder viewHolder, int position) {
        GeofenceHolder geofenceHolder = (GeofenceHolder) viewHolder;
        geofenceHolder.setData(context,getItem(position));

    }

    @Override
    protected void bindFooterViewHolder(BaseViewHolder viewHolder) {

    }

    @Override
    protected void displayLoadMoreFooter() {

    }

    @Override
    protected void displayErrorFooter() {

    }

    @Override
    public void addFooter() {
        isFooterAdded  = true;
        add(new GeofencePojo());

    }

    @Override
    public int getItemViewType(int position) {
        return (isLastPosition(position) && isFooterAdded) ? FOOTER : ITEM;
    }
}
