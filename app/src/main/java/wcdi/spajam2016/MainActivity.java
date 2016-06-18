package wcdi.spajam2016;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements
        MainActivityFragment.OnEventListener,
        ModeSelectingFragment.OnEventListener,
        ComingOutInputFragment.OnEventListener,
        ComingOutFragment.OnEventListener,
        QuestionInputFragment.OnEventListener,
        QuestionsFragment.OnEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_container, MainActivityFragment.newInstance())
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void onJoin(Integer groupId, Integer password, Integer userId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ModeSelectingFragment.newInstance(groupId, password, userId))
                .commit();
        });
    }

    @Override
    public void onQuestionModeChanged(Integer groupId, Integer password, Integer userId, Integer sessionId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, QuestionInputFragment.newInstance(groupId, sessionId, userId, sessionId))
                .commit();
        });
    }

    @Override
    public void onComingOutModeChanged(Integer groupId, Integer password, Integer userId, Integer sessionId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ComingOutInputFragment.newInstance(groupId, sessionId, userId, sessionId))
                .commit();
        });
    }

    @Override
    public void onQuestionModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, QuestionInputFragment.newInstance(groupId, password, userId, sessionId))
                .commit();
        });
    }

    @Override
    public void onComingOutModeSelected(Integer groupId, Integer password, Integer userId, Integer sessionId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ComingOutInputFragment.newInstance(groupId, password, userId, sessionId))
                .commit();
        });

    }

    @Override
    public void onComingOutResultChanged(Integer groupId, Integer password, Integer userId, Integer stateGroupId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ComingOutFragment.newInstance(groupId, password, userId, stateGroupId))
                    .commit();
        });
    }

    @Override
    public void onQuestionResultChanged(Integer groupId, Integer password, Integer userId, Integer stateGroupId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, QuestionsFragment.newInstance(groupId, password, userId, stateGroupId))
                    .commit();
        });
    }

    @Override
    public void onModeSelectingChanged(Integer groupId, Integer password, Integer userId) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ModeSelectingFragment.newInstance(groupId, password, userId))
                    .commit();
        });
    }
}
