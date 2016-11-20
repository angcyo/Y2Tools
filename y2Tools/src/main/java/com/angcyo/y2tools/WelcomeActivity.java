package com.angcyo.y2tools;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.angcyo.base.BaseActivity;
import com.angcyo.base.BaseApplication;
import com.angcyo.util.AnimUtil;
import com.angcyo.util.ColorTool;
import com.angcyo.util.ToastTool;
import com.angcyo.util.UnitTool;

/**
 * @author angcyo
 */
public class WelcomeActivity extends BaseActivity {

    private static String TAG = "WelcomeActivity";
    Handler handler = new Handler();
    private long exitTime = 0;
    private Button wel_button;
    private TextView wel_text;
    private ImageView wel_image;
    private RelativeLayout wel_bk_layout;
    private int ANIM_TIME = 2;// 动画持续时间，时间过后自动进入。单位秒
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String btString = ((String) wel_button.getText()).substring(0, 2);
            String btText = btString + " " + String.valueOf(ANIM_TIME);
            wel_button.setText(btText);
            if (ANIM_TIME >= 0) {
                handler.postDelayed(runnable, 1000);// 延迟1秒继续发送
                --ANIM_TIME;
            } else if (0 > ANIM_TIME) {
                btText = btString + " " + "→";
                wel_button.setText(btText);

                goMain();
            }

        }
    };


    public WelcomeActivity() {
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        initView();
        initAnim();

        if (UnitTool.isNetworkOK(this)) {
            //preLoad();
            //initAdsWidget();
        } else {
            ToastTool.getRandomColToast(this,
                    getResources().getString(R.string.str_netfail));
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        BaseApplication application = ((BaseApplication) getApplication());

        int count = application.preferences.getInt(application.RUN_COUNT, 0);
        application.editor.putInt(application.RUN_COUNT, ++count).commit();
    }

    private void initView() {
        // TODO Auto-generated method stub
        wel_button = (Button) findViewById(R.id.wel_button);
        wel_text = (TextView) findViewById(R.id.wel_text);
        wel_image = (ImageView) findViewById(R.id.wel_Logo_image);
        wel_bk_layout = (RelativeLayout) findViewById(R.id.wel_bk_layout);

        String text = ((BaseApplication) getApplication()).preferences
                .getString("TEXT_WEl", "此APP由  y2 出品");
        wel_text.setText(text);

        wel_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goMain();
            }
        });
    }

    private void initAnim() {
        // TODO Auto-generated method stub
        initBtAnim();
        initTextAnim();
        initLogoAnim();

        handler.postDelayed(runnable, 1000);
    }

    private int dpToPx(int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    private void initLogoAnim() {
        // TODO Auto-generated method stub
        final int anim_time = 1000;
        AnimationSet logoAnimationSet = new AnimationSet(true);
        Animation logoAnimation = AnimUtil.newTranAnim(0, 0, -dpToPx(100), 0,
                anim_time, true);
        logoAnimation.setStartOffset(anim_time);

        Animation logoAnimation2 = AnimUtil.newScaleAnim(1.0f, 2.0f, 1.0f,
                2.0f, 64f, 64f, anim_time, true);
        logoAnimation2.setStartOffset(2 * anim_time);

        logoAnimationSet.addAnimation(logoAnimation);
        // logoAnimationSet.addAnimation(logoAnimation2);
        wel_image.setAnimation(logoAnimationSet);
        logoAnimation.start();
    }

    private void initTextAnim() {
        // TODO Auto-generated method stub
        final int anim_time = 2000;
        Animation textAnimation = AnimUtil.newTranAnim(380, 0, 0, 0, anim_time,
                true);
        textAnimation.setInterpolator(new BounceInterpolator());
        wel_text.startAnimation(textAnimation);
    }

    private void initBtAnim() {
        // TODO Auto-generated method stub
        final int anim_time = 600;
        Animation wel_button_anim = AnimUtil.newTranAnim(0, 30, 0, 0,
                anim_time, true);
        Animation wel_button_anim3 = AnimUtil.newTranAnim(0, 20, 0, 0,
                anim_time, true);
        wel_button_anim3.setStartOffset(2 * anim_time);
        Animation wel_button_anim5 = AnimUtil.newTranAnim(0, 10, 0, 0,
                anim_time, true);
        wel_button_anim5.setStartOffset(4 * anim_time);
        Animation wel_button_anim7 = AnimUtil.newTranAnim(0, 5, 0, 0,
                anim_time, true);
        wel_button_anim7.setStartOffset(6 * anim_time);

        Animation wel_button_anim2 = AnimUtil.newTranAnim(0, -30, 0, 0,
                anim_time, true);
        wel_button_anim2.setStartOffset(anim_time);
        Animation wel_button_anim4 = AnimUtil.newTranAnim(0, -20, 0, 0,
                anim_time, true);
        wel_button_anim4.setStartOffset(3 * anim_time);
        Animation wel_button_anim6 = AnimUtil.newTranAnim(0, -10, 0, 0,
                anim_time, true);
        wel_button_anim6.setStartOffset(5 * anim_time);

        AnimationSet wel_button_animset = new AnimationSet(true);
        wel_button_animset.addAnimation(wel_button_anim);
        wel_button_animset.addAnimation(wel_button_anim2);
        wel_button_animset.addAnimation(wel_button_anim3);
        wel_button_animset.addAnimation(wel_button_anim4);
        wel_button_animset.addAnimation(wel_button_anim5);
        wel_button_animset.addAnimation(wel_button_anim6);
        wel_button_animset.addAnimation(wel_button_anim7);

        wel_button.setAnimation(wel_button_animset);
        wel_button_animset.start();
    }

    private void goMain() {
        clearAnim();
        handler.removeCallbacks(runnable);
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        overridePendingTransition(R.anim.mini2zoom, R.anim.rotate_anim);
        finish();
    }

    private void clearAnim() {
        wel_button.clearAnimation();
        wel_image.clearAnimation();
        wel_text.clearAnimation();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        if ((System.currentTimeMillis() - exitTime) > 2000) {

            ToastTool.getCentertToast(
                    WelcomeActivity.this,
                    ToastTool.getHtml("再按一次", "退出...",
                            ColorTool.getRandomColorString2()),
                    Toast.LENGTH_SHORT);
            exitTime = System.currentTimeMillis();
        } else {
            // super.onBackPressed();
            clearAnim();
            handler.removeCallbacks(runnable);

            finish();
            System.exit(0);
        }
    }

}
