package com.splitup.splitup20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Sebas on 23/10/2016.
 */

public class MenuActivity extends AppCompatActivity {

    TextView BarName;
    private ListView menuList;
    private ListView orderList;

    ArrayList ProductName = new ArrayList();
    ArrayList ProductPrice = new ArrayList();
    ArrayList Total= new ArrayList();
   // Button add;
   // Button delete;



    @Override
    protected void onCreate (Bundle savedInstaceState){
       super.onCreate(savedInstaceState);
       setContentView(R.layout.activity_menu);
       BarName = (TextView) findViewById(R.id.BarName);

       Intent IntentMenuList = getIntent();
       Bundle BundleNombreBar = IntentMenuList.getExtras();


        if(BundleNombreBar!=null){
            String BarIntentName = (String) BundleNombreBar.get("BarName").toString();
            BarName.setText(BarIntentName);
        }
        menuList =(ListView)findViewById(R.id.MenuList);
        menuList.setClickable(true);
        orderList =(ListView)findViewById(R.id.OrderList);
        DownloadImage();
       // add = (Button)findViewById(R.id.Add);
        //delete =(Button)findViewById(R.id.Delete);
        //add.setOnClickListener(this);
        //delete.setOnClickListener(this);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView v = (TextView)view.findViewById(R.id.textviewname);
                Button add =(Button)view.findViewById(R.id.Add);
                Button delete =(Button)view.findViewById(R.id.Delete);
                add.setOnClickListener((View.OnClickListener) getApplicationContext());
                delete.setOnClickListener((View.OnClickListener) getApplicationContext());

                switch (view.getId()){
                    case R.id.Add:
                        Total.add(v);
                        break;
                    case R.id.Delete:
                        break;
                }
                }



            });

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Total);
        //orderList.setAdapter(adapter);

    }

    private void DownloadImage(){
        ProductName.clear();
        ProductPrice.clear();
        Total.clear();


        final ProgressDialog progressDialog = new ProgressDialog(MenuActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String urlDataJasonProductsList ="http://sebastian.96.lt/ProductsApp.php";
        client.get(urlDataJasonProductsList, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    progressDialog.dismiss();
                    String priceString;
                    String priceAux;
                    String nameAux;
                    String storeNameAux;
                    try {
                        JSONArray jsonArray = new JSONArray(new String(responseBody));

                        for (int i=0; i<jsonArray.length();i++) {

                            //storeNameAux = jsonArray.getJSONObject(i).get("Stores_Name").toString();
                            Intent IntentMenuList = getIntent();
                            Bundle BundleNombreBar = IntentMenuList.getExtras();
                            String BarIntentName = new String();

                            if(BundleNombreBar!=null){
                             BarIntentName = BundleNombreBar.get("BarName").toString();
                                                                                          }

                            if(jsonArray.getJSONObject(i).get("Stores_Name").toString().equals(BarIntentName)) {
                                nameAux = jsonArray.getJSONObject(i).get("Name").toString();
                                priceAux = jsonArray.getJSONObject(i).get("Price").toString();
                                ProductName.add(nameAux);
                                priceString = "$" + priceAux;
                                ProductPrice.add(priceString);
                            }

                        }

                        menuList.setAdapter(new PriceAdapter(getApplicationContext()));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }



    private class PriceAdapter extends BaseAdapter{
        Context ctx;
        LayoutInflater layoutInflater;
        TextView textviewname, textviewprice;

        public PriceAdapter(Context applicationContext) {
            this.ctx = applicationContext;
            layoutInflater =(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            int count=0;
           for (int i=0; i<ProductName.size();i++){
            if (ProductName.get(i)!=null){
                count=count+1;
            }}
            return count;

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.activity_menu_price, null);

            textviewname=(TextView)viewGroup.findViewById(R.id.textviewname);
            textviewprice=(TextView)viewGroup.findViewById(R.id.textviewprice);

            textviewname.setText(ProductName.get(position).toString());
            textviewprice.setText(ProductPrice.get(position).toString());

            return viewGroup;
        }
    }


}
