package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	TaotaoResult addContent(TbContent content)throws Exception; 
	EasyUIDataGridResult listContent(Long categoryId,int page, int rows)throws Exception;
	TaotaoResult updateContent(TbContent content)throws Exception;
	TaotaoResult delContent(List<Long> ids)throws Exception;
	List<TbContent> getContentsByCid(long cid);
}
