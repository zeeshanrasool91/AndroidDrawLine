package com.ahmadarif.drawline;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // component
    @BindView(R.id.textX1) EditText textX1;
    @BindView(R.id.textY1) EditText textY1;
    @BindView(R.id.textX2) EditText textX2;
    @BindView(R.id.textY2) EditText textY2;
    @BindView(R.id.btnDraw) Button btnDraw;
    @BindView(R.id.btnClear) Button btnClear;
    @BindView(R.id.imageView) ImageView imageView;

    // drawer
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    int drawerWidth, drawerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // get dynamic size imageView
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                drawerWidth = imageView.getWidth();
                drawerHeight = imageView.getHeight();

                resetDrawer();
            }
        });
    }

    private void resetDrawer() {
        bitmap = Bitmap.createBitmap(drawerWidth, drawerHeight, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        imageView.setImageBitmap(bitmap);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
    }

    @OnClick(R.id.btnDraw)
    void btnDrawClicked() {
        if (textX1.getText().toString().trim().length() == 0) {
            textX1.setError("Cannot empty");
            return;
        }
        if (textY1.getText().toString().trim().length() == 0) {
            textY1.setError("Cannot empty");
            return;
        }
        if (textX2.getText().toString().trim().length() == 0) {
            textX2.setError("Cannot empty");
            return;
        }
        if (textY2.getText().toString().trim().length() == 0) {
            textY2.setError("Cannot empty");
            return;
        }

        int x1 = Integer.parseInt(textX1.getText().toString());
        int y1 = Integer.parseInt(textY1.getText().toString());
        int x2 = Integer.parseInt(textX2.getText().toString());
        int y2 = Integer.parseInt(textY2.getText().toString());

        canvas.drawLine(x1, y1, x2, y2, paint);
        imageView.postInvalidate();
        imageView.postInvalidate();
    }

    @OnClick(R.id.btnClear)
    void btnClearClicked() {
        resetDrawer();
    }
}
