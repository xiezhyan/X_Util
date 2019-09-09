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
	@GetMapping(value="/get/{id}")
	public Response getById(HttpServletRequest request, @PathVariable("id") <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {

		${table.javaName?cap_first}Vo ${table.javaName}Vo = ${table.javaName}Service.findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);

		return ${table.javaName}Vo != null ? new Response().success(${table.javaName}Vo) : new Response().failure();
	}

	@LogAnnotation(description = "删除数据")
	@DeleteMapping(value="/delete")
	public Response deleteById(HttpServletRequest request, @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		int result = ${table.javaName}Service.delete(${table.javaName}Vo);

		return result != 0 ? new Response().success() : new Response().failure();
	}

	@LogAnnotation(description = "分页查询数据")
	@GetMapping(value="/list")
	public Response findListByPager(HttpServletRequest request, ${table.javaName?cap_first}Vo ${table.javaName}Vo, Pagination pagination) {

		Pager<${table.javaName?cap_first}Vo> pager = ${table.javaName}Service.findListByPage(${table.javaName}Vo, pagination);

		return new Response().success(pager);
	}

	@LogAnnotation(description = "查询所有数据")
	@GetMapping(value="/all")
	public Response findList(HttpServletRequest request, ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		List<${table.javaName?cap_first}Vo> list = ${table.javaName}Service.findList(${table.javaName}Vo);

		return list != null ? new Response().success(list) : new Response().failure();
	}

	@LogAnnotation(description = "添加数据")
	@PostMapping(value="/save")
	public Response add(HttpServletRequest request, @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		int result = ${table.javaName}Service.save(${table.javaName}Vo);

		return result != 0 ? new Response().success() : new Response().failure();
	}

	@LogAnnotation(description = "通过ID修改数据")
	@PutMapping(value = "/update/{id}")
	public Response updateByKey(HttpServletRequest request, @RequestBody ${table.javaName?cap_first}Vo ${table.javaName}Vo, @PathVariable("id") <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {

		int result = ${table.javaName}Service.update(${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);

		return result != 0 ? new Response().success() : new Response().failure();
	}
}