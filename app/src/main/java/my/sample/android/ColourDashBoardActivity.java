package my.sample.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;

import my.sample.android.uikit.widgets.dashboard.ArcProgressBar;
import my.sample.android.uikit.widgets.dashboard.CreditScoresDashboard;

public class ColourDashBoardActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private ArcProgressBar mArcProgressBar;
    private CreditScoresDashboard mCreditScoresDashboard;
    private AppCompatSeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArcProgressBar = (ArcProgressBar) findViewById(R.id.arc_progress_bar);
        mCreditScoresDashboard = (CreditScoresDashboard) findViewById(R.id.credit_scores_dashboard);
        mSeekBar = (AppCompatSeekBar) findViewById(R.id.app_compat_seek_bar);
        mSeekBar.setOnSeekBarChangeListener(this);

        mArcProgressBar.setMaxProgress(100);
        mCreditScoresDashboard.setMaxProgress(100);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mArcProgressBar.setProgress(progress);
        mCreditScoresDashboard.setProgressValue(progress, seekBar.getMax());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
