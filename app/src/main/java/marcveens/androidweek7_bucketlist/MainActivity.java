package marcveens.androidweek7_bucketlist;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements BucketAdapter.ClickListener {
    private List<BucketListItem> bucketList;
    private BucketAdapter bucketAdapter;

    private RecyclerView bucketRecyclerView;

    public static final int REQUESTCODE = 1234;
    public static final String UPDATE_BUCKET_ITEM = "UPDATE_BUCKET_ITEM";
    public static final String INSERT_OR_UPDATE = "INSERT_OR_UPDATE";

    public final static int TASK_GET_ALL_BUCKET_ITEMS = 0;
    public final static int TASK_DELETE_BUCKET_ITEMS = 1;
    public final static int TASK_UPDATE_BUCKET_ITEMS = 2;
    public final static int TASK_INSERT_BUCKET_ITEMS = 3;

    protected static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);
        new ReminderAsyncTask(TASK_GET_ALL_BUCKET_ITEMS).execute();

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
                intent.putExtra(INSERT_OR_UPDATE, TASK_INSERT_BUCKET_ITEMS);
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
                        new ReminderAsyncTask(TASK_DELETE_BUCKET_ITEMS).execute(bucketList.get(position));
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(bucketRecyclerView);
    }

    public void onBucketDbUpdated(List list) {
        bucketList = list;
        updateUI();
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
            Integer insertOrUpdate = data.getIntExtra(INSERT_OR_UPDATE, TASK_UPDATE_BUCKET_ITEMS);
            new ReminderAsyncTask(insertOrUpdate).execute(updatedBucketItem);
            updateUI();
        }
    }

    @Override
    public void bucketItemOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        intent.putExtra(UPDATE_BUCKET_ITEM, bucketList.get(i));
        intent.putExtra(INSERT_OR_UPDATE, TASK_UPDATE_BUCKET_ITEMS);
        startActivityForResult(intent, REQUESTCODE);
    }

    public class BucketRepository {
        private AppDatabase mAppDatabase;
        private BucketDao bucketDao;
        private LiveData<List<BucketListItem>> bucketList;
        private Executor mExecutor = Executors.newSingleThreadExecutor();

        public BucketRepository(Context context) {
            mAppDatabase = AppDatabase.getInstance(context);
            bucketDao = mAppDatabase.bucketDao();
            bucketList = bucketDao.getAll();
        }

        public LiveData<List<BucketListItem>> getAllReminders() {
            return bucketList;
        }

        public void insert(final BucketListItem bucketItem) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    bucketDao.insert(bucketItem);
                }
            });
        }

        public void update(final BucketListItem bucketItem) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    bucketDao.update(bucketItem);
                }
            });
        }

        public void delete(final BucketListItem bucketItem) {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    bucketDao.delete(bucketItem);
                }
            });
        }
    }


    public class ReminderAsyncTask extends AsyncTask<BucketListItem, Void, List> {
        private int taskCode;

        public ReminderAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(BucketListItem... bucketItems) {
            switch (taskCode) {
                case MainActivity.TASK_DELETE_BUCKET_ITEMS:
                    db.bucketDao().delete(bucketItems[0]);
                    break;

                case MainActivity.TASK_UPDATE_BUCKET_ITEMS:
                    db.bucketDao().update(bucketItems[0]);
                    break;

                case MainActivity.TASK_INSERT_BUCKET_ITEMS:
                    db.bucketDao().insert(bucketItems[0]);
                    break;
            }

            return db.bucketDao().getAll();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onBucketDbUpdated(list);
        }
    }
}
