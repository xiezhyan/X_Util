package ${controllerPackage!""};

import org.springframework.web.bind.annotation.PathVariable;

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import ${servicePackage}.${table.javaName?cap_first}Service;

import com.sanq.product.config.utils.annotation.LogAnnotation;
import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import com.sanq.product.config.utils.entity.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/${table.name}")
public class ${table.javaName?cap_first}Controller {

	@Resource
	private ${table.javaName?cap_first}Service ${table.javaName}Service;

	@LogAnnotation(description = "通过ID得到详情")
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public Response getById(HttpServletRequest request, @PathVariable("id") <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		${table.javaName?cap_first}Vo ${table.javaName}Vo = ${table.javaName}Service.findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);

		return ${table.javaName}Vo != null ? new Response().success(${table.javaName}Vo) : new Response().failure();
	}

	@LogAnnotation(description = "删除数据")
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	public Response deleteById(HttpServletRequest request, @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		int result = ${table.javaName}Service.delete(${table.javaName}Vo);
		return result == 1 ? new Response().success() : new Response().failure();
	}

	@LogAnnotation(description = "分页查询数据")
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Response findListByPager(HttpServletRequest request, ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		Pagination pagination = new Pagination();
		if(null != ${table.javaName}Vo) {
			pagination.setPageSize(${table.javaName}Vo.getPageSize());
			pagination.setCurrentIndex(${table.javaName}Vo.getCurrentIndex());
		}
		Pager<${table.javaName?cap_first}Vo> pager = ${table.javaName}Service.findListByPage(${table.javaName}Vo, pagination);

		return pager != null ? new Response().success(pager) : new Response().failure();
	}

	@LogAnnotation(description = "查询所有数据")
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public Response findList(HttpServletRequest request, ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		List<${table.javaName?cap_first}Vo> list = ${table.javaName}Service.findList(${table.javaName}Vo);
		return list != null ? new Response().success(list) : new Response().failure();
	}

	@LogAnnotation(description = "添加数据")
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public Response add(HttpServletRequest request, @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		int result = ${table.javaName}Service.save(${table.javaName}Vo);

		return result == 1 ? new Response().success() : new Response().failure();
	}

	@LogAnnotation(description = "通过ID修改数据")
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT)
	public Response updateByKey(HttpServletRequest request,
        @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo,
        @PathVariable("id") <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {

		int result = ${table.javaName}Service.update(${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);

		return result == 1 ? new Response().success() : new Response().failure();
	}
}