package hvasoftware.com.thongtindoino.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by dell on 3/19/2018.
 */

@SuppressLint("AppCompatCustomView")
public class SmartButton extends TextView {
    View.OnClickListener SmartClick;
    long lastClickTime = SystemClock.elapsedRealtime();
    public boolean IsClickable = true;
    public SmartButton(Context context) {
        super(context);
        init();
    }

    public SmartButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

void init(){
    setGravity(Gravity.CENTER);
    if(!hasOnClickListeners())
    {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((SystemClock.elapsedRealtime() - lastClickTime) < 1000 || !IsClickable || SmartClick == null)
                {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                SmartClick = this;
            }
        });
    }
}


}
