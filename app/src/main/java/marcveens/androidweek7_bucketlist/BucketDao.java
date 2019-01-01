package marcveens.androidweek7_bucketlist;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BucketDao {
    @Query("SELECT * FROM bucketItem")
    LiveData<List<BucketListItem>> getAll();

    @Insert
    void insert(BucketListItem item);

    @Delete
    void delete(BucketListItem item);

    @Update
    void update(BucketListItem item);
}
