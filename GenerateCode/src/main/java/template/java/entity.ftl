package ${entityPackage!""};

import java.io.Serializable;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import com.sanq.product.config.utils.mvc.entity.Base;

public class ${table.javaName?cap_first!""} extends Base<<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType}</#if></#list>> implements Serializable {

/**
 *    version: ${table.comment!""}
 *----------------------
 *    author: Mr.sanq
 *    date: ${nowDate?string("yyyy-MM-dd")}
 */
private static final long serialVersionUID = 1L;

<#if table.fields?? && table.fields?size gt 0>
    <#list table.fields as field>
    <#if (field.javaField != "id"  && field.javaField != "isDelete"
    && field.javaField != "createTime" && field.javaField != "updateTime"
    && field.javaField != "deleteTime"
    && field.javaField != "creater" && field.javaField != "updater"
    && field.javaField != "deleter" && field.javaField != "appertainId")>
    /**${field.columnComment!""}*/
    <#if field.javaType == "Date">
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    </#if>
    private ${field.javaType!""} ${field.javaField!""};
    </#if>
    </#list>
</#if>

<#if table.fields?? && table.fields?size gt 0>
<#list table.fields as field>
    <#if (field.javaField != "id"  && field.javaField != "isDelete"
        && field.javaField != "createTime" && field.javaField != "updateTime"
        && field.javaField != "deleteTime"
        && field.javaField != "creater" && field.javaField != "updater"
        && field.javaField != "deleter" && field.javaField != "appertainId")>
    public ${field.javaType!""} get${field.javaField?cap_first!""}() {
        return ${field.javaField};
    }

    public void set${field.javaField?cap_first!""}(${field.javaType!""} ${field.javaField!""}) {
        this.${field.javaField!""} = ${field.javaField!""};
    }

    </#if>
</#list>
</#if>
}
