package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.utils.JsonUtils;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	
	@Override
	public TaotaoResult addContent(TbContent content) throws Exception {
		//补全pojo属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入
		contentMapper.insert(content);
		//同步缓存
		//删除对应cid的缓存信息
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	@Override
	public EasyUIDataGridResult listContent(Long categoryId,int page, int rows) throws Exception {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		//取查询结果
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		easyUIDataGridResult.setRows(list);
		
		return easyUIDataGridResult;
	}

	@Override
	public TaotaoResult updateContent(TbContent content) throws Exception {
		contentMapper.updateByPrimaryKey(content);
		//同步缓存
		//删除对应cid的缓存信息
		jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult delContent(List<Long> ids) throws Exception {
		/*TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdIn(ids);
		contentMapper.deleteByExample(example);*/
		for (Long long1 : ids) {
			
			TbContent content = contentMapper.selectByPrimaryKey(long1);
			contentMapper.deleteByPrimaryKey(long1);
			try {
				//查询缓存
				String json = jedisClient.hget(INDEX_CONTENT, content.getCategoryId().toString());
				//查询到结果，把json转换成List返回
				if (StringUtils.isNotBlank(json)) {
					//不为空
					//同步缓存
					//删除对应cid的缓存信息
					jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return TaotaoResult.ok();
	}

	@Override
	public List<TbContent> getContentsByCid(long cid) {
		//先查询缓存
		//添加缓存不能影响正常业务逻辑
		try {
			//查询缓存
			String json = jedisClient.hget(INDEX_CONTENT, cid+"");
			//查询到结果，把json转换成List返回
			if (StringUtils.isNotBlank(json)) {
				//不为空
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，需要查询数据库
		TbContentExample example  =new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		//把结果添加到缓存
		try {
			jedisClient.hset(INDEX_CONTENT, cid+"", JsonUtils.objectToJson(list));
			System.out.println("添加缓存成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		//返回结果
		return list;
	}

}
