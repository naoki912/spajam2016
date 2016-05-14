package wcdi.spajam2016;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onJoin(int groupId, int password, int userId) {
        Toast.makeText(this, password + "aa", Toast.LENGTH_SHORT).show();
    }

}
