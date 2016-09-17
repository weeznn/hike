package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nuc.hikeplus.R;


public class StorInfoFragment extends Fragment {

    private boolean isPer;
    private String storeNo;
    private String name;
    private String tel;
    private String address;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        isPer = bundle.getBoolean("isPer");
        if (isPer) {
            name = bundle.getString("name");
            tel = bundle.getString("tel");
            address = bundle.getString("address");
        } else {
            storeNo = bundle.getString("storeNo");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stor_info, container, false);
        RelativeLayout per = (RelativeLayout) view.findViewById(R.id.fgs_per);
        RelativeLayout sfBest = (RelativeLayout) view.findViewById(R.id.fgs_sfbest);

        if (isPer) {
            //per
            PerLayout(per,sfBest,view);
        } else {
            //sfbest
            StoreLayout(per,sfBest,view);
        }
        return view;
    }


    private void PerLayout(RelativeLayout per, RelativeLayout sf, View view) {

        per.setVisibility(View.VISIBLE);
        sf.setVisibility(View.GONE);

        TextView nameView = (TextView) view.findViewById(R.id.fgs_per_name);
        nameView.setText("姓名：" + name);
        nameView.setTextSize(20);


        TextView addressView = (TextView) view.findViewById(R.id.fgs_per_address);
        addressView.setText("收货地址：" + address);
        addressView.setTextSize(20);


        TextView telView = (TextView) view.findViewById(R.id.fgs_per_tel);
        telView.setText("联系电话：" + tel);
        telView.setTextSize(20);
    }


    private void StoreLayout(RelativeLayout per, RelativeLayout sf, View view) {

        final String storeNo = "晋1598";
        final String storeAdd = "太原市中北大学";
        final String olderName = "冯羽裳";
        final String olderTel = "15513020014";
        per.setVisibility(View.GONE);
        sf.setVisibility(View.VISIBLE);

        TextView storeNoView = (TextView) view.findViewById(R.id.fgs_sfBest_storeNo);
        storeNoView.setText("店铺编号：" + storeNo);
        storeNoView.setTextSize(20);

        TextView storeAddView = (TextView) view.findViewById(R.id.fgs_sfBest_address);
        storeAddView.setText("店铺地址：" + storeAdd);
        storeAddView.setTextSize(20);

        TextView storeOlderNameView = (TextView) view.findViewById(R.id.fgs_sfBest_olderName);
        storeOlderNameView.setText("店主姓名：" + olderName);
        storeOlderNameView.setTextSize(20);

        TextView storeOlderTelView = (TextView) view.findViewById(R.id.fgs_sfBest_olderTel);
        storeOlderTelView.setText("店主联系方式：" + olderTel);
        storeOlderTelView.setTextSize(20);
    }
}
