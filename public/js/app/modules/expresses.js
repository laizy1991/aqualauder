/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件
    require('../../thirdParty/underscore');
    
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');

    var Express = {
        init: function() {
            this.createExpress($('[role="create"]'));
            this.deleteExpress($('[role="delete"]'));
            this.editExpress($('[role="edit"]'));
        },

        /**
         * 添加
         */
        createExpress: function(obj) {
            var self = this;

            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('.folded-panel'),
                    $hiddenInputs = $wrap.find('input:hidden');

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
        deleteExpress: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                	$expressId = $wrap.find('.id'),
                	expressId = $expressId.val();

                var deleteLevelDialog = dialog({
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
                    	$("#expressIdOnDelete").val(expressId);
                    }
                }).showModal();
            });
        },

        /**
         * 编辑
         */
        editExpress: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    $expressId = $wrap.find('.id'),
                    $name = $wrap.find('.name'),
                    expressId = $expressId.val(),
                	name = $name.val(),
                editExpressDialog = dialog({
                    id: 'editDialog',
                    title: '编辑',
                    content: document.getElementById('editDialogTmpl').innerHTML,
                    button: [
                        {
                        	value: '确定',
                            callback: function () {
                                var dia = this,
                                $form = this.__popup.find('form');
                                
                                ajax.post($form.attr('action'), $form.serialize(), function(result){
                                    if(result.success){
                                        dia.close();
                                        dd.alert('编辑成功！', function(){
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
                    	$("#expressIdOnUpdate").val(expressId);
                    	$("#nameOnUpdate").text(name);
                    }
                }).showModal();
            });
        }
        
    }

    Express.init();
});
