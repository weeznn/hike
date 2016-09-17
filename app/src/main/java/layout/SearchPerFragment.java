package layout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nuc.hikeplus.DBTool;
import com.nuc.hikeplus.OlderInfoActivity;
import com.nuc.hikeplus.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weeznn on 2016/9/2.
 */
public class SearchPerFragment extends Fragment {
    private static final String TAG = "SearchPerFragment";

    private ListView listView = null;
    private SimpleAdapter adapter = null;
    private DBTool dbTool = null;
    private Cursor cursor = null;

    private List<String> orderNoList = null;
    private List<String> logisticList = null;
    private List<Map<String, Object>> olderInfo = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "oncreat");

        dbTool = new DBTool(getContext());
        cursor = dbTool.searchDB();
        List<String> goodsList = new ArrayList<>();
        orderNoList = new ArrayList<>();
        logisticList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                orderNoList.add(cursor.getString(0));
                goodsList.add(cursor.getString(7));
                logisticList.add(cursor.getString(8));
                Log.i(TAG, cursor.getString(0) + cursor.getString(7) + cursor.getString(8));
            }
        }

        //Log.i(TAG, "商品名" + goodsList + "");


        olderInfo = new ArrayList<>();
        for (int i = 0; i < goodsList.size(); i++) {
            Map perOrderInfo = new HashMap<>();
            Cursor cursorGood = dbTool.searchGoodsFromName(goodsList.get(i));

            Log.i(TAG, "search " + goodsList.get(i));

            if (cursorGood.moveToFirst()) {
                byte[] imageByte = cursorGood.getBlob(8);
                perOrderInfo.put("image", imageByte);
                perOrderInfo.put("goodName", cursorGood.getString(0));
                perOrderInfo.put("goodInfo", cursorGood.getString(9));
                olderInfo.add(perOrderInfo);

                Log.i(TAG, "商品名+信息" + cursorGood.getString(0) + cursorGood.getString(9));
            }

        }

        adapter = new SimpleAdapter(getContext(), olderInfo, R.layout.list_item_per,
                new String[]{"image", "goodName", "goodInfo"},
                new int[]{R.id.search_per_image, R.id.search_per_good_name, R.id.search_per_good_info});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && R.id.search_per_image == view.getId()) {
                    Log.i(TAG, view.getId() + "");
                    byte[] image_byte= (byte[]) data;
                    Bitmap image_bitmap = BitmapFactory.decodeByteArray(image_byte,0,image_byte.length);
                    ((ImageView) view).setImageBitmap(image_bitmap);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_per, container, false);
        listView = (ListView) view.findViewById(R.id.fgs_per_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, position + "");

                Intent intent = new Intent(getActivity(), OlderInfoActivity.class);
                intent.putExtra("orderNo", orderNoList.get(position));
                intent.putExtra("logistic", logisticList.get(position));

                Log.i(TAG, orderNoList.get(position) + logisticList.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        dbTool.close();
    }
}


