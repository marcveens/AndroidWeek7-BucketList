package marcveens.androidweek7_bucketlist;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BucketAdapter.ClickListener {
    private List<BucketListItem> bucketList;
    private BucketAdapter bucketAdapter;

    private RecyclerView bucketRecyclerView;
    private MainViewModel mMainViewModel;

    public static final int REQUESTCODE = 1234;
    public static final String UPDATE_BUCKET_ITEM = "UPDATE_BUCKET_ITEM";
    public static final String UPDATE = "UPDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = new MainViewModel(getApplicationContext());
        mMainViewModel.getReminders().observe(this, new Observer<List<BucketListItem>>() {
            @Override
            public void onChanged(@Nullable List<BucketListItem> bucketListItems) {
                bucketList = bucketListItems;
                updateUI();
            }
        });

        bucketList = new ArrayList<>();

        bucketRecyclerView = findViewById(R.id.bucketListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        bucketRecyclerView.setLayoutManager(mLayoutManager);

        updateUI();

        FloatingActionButton fab = findViewById(R.id.addBucketItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra(UPDATE_BUCKET_ITEM, new BucketListItem());
                intent.putExtra(UPDATE, false);
                startActivityForResult(intent, REQUESTCODE);
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }


                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = (viewHolder.getAdapterPosition());
                        mMainViewModel.delete(bucketList.get(position));
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(bucketRecyclerView);
    }

    public void updateUI() {
        if (bucketAdapter == null) {
            bucketAdapter = new BucketAdapter(bucketList, this);
            bucketRecyclerView.setAdapter(bucketAdapter);
        } else {
            bucketAdapter.swapList(bucketList);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
            BucketListItem updatedBucketItem = data.getParcelableExtra(UPDATE_BUCKET_ITEM);
            boolean shouldUpdate = data.getBooleanExtra(UPDATE, false);

            if (shouldUpdate) {
                mMainViewModel.update(updatedBucketItem);
            } else {
                mMainViewModel.insert(updatedBucketItem);
            }

            updateUI();
        }
    }

    @Override
    public void bucketItemOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(UPDATE_BUCKET_ITEM, bucketList.get(i));
        intent.putExtra(UPDATE, true);
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void checkOnClick(int i, boolean isChecked) {
        BucketListItem item = bucketList.get(i);
        item.setFinished(isChecked);
        mMainViewModel.update(item);
    }
}
