<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperPackage}.${table.javaName?cap_first}Mapper">

    <resultMap type="${table.javaName?cap_first}Vo" id="${table.javaName}Map">
        <#list table.fields as field>
            <#if field.columnKey == "PRI">
                <id column="${field.columnName}" property="${field.javaField}"/>
            <#else>
                <result column="${field.columnName}" property="${field.javaField}"/>
            </#if>
        </#list>
    </resultMap>

    <sql id="${table.name}_columns">
        <#list table.fields as field> ${table.name?substring(0,1)}.${field.columnName}<#if field_index + 1 != table.fields?size>,</#if></#list>
    </sql>

    <sql id="${table.name}_where">
        <if test="${table.javaName} != null">
            <#list table.fields as field>
                <#if (field.javaType!"") == "String">
                    <if test="${table.javaName}.${field.javaField} !=null and ${table.javaName}.${field.javaField} != ''">
                        <#if field_index != 0>AND </#if>${table.name?substring(0,1)}.${field.columnName} LIKE
                        CONCAT('%',${r"#{" + table.javaName + "." + field.javaField + "}"},'%')
                    </if>
                <#else>
                    <if test="${table.javaName}.${field.javaField} !=null ">
                        <#if field_index != 0>AND </#if>${table.name?substring(0,1)}.${field.columnName}
                        = ${r"#{" + table.javaName + "." + field.javaField + "}"}
                    </if>
                </#if>
            </#list>
        </if>
    </sql>

    <sql id="${table.name}_del_where">
        <#list table.fields as field>
            <#if (field.javaType!"") == "String">
                <if test="${field.javaField} !=null and ${field.javaField} != ''">
                    <#if field_index != 0>AND </#if>${field.columnName} LIKE CONCAT('%',${r"#{" + field.javaField + "}"},'%')
                </if>
            <#else>
                <if test="${field.javaField} !=null ">
                    <#if field_index != 0>AND </#if>${field.columnName} = ${r"#{" + field.javaField + "}"}
                </if>
            </#if>
        </#list>
    </sql>

    <select id="findById" resultMap="${table.javaName}Map"
            parameterType="<#list table.fields as field><#if field.columnKey == "PRI">${field.javaType}</#if></#list>">
        SELECT
        <include refid="${table.name}_columns"/>
        FROM ${table.name} ${table.name?substring(0,1)}
        WHERE
        <#list table.fields as field>
            <#if field.columnKey == "PRI">
                ${table.name?substring(0,1)}.${field.columnName} = ${r"#{" + field.javaField + "}"}
            </#if>
        </#list>
        LIMIT 1
    </select>

    <select id="findList" resultMap="${table.javaName}Map" parameterType="${table.javaName?cap_first}Vo">
        SELECT
        <include refid="${table.name}_columns"/>
        FROM ${table.name} ${table.name?substring(0,1)}
        <where>
            <include refid="${table.name}_where"/>
        </where>
    </select>

    <select id="findListByPage" resultMap="${table.javaName}Map">
        SELECT
        <include refid="${table.name}_columns"/>
        FROM ${table.name}  ${table.name?substring(0,1)}
        <where>
            <include refid="${table.name}_where"/>
        </where>
        LIMIT ${r"#{startPage}"},${r"#{pageSize}"}
    </select>

    <select id="findCount" parameterType="${table.javaName?cap_first}Vo" resultType="java.lang.Integer">
        SELECT
        COUNT(*)
        FROM ${table.name} ${table.name?substring(0,1)}
        <where>
            <include refid="${table.name}_where"/>
        </where>
    </select>

    <delete id="delete" parameterType="${table.javaName?cap_first}Vo">
        DELETE FROM ${table.name}
        <where>
            <include refid="${table.name}_del_where"/>
        </where>
    </delete>

    <update id="update" parameterType="${table.javaName?cap_first}Vo">
        UPDATE ${table.name}
        <set>
            <#list table.fields as field>
                <if test="${field.javaField} !=null ">
                    ${field.columnName} = ${r"#{" + field.javaField + "}"},
                </if>
            </#list>
        </set>
        WHERE
        <#list table.fields as field>
            <#if field.columnKey == "PRI">
                ${field.columnName} = ${r"#{" + field.javaField + "}"}
            </#if>
        </#list>
    </update>

    <insert id="save" parameterType="${table.javaName?cap_first}Vo">
        INSERT INTO ${table.name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list table.fields as field>
                <if test="${field.javaField} !=null ">
                    ${field.columnName},
                </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#list table.fields as field>
                <if test="${field.javaField} !=null ">
                    ${r"#{" + field.javaField + "}"},
                </if>
            </#list>
        </trim>
    </insert>

    <insert id="saveByList">
        INSERT INTO ${table.name}
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <#list table.fields as field>
                ${field.columnName},
            </#list>
        </trim>
        VALUES
        <foreach collection="${table.javaName}Vos" item="${table.javaName}Vo" separator=",">
            (
            <#list table.fields as field>
                ${r"#{" + table.javaName + "Vo." + field.javaField + "}"}<#if field_index + 1 != table.fields?size>,</#if>
            </#list>
            )
        </foreach>
    </insert>
</mapper>