package com.xiaomai.shopping.module.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragment;

/**
 * 消息页面
 * 
 * @author XiaoMai
 *
 */
public class XiaoXiFragment extends BaseFragment {

	private View back;
	private TextView title;
	private View share;
	
	private GridView gridView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_fenlei, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		
		//隐藏返回
		back = view.findViewById(R.id.title_back);
		back.setVisibility(View.GONE);
		//设置标题
		title = (TextView)view.findViewById(R.id.title_title);
		title.setText("消息列表");
		//隐藏分享
		share = view.findViewById(R.id.title_share);
		share.setVisibility(View.GONE);
		
//		gridView = (GridView) view.findViewById(R.id.fenlei_gridView);
//		data = new ArrayList<Map<String, Object>>();
//		for (int i = 0; i < 10; i++) {
//			Map<String, Object> map = new HashMap<>();
//			map.put("image_url", R.drawable.ic_launcher);
//			map.put("name", "图书");
//			data.add(map);
//		}
//		adapter = new SimpleAdapter(getContext(), data, R.layout.item_fenlei,
//				new String[] { "image_url", "name" }, new int[] {
//						R.id.item_fenlei_image, R.id.item_fenlei_name });
//		gridView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {

	}

}
