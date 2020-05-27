package com.example.wheelpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements WheelPicker.OnItemSelectedListener, View.OnClickListener {
    private WheelPicker wheelLeft;
    private WheelPicker wheelCenter;
    private WheelPicker wheelRight;

    private Button gotoBtn;
    private Integer gotoBtnItemIndex;
    private List<String> cityArrayList, provinceArrayList;
    private List<AreaManager.KeyValuePair<String, String>> provincesList;
    private AreaManager.KeyValuePair<String, String> keyValuePair;
    private HashMap<String, List<AreaManager.KeyValuePair<String, String>>> cityMaps;
    private List<AreaManager.KeyValuePair<String, String>> cityList;
    private AreaManager.KeyValuePair<String, String> cityValuePair;
    private String key, finallyCityValue, finallyCityKey;
    private String text="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        init();
//        loadAreaMager();
    }

    private void init() {

        wheelLeft = findViewById(R.id.main_wheel_left);
//        wheelLeft.setOnItemSelectedListener(this);
        wheelCenter = findViewById(R.id.main_wheel_center);
//        wheelCenter.setOnItemSelectedListener(this);
//        wheelRight = findViewById(R.id.main_wheel_right);
//        wheelRight.setOnItemSelectedListener(this);
        initSelectorWind();
//        cityArrayList = new ArrayList<>();
//        provinceArrayList = new ArrayList<>();
//
//        gotoBtn = (Button) findViewById(R.id.goto_btn);
//        randomlySetGotoBtnIndex();
//        gotoBtn.setOnClickListener(this);
    }

    //Wind的方法
    private void initSelectorWind() {

        //1.取得City資料跟Province資料
        AreaManager areaManager = AreaManager.getInstance(this);
        List<AreaManager.KeyValuePair<String, String>> province = areaManager.getProvinces();
        final HashMap<String, List<AreaManager.KeyValuePair<String, String>>> cityMap = areaManager.getProvinceCityHashMap();

        //2左邊欄位設定省分資料
        wheelLeft.setData(province);


        wheelLeft.setSelectedItemPosition(0);
        //3.設定程式資料
        wheelCenter.setData(cityMap.get(province.get(0).key));
        wheelCenter.setSelectedItemPosition(0);

        //4..當左邊元件滑到資料時
        wheelLeft.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {

                //data:取得你滑到的省分資料
                AreaManager.KeyValuePair<String, String> selectedProvince = (AreaManager.KeyValuePair<String, String>) data;//data:广西壮族自治区

                //設定顯示的資料為:城市資料結構裡(滑到的省份,裡面的key)
                wheelCenter.setData(cityMap.get(selectedProvince.key));//cityMap.get(selectedProvince.key:[南宁市, 柳州市, 桂林市, 梧州市, 北海市, 防城港市, 钦州市, 贵港市, 玉林市, 百色市, 贺州市, 河池市, 来宾市, 崇左市]

                wheelCenter.setSelectedItemPosition(0);

                Log.v("hank","data:" + selectedProvince +"/cityMap.get(selectedProvince.key:" + cityMap.get(selectedProvince.key));

            }
        });

    }

    private void randomlySetGotoBtnIndex() {
        gotoBtnItemIndex = (int) (Math.random() * wheelCenter.getData().size());
        gotoBtn.setText("Goto '" + wheelCenter.getData().get(gotoBtnItemIndex) + "'");

    }

    //當滑到時
//    onItemSelected(
//    WheelPicker picker, //1.耀滑的item元件
//    Object data,//2.被選到的Object物件資訊
//    int position)//3.位置
    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
        switch (picker.getId()) {
            case R.id.main_wheel_left:
                text = "Left:";
                showLog("main_wheel_left");
                break;
            case R.id.main_wheel_center:
                text = "Center:";
                showLog("main_wheel_center");
                break;
        }


        //1.取得Provinces解析好的資料
        List<AreaManager.KeyValuePair<String, String>> provincesList = AreaManager.getInstance(getBaseContext()).getProvinces();

        //2.利用intetator蝶帶氣撈出來
        Iterator<AreaManager.KeyValuePair<String, String>> iterator = provincesList.iterator();
        while (iterator.hasNext()) { //當裡面有值時,
            AreaManager.KeyValuePair<String, String> keyValuePair = iterator.next();//當還有下一筆資料時
            String key = keyValuePair.key;//取得key
            String value = keyValuePair.value; //取得value

            Log.v("hank", "Provinces:" + "key:" + key + "/value:" + value);

            provinceArrayList.add(value);
            wheelLeft.setMaximumWidthText("1234567890"); //設定滾輪Item文字的預設寬度
            wheelLeft.setData(provinceArrayList); //設定內容物


            for (int i = 0; i < provincesList.size(); i++) {
                //如果省分的資料跟滑到的省分一樣
                if (provinceArrayList.get(i).equals(picker.getData().get(position).toString())) {//equals=>provinceArrayList.get(i):天津/picker.getData().get(position).toString()):天津
                    showLog("equals:" + provinceArrayList.get(i) + "/picker.getData().get(position).toString()):" + picker.getData().get(position).toString());
                    //如果省分的key跟城市的key一樣的話
                    if (provincesList.get(i).key.equals(key)) {//provincesList.get(i).key:120000cityKey:120000
                        Log.v("hank", "provincesList.get(i).key:" + provincesList.get(i).key + "cityKey:" + key);
                        //抓取city:110000裡面的List<Key:value>
                        Iterator<AreaManager.KeyValuePair<String, String>> a = cityMaps.get(key).iterator();

                        //先清空舊的city資料
                        cityArrayList.clear();

                        //取得city的110000指定資料取得裡面list的 key:value
                        while (a.hasNext()) {

                            AreaManager.KeyValuePair<String, String> keyPairList = a.next();
                            String finallyCityValue = keyPairList.value;
                            String finallyCityKey = keyPairList.key;
                            Log.v("hank", "finallyCityKey:" + finallyCityKey + "/finallyCityValue:" + finallyCityValue);


                            Log.v("hank", "cityArrayList" + cityArrayList.toString());
                            cityArrayList.add(finallyCityValue);
                            wheelCenter.setMaximumWidthText("1234567890"); //設定滾輪Item文字的預設寬度
                            wheelCenter.setData(cityArrayList); //設定內容物
                            wheelCenter.getData();
                        }
                        Log.v("hank", "cityMaps.get(key).get(i).value:" + cityMaps.get(key).size()); //取得city欄位1(110000指定得key)

                    }

                }
            }

        }
        Toast.makeText(this, text + String.valueOf(data), Toast.LENGTH_SHORT).show();
    }

    public static ArrayList getSingleList(ArrayList list) {
        //1.新的tempList容器
        ArrayList tempList = new ArrayList();

        //2.使用者丟進來的list用iterator去掉出來
        Iterator it = list.iterator();

        //當裡面有下一個值時
        while (it.hasNext()) {

            //取得值
            Object obj = it.next();
            Log.v("hank", "obj:" + obj.toString());

            //如果新的list容器裡沒有包含就容器的元素的話新增
            if (!tempList.contains(obj)) {
                tempList.add(obj);
                Log.v("hank", "沒有重複");

            } else {
                //有包含一樣的元素不新增
                Log.v("hank", "有重複");
            }

        }
        return tempList;
    }



    @Override
    public void onClick(View v) {
        wheelCenter.setSelectedItemPosition(gotoBtnItemIndex);
        randomlySetGotoBtnIndex();
    }

    public void showLog(String msg) {
        Log.v("hank", msg.toString());
    }


    //3.用資料結構方式讀取檔案加將資料灌入Selector
    //用資料結構讀取檔案方式
    public void loadAreaMager() {
        //陣列,物件 [{}] => List<KeyValuePair<String, String>>
        //解Provinces[{ key":"110000","value": "北京" }, { "key":"120000","value": "天津"}

        //1.取得Provinces解析好的資料
        provincesList = AreaManager.getInstance(getBaseContext()).getProvinces();

        //2.利用intetator蝶帶氣撈出來
        Iterator<AreaManager.KeyValuePair<String, String>> iterator = provincesList.iterator();
        while (iterator.hasNext()) { //當裡面有值時,
            keyValuePair = iterator.next();//當還有下一筆資料時
            String key = keyValuePair.key;//取得key
            String value = keyValuePair.value; //取得value

            Log.v("hank", "Provinces:" + "key:" + key + "/value:" + value);

            provinceArrayList.add(value);
            wheelLeft.setMaximumWidthText("1234567890"); //設定滾輪Item文字的預設寬度
            wheelLeft.setData(provinceArrayList); //設定內容物
        }
        //物件,陣列,物件 =>  HashMap<1.String(key == 11000),2.value List<KeyValuePair<key:11000, value:北京市>>
        //解city{"110000":[{"key":"110000","value": "北京市" }],"120000": [{"key":"120000","value": "天津市" }]}

        //1.取得city解析好的資料
        cityMaps = AreaManager.getInstance(getBaseContext()).getProvinceCityHashMap();

        //讓cityMaps裡面的值,照訓續排序
        ArrayList<String> cityArrayList = new ArrayList<>(cityMaps.keySet());
        Log.v("hank", "ArrayList<String> citySort = new ArrayList<>(cityMaps.keySet():" + cityArrayList); //[370000, 620000, 320000, 110000...]
        Collections.sort(cityArrayList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int o1Int = Integer.parseInt(o1);
                int o2Int = Integer.parseInt(o2);
                return o1Int > o2Int ? 1 : -1;
            }
        });

        Iterator<String> iteratorCity2 = cityArrayList.iterator();
        while (iteratorCity2.hasNext()) {
            key = iteratorCity2.next();
            Log.v("hank", "key:" + key + " cityMaps.get(key):" + cityMaps.get(key));
        }

        //2.物件開頭Map結構撈出第一筆String (key欄位)
        for (Map.Entry<String, List<AreaManager.KeyValuePair<String, String>>> cityMap : cityMaps.entrySet()) {
            cityList = cityMap.getValue();
            Log.v("hank", "data:" + cityMap.getKey()); //data:110000 (亂數排序)

            //3.轉成蝶帶器準備撈List (value資料){}
            Iterator<AreaManager.KeyValuePair<String, String>> iteratorCity = cityList.iterator();
            Log.v("hank", "iteratorCity:" + iteratorCity);
            //4.撈KeyValuePair的 key:value資料
            while (iteratorCity.hasNext()) {
                cityValuePair = iteratorCity.next();
                String cityKey = cityValuePair.key;
                String cityValue = cityValuePair.value;

                Log.v("hank", "city =>" + "/cityKey:" + cityKey + "/cityValue:" + cityValue);
//                cityArrayList.add(cityValue);
//                wheelCenter.setMaximumWidthText("1234567890");
//                wheelCenter.setData(cityArrayList);

            }
        }


    }


}