package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	TbItem getItemById(long itemId) throws Exception;
	EasyUIDataGridResult getItemList(int page,int rows)throws Exception;
	TaotaoResult addItem(TbItem item,String desc) throws Exception;
	TbItemDesc geTbItemDescById(long itemId);
}
