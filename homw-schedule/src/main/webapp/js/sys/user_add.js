//用户ID
var userId = T.p("userId");

$(function() {
	setTimeout(function() {
		$("#editForm").bootstrapValidator({
			message : 'This value is not valid',
			feedbackIcons : {
				valid : 'glyphicon glyphicon-ok',
				invalid : 'glyphicon glyphicon-remove',
				validating : 'glyphicon glyphicon-refresh'
			},
			fields : {
				username: {
					message: '用户名验证失败',
					validators: {
						notEmpty: {
							message: '用户名不能为空'
						},
						stringLength : {
							min: 1,
							max: 11,
							message: '用户名必须少于11个字符'
						}
					}
				},
				password: {
					message: '密码验证失败',
					validators: {
						notEmpty: {
							enabled: userId == null ? true : false,
							message: '密码不能为空'
						},
						stringLength : {
							min: 6,
							max: 15,
							message: '密码长度必须在6-15个字符之间'
						}
					}
				},
				confirmPassword: {
					message: '密码验证失败',
					validators: {
						notEmpty: {
							enabled: userId == null ? true : false,
							message: '密码不能为空'
						},
						stringLength : {
							min: 6,
							max: 15,
							message: '密码长度必须在6-15个字符之间'
						},
						identical: {
							field: 'password',
							message: '两次输入的密码不一致，请重新输入'
						}
					}
				},
				mobile : {
					message : '手机号码验证失败',
					validators : {
						notEmpty: {
	                        message: '手机号码不能为空'
	                    },
	                    stringLength: {
	                        min: 11,
	                        max: 11,
	                        message: '请输入11位手机号码'
	                    }
					}
				},
				roleId : {
					message : '角色验证失败',
					validators : {
						notEmpty: {
	                        message: '请选择角色'
	                    }
					}
				},
				realname : {
					message : '姓名验证失败',
					validators : {
						notEmpty: {
	                        message: '姓名不能为空'
	                    },
						stringLength : {
							min: 1,
							max: 6,
							message: '姓名必须少于6个字符'
						}
					}
				},
				describ: {
					message: '说明验证失败',
					validators: {
						stringLength : {
							min: 0,
							max: 80,
							message: '说明必须少于80个字符'
						}
					}
				},
				status: {
					message: '账号状态验证失败',
					validators: {
						notEmpty: {
							message: '账号状态不能为空'
						}
					}
				}
			}
		});
	}, 50);
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		title:"新增管理员",
		roleList:{},
		user:{
			status:1,
			roleIdList:[]
		}
	},
	created: function() {
		//获取角色信息
		this.getRoleList();
    },
	methods: {
		getUser: function(userId){
			$.get("../sys/user/info/"+userId, function(r){
				vm.user = r.user;
			});
		},
		getRoleList: function(){
			$.get("../sys/role/select", function(r){
				vm.roleList = r.list;
				
				if(userId != null){
					vm.title = "修改管理员";
					vm.getUser(userId);
				}
			});
		},
		saveOrUpdate: function (event) {
			$("#editForm").bootstrapValidator('validate');// 提交验证
			// 获取验证结果
			if (!$("#editForm").data('bootstrapValidator').isValid()) {
				return;
			}
			
			var url = vm.user.userId == null ? "../sys/user/save" : "../sys/user/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.user),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.back();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		back: function (event) {
			history.go(-1);
		}
	}
});