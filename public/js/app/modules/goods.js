/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/underscore');

    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');

    var Initiator = {
        init: function() {
            this.create($('[role="submitAdd"]'));
            this.delete($('[role="delete"]'));
            this.update($('#update'), $('[role="submitUpdate"]'));
            this.view($('[role="view"]'));
            this.edictStock($('[role="addStock"]'), $('[role="deleteStock"]'));
        },

        edictStock: function($addBtn, $deleteBtn) {
            var index = 0;
            $addBtn.click(function(){
                var temp = $('#addGoodStockTmpl').html();
                while(temp.indexOf("{index}") > 0) {
                    temp = temp.replace("{index}", index);
                }
                var ele = $(temp);
                $("#stockList").append(ele);
                $("#stockList").scrollTop($("#stockList > div").height() * index);
                index++;
            });

            $(document).delegate($deleteBtn.selector, "click", function () {
                $(this).closest('div').slideUp("fast", function(){this.remove()});
            })
        },

        /**
         * 添加
         */
        create: function($btn) {
            $btn.click(function () {
                var $form = $('#create');
                ajax.post($form.attr('action'), $form.serialize(), function(result){
                    if(result.success){
                        dd.alert('保存成功！', function(){
                            window.location.reload(false);
                        });
                    }else{
                        dd.alert(result.error);
                    }
                });
            })
        },

        /**
         * 删除
         */
        delete: function(obj) {
            obj.click(function() {
                var $this = $(this),
                    $wrap = $this.closest('tr'),
                    userId = $wrap.find('.userId').val(),
                    nickname = $wrap.find('.nickname').val(),
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
                        $("#nicknameToDelete").text(nickname);
                    }
                }).showModal();
            });
        },

        /**
         * 修改
         */
        update: function($form, $btn) {
            $btn.click(function() {
                ajax.post($form.attr('action'), $form.serialize(), function(result){
                    if(result.success){
                        dd.alert('编辑成功！', function(){
                            window.location.reload(false);
                        });
                    }else{
                        dd.alert(result.error);
                    }
                });
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
                    imgs = $wrap.find('.icons'),
                    title = $wrap.find('.title').val(),
                    createTime = $wrap.find('.createTime').val(),
                    updateTime = $wrap.find('.updateTime').val(),
                    state = $wrap.find('.state').val(),
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
                        $("#titleToView").text(title);
                        $("#stateToView").text(state==1?"上架":state==0?"下架":"未知");
                        $("#createTimeToView").text(createTime);
                        $("#updateTimeToView").text(updateTime);
                        imgs.each(function(index) {
                            var file = imgs[index].value;
                            var dom = $("<img>").attr("src", "/public/pictures/goods/"+file);
                            $("#imgToView").append(dom)
                        });

                    }
                }).showModal();
            });
        }
    }

    Initiator.init();
});
