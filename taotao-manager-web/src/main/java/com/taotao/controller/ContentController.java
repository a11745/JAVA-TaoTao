package com.taotao.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

/**
 * 
 * @author Administrator
 *
 */
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent content) throws Exception{
		TaotaoResult result = contentService.addContent(content);
		return result;
	}
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult listContent(Long categoryId,int page, int rows)throws Exception{
		EasyUIDataGridResult easyUIDataGridResult = contentService.listContent(categoryId, page, rows);
		
		return easyUIDataGridResult;
	}
	
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult updateContent(TbContent tbContent)throws Exception{
		TaotaoResult result = contentService.updateContent(tbContent);
		return result;
	}
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public TaotaoResult delContent(String ids)throws Exception{
		//System.out.println(ids);
		String[] strings = ids.split(",");
		List<Long> list = new ArrayList<>();
		for (String string : strings) {
			//System.out.println(Long.valueOf(string));
			list.add(Long.valueOf(string));
		}
		TaotaoResult result = contentService.delContent(list);
		return result;
	}
}
