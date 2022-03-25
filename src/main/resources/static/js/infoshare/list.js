$(document).ready(function() {

});

const infoShare_list = {

	/** 초기화 */
	init : function () {

	},
	/** 게시글 작성 페이지로 이동 */
	writeInfoShare : function () {
		if(!infoShare_list.loginChk()){
			alert("로그인 정보가 없습니다. 로그인 먼저 해주십시오");
			location.href = "/user/login";
			return;
		}
		location.href = "/infoshare/input";
	},
	/** 게시글 검색 */
	getSearch : function () {
		$("#searchForm").submit();
	},
	/** 로그인 체크 */
	loginChk : function () {
		let userId = $('#userId').val();
		let isLogin = false;
		if(userId != "")
			isLogin = true;

		return isLogin;
	}

}
