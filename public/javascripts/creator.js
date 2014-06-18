$(document).ready(function(){

	$("#next_update_at_string").datetimepicker({ minDateTime: new Date() })

	//For adding new subscriptions
	$("#addSubscription").on("click", function(){

		//Get high index number
		var allInputs = $("#subscriptions").find("select")

		var highestIndexNumber = -1 //-1 is no inputs, 0 is one input

		//Find highest index number
		allInputs.each(function(index){
			var indexNumber = parseInt($(this).attr("index"))
			if (indexNumber > highestIndexNumber){
				highestIndexNumber = indexNumber
			}
		}
		)


		var template = $("#subscription_template")


		//Modify selects
		template.find("select").each(function(){

			$(this).attr("name", "subscriptions[" + (highestIndexNumber+1) + "]" )
			$(this).attr("index", (highestIndexNumber+1))
		}
		)
		//Modify remove link
		template.find("a").each(function(){
			$(this).attr("index", (highestIndexNumber+1))
		})


		$("#subscriptions").append(
			//htmlTemplate
			template.html()
		)
	})

	//Removing
	//For live functionality. Click evenst won't bind to dynamically
	//created elements unless using this format
	$("#subscriptions").on("click", "a" ,function(e){
		var index = parseInt($(this).attr("index"))
		//Remove selector field
		$("#subscriptions").find("select[index="+ index + "]").each(function(){
			$(this).parentsUntil($(".subscription")).parent().remove()

		})


		rename()

	})


	function rename(){
		//When selectors are deleted, indexes may not be contiguous anymore
		//Must rename indexes

		var numberOfSubscriptions = $("#subscriptions").find("select").size()


		$("#subscriptions").find("select").each(function(index, value){
			$(this).attr("index", index).attr("name", "subscriptions[" + index + "]" )
		})

		$("#subscriptions").find("a").each(function(index, value){
			$(this).attr("index", index)
		})


	}



})