package marcveens.androidweek7_bucketlist;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@Entity(tableName = "bucketItem")
public class BucketListItem implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    public String title;
    public String description;
    public boolean finished;
    private Date saveDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(Date saveDate) {
        this.saveDate = saveDate;
    }

    public BucketListItem() {}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeSerializable(this.saveDate);
    }

    protected BucketListItem(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.saveDate = (Date) in.readSerializable();
    }

    public static final Parcelable.Creator<BucketListItem> CREATOR = new Parcelable.Creator<BucketListItem>() {
        @Override
        public BucketListItem createFromParcel(Parcel source) {
            return new BucketListItem(source);
        }

        @Override
        public BucketListItem[] newArray(int size) {
            return new BucketListItem[size];
        }
    };
}
