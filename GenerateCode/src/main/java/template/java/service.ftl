package ${servicePackage};

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import com.sanq.product.config.utils.mvc.service.BaseService;

/**
 *	version: ${table.comment!""}
 *----------------------
 * 	author: Mr.sanq
 * 	date: ${nowDate?string("yyyy-MM-dd")}
 */
public interface ${table.javaName?cap_first}Service extends BaseService<${table.javaName?cap_first}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType}</#if></#list>> {

	<#--int save(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--int delete(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--int update(${table.javaName?cap_first}Vo ${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>);-->
	<#---->
	<#--${table.javaName?cap_first}Vo findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>);-->
	<#---->
	<#--List<${table.javaName?cap_first}Vo> findList(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--Pager<${table.javaName?cap_first}Vo> findListByPage(${table.javaName?cap_first}Vo ${table.javaName}Vo,Pagination pagination);-->
	<#---->
	<#--int findCount(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--void saveByList(List<${table.javaName?cap_first}Vo> ${table.javaName}Vos);-->
}