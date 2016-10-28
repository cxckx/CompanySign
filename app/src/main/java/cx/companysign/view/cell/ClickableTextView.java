package cx.companysign.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by cxcxk on 2016/10/12.
 */
public class ClickableTextView extends TextView implements View.OnClickListener {


    public ClickableTextView(Context context) {
        super(context);
        init();
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    private void init(){
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(iOnClick != null){
            iOnClick.click(this.getText().toString());
        }
    }

    private IOnClick  iOnClick;

    public void setiOnClick(IOnClick iOnClick) {
        this.iOnClick = iOnClick;
    }

    public interface IOnClick{
        void click(String text);
    }
}
