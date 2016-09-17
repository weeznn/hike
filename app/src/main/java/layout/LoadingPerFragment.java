package layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nuc.hikeplus.MainActivity;
import com.nuc.hikeplus.R;

/**
 * Created by weeznn on 2016/9/1.
 */
public class LoadingPerFragment extends Fragment {

    private EditText name,tel,address;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading_per, container, false);
        name = (EditText) view.findViewById(R.id.loading_per_name);
        tel = (EditText) view.findViewById(R.id.loading_per_tel);
        address = (EditText) view.findViewById(R.id.loading_per_address);

        view.findViewById(R.id.loading_per_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("isPer",true);
                intent.putExtra("name",name.getText().toString());
                intent.putExtra("tel",tel.getText().toString());
                intent.putExtra("address",address.getText().toString());

                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }
}
