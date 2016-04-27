/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件
    require('../../thirdParty/underscore');

    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');

    var Initiator = {
        init: function() {
            this.create($('[role="create"]'));
            this.delete($('[role="delete"]'));
            this.update($('[role="update"]'));
            this.view($('[role="view"]'));
            this.viewDetail($('[role="viewDetail"]'));
        },

        /**
         * 添加
         */
        create: function(obj) {
            var self = this;

            obj.click(function() {
                var $this = $(this);

                var createDialog = dialog({
                    id: 'createDialog',
                    title: '添加',
                    content: document.getElementById('createDialogTmpl').innerHTML,
                    button: [
                        {
                            value: '保存',
                            callback: function () {
                                var dia = this,
                                    $form = this.__popup.find('form');

                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('添加成功！', function(){
                                            window.location.reload(false);
                                        });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow: function() {

                    }
                }).showModal();
            });
        },



        /**
         * 删除
         */
        delete: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    userId = $wrap.find('.userId').val(),
                    realName = $wrap.find('.realName').val(),
                    deleteDialog = dialog({
                    id: 'deleteDialog',
                    title: '删除',
                    content: document.getElementById('deleteDialogTmpl').innerHTML,
                    button: [
                        {
                            value: '确定',
                            callback: function () {
                                var dia = this,
                                    $form = this.__popup.find('form');

                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('删除成功！', function(){
                                            window.location.reload(false);
                                        });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow:function() {
                        $("#userIdToDelete").val(userId);
                        $("#realNameToDelete").text(realName);
                    }
                }).showModal();
            });
        },

        /**
         * 修改
         */
        update: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    userId = $wrap.find('.userId').val(),
                    realName = $wrap.find('.realName').val(),
                    type = $wrap.find('.type').val(),
                    status = $wrap.find('.status').val(),
                    link = $wrap.find('.link').val(),
                    qrcodeUrl = $wrap.find('.qrcodeUrl').val(),
                    updateDialog = dialog({
                    id: 'updateDialog',
                    title: '修改',
                    content: document.getElementById('updateDialogTmpl').innerHTML,
                    button: [
                        {
                            value: '确定',
                            callback: function () {
                                var dia = this,
                                    $form = this.__popup.find('form');

                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('修改信息成功！', function(){
                                            window.location.reload(false);
                                        });
                                    }else{
                                        dd.alert(result.error);
                                    }
                                });
                                return false;
                            },
                            autofocus: true
                        }
                    ],
                    cancelValue: '取消',
                    cancel: function() {

                    },
                    onshow:function() {
                        $("#userIdToUpdate").val(userId);
                        $("#realNameToUpdate").text(realName);
                        $("#realNameToUpdateEx").val(realName);
                        $("#typeToUpdate").val(type);
                        $("#statusToUpdate").val(status);
                        $("#linkToUpdate").val(link);
                        $("#qrcodeUrlToUpdate").val(qrcodeUrl);
                    }
                }).showModal();
            });
        },

        /**
         * 查看
         */
        view: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    userId = $wrap.find('.userId').val(),
                    type = $wrap.find('.type').val(),
                    status = $wrap.find('.status').val(),
                    realName = $wrap.find('.realName').val(),
                    joinTime = $wrap.find('.joinTime').val(),
                    link = $wrap.find('.link').val(),
                    qrcodeUrl = $wrap.find('.qrcodeUrl').val(),
                    createTime = $wrap.find('.createTime').val(),
                    updateTime = $wrap.find('.updateTime').val(),
                    viewDialog = dialog({
                    id: 'viewDialog',
                    title: '查看',
                    content: document.getElementById('viewDialogTmpl').innerHTML,
                    button: [],
                    cancelValue: '关闭',
                    cancel: function() {

                    },
                    onshow:function() {
                        var statusList={"0":"未认证","1":"认证通过","-1":"认证不通过"}
                        $("#userIdToView").text(userId);
                        $("#typeToView").text(type=="0"?"个人":"未知");
                        $("#statusToView").text(statusList[status]);
                        $("#realNameToView").text(realName);
                        $("#joinTimeToView").text(joinTime);
                        $("#linkToView").text(link);
                        $("#qrcodeUrlToView").text(qrcodeUrl);
                        $("#createTimeToView").text(createTime);
                        $("#updateTimeToView").text(updateTime);
                    }
                }).showModal();
            });
        },

        /**
         * 查看详情
         */
        viewDetail: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    userId = $wrap.find('.userId').val(),
                    viewDialog = dialog({
                    id: 'viewDetailDialog',
                    title: '查看详情',
                    content: document.getElementById('viewDetailDialogTmpl').innerHTML,
                    button: [],
                    cancelValue: '关闭',
                    cancel: function() {

                    },
                    onshow:function() {
                    	var params={"userId":userId};
                        ajax.post('/ajax.DistributorCtrl/distributorDetail', params, function(result){
                            if(result.error){
                            }else{
                            	if(result.underling["1"] != null) {
                            		$("#underling_1").text(result.underling["1"].length);
                            	} else {
                            		$("#underling_1").text(0);
                            	}
                            	if(result.underling["2"] != null) {
                            		$("#underling_2").text(result.underling["2"].length);
                            	} else {
                            		$("#underling_2").text(0);
                            	}
                            	if(result.underling["3"] != null) {
                            		$("#underling_3").text(result.underling["3"].length);
                            	} else {
                            		$("#underling_3").text(0);
                            	}

                            	$("#allBalance").text(result.allBalance/100);
                            	$("#usefulBalance").text(result.usefulBalance/100);
                            	$("#totalIncome").text(result.totalIncome/100);
                            	$("#extensionQrCode").text(result.extensionQrCode);
                            }
                        });
                    }
                }).showModal();
            });
        }

    }

    Initiator.init();
});
