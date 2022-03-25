const kdh = {
	log: msg => {
		if(true){
			console.log(msg)
		}
	},
	setObject: (targetId, data) => {
		let target = $('#' + targetId);
		
		$.each(data, (dataIdx, dataItem) => {
			let tags = target.find('[data-column="'+dataIdx+'"]');
			
			$.each(tags, (idx, item) => {
				if(!$.isEmptyObject($(item).attr("formatter"))){
					if($(item).prop("tagName") === "input"){
						$(item).val(window[$(item).attr("formatter")](dataItem[$(item).attr("data-column")]));
					}else{
						$(item).html(window[$(item).attr("formatter")](dataItem[$(item).attr("data-column")]));
					}	
				}else{
					if($(item).prop("tagName") === "INPUT"){
						$(item).val(dataItem);
					}else{
						$(item).html(dataItem);
					}
				}
			})
		})
	},
	setTableList: () => {
		
	},
	gagination: () => {
		
	},
	validation: (formId) => {
		let self = kdh;
		let result = true;
		
		$.each($('#'+formId).find("input"), (inputIdx, inputValue) => {
			let validateStr = $(inputValue).attr("validate");
			let pattern = "";
			let optioin = "";
			
			if(result && !$.isEmptyObject(validateStr)){
				let validateArr = $(inputValue).attr("validate").split(" ");
				
				$.each(validateArr, (validInx, validValue) => {
					let resultValue = self.validateChecker(inputValue, validValue);
					if(typeof resultValue === "object"){
						pattern = resultValue[0];
						option = resultValue[1];
					}else{
						if(!resultValue){
							result = resultValue;
							return false;
						}
					}
				})
			}
			
			if(result && !$.isEmptyObject(pattern)){
				
				let regex = new RegExp(pattern, option);
				
				if(!regex.test($(inputValue).val())){
					alert($(inputValue).prev().text() + "의 형식에 맞지 않습니다.");
					$(inputValue).focus();
					result = false;
				}
			}
		})
		
		return result;
	},
	validateChecker: (target, str) => {
		let self = kdh;
		let result = true;
		let val = $(target).val();
		//let targetText = $(target).prev().text();
		let targetText = !$.isEmptyObject($(target).attr("text")) ? $(target).attr("text") : $(target).prev().text();
		
		switch(str){
		case "notEmpty":
			if($.isEmptyObject(val)){
				alert(targetText + "는(은) 필수 입력 값 입니다.");
				$(target).focus();
				result = false;
			}
			break;
		default:
			if(/^max/.test(str) || /^min/.test(str)){
				result = self.numberChecker(str, val, targetText);
			}else{
				result = self.regex(str);
			}
			
			break;
		}
		
		return result;
	},
	regex : (str) => {
		let regex = "";
		let strArr = str.split('&');
		let option = "";
		let result = new Array();
		$.each(strArr, (idx, val) => {
			switch(val){
			case "number":
				regex += '0-9';
				break;
			case "english":
				regex += 'a-zA-Z';
				break;
			case "hangle":
				regex += 'ㄱ-ㅎㅏ-ㅣ가-힣';
				break;
			case "email":
				regex = '^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$';
				option = "i"
				break;
			case "phoneNumber":
				regex = '^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$';
				break;
			default:
				if(/^str/.test(val)){
					let strArr = val.split("=");
					regex += strArr[1];
				}else{
					alert("지정되지 않은 정규식입니다. \n" + val);
				}
				break;
			}
		})
		
		if(strArr.length > 1){
			regex = "^[" + regex + "]*$";
			option = "g";
		}
			
		
		result.push(regex)
		result.push(option)
		
		return result;
	},
	numberChecker: (str, val, text) => {
		str = str.split("=");
		let result = true;
		if(str.length != 2){
			alert(str + " 이 형식에 맞지 않습니다.");
			return false;
		}
		
		let num = str[1];
		
		switch(str[0]){
		case "max":
			if(val > num){
				alert(text + " 은(는) " + num + "보다 작아야합니다.");
				result = false;
			}
			break;
		case "min":
			if(val < num){
				alert(text + " 은(는) " + num + "보다 커야합니다.");
				result = false;
			}
			break;
		case "maxLength":
			if(val.length > num){
				alert(text + " 의 길이는 최대 " + num + "입니다." );
				result = false;
			}
			break;
		case "minLength":
			if(val.length < num){
				alert(text + " 의 길이는 최소 " + num + "입니다.");
				result = false;
			}
			break;
		default:
			result = false;
		}
		
		return result;
	},
	extensionChecker: (target) => {
		let result = true;
		let files = $(target).prop("files");
		
		$.each(files, (idx, val) => {
			console.log(val);
		})
	},
	async: (url, param, type, dataType) => {
		return new Promise((resolve, reject) => {
			$.ajax({
				url : url,
				type : type,
				data : param,
				dataType : dataType,
				success : (data) => {
					kdh.log(url + " : 정상처리")
					resolve(data)
				},
				error : (request, status, error) => {
					let errorMsg = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error 
					kdh.log(errorMsg)
					alert(request.responseText);
					reject(errorMsg)
				}
			})
		})
	},
	multipartAsync: (url, param, type, dataType) => {
		return new Promise((resolve, reject) => {
			$.ajax({
				url : url,
				enctype: "multipart/form-data",
				type : type,
				data : param,
//				dataType : dataType,
				processData: false,    
		        contentType: false,            
				success : (data) => {
					kdh.log(url + " : 정상처리")
					resolve(data)
				},
				error : (request, status, error) => {
					let errorMsg = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error 
					kdh.log(errorMsg)
					reject(errorMsg)
				}
			})
		})
	},
	sync: (url, param, type, dataType, callback) => {
		let result;
		
		$.ajax({
			url : url,
			data :  param,
			type : type,
			async : false,  
			dataType : dataType,
			success : (data) => {
				kdh.log(url + " : 정상처리")
				if(typeof callback === "function"){
					callback(data);
				}
				result = data;
			},
			error : (request, status, error) => {
				kdh.log(request.responseText)
				result = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error
			}
		});
		
		return result
	},
	multipartSync: (url, param, type, dataType, callback) => {
		let result;
		
		$.ajax({
			url : url,
			enctype: "multipart/form-data",
			data :  param,
			type : type,
			async : false,
			processData: false,    
	        contentType: false,      
			dataType : dataType,
			success : (data) => {
				kdh.log(url + " : 정상처리")
				if(typeof callback === "function"){
					callback(data);
				}
				result = data;
			},
			error : (request, status, error) => {
				kdh.log(request.responseText)
				result = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error
			}
		});
		
		return result
	}
}