package cx.companysign.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cx.companysign.bean.NameHeader;
import cx.companysign.view.cell.SortLabelView;

/**
 * Created by cxcxk on 2016/10/15.
 */
public abstract class SortListAdapter extends BaseAdapter {
    public static class Type {
        public static int TYPE_LABLE = 0;
        public static int TYPE_CONTENT = 1;
        public static int TYPE_HEADER_CONTENT = 2;
    }

    protected Context mContext;
    private List<LineType> sortContent = new ArrayList<>();
    private List<NameHeader> sortContentNameHeader = new ArrayList<>();
    //List<String> headChars = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
    private Map<String, List<NameHeader>> sortedMap = new TreeMap<>();
    //SparseArray<Integer> headPos = new SparseArray<>();
    List<String> whichChars = new ArrayList<>();

    public SortListAdapter(Context context, List<NameHeader> SortContents) {
        handleSortConent(SortContents);
        this.mContext = context;
    }

    public int getLineType(int position) {
        return getItemViewType(position);
    }

    public void refreshSortData(List<NameHeader> sortNewContent) {
        handleSortConent(sortNewContent);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sortContent.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != Type.TYPE_LABLE;
    }

    @Override
    public int getItemViewType(int position) {
        if (sortContent.get(position).getType() == LineType.LABLE) {
            return 0;
        } else if (sortContent.get(position).getType() == LineType.CONTENT) {
            if (sortContentNameHeader.get(position).getHeader() != null && !sortContentNameHeader.get(position).getHeader().equals("")) {
                return 1;
            } else {
                return 2;
            }
        }
        return 3;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LineType lineType = sortContent.get(position);
        int viewtype = getItemViewType(position);
        if (viewtype == 0) {
            if (convertView == null) {
                convertView = new SortLabelView(mContext);
            }
            SortLabelView view = (SortLabelView) convertView;
            view.setText(lineType.getContent());
        } else {
            convertView = contentView(sortContentNameHeader.get(position), position, convertView, parent);
        }

        return convertView;
    }

    public abstract View contentView(NameHeader header, int position, View convertView, ViewGroup parent);
        /*public abstract int getItemViewTypeM(int position);
        public abstract int getViewTypeCountM();*/

    private void handleSortConent(List<NameHeader> sortContent) {

        try {
            handleHanziToPinyin(sortContent);
            for (String key : sortedMap.keySet()) {
                LineType lineType = new LineType();
                lineType.setType(LineType.LABLE);
                lineType.setContent(key);
                this.sortContent.add(lineType);
                whichChars.add(key);
                sortContentNameHeader.add(null);
                for (NameHeader header : sortedMap.get(key)) {
                    String content = header.getName();
                    LineType lineType1 = new LineType();
                    lineType1.setType(LineType.CONTENT);
                    lineType1.setContent(content);
                    this.sortContent.add(lineType1);
                    sortContentNameHeader.add(header);
                }
            }
        } catch (PinyinException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void handleHanziToPinyin(List<NameHeader> sortContent) throws PinyinException {

        for (NameHeader header : sortContent) {
            String content = header.getName();
            String pinyin = PinyinHelper.getShortPinyin(content).substring(0, 1).toUpperCase();
            List<NameHeader> list = sortedMap.get(pinyin);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(header);
            sortedMap.put(pinyin, list);
        }
    }

    public List<String> getWhichChars() {
        return whichChars;
    }

    public int getPositionOnList(String headChar) {
        for (int i = 0; i < sortContent.size(); i++) {
            if (sortContent.get(i).getContent().equals(headChar)) {
                return i;
            }
        }
        return -1;
    }

    public String getContentOnList(int position) {
        return sortContent.get(position).getContent();
    }
}
