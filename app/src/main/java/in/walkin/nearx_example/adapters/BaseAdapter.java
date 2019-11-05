package in.walkin.nearx_example.adapters;

import android.view.View;
import android.view.ViewGroup;

import in.walkin.nearx_example.adapters.holders.BaseViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    // region Constants
    protected static final int HEADER = 0;
    protected static final int ITEM = 1;
    protected static final int FOOTER = 2;
    // endregion

    // region Member Variables
    protected List<T> items;
    protected OnItemClickListener onItemClickListener;
    protected OnViewClickListener onViewClickListener;
    protected OnReloadClickListener onReloadClickListener;
    protected boolean isFooterAdded = false;
    // endregion

    // region Interfaces
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    // region Interfaces
    public interface OnViewClickListener {
        void onViewClick(int position, View view);
    }

    public interface OnReloadClickListener {
        void onReloadClick();
    }
    // endregion


    // region Constructors
    public BaseAdapter() {
        items = new ArrayList<>();
    }
    // endregion

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;

        switch (viewType) {
            case HEADER:
                viewHolder = createHeaderViewHolder(parent);
                break;
            case ITEM:
                viewHolder = createItemViewHolder(parent);
                break;
            case FOOTER:
                viewHolder = createFooterViewHolder(parent);
                break;
            default:
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                bindHeaderViewHolder(viewHolder);
                break;
            case ITEM:
                bindItemViewHolder(viewHolder, position);
                break;
            case FOOTER:
                bindFooterViewHolder(viewHolder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // region Abstract Methods
    protected abstract BaseViewHolder createHeaderViewHolder(ViewGroup parent);

    protected abstract BaseViewHolder createItemViewHolder(ViewGroup parent);

    protected abstract BaseViewHolder createFooterViewHolder(ViewGroup parent);

    protected abstract void bindHeaderViewHolder(BaseViewHolder viewHolder);

    protected abstract void bindItemViewHolder(BaseViewHolder viewHolder, int position);

    protected abstract void bindFooterViewHolder(BaseViewHolder viewHolder);

    protected abstract void displayLoadMoreFooter();

    protected abstract void displayErrorFooter();

    public abstract void addFooter();
    // endregion

    // region Helper Methods
    public T getItem(int position) {
        return items.get(position);
    }

    // region Helper Methods
    public List<T> getTotalItems() {
        return items;
    }

    public void add(T item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);

    }

    public void add(int index, T item) {
        items.add(index, item);
        notifyItemInserted(index);

    }


    public void addOnly(int index, T item) {
        items.add(index, item);
//        notifyItemInserted(index);
    }

    public void addAll(List<T> items) {
        if (null == items) {
            return;
        }
        for (T item : items) {
            add(item);
        }
    }

    public void addAllData(List<T> items) {
        if (null == items) {
            return;
        }
        items.addAll(items);



    }

    public void remove(T item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void clearData() {
        isFooterAdded = false;
        items.clear();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public boolean isLastPosition(int position) {
        return (position == items.size() - 1);
    }

    public void removeFooter() {
        isFooterAdded = false;

        int position = items.size() - 1;
        T item = getItem(position);

        if (item != null) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFooter(FooterType footerType) {
        switch (footerType) {
            case LOAD_MORE:
                displayLoadMoreFooter();
                break;
            case ERROR:
                displayErrorFooter();
                break;
            default:
                break;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    public void setOnReloadClickListener(OnReloadClickListener onReloadClickListener) {
        this.onReloadClickListener = onReloadClickListener;
    }
    // endregion

    // region Inner Classes
    public enum FooterType {
        LOAD_MORE,
        ERROR
    }
    // endregion
}
