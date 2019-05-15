package cn.edu.swufe.second;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements  View.OnClickListener {
TextView out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        out=findViewById(R.id.txtout);

        EditText inp=findViewById(R.id.inp);
        String str=inp.getText().toString();
        Button btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("main","btn clicked 2222");
               int c;
               double f;
                EditText inp=findViewById(R.id.inp);
                c=Integer.parseInt(inp.getText().toString());
                f=(9.00*c)/5.00+32.00;
                String temp=""+f;
                //out.setText (R.string.ccc) ;
                out.setText ("结果为："+temp);
        }
        });


    }



    @Override
    public void onClick(View v) {
      out.setText ("Clicked") ;
    }
}
