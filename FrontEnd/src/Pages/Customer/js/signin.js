const submitLogin = (e, formData) => {
  e.preventDefault();

  const phone = formData?.phone?.value;
  const otp = formData?.opt?.value;

  if (!phone) {
    alert("Please enter phone number");
    return;
  } else if (!otp || otp.length < 6) {
    alert("Please enter otp");
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
      url: API_URL + "/RoadRescue/otp",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(data),
      success: function (res) {
        if (res.status == 200) {
          if (res.code === "00") {
            // Success
            const loginData = {
              mobileNo: phone,
              option: "LOGIN"
            };
            // Login and get token
            $.ajax({
              url: API_URL + "/RoadRescue/customer",
              method: "POST",
              contentType: "application/json",
              data: JSON.stringify(loginData),
              success: function (res) {
                if (res.status == 200) {
                  alert(res.message);
                  localStorage.setItem("token", res?.data?.token);
                  localStorage.setItem("customer", JSON.stringify(res?.data?.customer));
                  window.location.href = "index.html";
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
            if (res.code !== "03") {
              window.location.reload();
            }
          }
        } else {
          alert(res.data);
          window.location.reload();
        }
      }, error: function (ob, textStatus, error) {
        alert(textStatus);
      }
    });
  }
};