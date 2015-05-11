package tk.wlemuel.cotable.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import tk.wlemuel.cotable.R;

/**
 * Created by stevelemuel on 3/28/15.
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected LayoutInflater mInflater;
    private Toolbar mActionBar;
    private TextView mActionTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected boolean hasActionBar(){
        return true;
    }

    protected abstract int getLayoutRes();

    protected View inflateView(int resId){
        return mInflater.inflate(resId, null);
    }

    protected int getActionBarTitle(){
        return R.string.app_name;
    }

    protected boolean hasBackButton(){
        return false;
    }

    protected int getActionBarCustomView(){
        return 0;
    }

    protected abstract void init(Bundle savedInstanceState);

    protected void initActionBar(Toolbar actionBar){
        if(actionBar == null) return;

        setSupportActionBar(actionBar);

    }
}
