const submitRegistration = (e, formData) => {
  e.preventDefault();
  const phone = formData?.phone?.value;
  const otp = formData?.opt?.value;
  const firstName = formData?.firstName?.value;
  const lastName = formData?.lastName?.value;

  if (!phone) {
    alert("Please enter phone number");
    return;
  } else if (!otp || otp.length < 6) {
    alert("Please enter otp");
    return;
  } else if (!firstName) {
    alert("Please enter first name");
    return;
  } else if (!lastName) {
    alert("Please enter last name");
    return;
  }

  // Confirm OTP first
  if (otp.length >= 6) {
    const data = {
      mobileNo: phone,
      option: "CONFIRM_OTP",
      otp: otp
    }

    $.ajax({
      url: "http://localhost:8082/RoadRescue/otp",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        if (res.status == 200) {
          if (res.code === "00") {
            // Success
            const registrationData = {
              mobileNo: phone,
              firstName: firstName,
              lastName: lastName,
              option: "REGISTRATION"
            };
            // Create customer
            $.ajax({
              url: "http://localhost:8082/RoadRescue/customer",
              method: "POST",
              contentType: "application/json",
              data: JSON.stringify(registrationData),
              success: function (res) {
                if (res.status == 200) {
                  alert(res.message);
                  window.location.href = "signin.html";
                } else {
                  alert(res.data);
                  window.location.reload();
                }
              }, error: function (ob, textStatus, error) {
                alert(textStatus);
              }
            });
          } else {
            alert(res.message);
            if (res.code === "01" || res.code === "02") {
              window.location.reload();
            }
          }
        } else {
          $('#error').text(res.data);
        }
      }, error: function (ob, textStatus, error) {
        alert(textStatus);
      }
    });
  }
};