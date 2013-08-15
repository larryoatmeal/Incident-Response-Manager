$(document).ready(function(){

	var user_id = $("#user_id").attr("value")

	//Add team
	$("#add_team").on("click", function(){
		var team_id = $("#team_select").find(':selected')[0].value

		jsRoutes.controllers.UserInfo.addTeamMap(user_id, team_id).ajax({
			success: function(){
				location.reload()
			},
			error: function(){
			}

		})


	})

	//Remove team
	$(".remove").on("click", function(){
		var team_id = $(this).attr("value")
		jsRoutes.controllers.TeamInfo.deleteUserMap(user_id, team_id).ajax({
			success: function(){
				location.reload()
			},
			error: function(){

			}
		})
	})



})