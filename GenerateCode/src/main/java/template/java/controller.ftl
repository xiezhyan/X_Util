package ${controllerPackage!""};

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import ${servicePackage}.${table.javaName?cap_first}Service;
import com.xiezhyan.utils.string.StringUtil;

import com.xiezhyan.utils.entity.Response;
import com.xiezhyan.utils.entity.Pager;
import com.xiezhyan.utils.entity.Pagination;
import com.xiezhyan.utils.annotation.LogAnnotation;

import java.util.List;

@RestController
@RequestMapping("/api/${table.name}")
public class ${table.javaName?cap_first}Controller {

	@Resource
	private ${table.javaName?cap_first}Service ${table.javaName}Service;

	@LogAnnotation(description = "通过ID得到详情")
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public Response getById(HttpServletRequest request, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		${table.javaName?cap_first}Vo ${table.javaName}Vo = ${table.javaName}Service.findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);
		
		return ${table.javaName}Vo != null ? new Response().success(${table.javaName}Vo) : new Response().failure();
	}
	
	@LogAnnotation(description = "通过ID删除")
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public Response deleteById(HttpServletRequest request, ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		int result = ${table.javaName}Service.delete(${table.javaName}Vo);
		return result == 1 ? new Response().success() : new Response().failure();
	}
	
	@LogAnnotation(description = "分页查询数据")
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public Response findList(HttpServletRequest request,${table.javaName?cap_first}Vo ${table.javaName}Vo,
		Pagination pagination,
		@RequestParam(value="page", required=false, defaultValue="0") int page,
		@RequestParam(value="limit", required=false, defaultValue="10") int limit) {
			
		pagination.setPageSize(limit);
		pagination.setCurrentIndex(page);
		
		Pager<${table.javaName?cap_first}Vo> pager = ${table.javaName}Service.findListByPage(${table.javaName}Vo, pagination);
		return pager != null ? new Response().success(pager) : new Response().failure();
	}
	
	@LogAnnotation(description = "查询所有数据")
	@RequestMapping(value="/all",method=RequestMethod.POST)
	public Response findList(HttpServletRequest request,${table.javaName?cap_first}Vo ${table.javaName}Vo) {
			
		List<${table.javaName?cap_first}Vo> list = ${table.javaName}Service.findList(${table.javaName}Vo);
		return list != null ? new Response().success(list) : new Response().failure();
	}
	
	@LogAnnotation(description = "添加数据")
	@RequestMapping(value="/saveOrUpdate",method=RequestMethod.POST)
	public Response add(HttpServletRequest request,${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		if(${table.javaName}Vo.get<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField?cap_first}()</#if></#list> != null && ${table.javaName}Vo.get<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField?cap_first}()</#if></#list> != 0) {

			int result = ${table.javaName}Service.update(${table.javaName}Vo, ${table.javaName}Vo.get<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField?cap_first}()</#if></#list>);
        	return result == 1 ? new Response().success() : new Response().failure();
        }
		int result = ${table.javaName}Service.save(${table.javaName}Vo);
		
		return result == 1 ? new Response().success() : new Response().failure();
	}
	
	@LogAnnotation(description = "通过ID修改数据")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	public Response updateByKey(HttpServletRequest request,${table.javaName?cap_first}Vo ${table.javaName}Vo,<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {

		int result = ${table.javaName}Service.update(${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);
		
		return result == 1 ? new Response().success() : new Response().failure();
	}
}