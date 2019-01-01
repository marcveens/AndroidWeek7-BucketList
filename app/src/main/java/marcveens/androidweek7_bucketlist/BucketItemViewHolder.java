package marcveens.androidweek7_bucketlist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class BucketItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    final private BucketAdapter.ClickListener clickListener;
    public TextView title;
    public TextView description;
    public CheckBox check;
    public View view;

    public BucketItemViewHolder(View itemView, final BucketAdapter.ClickListener clickListener) {
        super(itemView);
        title =  itemView.findViewById(R.id.title);
        description = itemView.findViewById(R.id.description);
        check = itemView.findViewById(R.id.checkBox);

        view = itemView;
        itemView.setOnClickListener(this);
        this.clickListener = clickListener;

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int clickedPosition = getAdapterPosition();
                clickListener.checkOnClick(clickedPosition, isChecked);
            }
        });

    }

    @Override
    public void onClick(View view) {
        int clickedPosition = getAdapterPosition();
        clickListener.bucketItemOnClick(clickedPosition);
    }
}
