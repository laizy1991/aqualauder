/**
 */

define(function(require) {
    require('../../thirdParty/swfupload/swfupload');

    window.initImgUploader = function () {
        window.swfu = new SWFUpload({
            upload_url: "/ajax.qrShareCtrl/upload",
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
            button_width: 180,
            button_height: 315,
            button_placeholder_id : "fileSelectButton",
            flash_url : "/public/js/thirdParty/swfupload/swfupload.swf",
            debug: false,
        });
    };

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

        try {
            //var percent = Math.ceil((bytesLoaded / file.size) * 100);
        } catch (ex) {
            this.debug(ex);
        }
    }

    function uploadSuccess(file, serverData) {
        try {
            serverData = JSON.parse(serverData)
            if(serverData.success) {
                console.log(serverData)
                $("#imgUrlOnHiddenToUpdate").val(serverData.data.imgUrl);
                $("#imgPathOnHiddenToUpdate").val(serverData.data.imgPath);
                $("#imgUrlToUpdate").attr("src", serverData.data.imgUrl);
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
});
