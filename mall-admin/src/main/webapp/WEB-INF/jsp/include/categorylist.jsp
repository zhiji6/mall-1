<%--
  Created by IntelliJ IDEA.
  User: qf
  Date: 2018/3/7
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页商品分类列表</title>
</head>
<body>
<script>
    function onCategoryClick(index, row) {
        var products = row.products
//        console.log(products)
        $("#index_product_list").datagrid('loadData', products)

    }

    (function () {
        $.ajax({
            url: '${pageContext.request.contextPath}/home/list',
            method: 'get',
            success: function (data) {
                var arr = []
                for (var i = 0; i < data.length; i++) {
                    arr.push({
                        text: data[i].category.name,
                        id: data[i].category.id,
                        products: data[i].products
                    })
                }
                $('#category_list').datalist('loadData', arr)
            }
        })
    })()
</script>
<div class="easyui-layout" style="width: 100%;height: 100%;">
    <div data-options="region:'west',split:true" style="width:250px;height: 100%;">
        <ul id="category_list" class="easyui-datalist" title="商品分类" lines="true" data-options="{
            onClickRow:onCategoryClick
        }">
        </ul>
    </div>
    <div data-options="region:'center'" style="height: 100%">
        <%--     <div id="product_tt" class="easyui-tabs" style="width:100%;height: auto;">
             </div>--%>
        <table id="index_product_list" class="easyui-datagrid" title="商品列表" style="width:100%;height:100%"
               data-options="singleSelect:true,collapsible:true">
            <div id="index_product_operation">
                <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:alert('Add')">Add</a>
                <a href="#" class="easyui-linkbutton" iconCls="icon-cut" plain="true" onclick="javascript:alert('Cut')">Cut</a>
            </div>
            <thead>
            <tr>
                <th data-options="field:'id',width:80">商品ID</th>
                <th data-options="field:'name',width:100">商品名称</th>
                <th data-options="field:'price',width:80,align:'right'">价格</th>
                <th data-options="field:'discount',width:80,align:'right'">折后价</th>
                <th data-options="field:'status',width:60,align:'center'">商品状态</th>
                <th data-options="field:'categoryId',width:60,align:'center'">分类ID</th>
                <th data-options="field:'storeNum',width:60,align:'center'">库存总量</th>
                <th data-options="field:'saleNum',width:60,align:'center'">销售总量</th>
                <th data-options="field:'imageUrls',width:60,align:'center'">商品图片</th>
                <th data-options="field:'descriptionId',width:60,align:'center'">产品参数</th>
                <th data-options="field:'commentNum',width:60,align:'center'">评论总数</th>
                <th data-options="field:'saleDate',width:60,align:'center'">上架时间</th>
            </tr>
            </thead>
        </table>

    </div>
</div>
</body>
</html>
