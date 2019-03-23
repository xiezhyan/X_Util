package ${serviceImplPackage};

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import ${servicePackage}.${table.javaName?cap_first}Service;
import ${mapperPackage}.${table.javaName?cap_first}Mapper;
import java.util.List;
import java.math.BigDecimal;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;


@Service("${table.javaName}Service")
public class ${table.javaName?cap_first}ServiceImpl implements ${table.javaName?cap_first}Service {

	@Resource
	private ${table.javaName?cap_first}Mapper ${table.javaName}Mapper;
	
	@Override
	public int save(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		return ${table.javaName}Mapper.save(${table.javaName}Vo);
	}
	
	@Override
	public int delete(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		return ${table.javaName}Mapper.delete(${table.javaName}Vo);
	}	
	
	@Override
	public int update(${table.javaName?cap_first}Vo ${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		${table.javaName?cap_first}Vo old${table.javaName?cap_first}Vo = findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);
		
		if(null != ${table.javaName}Vo) {
			<#list table.fields as field>
			if(null != ${table.javaName}Vo.get${field.javaField?cap_first!""}()) {
				old${table.javaName?cap_first}Vo.set${field.javaField?cap_first!""}(${table.javaName}Vo.get${field.javaField?cap_first!""}());
			}
			</#list>
			return ${table.javaName}Mapper.update(old${table.javaName?cap_first}Vo);
		}
		return 0;
	}
	
	@Override
	public ${table.javaName?cap_first}Vo findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		return ${table.javaName}Mapper.findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);
	}
	
	@Override
	public List<${table.javaName?cap_first}Vo> findList(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		return ${table.javaName}Mapper.findList(${table.javaName}Vo);
	}
	
	@Override
	public Pager<${table.javaName?cap_first}Vo> findListByPage(${table.javaName?cap_first}Vo ${table.javaName}Vo,Pagination pagination) {
		pagination.setTotalCount(findCount(${table.javaName}Vo));
		
		List<${table.javaName?cap_first}Vo> datas = ${table.javaName}Mapper.findListByPage(${table.javaName}Vo,pagination.getStartPage(),pagination.getPageSize());
		
		return new Pager<${table.javaName?cap_first}Vo>(pagination,datas);
	}
	
	@Override
	public int findCount(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		return ${table.javaName}Mapper.findCount(${table.javaName}Vo);
	}
	
	@Override
	public void saveByList(List<${table.javaName?cap_first}Vo> ${table.javaName}Vos) {
		${table.javaName}Mapper.saveByList(${table.javaName}Vos);
	}
}