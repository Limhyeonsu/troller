$(document).ready(function() {
    infoShare_input.init();
});

const infoShare_input = {

    /** 초기화 */
    init : function (){
        //썸머 노트 세팅
    	//2021.08.24 박소영 수정 [as-is : height:350;]
        $('#summernote').summernote({
            placeholder: '내용을 입력해주세요.',
            tabsize: 2,
            height: 230,
            codeviewFilter: false,
            codeviewIframeFilter: true,
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'underline', 'clear']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['table', ['table']],
                ['insert', ['link', 'picture', 'video']],
                ['view', ['fullscreen', 'codeview', 'help']]
            ],
            callbacks: {
                onImageUpload: function (files) {    // 파일 업로드 (이미지 업로드시 동작)
                    for (let i = 0; i < files.length; i++) {
                        infoShare_input.sendFiles(files[i], this);
                    }
                }
            }
        });

        let nttGb = $('#dto_nttGb').val();
        $('#nttGb').val(nttGb).prop("selected", true);

        $('#uploadFile').change(function () {
            let file = $('#uploadFile')[0].files[0];
            infoShare_input.sendFiles(file);
        });
    },
    /** 유효성 검증 */
    vaildate : function () {
        let title = $('#title').val();
        let content = $('#summernote').val();

        if(title.length == 0) {
            alert("제목을 입력하여 주시기 바랍니다.");
            return false;
        }
        if(content.length == 0) {
            alert("내용을 입력하여 주시기 바랍니다.");
            return false;
        }
        return true;

    },
    /** 게시글 등록, 수정 */
    saveInfoShare : function (gubun) {
        let fm = document.infoshare_form;

        let field = document.createElement("input");
        field.setAttribute("type", "hidden");
        field.setAttribute("name", "gubun");
        field.setAttribute("value", gubun);
        fm.appendChild(field);

        if(gubun == 'save') gubun = "등록";
        else gubun = "수정";

        if(infoShare_input.vaildate()) {
            if(confirm("게시글을 "+gubun+"하시겠습니까?")) {
                fm.submit();
            }
        }
    },
    /** 파일 업로드 진행, 서버에서 파일 업로드 성공 후 파일 경로를 return 받는다. */
    sendFiles : function (file, editor) {
        //spring security 때문에 post 방식 전송시 에러 발생 csrf 정보 같이 전달해야 정상 작동함
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        let data = new FormData();
        data.append("file", file);
        $.ajax({
            data: data,
            type: "POST",
            url: '/infoshare/fileUpload_ajax',
            beforeSend: function (xhr){
                xhr.setRequestHeader(header, token);
            },
            contentType: false,
            enctype: "multipart/form-data",
            processData: false,
            success: function (data) {
                if(editor != null)
                    $(editor).summernote('editor.insertImage', data.url);
                    let fileName = data.url.split('/');
                    $('#saveFileName').val(fileName[3]);
            }
        });
    },
    /** 파일 업로드 버튼 event */
    uploadFile : function () {

    },
    /** 파일 삭제 */
    removeFile : function (nttId, saveFileName) {

        if(confirm("첨부파일을 삭제 하시겠습니까?")) {

            let params = {
                nttId: nttId,
                saveFileName: saveFileName
            };
            $.ajax({
                data: params,
                type: "GET",
                url: '/infoshare/removeFile',
                success: function (data) {
                    if (data.result == 'success')
                        alert('첨부파일 삭제 성공');
                    else
                        alert('첨부파일 삭제 실패');
                    location.reload();
                }
            });
        }
    }
    
}

