package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
/**
 * 内容分类管理service
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) throws Exception {
		//根据parentId查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> selectByExample = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : selectByExample) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
		return resultList;
	}
	
	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) throws Exception {
		//创建一个pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//补全对象的属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
			//状态 1正常 2删除
		contentCategory.setStatus(1);
			//排序  默认为1
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库
		contentCategoryMapper.insert(contentCategory);
		//判断父节点的状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			//如果父节点为叶子结点应该改为父节点
			parent.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		//返回结果
		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(Long id, String name) throws Exception {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		tbContentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult delContentCategory(Long id) throws Exception {
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		if (!tbContentCategory.getIsParent()) {
			//如果是叶子结点直接删除
			contentCategoryMapper.deleteByPrimaryKey(id);
		}else {
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			contentCategoryMapper.deleteByExample(example);
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
		Long parentId = tbContentCategory.getParentId();
		TbContentCategory parentContentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		TbContentCategoryExample parentExample = new TbContentCategoryExample();
		Criteria parentCriteria = parentExample.createCriteria();
		parentCriteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(parentExample);
		if (list.size()==0) {
			parentContentCategory.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parentContentCategory);
		}
		
		return TaotaoResult.ok();
	}
	
	

}
