import $axios from 'utils/Request.js'

// 列表分页
export const get${table.javaName?cap_first}List = data => {
    return $axios({
        url: '/api/${table.name}/list',
        method: 'get',
        data
    });
}

// 获取所有数据
export const get${table.javaName?cap_first}AllList = data => {
    return $axios({
        url: '/api/${table.name}/all',
        method: 'get',
        data
    });
}

// 保存数据
export const save${table.javaName?cap_first} = data => {
    return $axios({
        url: '/api/${table.name}/save',
        method: 'post',
        data
    });
}

// 删除数据
export const delete${table.javaName?cap_first} = data => {
    return $axios({
        url: '/api/${table.name}/delete',
        method: 'delete',
        data
    });
}

// 修改数据
export const update${table.javaName?cap_first}ById = data => {
    return $axios({
        url: '/api/${table.name}/update/' + data.id,
        method: 'put',
        data
    });
}

// 通过ID查询
export const get${table.javaName?cap_first}ById = data => {
    return $axios({
        url: '/api/${table.name}/get/' + data.id,
        method: 'get',
        data
    });
}