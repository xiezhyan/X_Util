package ${serviceImplPackage};

import ${entityVoPackage}.${table.javaName?cap_first}Vo;
import com.sanq.product.config.utils.entity.Pager;
import com.sanq.product.config.utils.entity.Pagination;
import ${servicePackage}.${table.javaName?cap_first}Service;
import ${mapperPackage}.${table.javaName?cap_first}Mapper;
import java.util.List;
import java.math.BigDecimal;
import com.sanq.product.config.utils.mvc.utils.BeanCopyUtils;
import com.sanq.product.config.utils.mvc.utils.interfaces.BeanCopyCallback;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *	version: ${table.comment!""}
 *----------------------
 * 	author: Mr.sanq
 * 	date: ${nowDate?string("yyyy-MM-dd")}
 */
@Service("${table.javaName}Service")
public class ${table.javaName?cap_first}ServiceImpl implements ${table.javaName?cap_first}Service {

	@Resource
	private ${table.javaName?cap_first}Mapper ${table.javaName}Mapper;

	@Override
	public int save(${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		return ${table.javaName}Mapper.save(convertDO(${table.javaName}Vo));
	}

	@Override
	public int delete(${table.javaName?cap_first}Vo ${table.javaName}Vo) {

		return ${table.javaName}Mapper.delete(convertDO(${table.javaName}Vo));
	}

	@Override
	public int update(${table.javaName?cap_first}Vo ${table.javaName}Vo, <#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		${table.javaName?cap_first} old${table.javaName?cap_first} = findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>);

		if(null != old${table.javaName?cap_first}Vo && null != ${table.javaName}Vo) {
            convertDO(${table.javaName}Vo, old${table.javaName?cap_first});

			return ${table.javaName}Mapper.update(old${table.javaName?cap_first});
		}
		return 0;
	}

	@Override
	public ${table.javaName?cap_first}Vo findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType} ${field.javaField}</#if></#list>) {
		return convertEntity(${table.javaName}Mapper.findById(<#list table.fields as field><#if field.columnKey == "PRI">${field.javaField}</#if></#list>));
	}

	@Override
	public List<${table.javaName?cap_first}Vo> findList(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
        List<${table.javaName?cap_first}> datas = ${table.javaName}Mapper.findList(convertDO(${table.javaName}Vo));

		return convertEntityList(datas);
	}

	@Override
	public Pager<${table.javaName?cap_first}Vo> findListByPage(${table.javaName?cap_first}Vo ${table.javaName}Vo,Pagination pagination) {
		pagination.setTotalCount(findCount(${table.javaName}Vo));

		List<${table.javaName?cap_first}> datas = ${table.javaName}Mapper.findListByPage(convertDO(${table.javaName}Vo),pagination.getStartPage(),pagination.getPageSize());

		return new Pager<${table.javaName?cap_first}Vo>(pagination,convertEntityList(datas));
	}


	@Override
	public int findCount(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		return ${table.javaName}Mapper.findCount(convertDO(${table.javaName}Vo));
	}


	@Override
	public void saveByList(List<${table.javaName?cap_first}Vo> ${table.javaName}Vos) {
		${table.javaName}Mapper.saveByList(convertDOList(${table.javaName}Vos));
	}

	// vo 转 entity
	private ${table.javaName?cap_first} convertDO(${table.javaName?cap_first}Vo ${table.javaName}Vo) {
		${table.javaName?cap_first} ${table.javaName} = new ${table.javaName?cap_first}();
		BeanCopyUtils.copyProperties(${table.javaName}Vo, ${table.javaName}, (s, t) -> {});

		return ${table.javaName};
	}

    // list vo 转 list entity
    private List<${table.javaName?cap_first}> convertDOList(List<${table.javaName?cap_first}Vo> ${table.javaName}Vos) {

        return BeanCopyUtils.copyListProperties(${table.javaName}Vos, ${table.javaName?cap_first}::new, (s, t) -> {});
    }

	// entity 转 vo
    private ${table.javaName?cap_first}Vo convertEntity(${table.javaName?cap_first} ${table.javaName}) {
        ${table.javaName?cap_first}Vo ${table.javaName}Vo = new ${table.javaName?cap_first}Vo();
        BeanCopyUtils.copyProperties(${table.javaName}, ${table.javaName}Vo, (s, t) -> {});

        return ${table.javaName}Vo;
    }

    // list vo 转 list entity
    private List<${table.javaName?cap_first}Vo> convertEntityList(List<${table.javaName?cap_first}> ${table.javaName}s) {

        return BeanCopyUtils.copyListProperties(${table.javaName}s, ${table.javaName?cap_first}Vo::new, (s, t) -> {});
    }

}