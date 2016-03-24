package com.xiaoming.listandfragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Bean> beans;
    private ListView listView;
    private FrameLayout frameLayout;

    private ViewGroup container;
    /**
     * 标签之间的间距 px
     */
    final int itemMargins = 50;

    /**
     * 标签的行间距 px
     */
    final int lineMargins = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        frameLayout = (FrameLayout) findViewById(R.id.fragment);
        container = (ViewGroup) findViewById(R.id.container);
        beans = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Bean bean = new Bean();
            bean.setName(new Random().nextInt(100) + "");
            ArrayList<Bean.SubBean> subBeans = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                Bean.SubBean subBean = bean.new SubBean();
                subBean.setName(new Random().nextInt(100) + "");
                ArrayList<String> tags = new ArrayList<>();
                for (int k = 0; k < 10; k++) {
                    tags.add(new Random().nextInt(100) + "");
                }
                subBean.setTags(tags);
                subBeans.add(subBean);
                bean.setSubBeans(subBeans);
            }
            beans.add(bean);
        }
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return beans.size();
            }

            @Override
            public Object getItem(int position) {
                return beans.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = new TextView(MainActivity.this);
                textView.setTextSize(30);
                textView.setBackgroundColor(Color.RED);
                textView.setText(beans.get(position).getName());
                return textView;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bean bean = beans.get(position);
                container.removeAllViews();
                List<Bean.SubBean> subBeans = bean.getSubBeans();
                for (Bean.SubBean sub :
                        subBeans) {
                    showTags(sub);
                }
                int childCount = parent.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = parent.getChildAt(i);
                    childAt.setBackgroundColor(Color.RED);
                }
                view.setBackgroundColor(Color.WHITE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Bean bean = beans.get(0);
//        List<Bean.SubBean> subBeans = bean.getSubBeans();
//        for (Bean.SubBean sub :
//                subBeans) {
//            showTags(sub);
//        }
    }

    public void showTags(Bean.SubBean sub) {
        // add a title tag
        LinearLayout layoutTitle = new LinearLayout(this);
        layoutTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layoutTitle.setOrientation(LinearLayout.HORIZONTAL);
        TextView tvTitle = new TextView(this);
        tvTitle.setTextSize(26);
        tvTitle.setText(sub.getName());
        layoutTitle.addView(tvTitle);
        container.addView(layoutTitle);


        final int containerWidth = container.getMeasuredWidth() - container.getPaddingRight()
                - container.getPaddingLeft();

        final LayoutInflater inflater = getLayoutInflater();

        /** 用来测量字符的宽度 */
        final Paint paint = new Paint();
        final TextView textView = new TextView(this);
        textView.setBackgroundColor(Color.RED);
        final int itemPadding = textView.getCompoundPaddingLeft() + textView.getCompoundPaddingRight();
        final LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tvParams.setMargins(0, 0, itemMargins, 0);

        paint.setTextSize(textView.getTextSize());

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        container.addView(layout);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, lineMargins, 0, 0);

        /** 一行剩下的空间 **/
        int remainWidth = containerWidth;

        Object[] tags = sub.getTags().toArray();
        for (int i = 0; i < tags.length; ++i) {
            final String text = (String) tags[i];
            final float itemWidth = paint.measureText(text) + itemPadding;
            if (remainWidth > itemWidth) {
                addItemView(inflater, layout, tvParams, text);
            } else {
                resetTextViewMarginsRight(layout);
                layout = new LinearLayout(this);
                layout.setLayoutParams(params);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                /** 将前面那一个textview加入新的一行 */
                addItemView(inflater, layout, tvParams, text);
                container.addView(layout);
                remainWidth = containerWidth;
            }
            remainWidth = (int) (remainWidth - itemWidth + 0.5f) - itemMargins;
        }
        resetTextViewMarginsRight(layout);
    }

    /*****************
     * 将每行最后一个textview的MarginsRight去掉
     *********************************/
    private void resetTextViewMarginsRight(ViewGroup viewGroup) {
        final TextView tempTextView = (TextView) viewGroup.getChildAt(viewGroup.getChildCount() - 1);
        tempTextView
                .setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void addItemView(LayoutInflater inflater, ViewGroup viewGroup, ViewGroup.LayoutParams params, String text) {
        final TextView tvItem = new TextView(this);
        tvItem.setBackgroundColor(Color.RED);
        tvItem.setText(text);
        tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, ((TextView) v).getText(), Toast.LENGTH_LONG).show();
            }
        });
        viewGroup.addView(tvItem, params);
    }


}
