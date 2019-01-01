package marcveens.androidweek7_bucketlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class BucketItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final private BucketAdapter.ClickListener clickListener;
    public TextView title;
    public TextView description;
    public View view;

    public BucketItemViewHolder(View itemView, BucketAdapter.ClickListener clickListener) {
        super(itemView);
        title =  itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);

        view = itemView;
        itemView.setOnClickListener(this);
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        clickListener.bucketItemOnClick(clickedPosition);
    }
}
