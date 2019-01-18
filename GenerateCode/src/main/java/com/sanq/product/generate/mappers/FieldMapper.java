package com.sanq.product.generate.mappers;

import com.sanq.product.generate.entity.Fields;

import java.util.List;
import java.util.Map;

public interface FieldMapper {

	List<Fields> findFieldByTable(Map<String, String> map);
}
