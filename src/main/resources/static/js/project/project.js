let testValue;
const project = {
	init: () => {
		let self = project;
		
		//입력버튼 클릭이벤트
		$('div .box').click((event) => {
			let id = $(event.currentTarget).attr("btntype");
			
			$('#' + id).dialog({
				modal: true,
				resizable: false,
				allowScrolling: false,
				width: 500,
				title: id
			})
		});
		
		//파일업로드버튼 클릭이벤트
		$("form").find(".fileUpload").click((event) => {
			$(event.currentTarget).next().trigger("click");
		})
		
		
		self.list(self.setProjectList);
		//프로젝트 드롭다운 체인지이벤트
		$('#dropdownProjectList').change((event) => {
			project_wbs.list().then(project.updateCalendar);
		})
		
		project_wbs.list().then(self.createCalendar);
		project_wbs.list().then(project_wbs.setSchedule);
		project_user.list().then(project_wbs.setDeveloperList);
		project_client.list().then(project_client.setCLientList);
		project_user.list().then(project_wbs_issue.setDeveloperList);
		
		
	},
	calendar : "",
	list: (callback) => {
		if(typeof callback === "function"){
			kdh.sync("/project/list", "", "get", "json", callback);
		}else{
			return kdh.async("/project/list", "", "get", "json");
		}
	},
	setProjectList: (data) => {
		console.log(data);
		let list = data.list;
		let cnt = data.totCnt;
		let target = $('#dropdownProjectList');
		
		target.empty();
		
		$.each(list, (idx, item) => {
			let $option = $('<option></option>');
			
			$option.val(item.proId);
			$option.html(item.title);
			
			target.append($option);
		})
		
		
		dropdown.init();
	},
	createCalendarData: (data) => {
		let result = {};
		let resource = new Array();
		let event = new Array();
		
		$.each(data.list, (idx, item) => {
			let children = new Array();

			let res = {
				id: item.wbsId,
				title: item.title,
				eventColor: "green",
				children : children
			}
			
			if(item.parent != 0){
				res.parentId = item.parent;
			}
			
			let ev = {
				id: item.wbsId,
				resourceId: item.wbsId, 
				start: $.datepicker.formatDate("yy-mm-dd", new Date(item.schStartDt.year, item.schStartDt.month-1, item.schStartDt.day)),
				end: $.datepicker.formatDate("yy-mm-dd", new Date(item.schEndDt.year, item.schEndDt.month-1, item.schEndDt.day + 1)),
				eventColor: "green",
				title: item.title
			}
			event.push(ev);
			resource.push(res);
		})
		
		result.event = event;
		result.resource = resource;
		
		return result;
	},
	updateCalendar: (data) => {
		let self = project;
		let calendarData = self.createCalendarData(data);
		
		self.calendar.batchRendering(() => {		    
		    self.calendar.getEvents().forEach(event => event.remove());
		    calendarData.event.forEach(event => self.calendar.addEvent(event));
		    
		    self.calendar.getResources().forEach(resource => resource.remove());
		    calendarData.resource.forEach(resource => self.calendar.addResource(resource));
		});
		
	},
	createCalendar: (data) => {
		let self = project;
		let calendarData = self.createCalendarData(data);
		
		
		//캘린더
		let calendarEl = document.getElementById('calendar');
		
		let calendar = new FullCalendar.Calendar(calendarEl, {
		    timeZone: 'Asia/Seoul',
			initialView: 'resourceTimelineMonth',
			locale: 'ko',
//			editable: true,
			headerToolbar: {
				left: "saveIssue saveSchedule",
				center: 'title',
				//right: 'month,basicWeek,basicDay'
			},
//			validRange: {
//			    start: '2017-05-01',
//			    end: '2017-06-01'
//			  },
			customButtons: {
				saveIssue: {
					text: "new Issue",
					click: () => {
						$('#newProjectIssue').dialog({
							modal: true,
							resizable: false,
							allowScrolling: false,
							width: 500,
							title: "new Issue"
						})
					}
				},
				saveSchedule: {
					text:"new Schedule",
					click: () => {
						$('#newWbs').dialog({
							modal: true,
							resizable: false,
							allowScrolling: false,
							width: 500,
							title: "new Schedule"
						})	
						
						$('#wbsDate').flatpickr({
							mode: "range",
						    dateFormat: "Y-m-d",
						    //conjunction: " ~ ",
						    local: 'ko',
						    onChange: [
						    	(selectedDates) => {
						    		if(selectedDates.length > 1){
				    			
						    			$('#projectWbsSaveForm [name="str_schStartDt"]').val(flatpickr.formatDate(selectedDates[0], "Y-m-d"));
						    			$('#projectWbsSaveForm [name="str_schEndDt"]').val(flatpickr.formatDate(selectedDates[1], "Y-m-d"));
						    		}
						    	}
						    ]
//						    defaultDate: ["2016-10-10", "2016-10-20"]
						})
					}	
				}
			},
			//resourceAreaHeaderContent: $('#headerProjectList option:selected').html(),
			eventClick: (info) => {
				project_wbs.detail(info.event.id);

				$('#newProjectWbsIssue').dialog({
					modal: true,
					resizable: false,
					allowScrolling: false,
					width: 500,
					title: "update wbs"
				})
				
				
			},
			resources: calendarData.resource,
			events: calendarData.event
		
		});
		calendar.render();
		
		self.calendar = calendar;
	},
	save : () => {
		let self = project;
		let validate = self.validation();
		
		if(validate){
			//let param = $('#projectSaveForm').serialize();
			let param = new FormData($('#projectSaveForm')[0]);
			kdh.multipartAsync("/project/save", param, "post", "json")
				.then(self.save_after);
		}else{
			alert(validate);
		}
	},
	save_after: () => {
		$('#newProject').dialog("close");
	},
	validation: () => {
		return true;
	}
}

const project_issue = {
	save: () => {
		let self = project_issue;
		let validate = self.validation();
		
		if(validate){
			
			let param = $('#projectIssueSaveForm').serialize();
			kdh.async("/project/issue/save", param, "post", "json")
				.then(self.save_after);
		}else{
			alert(validate);
		}
		
	},
	save_after: (data) => {
		$('#newIssue').dialog("close");
	},
	validation: () => {
		return true;
	}
}

const project_wbs = {
	save: () => {
		let self = project_wbs;
		let validate = self.validation();
		
		if(validate){
			$('#projectWbsSaveForm [name=proId]').val($('#dropdownProjectList option:selected').val());
			let param = $('#projectWbsSaveForm').serialize();
			kdh.log(param);
			kdh.async("/project/wbs/save", param, "post", "json")
				.then(self.save_after);
		}else{
			alert(validate);
		}
		
	},
	update: (callback) => {
		let self = project_wbs;
		
		let param = $('#projectWbsIssueSaveForm').serialize();
		
		kdh.async("/project/wbs/update", param, "post", "json")
			.then(self.save_after);
	},
	update_after: (date) => {
		project_wbs.list().then(project.updateCalendar);
		$('#projectWbsSaveForm input[type=text]').val("");
		$('#projectWbsSaveForm input[type=textarea]').val("");
		$('#newWbsIssue').dialog("close");
	},
	save_after: (data) => {
		project_wbs.list().then(project.updateCalendar);
		$('#projectWbsSaveForm input[type=text]').val("");
		$('#projectWbsSaveForm input[type=textarea]').val("");
		$('#newWbs').dialog("close");
	},
	validation: () => {
		return true;
	},
	list: (callback) => {
		let param = {proId: $('#dropdownProjectList option:selected').val()}
		
		if(typeof callback === "function"){
			kdh.sync("/project/wbs/list", param, "get", "json", callback);
		}else{
			return kdh.async("/project/wbs/list", param, "get", "json");
		}
	},
	setDeveloperList: (data) => {
		let target = $('#wbsDeveloper');

		target.empty();
		$.each(data.list, (idx, item) => {
			let $option = $('<option></option>');
			
			$option.val(item.uniqId);
			$option.html(item.name);
			
			target.append($option);
		})
	},
	setSchedule: (data) => {
		let target = $('#wbsParent');
		let target2 = $('#wbsIssueParent');
		target.empty();
		
		let $option = $('<option></option>');
		
		$option.val('0');
		$option.html("최상위");
		target.append($option);
		target2.append($option);
		$.each(data.list, (idx, item) => {
			let $option = $('<option></option>');
			
			$option.val(item.wbsId);
			$option.html(item.title);
			target.append($option);
			target2.append($option);
		})
	},
	detail : (wbsId) => {
		let self = project_wbs;
		
		let param = {
			wbsId : wbsId
		}
		kdh.sync("/project/wbs/detail", param, "get", "json", self.setDetail);
	},
	setDetail : (data) => {
		data = data.data;
		kdh.setObject("newProjectWbsIssue", data);
		$('#wbsIssueDeveloper').val(data.userinfo.uniqId);
		
		$('#wbsIssueDate').flatpickr({
			mode: "range",
		    dateFormat: "Y-m-d",
		    defaultDate : [new Date(data.schStartDt.year+"-"+data.schStartDt.month+"-"+data.schStartDt.day)
		    			 , new Date(data.schEndDt.year+"-"+data.schEndDt.month+"-"+data.schEndDt.day)],
		    //conjunction: " ~ ",
		    local: 'ko',
		    onChange: [
		    	(selectedDates) => {
		    		if(selectedDates.length > 1){
    			
		    			$('#projectWbsIssueSaveForm [name="str_schStartDt"]').val(flatpickr.formatDate(selectedDates[0], "Y-m-d"));
		    			$('#projectWbsIssueSaveForm [name="str_schEndDt"]').val(flatpickr.formatDate(selectedDates[1], "Y-m-d"));
		    		}
		    	}
		    ]
		})
	}
}

const project_wbs_issue = {
	save: () => {
		let self = project_wbs_issue;
		
		
		
		let validate = self.validation();
		
		if(validate){
			let param = $('#projectWbsIssueSaveForm').serialize();

			kdh.async("/project/wbs/issue/save", param, "post", "json")
				.then(self.save_after);
		}else{
			alert(validate);
		}
		
	},
	save_after: (data) => {
		$('#newWbsIssue').dialog("close");
	},
	validation: () => {
		return true;
	},
	setDeveloperList: (data) => {
		let target = $('#wbsIssueDeveloper');

		target.empty();
		$.each(data.list, (idx, item) => {
			let $option = $('<option></option>');
			
			$option.val(item.uniqId);
			$option.html(item.name);
			
			target.append($option);
		})
	}
}

const project_user = {
	save: (callback) => {
		let self = project_user;
		
		let validate = kdh.validation("userSaveForm");
		
		if(validate){
			let param = new FormData($('#userSaveForm')[0])
//			if(typeof callback === "function"){
//				kdh.multipartSync("/user/joinuser", param, "post", "json", callback)
//			}else{
//				kdh.multipartAsync("/user/joinuser", param, "post", "json")
//				.then(self.save_after);
//			}	
		}
		
	},
	save_after: (data) => {
		$('#newUser').dialog("close");
	},
	validation: () => {
		return true;
	},
	list: (callback) => {
		
		if(typeof callback === "function"){
			kdh.multipartSync("/user/list", "", "get", "json", callback);
		}else{
			return kdh.multipartAsync("/user/list", "", "get", "json")
		}
		
		
	}
}

const project_client = {
	save : () => {
		let self = project_client;
		let validate = self.validation();
		
		if(validate){
			let param = $('#clientSaveForm').serialize();

			kdh.async("/client/save", param, "post", "json")
				.then(self.save_after);
		}else{
			alert(validate);
		}
	},
	save_after: () => {
		let self = project_client;
		self.getClientList();
		$('#newClient').dialog("close");
	},
	validation: () => {
		return true;
	},
	list: (callback) => {
		
		if(typeof callback === "function"){
			kdh.sync("/client/list", "", "get", "json", callback);
		}else{
			return kdh.async("/client/list", "", "get", "json")
		}
	},
	setCLientList: (data) => {
		let list = data.list;
		let cnt = data.totCnt;
		let target = $('#clientList')
		
		$.each(list, (idx, item) => {
			let $option = $('<option></option>');
			
			$option.val(item.clientId);
			$option.html(item.name);
			target.append($option);
		})
	}
}


$(document).ready(() => {
	kdh.log("project")
	project.init();
})