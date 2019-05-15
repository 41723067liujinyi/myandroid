package cn.edu.swufe.second;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public abstract class RateListActivity extends ListActivity implements Runnable{
    String date[]={"one","two","three"};
    Handler handler;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
        //for(int i=0;i<date.length;i++)
        List<String>list1=new ArrayList<String>();
        for(int i=0;i<100;i++){
            list1.add("item"+ i);
        }
        //创建一个新的adapter
        ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        setListAdapter(adapter);//使用父类方法，是当前界面用adapter来管理

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==7){
                    List<String>list2 =(List<String>)msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void run(){
        //获取网络数据，放入list带回到主线程中
     List<String>retList=new ArrayList<String>();


        Document doc = null;
        try {
            Thread.sleep(3000);
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
                Log.i(TAG,"run:" + str1 + "==>" + val);
                retList.add(str1 + "==>" + val);


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Message msg=handler.obtainMessage(7);
        msg.obj=retList;
        handler.sendMessage(msg);//把mag放到队列里
    }
}
