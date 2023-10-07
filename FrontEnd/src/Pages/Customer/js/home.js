$(document).ready(function () {
  $.ajax({
    url: API_URL + "/RoadRescue/customer?option=VIEW",
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
        $('#welcomeCustomer').text("Welcome, " + res.data.firstName + " " + res.data.lastName);
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
});