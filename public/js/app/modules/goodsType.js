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
                    	$("#parentIdToAdd").empty();
                		$("#parentIdToAdd").append("<option value='0'>请选择</option>");
                		for(id in names) {
                			$("#parentIdToAdd").append("<option value='"+id +"'>" + names[id] + "</option>");
                		}
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
                    id = $wrap.find('.id').val(),
                    name = $wrap.find('.name').val(),
                    hasSubType = $wrap.find('.hasSubType').val(),
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
                            $("#idToDelete").val(id);
                            $("#nameToDelete").text(name);
                            if(hasSubType == 1) {
                            		$("#msg").text("该商品类型包含子类，将会一并删除！！");
                            }
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
                    id = $wrap.find('.id').val(),
                    name = $wrap.find('.name').val(),
                    desc = $wrap.find('.desc').val(),
                    createTime = $wrap.find('.createTime').val(),
                    sortNum = $wrap.find('.sortNum').val(),
                    pid = $wrap.find('.pid').val(),
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
                            $("#idToUpdate").val(id);
                            $("#nameToUpdate").val(name);
                            $("#descToUpdate").val(desc);
                            $("#createTimeToUpdate").val(createTime);
                            $("#sortNumToUpdate").val(sortNum);
                            $("#parentIdToUpdate").empty();
                            
							$("#parentIdToUpdate").append("<option value='0'>请选择</option>");
                    		for(item in names) {
								if(item != id) {
									$("#parentIdToUpdate").append("<option value='"+item +"'>" + names[item] + "</option>");
								}
                    			
                    		}
                    		$("#parentIdToUpdate").val(pid);
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
                    id = $wrap.find('.id').val(),
                    viewDialog = dialog({
                        id: 'viewDialog',
                        title: '查看',
                        content: document.getElementById('viewDialogTmpl').innerHTML,
                        button: [],
                        cancelValue: '关闭',
                        cancel: function() {

                        },
                        onshow:function() {
                            $("#idToView").text(id);
                        }
                    }).showModal();
            });
        }

    }

    Initiator.init();
});
