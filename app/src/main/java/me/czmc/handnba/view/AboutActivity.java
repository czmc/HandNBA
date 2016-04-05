package me.czmc.handnba.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.czmc.handnba.R;
import me.czmc.handnba.utils.ToolUtils;

public class AboutActivity extends Activity {
    @Bind(R.id.version_name )
    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        version.setText("Version: "+ToolUtils.getAppVersionName(this));
    }
}
