$(document).ready(function(){

	$(".delete").on("click", function(){
		var id = parseInt(this.id, 10)

		var url = jsRoutes.controllers.IncidentUpdate.markDeleted(id).ajax({
			success: function(data){
				//alert("data")
				location.reload()

			},
			error: function(err){
				//alert("err")
			}



		})		

	})


	// var url = jsRoutes.controllers.IncidentBrowser.getIncidents(
	// 		page,
	// 		sort,
	// 		query,
	// 		queryCol,
	// 		queryOptions
	// 	).url

	// 	location.href = url

})