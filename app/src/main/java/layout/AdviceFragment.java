package layout;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.nuc.hikeplus.R;


public class AdviceFragment extends Fragment {

    private EditText editText = null;
    private EditText editQQ = null;
    private boolean cancel;
    private View focusView;
    private ImageButton button = null;
    private Context context = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);
        editText = (EditText) view.findViewById(R.id.fga_editext);
        editQQ = (EditText) view.findViewById(R.id.fga_edit_qq);

        editQQ.setError(null);
        editText.setError(null);
        cancel = false;
        focusView = null;

        String qq = editQQ.getText().toString();
        String txt = editText.getText().toString();

        if (txt.isEmpty()) {
            editText.setError("请描述您的问题");
            cancel = true;
            focusView = editText;
        }
        if (qq.isEmpty()) {
            editQQ.setError("请填写您的qq号码");
            cancel = true;
            focusView = editQQ;
        }
        button = (ImageButton) view.findViewById(R.id.fga_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    editText.setText("");
                    new AlertDialog.Builder(context)
                            .setMessage("感谢您的反馈！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //结束本fragment
                                    FragmentManager fg = getFragmentManager();
                                    fg.popBackStack();
                                }
                            })
                            .show();
                }
            }
        });
        return view;
    }

}
