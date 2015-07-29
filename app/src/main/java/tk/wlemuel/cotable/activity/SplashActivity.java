package tk.wlemuel.cotable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import tk.wlemuel.cotable.R;
import tk.wlemuel.cotable.base.BaseActivity;
import tk.wlemuel.cotable.utils.TDevice;


public class SplashActivity extends BaseActivity {

    public static final int MAX_WAITING_TIME = 3000; // Wait for 3 seconds
    private static final String SPLASH_SCREEN = "SplashScreen";
    protected boolean mShouldGoTo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TDevice.setFullScreen(this);

        final View view = View.inflate(this, R.layout.aty_splash_background, null);
        setContentView(view);


        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(MAX_WAITING_TIME);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (mShouldGoTo) {
                    redirectTo();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });


    }

    private void redirectTo() {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        finish();
    }
}
