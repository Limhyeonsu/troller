$(document).ready(function() {
    header.init();
});

const header = {
    init: function() {
        let userId = '';
        let name = '';
        //아이콘 클릭시 회원정보 수정 팝업 오픈
        $('.infoUser').on('click', function(){
            if(userId != ''){
                var param = {
                    userId : userId,
                    name : name
                };
            }

            let result = kdh.async("/user/detail", param, "get", "json");
            result.then(value => {
                let image = value.userInfo.image;
                if(image != undefined) {
                    $('#userInfoImg').attr('src', '/resources/userImages/'+image);
                    $('#userInfoImg').attr('style', 'width: 120px; height: 150px;');
                }
                $('#uniqId').val(value.userInfo.uniqId);
                $('#note').val(value.userInfo.note);
                $('#password').val('');
                $('#checkPwd').val('');
                userId = $('#userId').val(value.userInfo.userId);
                name = $('#name').val(value.userInfo.name);
            });

            $('#modifyUser').dialog({
                modal: true,
                resizable: false,
                allowScrolling: false,
                width: 500,
                title: 'modifyUser'
            })
        });

        //파일업로드버튼 클릭이벤트
        $("#userModifyForm").find(".fileUpload").click((event) => {
            $(event.currentTarget).next().trigger("click");
        })
    }
} //header end

/** 회원정보  수정 */
const userInfo = {
    modify : function () {
        let validate = kdh.validation("userModifyForm");
        if(validate){
            let param = new FormData($('#userModifyForm')[0]);
            kdh.multipartAsync("/user/update", param, "post", "json")
				.then(userInfo.modify_after);
        }
    },
    modify_after: function () {
        alert("수정되었습니다.");
        $('#modifyUser').dialog("close");
    },
    delete : function () {
        alert("탈주 금지...");
        return;
    }


} //userInfo end