$(document).ready(function(){

	var page_current = parseInt($("#page_current").attr("data"), 10)
	var sort_current = $("#sort_current").attr("data")
	var query_current = $("#query_current").attr("data")
	var queryCol_current = $("#queryCol_current").attr("data")
	var queryOptions_current = parseInt($("#queryOptions_current").attr("data"))


	//Populate search form
	if(query_current == "NoQuery"){
		$("#query").prop("placeholder","Search...")
	}else{
		$("#query").val(query_current)
	}
	
	$("#queryCol").val(queryCol_current)//will check for value attribute

	if(queryOptions_current == 0){
		$("#wholeword").prop('checked',true)

	}
	$(".sort").on("click", function(){

		var sort = $(this).attr("id")
		getList(page_current, sort, query_current, queryCol_current, queryOptions_current)

	})

	$(".next:not(.disabled)").on("click", function(){

		getList(page_current+1, sort_current, query_current, queryCol_current, queryOptions_current)
	})
	$(".previous:not(.disabled)").on("click", function(){

		getList(page_current-1, sort_current, query_current, queryCol_current, queryOptions_current)
	})
	$("#search").on("click", function(){
		var queryRaw = $("#query").val()
		var queryCol = $("#queryCol").find(":selected").attr("value")
		var wholeword = $("#wholeword").is(":checked")
		var queryOptions = 1
		if (wholeword){
			queryOptions = 0
		}
		
		//alert(queryFinal)

		getList(1, sort_current, queryRaw, queryCol, queryOptions)


	})



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