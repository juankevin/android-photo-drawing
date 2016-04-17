
package com.flor.photodrawing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static Bitmap bmOriginal;

    int pattern = 0;

    @Bind(R.id.save)
    FloatingActionButton fabSave;

    @Bind(R.id.imv_photo)
    TouchImageView imvPhoto;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initProperties();
    }

    public void initProperties() {

        ButterKnife.bind(this);

        bmOriginal = ((BitmapDrawable) imvPhoto.getDrawable()).getBitmap();

        setSupportActionBar(toolbar);
        fabSave.setEnabled(false);

    }

    @OnClick(R.id.save)
    public void savePhoto(View v) {
        Snackbar.make(v, "Save", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @OnClick(R.id.photo)
    public void drawingPhoto(View v) {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, REQUEST_IMAGE_CAPTURE);

    }

    @OnClick(R.id.touch_left_top)
    public void leftTop() {
        pattern ++;
        isValidPattern(pattern);
    }

    @OnClick(R.id.touch_right_top)
    public void rightTop() {
        pattern ++;
        isValidPattern(pattern);
    }

    @OnClick(R.id.touch_left_bottom)
    public void leftBottom() {
        pattern ++;
        isValidPattern(pattern);
    }

    @OnClick(R.id.touch_right_bottom)
    public void rightBottom() {
        pattern ++;
        isValidPattern(pattern);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pattern = 0;
        try {
            Bitmap mBitmap = (Bitmap) data.getExtras().get("data");

            imvPhoto.setImageBitmap(mBitmap);
            imvPhoto.setMaxZoom(4f);

        } catch (NullPointerException e) {

        }
    }

    private void isValidPattern(int patternValue) {
        if (pattern == 2){
            drawRectangleOverImage();
        } else if(pattern == 3){
            removeRectangle();
            pattern = 0;
        }
    }

    private void drawRectangleOverImage() {
        bmOriginal = ((BitmapDrawable) imvPhoto.getDrawable()).getBitmap();

        float initX = 1;
        float initY = 1;
        float finalX = bmOriginal.getWidth() - 1;
        float finalY = bmOriginal.getHeight() - 1;

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        Bitmap tempBitmap = Bitmap.createBitmap(bmOriginal.getWidth(), bmOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(tempBitmap);

        //Draw the image bitmap into the cavas
        tempCanvas.drawBitmap(bmOriginal, 0, 0, null);

        //Draw everything else you want into the canvas, in this example a rectangle with rounded edges
        tempCanvas.drawRect(initX, initY, finalX, finalY, paint);

        //Attach the canvas to the ImageView
        imvPhoto.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

    }

    private void removeRectangle(){
        imvPhoto.setImageDrawable(new BitmapDrawable(getResources(), bmOriginal));
    }
}