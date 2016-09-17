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
public class LoadingSFbestFragment extends Fragment{


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fagment_loading_sfbest,container,false);

        final EditText editText= (EditText) view.findViewById(R.id.loading_sfBest_storeNo);

        view.findViewById(R.id.loading_sfbest_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("isPer",false);
                intent.putExtra("name",editText.getText().toString());

                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

}
