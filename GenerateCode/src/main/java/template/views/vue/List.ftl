<template>
    <main-page>
        <div slot="body">
            <el-form :inline="true" :model="search" size="mini">
                <el-form-item>
                    <el-button type="info" @click="get${table.javaName?cap_first}List()">搜索</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="dialogFormVisible = true;">新增</el-button>
                </el-form-item>
            </el-form>

            <el-table :data="tableData" style="width: 100%" tooltip-effect="dark">
                <#list table.fields as field>
                <#if field.javaField != "createTime">
                <el-table-column prop="${field.javaField}" label="${field.columnComment!""}"></el-table-column>
                <#else>
                <el-table-column prop="${field.javaField}" label="${field.columnComment!""}"width="180">
                    <template slot-scope="scope">{{scope.row.createTime | datetime}}</template>
                </el-table-column>
                </#if>
                </#list>

                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button
                            type="primary"
                            icon="el-icon-edit"
                            circle
                            title="编辑"
                            size="small"
                            @click="handleClick2EditById(scope.row)"
                        ></el-button>
                        <el-button
                            type="danger"
                            icon="el-icon-delete"
                            circle
                            title="删除"
                            size="small"
                            @click="handleClick2DeleteById(scope.row.id)"
                        ></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <pagination :listNum="listNum" @currentChange="handleCurrentChange"></pagination>

            <el-dialog title="${table.comment!""}管理" :visible.sync="dialogFormVisible" width="30%">
                <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                    <#list table.fields as field>
                    <#if field.javaField != "id">
                    <el-form-item label="${field.columnComment!""}" prop="${field.javaField}">
                        <el-input v-model="form.${field.javaField}" autocomplete="off"></el-input>
                    </el-form-item>
                    </#if>
                    </#list>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="onSubmit">提交</el-button>
                </div>
            </el-dialog>
        </div>
    </main-page>
</template>

<script>
    import {
        get${table.javaName?cap_first}List,
        get${table.javaName?cap_first}AllList,
        save${table.javaName?cap_first},
        delete${table.javaName?cap_first},
        update${table.javaName?cap_first}ById
    } from "api/${table.javaName?cap_first}Api";

    export default {
        name: "${table.javaName?cap_first}List",
        data() {
            return {
                search: {
                    currentIndex: 1
                },
                form: {
                    <#list table.fields as field>
                    <#if field.javaField != "createTime">
                    ${field.javaField!""}: '',
                    </#if>
                    </#list>
                },
                rules: {
                },
                tableData: [],
                listNum: 1,
                dialogFormVisible: false
            };
        },
        methods: {
            get${table.javaName?cap_first}List() {
                get${table.javaName?cap_first}List(this.search).then(res => {
                    this.tableData = res.datas;
                    this.listNum = res.pagination.totalCount;
                });
            },
            handleCurrentChange(page) {
                this.search.currentIndex = page;
                this.get${table.javaName?cap_first}List();
            },
            onSubmit() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        if (this.form.id) {
                            //修改
                            update${table.javaName?cap_first}ById(this.form).then(res => {
                                if (res) {
                                    this.$base.showOk();
                                    this.dialogFormVisible = false;
                                    this.get${table.javaName?cap_first}List();
                                }
                            });
                        } else {
                            //保存
                            save${table.javaName?cap_first}(this.form).then(res => {
                                if (res) {
                                    this.$base.showOk();
                                    this.dialogFormVisible = false;
                                    this.get${table.javaName?cap_first}List();
                                }
                            });
                        }
                    }
                });
            },
            handleClick2EditById(row) {
                this.dialogFormVisible = true;
                this.$base.set2Form(this.form, row);
            },
            handleClick2DeleteById(id) {
                this.$base.deleteConfirm(() => {
                    delete${table.javaName?cap_first}({id}).then(res => {
                        if (res) {
                            this.$base.deleteOk();
                            this.get${table.javaName?cap_first}List();
                        }
                    });
                });
            }
        },
        created() {
            this.get${table.javaName?cap_first}List();
        },
        watch: {
            dialogFormVisible(newDialogFormVisible) {
                if (!newDialogFormVisible) {
                    this.$base.clearObj(this.form);
                    this.$refs.form.resetFields();
                }
            }
        }
    };
</script>

<style lang="stylus" scoped></style>