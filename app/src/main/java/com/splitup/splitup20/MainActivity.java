package com.splitup.splitup20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.snowdream.android.widget.SmartImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ListView storeList;
    //Setting the Arrays variables
    ArrayList icon = new ArrayList();
    ArrayList title = new ArrayList();
    ArrayList description = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storeList =(ListView)findViewById(R.id.StoreList);
        DownloadImage();

        storeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent IntentMenuList = new Intent(view.getContext(),MenuActivity.class);
                TextView v = (TextView)view.findViewById(R.id.textviewtitle);
                IntentMenuList.putExtra("BarName",v.getText().toString());
                startActivity(IntentMenuList);
            }
        });

            }

    private void DownloadImage(){
        icon.clear();
        title.clear();
        description.clear();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        String urldatajasonbarlist ="http://sebastian.96.lt/Barapp.php";
        client.get(urldatajasonbarlist, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            if(statusCode==200){
                progressDialog.dismiss();
                String descriptionString;
                try {
                    JSONArray jsonArray = new JSONArray(new String(responseBody));
                    for (int i=0; i<jsonArray.length();i++){
                        title.add(jsonArray.getJSONObject(i).get("Name"));
                        descriptionString="Address: "+ jsonArray.getJSONObject(i).get("Address");
                        description.add(descriptionString);
                        icon.add(jsonArray.getJSONObject(i).get("Type"));

                    }

                    storeList.setAdapter(new ImageAdapter(getApplicationContext()));


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
    private class ImageAdapter extends BaseAdapter{
        Context ctx;
        LayoutInflater layoutInflater;
        SmartImageView smartImageView;
        TextView textviewtitle, textviewdescription;

        public ImageAdapter(Context applicationContext) {
            this.ctx = applicationContext;
            layoutInflater =(LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return icon.size();
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
            ViewGroup viewGroup = (ViewGroup)layoutInflater.inflate(R.layout.activity_main_item, null);

            smartImageView=(SmartImageView)viewGroup.findViewById(R.id.Icon);
            textviewtitle=(TextView)viewGroup.findViewById(R.id.textviewtitle);
            textviewdescription=(TextView)viewGroup.findViewById(R.id.textviewdescription);

            String imagesurl= "http://sebastian.96.lt/Images/"+icon.get(position).toString() ;
            Rect rect = new Rect(smartImageView.getLeft(), smartImageView.getTop(),smartImageView.getRight(), smartImageView.getBottom());

            smartImageView.setImageUrl(imagesurl, rect);

            textviewtitle.setText(title.get(position).toString());
            textviewdescription.setText(description.get(position).toString());

            return viewGroup;
        }
    }

    }
