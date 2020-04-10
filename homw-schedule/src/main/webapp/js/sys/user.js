$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/user/list',
        datatype: "json",
        colModel: [
			{ label: '用户ID', name: 'userId', width: 45, key: true, hidden: true },
			{ label: '用户名', name: 'username', width: 75 },
			//{ label: '邮箱', name: 'email', width: 90 },
			{ label: '手机号', name: 'mobile', width: 100 },
			{ label: '类型', name: 'roleName', width: 75 },
			{ label: '姓名', name: 'realname', width: 75 },
			{ label: '状态', name: 'status', width: 60, formatter: function(value, options, row){
				return value === 0 ?
					'<span class="label label-danger">禁用</span>' :
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建时间', name: 'createTime', width: 80},
			{ label: '操作', width: 60, formatter: function(value, options, row) {
				return '<a href="user_add.html?userId='+row['userId']+'">编辑</a>&nbsp;&nbsp;<a href="user_detail.html?userId='+row['userId']+'" @click="update">详情</a>';
			}},
        ],
		viewrecords: true,
        height: 400,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
        //pager: "#jqGridPager",
        jsonReader : {
        	root: "list"
            /*root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"*/
        },
        prmNames : {
            /*page:"page", 
            rows:"limit",*/
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },
        beforeRequest: function() {
        	if (!inited) {
        		inited = true;

        		var param = sessionStorage.getItem('user');
    			param = JSON.parse(param);
    			if (param) {
    				vm.username = param.username;
                	vm.realname = param.realname;
                	vm.status = param.status;
                	vm.mobile = param.mobile;
                	vm.type = param.type;

    				vm.query();

    				setTimeout(function() {
                    	vm.type = param.type;
                	}, 400);
    				return false;
    			}
        	}
        	return true;
        }
    });
});

var inited = false;

var vm = new Vue({
	el:'#rrapp',
	data:{
		username: null,
		realname: null,
		status: '-1',
		mobile: null,
		type: '-1',
		roleList:{},
	},
	created: function() {
		//获取角色信息
		this.getRoleList();
    },
	methods: {
		update: function (event) {
			var userId = getSelectedRow();
			if(userId == null){
				return ;
			}

			location.href = "user_add.html?userId="+userId;
		},
		getRoleList: function(){
			$.get("../sys/role/select", function(r){
				vm.roleList = r.list;
			});
		},
		reset: function () {
			vm.username = null;
			vm.realname = null;
			vm.status = -1;
			vm.mobile = null;
			vm.type = -1;

			sessionStorage.removeItem('user');

			vm.query();
		},
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{
                postData: {
                	'username': vm.username,
                	'realname': vm.realname,
                	'status': vm.status,
                	'mobile': vm.mobile,
                	'type': vm.type
                },
                page: 1
            }).trigger("reloadGrid");

			sessionStorage.setItem('user', JSON.stringify({
            	'username': vm.username,
            	'realname': vm.realname,
            	'status': vm.status,
            	'mobile': vm.mobile,
            	'type': vm.type
            }));
		},
		del: function (event) {
			var userIds = getSelectedRows();
			if(userIds == null){
				return ;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/user/delete",
				    data: JSON.stringify(userIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		}
	}
});