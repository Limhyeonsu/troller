$(document).ready(function() {
    infoShare_detail.init();
});

const infoShare_detail = {

    /** 초기화 */
    init : function (){

        if(!infoShare_detail.isRegUser()){
            $('.btn_area_02').hide();
            $('.btn_area_03').show();
            
            return;
        }
            $('.btn_area_03').hide();
            $('.btn_area_02').show();

    },
    /** 작성자 유저 검증 */
    isRegUser : function () {
        let result = false;
        let loginUser = $('#loginUserId').val();
        let regUser = $('#regUserId').val();

        if(loginUser == regUser) {
            result = true;
        }

        return result;
    },
    /** 게시글 수정 */
    modify : function () {
        let nttId = $('#nttId').html();
        let form = document.createElement("form");
        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "get");
        form.setAttribute("action", "/infoshare/input");

        let field = document.createElement("input");
        field.setAttribute("type", "hidden");
        field.setAttribute("name", "nttId");
        field.setAttribute("value", nttId);
        form.appendChild(field);

        document.body.appendChild(form);
        form.submit();
    },
    /** 첨부파일 다운로드 */
    fileDownload : function (nttId) {
        console.log("nttId :::: " + nttId);
        let form = document.createElement("form");
        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "get");
        form.setAttribute("action", "/infoshare/fileDownload");

        let field = document.createElement("input");
        field.setAttribute("type", "hidden");
        field.setAttribute("name", "nttId");
        field.setAttribute("value", nttId);
        form.appendChild(field);

        document.body.appendChild(form);
        form.submit();
    },
    /** 게시글 삭제 */
    remove : function (nttId) {
        let param = {
            nttId : nttId
        };
        if(confirm("게시글을 삭제하시겠습니까?")) {
            $.ajax({
                data: param,
                type: "GET",
                url: '/infoshare/remove',
                success: function (data) {
                    alert("게시글이 삭제되었습니다.");
                    location.href='/infoshare/list';
                }
            });
        }
    }
}