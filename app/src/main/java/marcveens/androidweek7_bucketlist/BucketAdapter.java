package marcveens.androidweek7_bucketlist;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

public class BucketAdapter extends RecyclerView.Adapter<BucketItemViewHolder> {
    final private ClickListener clickListener;
    public interface ClickListener {
        void bucketItemOnClick (int i);
        void checkOnClick (int i, boolean isChecked);
    }

    public List<BucketListItem> bucketList;

    public BucketAdapter(List<BucketListItem> bucketItemsList, ClickListener clickListener) {
        this.bucketList = bucketItemsList;
        this.clickListener = clickListener;
    }

    public void swapList (List<BucketListItem> newList) {
        bucketList = newList;
        if (newList != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public BucketItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bucket_item_viewholder, parent, false);
        return new BucketItemViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(final BucketItemViewHolder holder, final int position) {
        BucketListItem bucketItem = bucketList.get(position);
        holder.title.setText(bucketItem.getTitle());
        holder.description.setText(bucketItem.getDescription());

        if (bucketItem.isFinished()) {
            holder.check.setChecked(true);
            holder.title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.description.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.check.setChecked(false);
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.description.setPaintFlags(holder.description.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }
}
