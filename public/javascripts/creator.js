$(document).ready(function(){

	$("#next_update_at_string").datetimepicker({ minDateTime: new Date() })

	// subsciptions to new, unsaved incidents get submitted to the backend
	// with the rest of the incident data
	$("#newAddSubscription").on("click", function(){
		var team_id = parseInt($("#newSubscriptionSelector").val())
		$("#newSubscriberList")
			.append( $("<li />", {
				'class' : 'newSubscriber',
			}).append( $("<a />", {
				'class': 'removeSubscription',
				'id': 'removeSubscription-' + team_id,
				'value': team_id
			}).append( $("<span />", {
				'class': 'glyphicon glyphicon-remove'
			})
		)))
		$("<a />", {
			'href': '/team/' + team_id,
			'text': teamMap[team_id]
		}).insertAfter("#removeSubscription-" + team_id)
		$("<input />", {
			'type': 'hidden',
			'name': 'subscriptions[' + team_id + ']',
			'value': team_id
		}).insertAfter("#removeSubscription-" + team_id)
		
	})

	$("#newSubscriber").on("click", "a" ,function(e){
		var team_id = parseInt($(this).attr("value"))
		console.log("Removing subscription " + team_id)
		$(this).parentsUntil($(".newSubscriber")).remove()
	})

	$("#addSubscription").on("click", function(){
		var ids = $("#subscriptionSelector").val().split(":")
		var incident_id = parseInt(ids[0])
		var team_id = parseInt(ids[1])
		var url = jsRoutes.controllers.IncidentEditor.addIncidentSubscription(incident_id, team_id).ajax({
			success: function(data){
				location.reload()
			},
			error: function(err){
				//alert("err")
			}
		})
	})

	$(document).on('click', ".removeSubscription", function(e) {
		var ids = $(this).attr("value").split(":")
		var incident_id = parseInt(ids[0])
		var team_id = parseInt(ids[1])
		if (ids.length == 2) {
			// if we're editing subscriptions that've already been established, call
			// the backend
			var url = jsRoutes.controllers.IncidentEditor.deleteIncidentSubscription(incident_id, team_id).ajax({
				success: function(data){
					location.reload()
				},
				error: function(err){
					//alert("err")
				}
			})
		}
		$(this).parent().remove()
	})

})