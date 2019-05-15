package cn.edu.swufe.second;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//实现Runnable接口
public class Main3Activity extends AppCompatActivity implements Runnable{
    private   final String TAG="Main3Activity";
    private   float dollarRate=0.1f;
    private   float euroRate=0.2f;
    private   float wonRate=0.3f;
    private   String updateDate="";//定义字符串dateStr来获取日期，用来更新列表

     EditText inp1;
     TextView show;
     Handler handler;
    InputStream in;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        inp1 = (EditText) findViewById(R.id.inp1);
        show = (TextView) findViewById(R.id.showOut);

        //获取SP里保存的数据
        SharedPreferences sharedPreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
       //另一个获取方法 SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);

        dollarRate= sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate= sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate= sharedPreferences.getFloat("won_rate",0.0f);
        updateDate=sharedPreferences.getString("update_date","");//获取sp里的数据

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        //转换为字符串类型
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr=sdf.format(today);

        Log.i(TAG,"onCreate: sp dollarRate="+ dollarRate);
        Log.i(TAG,"onCreate: sp euroRate="+ euroRate);
        Log.i(TAG,"onCreate: sp wonRate="+ wonRate);
        Log.i(TAG,"onCreate: sp updateDate="+ updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG,"onCrate:需要更新");
            //开启子线程t
            Thread t = new Thread(this);//this 表示当前
            t.start();
        }else{
            Log.i(TAG,"onCrate:不需要更新");
        }



        handler = new Handler(){

            public void handleMessage(Message msg) {
                if(msg.what==5){//判断我们要获取那个子线程
                    Bundle bd1=(Bundle)msg.obj;//将获取的内容转为Bundle类型
                    dollarRate = bd1.getFloat("dollar-rate");
                    euroRate = bd1.getFloat("euro-rate");
                    wonRate = bd1.getFloat("won-rate");
                    Log.i(TAG,"handleMessage: dollarRate="+dollarRate);
                    Log.i(TAG,"handleMessage: euroRate="+ euroRate);
                    Log.i(TAG,"handleMessage: wonRate="+wonRate);

                    //保存更新的日期,同时保存汇率
                    SharedPreferences sharedPreferences= getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar_rate",dollarRate);
                    editor.putFloat("euro_rate",euroRate);
                    editor.putFloat("won_rate",wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();

                    Toast.makeText(Main3Activity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                    //将获取的内容转为字符串类型//String str=(String)msg.obj;
//                    Log.i(TAG,"handleMessage:getMessage msg="+str);
//                    show.setText(str);
                }
                super.handleMessage(msg);
            }
        };




    }


    public void onClick(View btn){
        Log.i(TAG,"onClick:");
        String str=inp1.getText().toString();
        float r =0;
        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        }

        if(btn.getId()==R.id.btn3_1){
            float val=r * dollarRate;//加f强制转换为float
            show.setText(String.valueOf(val));
        }else if(btn.getId()==R.id.btn3_2){
            float val=r * euroRate;
            show.setText(String.valueOf(val));
        }else if (btn.getId()==R.id.btn3_3){
            float val = r * wonRate;
            show.setText(String.valueOf(val));
            //保留两位小数？？
        }


    }

     public void openOne(View btn){
        //打开一个页面activity
         //Log.i("open","openOne: ");
         //Intent hello=new Intent(this,SecondActivity.class);
         //Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));//跳转到百度
         //Intent tele1=new Intent(Intent.ACTION_VIEW, Uri.parse("tel:87092173"));//跳转到拨号
         openConfig();
         //finish();
     }

    private void openConfig() {
        Intent config = new Intent(this, ConfigActivity.class);//打开一个窗口

        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);
        Log.i("open", "openOne:dollar_rate_key= " + dollarRate);
        Log.i("open", "openOne:euro_rate_key= " + euroRate);
        Log.i("open", "openOne:won_rate_key= " + wonRate);
        // startActivity(config);
        startActivityForResult(config, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();

        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this, MyList2Activity.class);//打开一个窗口
            startActivity(list);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==2){
//            bdl.putFloat("key_dollar",newDollar);
//            bdl.putFloat("key_euro",newEuro);
//            bdl.putFloat("key_won",newWon);
            Bundle bundle = data.getExtras();
            dollarRate=bundle.getFloat("key_dollar",0.1f);
            euroRate=bundle.getFloat("key_euro",0.1f);
            wonRate=bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult:dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult:euroRate="+euroRate);
            Log.i(TAG,"onActivityResult:wonRate="+wonRate);


            //将新设置的汇率写到sp里
            SharedPreferences sharedPreferences= getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();
            Log.i(TAG,"onActivityResult:数据已经保存到sharePreferences");

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {
        Log.i(TAG,"run:run()......");
        for(int i=1;i<3;i++){
            Log.i(TAG,"run:i="+i);
            try {
                Thread.sleep(2000); //延时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //用户保存获取的汇率
        Bundle bundle = new Bundle();



        //获取网络数据
//        URL url= null;
//        try {
//            url = new URL("http://www.usd-cny.com/icbc.htm");
//            HttpURLConnection http= (HttpURLConnection) url.openConnection();//类型转换
//            in=http.getInputStream();//获取一个输入流
//
//            String html =inputStream2String(in);
//            Log.i(TAG,"run:html="+ html);
//            Document  doc = Jsoup.parse(html); //
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            Log.i(TAG,"run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            //int i=1;
//            for(Element table:tables){
//                Log.i(TAG,"run:table=["+i+"]" + table);
//                i++;
//            }
            Element table6 = tables.get(5);
           // Log.i(TAG,"run:table6="+ table6);
            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
//            for(Element td:tds){
//                Log.i(TAG,"run:td=" + td);
//              }
            for(int i=0;i<tds.size();i+=8){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);

                String str1=td1.text();
                String val =td2.text();
                //Log.i(TAG,"run:" + str1 + "==>" + val);
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate",100f/Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate",100f/Float.parseFloat(val));
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate",100f/Float.parseFloat(val));
                }

            }
            //Elements newsHeadlines = doc.select("#mp-itn b a");
//            for (Element headline : newsHeadlines) {
//                Log.i(TAG,"%s\n\t%s" + headline.attr("title") + headline.absUrl("href"));
//            }    //测试方法
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取数据并返回
        //bundle中保存所获取的汇率
        //获取Msg对象，用于返回主线程
        Message msg=handler.obtainMessage();
       //msg.what=5;//what 用于标记当前msg的属性
        //msg.obj="Hello from run()";
        msg.obj=bundle;
        handler.sendMessage(msg);//把mag放到队列里

    }


    //设置方法把输入流转为字符串
      private String inputStream2String(InputStream inputStream){
              InputStreamReader reader = null;
              try {
                  reader = new InputStreamReader(in, "gb2312");
              } catch (UnsupportedEncodingException e1) {
                  e1.printStackTrace();
              }
              BufferedReader br = new BufferedReader(reader);
              StringBuilder sb = new StringBuilder();
              String line = "";
              try {
                  while ((line = br.readLine()) != null) {
                      sb.append(line);
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
              return sb.toString();
    }



}
