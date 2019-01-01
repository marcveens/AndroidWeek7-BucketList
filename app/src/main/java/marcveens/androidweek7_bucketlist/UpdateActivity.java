package marcveens.androidweek7_bucketlist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    public TextInputEditText mInputTitle;
    public TextInputEditText mInputDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mInputTitle = findViewById(R.id.inputTitle);
        mInputDescription = findViewById(R.id.inputDescription);
        final BucketListItem bucketUpdate = getIntent().getParcelableExtra(MainActivity.UPDATE_BUCKET_ITEM);
        final Integer insertOrUpdate = getIntent().getIntExtra(MainActivity.INSERT_OR_UPDATE, MainActivity.TASK_UPDATE_BUCKET_ITEMS);

        if (bucketUpdate != null) {
            mInputTitle.setText(bucketUpdate.getTitle());
            mInputDescription.setText(bucketUpdate.getDescription());
        }

        FloatingActionButton saveBucketItem = findViewById(R.id.saveBucketItem);
        saveBucketItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mInputTitle.getText().toString();
                String description = mInputDescription.getText().toString();

                if (!TextUtils.isEmpty(title)) {
                    bucketUpdate.setTitle(title);
                }
                if (!TextUtils.isEmpty(description)) {
                    bucketUpdate.setDescription(description);
                }
                bucketUpdate.setSaveDate(new Date());

                Intent resultIntent = new Intent();
                resultIntent.putExtra(MainActivity.UPDATE_BUCKET_ITEM, bucketUpdate);
                resultIntent.putExtra(MainActivity.INSERT_OR_UPDATE, insertOrUpdate);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
