package wcdi.spajam2016;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
    MainActivityFragment.OnEventListener,
    ModeSelectingFragment.OnEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, MainActivityFragment.newInstance())
            .commit();
    }

    @Override
    public void onJoin(Integer groupId, Integer password, Integer userId) {
        Toast.makeText(this, password.toString(), Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ModeSelectingFragment.newInstance(groupId, password, userId))
                .commit();
        });
    }

    @Override
    public void onQuestionModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId) {
        Toast.makeText(this, sessionId.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComingOutModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId) {

    }
}
