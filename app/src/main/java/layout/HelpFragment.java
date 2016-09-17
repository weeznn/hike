package layout;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuc.hikeplus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_help,container,false);
        TextView txt= (TextView) view.findViewById(R.id.fgh_text_view);
        String s="如您对该产品有任何问题，请联系郝文章！\n              联系方式：798191224（qq）";
        txt.setText(s);
        return view;
    }

}
