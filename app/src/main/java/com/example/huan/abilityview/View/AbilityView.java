package com.example.huan.abilityview.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.huan.abilityview.Model.AbilityModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by aquirrelhuan on 2017/7/24.
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
    private int redColor = 0xffd84561;//红色环状颜色
    private int blueColor = 0xff9bbfcb;//蓝色
    private int backColors[] = {0xeef2f4f3, 0xeeffffff, 0xeeeaeaea};//背景环状颜色集
    private List<AbilityModel> data;
    List<PointF> points = new ArrayList<PointF>();


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
    }


    private void initDate() {
        textSize_small = dip2px(mcontext, valueSize);
        textSize_big = dip2px(mcontext, titleSize);
        layer_count = 5;
        mwidth = (width > height ? height : width) * 4 / 5;
        radius = mwidth / 2;//半径
        center_x = (int) getX() + width / 2;
        center_y = (int) getY() + height / 2;

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

        //绘制底部环状背景
        for (int i = layer_count; i > 0; i--) {
            p.setColor(backColors[i % 3]);
            int des = mwidth / (2 * layer_count) * i;
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
            points.add(pointf1);
        }

        //绘制真实覆盖区域
        int layerId = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        Paint paint_line_b = new Paint();
        paint_line_b.setColor(redColor);
        Path path2 = new Path();
        path2.moveTo(points.get(0).x, points.get(0).y);//设置Path的起点
        paint_line_b.setXfermode(null);
        Random random = new Random();
        int t = random.nextInt(3);
        for (int i = 0; i < line_count; i++) {
            float t2 = (float) 1.2 + random.nextInt(3) / 10;
            if (i < line_count - 1) {
                path2.quadTo(((points.get(i).x + points.get(i + 1).x) / 2 - center_x) * t2 + center_x, ((points.get(i).y + points.get(i + 1).y) / 2 - center_y) * t2 + center_y, points.get(i + 1).x, points.get(i + 1).y);
            } else {
                path2.quadTo(((points.get(i).x + points.get(0).x) / 2 - center_x) * t2 + center_x, ((points.get(i).y + points.get(0).y) / 2 - center_y) * t2 + center_y, points.get(0).x, points.get(0).y);
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
            canvas.drawArc(oval, 0, 360, false, paint_line_b);///圆形
            c = c + 2;
        }

        //绘制圆线
        p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.STROKE);//设置空心
        p.setStrokeWidth(1);
        for (int i = 1; i <= layer_count - 1; i++) {
            int des = mwidth / (2 * layer_count) * i;
            oval.set(center_x - des, center_y - des, center_x + des, center_y + des);// 画一个椭圆
            canvas.drawArc(oval, 0, 360, false, p);///圆形
        }

        //绘制直线
        p = new Paint();
        p.setStrokeWidth(3);
        p.setColor(Color.GRAY);
        p.setStrokeWidth(2);

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
            path2.addArc(rectF, (float) (360 - i * (angle) - 10), (float) angle*2 / 3);
            paint.setStrokeWidth(1);
            //canvas.drawPath(path2, paint);
            // 沿着路径绘制一段文本
            canvas.drawTextOnPath(data.get(i - 1).getTitle() + i, path2, -10, 20, paint);
        }

        Paint paint_point = new Paint();
        paint_point.setColor(Color.WHITE);
        int radius_small = (int) (textSize_small * 1.1);
        //绘制小白点用于显示当前项的值 的背景
        for (int i = 0; i < line_count; i++) {
            canvas.drawCircle(points.get(i).x, points.get(i).y, radius_small, paint_point);
        }

        Paint paint_value = new Paint();
        paint_value.setColor(Color.BLACK);
        paint_value.setStrokeWidth(2);
        paint_value.setTextSize(textSize_small);
        //每一项value的值写在小白点上
        for (int i = 0; i < line_count; i++) {
            String a = data.get(i).getValue() + "";
            int length = a.length();
            //粗略的定位 使文字居中
            canvas.drawText(a, points.get(i).x - textSize_small * 3 / 11 * length, points.get(i).y + textSize_small * 2 / 5, paint_value);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        doOnTouched(x, y);
        return super.onTouchEvent(event);
    }

    private void doOnTouched(float x, float y) {
        for (int i = 0; i < line_count; i++) {
            double a = Math.pow(points.get(i).y - y, 2);
            double b = Math.pow(points.get(i).x - x, 2);
            double c = Math.pow(textSize_small,2);//有效点击半径的平方 ，这里设置为30的平放
            if (a + b < c) {
                //处理点击白点事件
                Toast.makeText(mcontext, "你点击了第"+i+"个小白点，它的坐标x:" + x + "，y:" + y, Toast.LENGTH_SHORT).show();
            }
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
