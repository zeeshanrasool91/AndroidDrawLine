package com.ahmadarif.drawline;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.davemorrissey.labs.subscaleview.ImageSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    // component
    @BindView(R.id.textX1)
    EditText textX1;
    @BindView(R.id.textY1)
    EditText textY1;
    @BindView(R.id.textX2)
    EditText textX2;
    @BindView(R.id.textY2)
    EditText textY2;
    @BindView(R.id.btnDraw)
    Button btnDraw;
    @BindView(R.id.btnClearPoints)
    Button btnClearPoints;
    @BindView(R.id.btnClearLines)
    Button btnClearLines;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.imageView)
    CustomImageView imageView;

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
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        drawerWidth = imageView.getWidth();
        drawerHeight = imageView.getHeight();

        resetDrawer();
    }

    private void resetDrawer() {
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    if (textX1.getText().toString().trim().length() == 0 | textY1.getText().toString().trim().length() == 0) {
                        if (sCoord != null) {
                            textX1.setText(String.valueOf(sCoord.x));
                            textY1.setText(String.valueOf(sCoord.y));
                        }
                    } else {
                        if (sCoord != null) {
                            textX2.setText(String.valueOf(sCoord.x));
                            textY2.setText(String.valueOf(sCoord.y));
                        }
                    }
                    Log.d("ZEESHAN", "Single tap: " + ((int) sCoord.x) + ", " + ((int) sCoord.y));
                    Toast.makeText(getApplicationContext(), "Single tap: " + ((int) sCoord.x) + ", " + ((int) sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

        });
        bitmap = drawableToBitmap(ContextCompat.getDrawable(MainActivity.this, R.drawable.floor_plan));
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas = new Canvas(mutableBitmap);
        //imageView.setImageBitmap(mutableBitmap);
        imageView.setImage(ImageSource.bitmap(mutableBitmap));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });


        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
    }

    @OnClick(R.id.btnClearLines)
    void btnClearLinesClicked() {
        resetDrawer();
    }

    @OnClick(R.id.btnClearPoints)
    void btnClearPointsClicked() {
        resetEditTexts();
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

        int x1 = (int) Float.parseFloat(textX1.getText().toString());
        int y1 = (int) Float.parseFloat(textY1.getText().toString());
        int x2 = (int) Float.parseFloat(textX2.getText().toString());
        int y2 = (int) Float.parseFloat(textY2.getText().toString());
        //to Draw single line
        canvas.drawLine(x1, y1, x2, y2, paint);
        Path path = new Path();
        /*path.moveTo(27, 27);
        path.lineTo(24, 357);
        path.lineTo(332, 380);
        path.lineTo(327, 633);
        path.lineTo(530, 655);
        path.lineTo(571, 899);
        path.lineTo(768, 904);
        path.lineTo(821, 1102);
        path.lineTo(936, 1109);*/

        path.moveTo(838, 1037);
        path.lineTo(954, 1037);
        path.lineTo(954, 835);
        path.lineTo(1066, 835);


        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLUE);
        strokePaint.setStrokeWidth(10);
        strokePaint.setStyle(Paint.Style.STROKE);
        float[] intervals = new float[]{30.0f, 20.0f};
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        strokePaint.setPathEffect(dashPathEffect);
        canvas.drawPath(path, strokePaint);
        imageView.postInvalidate();
    }

    @OnClick(R.id.btnClear)
    void btnClearClicked() {
        resetDrawer();
        resetEditTexts();
    }

    private void resetEditTexts() {
        textX1.getText().clear();
        textY1.getText().clear();
        textX2.getText().clear();
        textY2.getText().clear();
    }


    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawerWidth, drawerHeight, Bitmap.Config.ARGB_8888);
        }
        return bitmap;
    }


}
