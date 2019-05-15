package cn.edu.swufe.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;

import static java.lang.Integer.*;

public class SecondActivity extends AppCompatActivity {
    TextView score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basketball);

       score=(TextView) findViewById(R.id.score);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea=((TextView)findViewById(R.id.score)).getText().toString();

       // outState.putString("");
       // Log.i(TAG,"onSaveInstanceState")
    }

    public void btnAdd1(View btn){ showScore(1); }
    public void btnAdd2(View btn){ showScore(2); }
    public void btnAdd3(View btn){ showScore(3); }

    public void btnReset(View btn){
        score.setText("0");
    }

    private void showScore(int inc){
        Log.i("show","inc="+inc);
        String oldScore=(String) score.getText();
        int sss=parseInt(oldScore)+inc;
        score.setText(""+sss);
    }









}
