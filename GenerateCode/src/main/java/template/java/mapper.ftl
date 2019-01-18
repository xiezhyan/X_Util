package ${mapperPackage};

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ${table.javaName?cap_first}Mapper {
	
	int save(${table.javaName?cap_first}Vo ${table.javaName}Vo);
	
	int delete(${table.javaName?cap_first}Vo ${table.javaName}Vo);
	
	int update(${table.javaName?cap_first}Vo ${table.javaName}Vo);
	
	${table.javaName?cap_first}Vo findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>);
	
	List<${table.javaName?cap_first}Vo> findList(@Param("${table.javaName}")  ${table.javaName?cap_first}Vo ${table.javaName}Vo);
	
	List<${table.javaName?cap_first}Vo> findListByPage(@Param("${table.javaName}") ${table.javaName?cap_first}Vo ${table.javaName}Vo,@Param("startPage") int startPage, @Param("pageSize") int pageSize);
	
	int findCount(@Param("${table.javaName}")  ${table.javaName?cap_first}Vo ${table.javaName}Vo);

	void saveByList(@Param("${table.javaName}Vos") List<${table.javaName?cap_first}Vo> ${table.javaName}Vos);
}