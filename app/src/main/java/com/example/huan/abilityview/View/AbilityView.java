package com.example.huan.abilityview.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.huan.abilityview.Model.AbilityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by squirrelhuan on 2017/7/24.
 */

public class AbilityView extends View {


    public AbilityView(Context context) {
        super(context);
    }

    public AbilityView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbilityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private Context mcontext;//上下文对象
    private int layer_count;//层结构数量
    private int line_count;//线结构数量
    private double radius = 0;//半径
    private double angle = 0;//角度
    private int center_x, center_y, mwidth, width, height;
    //center_x 控件中间点x坐标,
    // center_y 控件中间点坐标,
    // mwidth 控件圆球的直径,
    // width,控件宽度
    // height控件高度
    //实力区域颜色
    private int redColor = 0xffaabbcc;//红色环状颜色
    private int blueColor = 0xffccbbaa;//蓝色

    private int backColors1[] = {0xfffcfcfc, 0xfff1f2f3};//背景环状颜色集
    private int backColors2[] = {0xfff0f0f0, 0xfffffdfc};//背景环状颜色集
    private int backColors3[] = {0xfff0f0f0, 0xffeaeaea};//背景环状颜色集
    private int[][] backColors = {backColors1, backColors2, backColors3};
    private List<AbilityModel> data;
    List<PointF> points_small = new ArrayList<PointF>();

    List<PointF> points_text = new ArrayList<PointF>();


    private int textSize_small, titleSize = 18, textSize_big, valueSize = 12;


    public int getTitleSize() {
        return titleSize;
    }

    /**
     * 设置每一项的名称字体大小
     *
     * @param titleSize
     */
    public void setTitleSize(int titleSize) {
        this.titleSize = titleSize;
    }

    public int getValueSize() {
        return valueSize;
    }

    /**
     * 设置每一项的值字体大小
     *
     * @param valueSize
     */
    public void setValueSize(int valueSize) {
        this.valueSize = valueSize;
    }

    public int getLayer_count() {
        return layer_count;
    }

    /**
     * 设置圆环层数
     * @param layer_count
     */
    public void setLayer_count(int layer_count) {
        this.layer_count = layer_count;
    }

    private static Canvas canvas;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null || data.size() == 0) {
            return;
        }
        //初始化数据
        initDate();
        //绘制背景
        drawBackground(canvas);
        this.canvas = canvas;
    }

    int start_angle = 0;

    private void initDate() {
        textSize_small = dip2px(mcontext, valueSize);
        textSize_big = dip2px(mcontext, titleSize);
        layer_count = 5;
        mwidth = (width > height ? height : width) * 4 / 5;
        radius = mwidth / 2;//半径
        center_x = (int) width / 2;
        center_y = (int) height / 2;

        line_count = data.size();
        angle = 360 / line_count;//角度
    }

    //绘制背景图片
    private void drawBackground(Canvas canvas) {

        //绘制圆
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        RectF oval = new RectF(150, 200, 500, 550);// 画一个椭圆
        p.setStrokeWidth(2);

        SweepGradient[] sweepGradients = new SweepGradient[3];
        for (int i = 0; i < 3; i++) {
            //先创建一个渲染器
            SweepGradient mSweepGradient = new SweepGradient(canvas.getWidth() / 2 - mwidth / 2,
                    canvas.getHeight() / 2 + mwidth / 2, //以圆弧中心作为扫描渲染的中心以便实现需要的效果
                    backColors[i], //这是我定义好的颜色数组，包含2个颜色：#35C3D7、#2894DD
                    null);
            sweepGradients[i] = mSweepGradient;
        }

        //绘制底部环状背景
        for (int i = layer_count; i > 0; i--) {
            // p.setColor(backColors[i % 3]);
            //把渐变设置到笔刷
            p.setShader(sweepGradients[i % 3]);
            int des = mwidth / (2 * layer_count) * i;
            // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
            p.setShadowLayer(5, 5, 5, Color.BLACK);
            oval.set(center_x - des, center_y - des, center_x + des, center_y + des);// 画一个椭圆
            canvas.drawArc(oval, 0, 360, false, p);///圆形
        }

        //绘制真实值的坐标
        p.setColor(Color.GREEN);
        p.setStrokeWidth(5);
        for (int i = 1; i <= line_count; i++) {
            int r = (int) (radius * data.get(i - 1).getValue());
            double d = i * angle * Math.PI / 180;
            double a = (i * angle) % 360;
            int x1 = (int) (center_x + r * Math.cos(d));
            int y1 = (int) (center_y + r * Math.sin(d));

            PointF pointf1 = new PointF(x1, y1);
            PointF pointf2 = new PointF(center_x, center_y);
            canvas.drawLine(pointf1.x, pointf1.y, pointf2.x, pointf2.y, p);// 画线
            points_small.add(pointf1);
        }

        //绘制真实覆盖区域
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        Paint paint_line_b = new Paint();
        paint_line_b.setColor(redColor);
        Path path2 = new Path();
        path2.moveTo(points_small.get(0).x, points_small.get(0).y);//设置Path的起点
        paint_line_b.setXfermode(null);
        Random random = new Random();
        int t = random.nextInt(3);
        for (int i = 0; i < line_count; i++) {
            float t2 = (float) 1.2 + random.nextInt(3) / 10;
            if (i < line_count - 1) {
                path2.quadTo(((points_small.get(i).x + points_small.get(i + 1).x) / 2 - center_x) * t2 + center_x, ((points_small.get(i).y + points_small.get(i + 1).y) / 2 - center_y) * t2 + center_y, points_small.get(i + 1).x, points_small.get(i + 1).y);
            } else {
                path2.quadTo(((points_small.get(i).x + points_small.get(0).x) / 2 - center_x) * t2 + center_x, ((points_small.get(i).y + points_small.get(0).y) / 2 - center_y) * t2 + center_y, points_small.get(0).x, points_small.get(0).y);
            }
        }
        canvas.drawPath(path2, paint_line_b);//画出贝塞尔曲线

        //绘制环状纹理
        paint_line_b.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        paint_line_b.setStyle(Paint.Style.STROKE);//设置空心
        paint_line_b.setColor(blueColor);
        paint_line_b.setStrokeWidth(mwidth / (layer_count) / 2);

        int c = 1;
        for (int i = 1; i < line_count / 2; i++) {
            int des = (int) (mwidth / (2 * layer_count) * (c + .5));
            oval.set(center_x - des, center_y - des, center_x + des, center_y + des);// 画一个椭圆
            canvas.drawArc(oval, start_angle, 360, false, paint_line_b);///圆形
            c = c + 2;
        }
        paint_line_b.setXfermode(null);

        //绘制圆线
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(1);
        for (int i = 1; i <= layer_count - 1; i++) {
            int des = mwidth / (2 * layer_count) * i;
            oval.set(center_x - des, center_y - des, center_x + des, center_y + des);// 画一个椭圆
            canvas.drawArc(oval, start_angle, 360, false, p);//圆形
        }

        //绘制直线
        p = new Paint();
        p.setColor(Color.GRAY);
        p.setStrokeWidth(2);
        p.setAntiAlias(true);

        PointF pointf0 = new PointF(center_x, center_y);
        for (int i = 1; i <= line_count; i++) {
            double x1 = 0;
            double y1 = 0;
            double angle_c = i * angle;

            double a = (i * angle) % 360;
            double d = i * angle * Math.PI / 180;
            x1 = (center_x + (int) radius * Math.cos(d));
            y1 = (center_y + (int) radius * Math.sin(d));

            PointF pointf1 = new PointF((float) x1, (float) y1);
            points_text.add(pointf1);
            canvas.drawLine(pointf1.x, pointf1.y, pointf0.x, pointf0.y, p);// 画线

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize_big);
            paint.setStrokeWidth(2);
            Path path = new Path();
            /*Diection.CCW 逆时针方向
            Diection.CW 顺时针方向*/
            path.addCircle(center_x + 10, center_y + 10, (float) radius * i, Path.Direction.CW);

            // 绘制路径
            // paint.setStyle(Paint.Style.STROKE);
            path2 = new Path();
            int margin = 40;//文字到圆的距离
            RectF rectF = new RectF((float) (center_x - radius - margin), (float) (center_y - radius - margin), (float) (center_x + radius + margin), (float) (center_y + radius + margin));
            //粗略的计算文字弧度
            path2.addArc(rectF, (float) (360 - i * (angle) - 10), (float) angle * 2 / 3);
            paint.setStrokeWidth(1);
            //canvas.drawPath(path2, paint);
            // 沿着路径绘制一段文本
            canvas.drawTextOnPath(data.get(i - 1).getTitle(), path2, -10, 20, paint);
        }

        Paint paint_point = new Paint();
        paint_point.setColor(Color.WHITE);
        paint_point.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除
        int radius_small = (int) (textSize_small * 1.1);
        //绘制小白点用于显示当前项的值 的背景
        for (int i = 0; i < line_count; i++) {
            if (clickedPosition != null && Integer.valueOf(clickedPosition) == i) {
                paint_point.setColor(0xffCD2626);//按下颜色
            } else {
                paint_point.setColor(Color.WHITE);//抬起
            }
            canvas.drawCircle(points_small.get(i).x, points_small.get(i).y, radius_small, paint_point);
        }

        // canvas.save();
       /* paint_point.setColor(Color.WHITE);
        for (int i = 0; i < line_count; i++) {
            canvas.drawCircle(points.get(i).x, points.get(i).y, radius_small, paint_point);
        }*/
        //canvas.restore();
        Paint paint_value = new Paint();
        paint_value.setColor(Color.BLACK);
        paint_value.setStrokeWidth(2);
        paint_value.setTextSize(textSize_small);
        //每一项value的值写在小白点上
        for (int i = 0; i < line_count; i++) {

            Float v = data.get(i).getValue();
            String a = v + "";
            int length = a.length();
            //粗略的定位 使文字居中
            canvas.drawText(String.format("%.2f", v), points_small.get(i).x - textSize_small * 3 / 11 * length, points_small.get(i).y + textSize_small * 2 / 5, paint_value);
        }


    }

    private float rotate_current;//当前旋转角度
    private float distance_valid = 0;//有效距离
    private float event_type;
    private boolean canTouched = false;//是否可以触摸
    float start_x = 0;//触摸起始点的x
    float start_y = 0;//触摸起始点的y
    float last_x = 0;//触摸起始点的x
    float last_y = 0;//触摸起始点的y

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Toast.makeText(mcontext, "按下了", Toast.LENGTH_SHORT).show();
                start_x = event.getX();
                start_y = event.getY();
                if (!isTouched) {//没有被点击则执行
                    doOnTouched(x, y);
                    isTouched = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //Toast.makeText(mcontext, "抬起了", Toast.LENGTH_SHORT).show();
                canTouched = false;
                clickedPosition = null;
                postInvalidate();
                isTouched = false;
                distance_valid = Math.abs(event.getX() - start_x) > Math.abs(event.getY() - start_y) ? (event.getX() - start_x) : (event.getY() - start_y);
                rotate_current += Math.abs(distance_valid) * 8;

                start_angle = (int) rotate_current;
                break;
            case MotionEvent.ACTION_MOVE:
                // Toast.makeText(mcontext, "移动了", Toast.LENGTH_SHORT).show();
                if (canTouched) {
                    clickedPosition = null;
                    isTouched = false;
                    distance_valid = Math.abs(event.getX() - start_x) > Math.abs(event.getY() - start_y) ? (event.getX() - start_x) : (event.getY() - start_y);

                    Double a = Math.pow((last_x - event.getX()), 2);
                    Double b = Math.pow((last_y - event.getY()), 2);
                    double c = Math.pow(15, 2);
                    if (Math.abs(distance_valid) > 10 && a + b > c) {
                        float r = (float) ((rotate_current + Math.abs(distance_valid) * 8) * Math.PI / 180);
                        //Toast.makeText(mcontext, "c："+ String.format("%.2f",Math.abs(r)), Toast.LENGTH_SHORT).show();
                        AbilityView.this.setRotation(r); //旋转部分
                        last_x = event.getX();
                        last_y = event.getY();
                    }
                }
                break;
        }
        return true;
    }

    private String clickedPosition = null;
    private boolean isTouched = false;

    private void doOnTouched(float x, float y) {
        boolean hasDeal = false;
        for (int i = 0; i < line_count; i++) {
            double a = Math.pow(points_small.get(i).y - y, 2);
            double b = Math.pow(points_small.get(i).x - x, 2);
            double c = Math.pow(textSize_small, 2);//有效点击半径的平方 ，这里设置为30的平放
            if (a + b < c) {
                //处理点击白点事件
                //Toast.makeText(mcontext, "你点击了第" + i + "个小白点，它的坐标x:" + x + "，y:" + y, Toast.LENGTH_SHORT).show();
                clickedPosition = i + "";
                postInvalidate();
                hasDeal = true;
            }
            if((points_text.get(i).y >0&& y>0)||(points_text.get(i).y<0&&y<0)){

                double a2 = Math.pow(points_text.get(i).y - y, 2);
                double b2 = Math.pow(points_text.get(i).x - x, 2);
                double c2 = Math.pow(textSize_small * 2, 2);//有效点击半径的平方
                if (a2 + b2 < c2) {
                    //处理点击文字事件

                    break;
                }
            }
        }
        if (!hasDeal) {
            canTouched = true;
        }
    }

    public static int px2dip(Context mContext, float px) {

        float scale = mContext.getResources().getDisplayMetrics().density;

        return (int) (px / scale + 0.5f);

    }

    public static int dip2px(Context mContext, float dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 设置数据
     *
     * @param realData
     */
    public void setData(List<AbilityModel> realData, Context context) {
        this.data = realData;
        this.mcontext = context;
        invalidate();
    }
}
