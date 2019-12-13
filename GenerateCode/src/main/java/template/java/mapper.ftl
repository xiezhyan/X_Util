package ${mapperPackage};

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import com.sanq.product.config.utils.mvc.mapper.BaseMapper;

/**
 *	version: ${table.comment!""}
 * ----------------------
 * 	author: Mr.sanq
 * 	date: ${nowDate?string("yyyy-MM-dd")}
 */
public interface ${table.javaName?cap_first}Mapper extends BaseMapper<${table.javaName?cap_first}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType}</#if></#list>> {
	
	<#--int save(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--int delete(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--int update(${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--${table.javaName?cap_first}Vo findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>);-->
	<#---->
	<#--List<${table.javaName?cap_first}Vo> findList(@Param("${table.javaName}")  ${table.javaName?cap_first}Vo ${table.javaName}Vo);-->
	<#---->
	<#--List<${table.javaName?cap_first}Vo> findListByPage(@Param("${table.javaName}") ${table.javaName?cap_first}Vo ${table.javaName}Vo,@Param("startPage") int startPage, @Param("pageSize") int pageSize);-->
	<#---->
	<#--int findCount(@Param("${table.javaName}")  ${table.javaName?cap_first}Vo ${table.javaName}Vo);-->

	<#--void saveByList(@Param("${table.javaName}Vos") List<${table.javaName?cap_first}Vo> ${table.javaName}Vos);-->
}