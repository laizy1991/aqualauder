/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件
    require('../../thirdParty/underscore');
    
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');
    var isEnabledList = {"0":"否","1":"是"};
    
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
    				friendShareTitle = $wrap.find('.friendShareTitle').val(),
					friendShareDesc = $wrap.find('.friendShareDesc').val(),
					momentShareTitle = $wrap.find('.momentShareTitle').val(),
					imgUrl = $wrap.find('.imgUrl').val(),
					isEnabled = $wrap.find('.isEnabled').val(),
					remark = $wrap.find('.remark').val(),
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
                        window.initImgUploader();
                    	$("#idToUpdate").val(id);
                    	$("#idToUpdateTmp").text(id);
                        $("#nameToUpdate").val(name);
                        $("#friendShareTitleToUpdate").val(friendShareTitle);
                        $("#friendShareDescToUpdate").val(friendShareDesc);
                        $("#momentShareTitleToUpdate").val(momentShareTitle);
                        $("#isEnabledToUpdate").val(isEnabled);
                        $("#remarkToUpdate").val(remark);
                        $("#imgUrlToUpdate").attr("src", imgUrl);
                        $("#imgUrlOnHiddenToUpdate").val(imgUrl);
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
            		name = $wrap.find('.name').val(),
    				friendShareTitle = $wrap.find('.friendShareTitle').val(),
					friendShareDesc = $wrap.find('.friendShareDesc').val(),
					momentShareTitle = $wrap.find('.momentShareTitle').val(),
					imgUrl = $wrap.find('.imgUrl').val(),
					isEnabled = $wrap.find('.isEnabled').val(),
					remark = $wrap.find('.remark').val(),
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
                        $("#idToView").text(id);
                        $("#nameToView").text(name);
                        $("#friendShareTitleToView").text(friendShareTitle);
                        $("#friendShareDescToView").text(friendShareDesc);
                        $("#momentShareTitleToView").text(momentShareTitle);
                        $("#isEnabledToView").text(isEnabledList[isEnabled]);
                        $("#remarkToView").text(remark);
                        $("#createTimeToView").text(createTime);
                        $("#updateTimeToView").text(updateTime);
                        $("#imgUrlToView").attr("src", imgUrl);
                    }
                }).showModal();
            });
        }
        
    }

    Initiator.init();
});
