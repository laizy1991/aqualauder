/**
 */

define(function(require) {
    require('../common/common');  // 公共模块
    require('../../thirdParty/dialog');  // 弹窗插件
    require('../../thirdParty/underscore');
    
    var ajax = require('../util/ajax');
    var dd = require('../util/dialog');

    require('../../thirdParty/swfupload/swfupload');

    var Initiator = {
        init: function() {
            this.create($('[role="create"]'));
            this.delete($('[role="delete"]'));
            this.view($('[role="view"]'));
            this.initImgUploader();
        },

        /**
         * 添加
         */
        create: function(obj) {

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
                    $wrap = $this.closest('.picture'),
                	id = $wrap.find('.id').val(),
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
                    name = $wrap.find('.username').val(),
                    createTime = $wrap.find('.createTime').val(),
                    updateTime = $wrap.find('.updateTime').val(),
                    deleted = $wrap.find('.deleted').val(),
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
                        $("#usernameToView").text(name);
                        $("#updateTimeToView").text(updateTime);
                        $("#createTimeToView").text(createTime);
                        $("#stateToView").text(deleted==1?"已删除":"有效");
                    }
                }).showModal();
            });
        },

        initImgUploader: function () {
            window.swfu = new SWFUpload({
                upload_url: "/ajax.albumCtrl/upload",
                use_query_string:true,
                // File Upload Settings
                file_size_limit : "10 MB",	// 文件大小控制
                file_types : "*.*",
                file_types_description : "All Files",
                file_upload_limit : "0",
                file_post_name: 'fileData',
                file_queue_error_handler : fileQueueError,
                file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
                file_queued_handler : fileQueued,
                upload_progress_handler : uploadProgress,
                upload_error_handler : uploadError,
                upload_success_handler : uploadSuccess,
                upload_complete_handler : uploadComplete,
                button_action: SWFUpload.BUTTON_ACTION.SELECT_FILE, //一次只能选一个文件
                button_width: 84,
                button_height: 40,
                button_placeholder_id : "fileSelectButton",
                button_text_style : '.SWFUpload_0 { opacity:0 }',
                flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",
                debug: false,
            });
        },
    }

    function fileQueueError(file, errorCode, message) {

    }

    function fileQueued(file){
    }

    function fileDialogComplete(numFilesSelected, numFilesQueued) {
        try {
            if (numFilesQueued > 0) {
                this.startUpload();
            }
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadProgress(file, bytesLoaded) {
    }

    function uploadSuccess(file, serverData) {
        try {
            serverData = JSON.parse(serverData);
            if(serverData.success) {
                window.location.reload();
                console.log("???");
            }

        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadComplete(file) {
        try {

        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadError(file, errorCode, message) {
        try{

        } catch (ex3) {
            this.debug(ex3);
        }

    }

    Initiator.init();
});
