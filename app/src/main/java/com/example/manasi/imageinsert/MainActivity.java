package com.example.manasi.imageinsert;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;


        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ImageView;

public class MainActivity extends Activity {

    public static final String IMAGE_ID = "IMG_ID";
    private final String TAG = "MainActivity";

    private DatabaseHelper databaseHelper;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        imageView = (ImageView)findViewById(R.id.imageView);

        Drawable dbDrawable = getResources().getDrawable(R.drawable.sqlite);
        databaseHelper.insetImage(dbDrawable, IMAGE_ID);

        new LoadImageFromDatabaseTask().execute(0);
    }

    private class LoadImageFromDatabaseTask extends AsyncTask<Integer, Integer, ImageHelper> {

        private final ProgressDialog LoadImageProgressDialog =  new ProgressDialog(MainActivity.this);

        protected void onPreExecute() {
            this.LoadImageProgressDialog.setMessage("Loading Image from Db...");
            this.LoadImageProgressDialog.show();
        }

        @Override
        protected ImageHelper doInBackground(Integer... integers) {
            return databaseHelper.getImage(IMAGE_ID);
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(ImageHelper imageHelper) {
            if (this.LoadImageProgressDialog.isShowing()) {
                this.LoadImageProgressDialog.dismiss();
            }
            setUpImage(imageHelper.getImageByteArray());
        }

    }

    private void setUpImage(byte[] bytes) {
        Log.d(TAG, "Decoding bytes");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }


}
