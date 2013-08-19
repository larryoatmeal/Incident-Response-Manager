$(document).ready(function(){

	var team_id = $("#team_id").attr("value")

	//Add user
	$("#add_user").on("click", function(){
		var user_id = $("#user_select").find(':selected')[0].value

		jsRoutes.controllers.TeamInfo.addUserMap(user_id, team_id).ajax({
			success: function(){
				location.reload()
			},
			error: function(){
			}

		})
	})
	
	//Remove user
	$(".remove").on("click", function(){
		var user_id = $(this).attr("value")
		jsRoutes.controllers.TeamInfo.deleteUserMap(user_id, team_id).ajax({
			success: function(){
				location.reload()
			},
			error: function(){

			}
		})
	})

	//Open up dropdowns initially
	$('.collapse').collapse()

})