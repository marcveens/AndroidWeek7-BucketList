package marcveens.androidweek7_bucketlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

public class MainViewModel extends ViewModel {
    private BucketRepository mRepository;
    private LiveData<List<BucketListItem>> bucketList;

    public MainViewModel(Context context) {
        mRepository = new BucketRepository(context);
        bucketList = mRepository.getAll();
    }

    public LiveData<List<BucketListItem>> getReminders() {
        return bucketList;
    }

    public void insert(BucketListItem bucketItem) {
        mRepository.insert(bucketItem);
    }

    public void update(BucketListItem bucketItem) {
        mRepository.update(bucketItem);
    }

    public void delete(BucketListItem bucketItem) {
        mRepository.delete(bucketItem);
    }
}