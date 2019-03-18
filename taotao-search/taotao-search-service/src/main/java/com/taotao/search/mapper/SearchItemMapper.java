package com.taotao.search.mapper;

import java.util.List;

import com.taotao.common.pojo.SearchItem;

public interface SearchItemMapper {
	List<SearchItem> getItemList() throws Exception;
	SearchItem getItemById(long itemId) throws Exception;
	
}
