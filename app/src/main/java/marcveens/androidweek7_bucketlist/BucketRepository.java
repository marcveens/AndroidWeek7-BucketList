package marcveens.androidweek7_bucketlist;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    public LiveData<List<BucketListItem>> getAll() {
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