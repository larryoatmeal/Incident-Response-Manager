$(document).ready(function(){

	var page_current = parseInt($("#page_current").attr("data"), 10)
	var sort_current = $("#sort_current").attr("data")
	var query_current = $("#query_current").attr("data")
	var queryCol_current = $("#queryCol_current").attr("data")
	var queryOptions_current = parseInt($("#queryOptions_current").attr("data"))
	var searchBoxTypeId_current = "regularQuery" 


	//1 for wildcard, 0 for wholeword, 2 and 3 for time, 4 and 5 for duration
	var WHOLEWORD = 0
	var WILDCARD = 1
	var BEFORETIME = 2
	var AFTERTIME = 3
	var SHORTER = 4
	var LONGER = 5

	//Populate search form

	// Don't refill search box for now
	// if(query_current == "NoQuery"){
	// 	$("#query").prop("placeholder","Search...")
	// }else{
	// 	$("#query").val(query_current)
	// }
	


	function initialize(){
		$("#queryCol").val(queryCol_current)
		show("queryCol") //show query column selector after coorect option chosen to avoid artifacts
		
		modifySearchBox()
		//refill inputs
		$("#" + searchBoxTypeId_current).val(query_current)

		//check correct checkboxes/radiobuttons
		if(queryOptions_current == WHOLEWORD){
			$("#wholeword").prop('checked',true)
		}else if(queryOptions_current == WILDCARD){
			$("#wholeword").prop('checked',false)
		}else{ 
			//alert($(":radio[value=" + queryOptions_current + "]").prop('checked'))
			$(":radio[value=" + queryOptions_current + "]").prop('checked', true)
		}

	}


	initialize()
	


	//Sort
	$(".sort").on("click", function(){

		var sort = $(this).attr("id")
		getList(1, sort, query_current, queryCol_current, queryOptions_current)

	})

	//Pagination
	$(".next:not(.disabled)").on("click", function(){

		getList(page_current+1, sort_current, query_current, queryCol_current, queryOptions_current)
	})
	$(".previous:not(.disabled)").on("click", function(){

		getList(page_current-1, sort_current, query_current, queryCol_current, queryOptions_current)
	})

	//Search
	$("#search").on("click", function(){
		//The query box will take multiple forms

		var queryRaw = $("#" + searchBoxTypeId_current).val()


		var queryCol = $("#queryCol").find(":selected").attr("value")
		var wholeword = $("#wholeword").is(":checked")

		//Radio buttons value field contain queryOptions int
		var checkedTime = $('input[name=time]').filter(':checked').val()
		var checkedDuration = $('input[name=duration]').filter(':checked').val()

		//1 for wildcard, 0 for wholeword, 2 and 3 for time, 4 and 5 for duration
		var queryOptions = WILDCARD
		if (searchBoxTypeId_current == "timeQuery"){
			queryOptions = checkedTime
		}else if(searchBoxTypeId_current == "durationQuery"){
			queryOptions = checkedDuration
		}else if (wholeword){

			queryOptions = WHOLEWORD
		}

		getList(1, sort_current, queryRaw, queryCol, queryOptions)
	})

	function hideAllQueries(){
		$("[querygroup='query']").parent().hide()
	}
	function show(id){
		$("#"+id).parent().show()

	}


	function modifySearchBox(){
		var column = $("#queryCol").find(':selected')[0].value

		hideAllQueries()

		if(column == "incident_type"){
			//hide all
			show("incidentTypeQuery")//show the selected one
			$("#wholeword").prop("checked", true)//whole word
			$("#wholewordgroup").hide()//hide checkbox because unnecessary
			$("#timeRadioGroup").hide()
			$("#durationRadioGroup").hide()
			searchBoxTypeId_current = "incidentTypeQuery"//mark current searchBox as the one we're using

		}else if(column == "status"){
			show("statusQuery")
			$("#wholeword").prop("checked", true)
			$("#wholewordgroup").hide()
			$("#timeRadioGroup").hide()
			$("#durationRadioGroup").hide()
			searchBoxTypeId_current = "statusQuery"
		}else if(column == "issue_type"){
			show("issueTypeQuery")
			$("#wholeword").prop("checked", true)
			$("#wholewordgroup").hide()
			$("#timeRadioGroup").hide()
			$("#durationRadioGroup").hide()
			searchBoxTypeId_current = "issueTypeQuery"
		}else if(column == "created_at" || column == "updated_at" || column == "next_update_at"){
			show("timeQuery")
			$("#wholeword").prop("checked", false)
			$("#wholewordgroup").hide()
			$("#timeRadioGroup").show()
			$("#beforeRadio").prop("checked", true)
			$("#durationRadioGroup").hide()
			searchBoxTypeId_current = "timeQuery"
		}else if(column == "incident_duration"){
			show("durationQuery")
			$(".durationRadioGroup").show()
			$("#wholewordgroup").hide()
			$("#timeRadioGroup").hide()
			$("#durationRadioGroup").show()
			$("#shorterRadio").prop("checked", true)
			searchBoxTypeId_current = "durationQuery"
		}else{
			show("regularQuery")
			$("#wholeword").prop("checked", false)
			$("#wholewordgroup").show()
			$("#timeRadioGroup").hide()
			$("#durationRadioGroup").hide()
			searchBoxTypeId_current = "regularQuery"
		}
	}

	$("#queryCol").change(function(){
		modifySearchBox()
		}
	)

	//Time picker
	$("#timeQuery").datetimepicker()



	function getList(page, sort, query, queryCol, queryOptions){

		var url = jsRoutes.controllers.IncidentBrowser.getIncidents(
			page,
			sort,
			query,
			queryCol,
			queryOptions
		).url

		location.href = url


	}







})