package cn.edu.swufe.second;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyList2Activity extends ListActivity implements Runnable, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{

    Handler handler;
    private List<HashMap<String, String>> listItems;
    private SimpleAdapter listItemAdapter;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        this.setListAdapter(listItemAdapter);
        //setContentView(R.layout.list_item);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    listItems = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this,
                            listItems,//数据源
                            R.layout.list_item,//自己建的行布局
                            new String[]{"ItemTitle", "ItemDetail"},
                            new int[]{R.id.itemTitle, R.id.itemDetail});
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    private void initListView() {
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " + i);//标题文字
            map.put("ItemDetail", "detail: " + i);//描述详情
            listItems.add(map);
            //生成适配器的item和动态数组对应元素
            listItemAdapter = new SimpleAdapter(this,
                    listItems,//数据源
                    R.layout.list_item,//自己建的行布局
                    new String[]{"ItemTitle", "ItemDetail"},
                    new int[]{R.id.itemTitle, R.id.itemDetail}
            );
        }
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<String> retList = new ArrayList<String>();


        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();

            Log.i(TAG, "run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table6 = tables.get(5);
            //获取TD中的数据
            Elements tds = table6.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 8) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);

                String str1 = td1.text();
                String val = td2.text();
                Log.i(TAG, "run:" + str1 + "==>" + val);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", str1);
                map.put("ItemDetail", val);
                retList.add(String.valueOf(map));

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);//把mag放到队列里
    }


    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Object itemAtPosition = getListView().getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");

        Log.i(TAG, "onClick:titleStr=" + titleStr);
        Log.i(TAG, "onClick:detailStr=" + detailStr);

        TextView title = (TextView) view.findViewById(R.id.itemTitle);
        TextView detail = (TextView) view.findViewById(R.id.itemDetail);

        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());

        Log.i(TAG, "onItemClick:title2=" + title2);
        Log.i(TAG, "onItemClick:detail2=" + detail2);

        //打开新的页面，传入参数
        //MyList2Activity中代码
        Intent rateClac =new Intent(this,RateCalcActivity.class);
        rateClac.putExtra("title",titleStr);
        rateClac.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateClac);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position"+ position);
       //删除执行
//        listItems.remove(position);
//        listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确认操作
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {  //which表示激活事件的是哪一个
                Log.i(TAG, "onClick: 对话框事件处理 ");
                listItems.remove(position);//引入final
              listItemAdapter.notifyDataSetChanged();
            }
        })
        .setNegativeButton("否",null);
        Log.i(TAG, "onItemLongClick: size"+ listItems.size());
        return true;//true 短按事件不执行，f短按事件执行
    }
}