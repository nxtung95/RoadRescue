const toggleMenu = () => {
  var loader = document.getElementById("menu");
  loader.classList.toggle("hide");
};

const toggleProfileMenu = () => {
  var loader = document.getElementById("profileMenu");
  loader.classList.toggle("hide");
};

const toggleNotificationMenu = () => {
  var loader = document.getElementById("helpMenu");
  loader.classList.toggle("hide");
};

const loadGarage = () => {

  document.querySelector(".activityButton").classList.remove("disabled");
  document.querySelector(".navActions").classList.remove("disableEvent");

  if ("content" in document.createElement("template")) {
    const garageSection = document.getElementById("garageContainer") || document.getElementById("garageForm");
    if(!!garageSection)
    {
      const template = document.getElementById("garageListTemplate");
      const clone = template.content.cloneNode(true);
      
      garageSection.replaceWith(clone);
      
      const buttons = document.querySelectorAll(".requestBtn");
      for (let i = 0; i < buttons?.length; i++) {
        buttons[i].disabled = false;
        buttons[i].classList?.remove("disabled");
      }
    }
  }
};

const toogleCheck = (el) => {
  if (el.getAttribute("src") === "images/check.svg") {
    el.src = "images/cross.svg";
  } else {
    el.src = "images/check.svg";
  }
};

const checkAll = () => {
  var checks = document.querySelectorAll(".checkCross");
  for (let i = 0; i < checks.length; i++) {
    checks[i].src = "images/check.svg";
  }
};

const checkInfo = () => {
  if ("content" in document.createElement("template")) {
    const garageSection = document.getElementById("garageList");
    const template = document.getElementById("garageInfoTemplate");
    const clone = template.content.cloneNode(true);

    garageSection.replaceWith(clone);
  }
};

const backInfo = () => {
  if ("content" in document.createElement("template")) {
    const garageSection = document.getElementById("garageInfo");
    const template = document.getElementById("garageListTemplate");
    const clone = template.content.cloneNode(true);

    garageSection.replaceWith(clone);
  }
};

const serviceForm = () => {

  const url = window.location.href;

  if(url.includes("index.html"))
  {
    document.getElementById("menu").classList.add("hide");
    if ("content" in document.createElement("template")) {
      const garageSection = document.getElementById("garageList") || document.getElementById("garageInfo");
      if (!!garageSection) {

        const buttons = document.querySelectorAll(".requestBtn");
        for (let i = 0; i < buttons?.length; i++) {
          buttons[i].disabled = true;
          buttons[i].classList?.add("disabled");
        }

        
        document.querySelector(".activityButton").classList.add("disabled");
        document.querySelector(".navActions").classList.add("disableEvent");

  
        const template = document.getElementById("serviceFormTemplate");
        const clone = template?.content?.cloneNode(true);
  
        garageSection.replaceWith(clone);
      }
    }
  }
  else
  {
    window.location.href = "index.html"
  }
};

const rate = () => {
  const template = document.getElementById("ratingTemplate");
  const clone = template?.content?.cloneNode(true);
  document.getElementById("payService").replaceWith(clone);
};

const payService = () => {
  const template = document.getElementById("payTemplate");
  const clone = template?.content?.cloneNode(true);
  document.getElementById("servicesProvided").replaceWith(clone);

  setTimeout(() => {
    rate();
  }, 3000);
};

const servicesProvided = () => {
  const template = document.getElementById("servicesProvidedTemplate");
  const clone = template?.content?.cloneNode(true);
  document.getElementById("providingServices").replaceWith(clone);

  setTimeout(() => {
    payService();
  }, 3000);
};

const providingSerices = () => {
  const template = document.getElementById("providingServicesTemplate");
  const clone = template?.content?.cloneNode(true);
  document.getElementById("garageAccepted").replaceWith(clone);

  setTimeout(() => {
    servicesProvided();
  }, 3000);
};

const requestAccepted = () => {
  const template = document.getElementById("requestAcceptedTemplate");
  const clone = template?.content?.cloneNode(true);
  document.getElementById("garageRequest").replaceWith(clone);

  setTimeout(() => {
    providingSerices();
  }, 3000);
};

const requestFailded = () => {
  const template = document.getElementById("requestFailedTemplate");
  const clone = template?.content?.cloneNode(true);

  document.getElementById("garageRequest").replaceWith(clone);
  const buttons = document.querySelectorAll(".requestBtn");
  for (let i = 0; i < buttons?.length; i++) {
    buttons[i].disabled = false;
    buttons[i].classList?.remove("disabled");

    document.querySelector(".activityButton").classList.remove("disabled");
  document.querySelector(".navActions").classList.remove("disableEvent");
  }
};

const requestService = (e, formData) => {
  e.preventDefault();
  if ("content" in document.createElement("template")) {
    const garageSection = document.getElementById("garageForm");
    if (!!garageSection) {
      console.log("asdsad");
      const template = document.getElementById("requestTemplate");
      const clone = template?.content?.cloneNode(true);

      garageSection.replaceWith(clone);

      console.log("requesting");

      setTimeout(() => {
        const success = true; // Garage request successful or failed.

        if (success) {
          requestAccepted();
        } else {
          requestFailded();
        }
      }, 2000);
    }
  }
};

const reset = () => {
  if ("content" in document.createElement("template")) {
    const garageSection = document.getElementById("garageFailed");
    if (!!garageSection) {
      const template = document.getElementById("garageListTemplate");
      const clone = template.content.cloneNode(true);

      garageSection.replaceWith(clone);
    }
  }
};

// ----------------------- Help Request ----------------------

const requestInfo = (el, id) => {
  if (el.getAttribute("src") === "images/info.svg") {
    el.src = "images/return.svg";
  } else {
    el.src = "images/info.svg";
  }

  const help = parseInt(id.split("-")[1]) - 1;
  const activities = document.querySelectorAll(".helpRequest");

  for (let i = 0; i < activities.length; i++) {
    if (i !== help) {
      activities[i].classList.toggle("hide");
    }
  }

  document.getElementById(id).querySelector(".help").classList.toggle("expand");
  document
    .getElementById(id)
    .querySelector(".helpInfo")
    .classList.toggle("hide");
};

// ----------------------- Activities ----------------------

const activitiyInfo = (el, id) => {
  if (el.getAttribute("src") === "images/info.svg") {
    el.src = "images/return.svg";
  } else {
    el.src = "images/info.svg";
  }

  const activity = parseInt(id.split("-")[1]) - 1;
  const activities = document.querySelectorAll(".activities");

  for (let i = 0; i < activities.length; i++) {
    if (i !== activity) {
      activities[i].classList.toggle("hide");
    }
  }

  document
    .getElementById(id)
    .querySelector(".activity")
    .classList.toggle("expand");
  document
    .getElementById(id)
    .querySelector(".activityInfo")
    .classList.toggle("hide");
};

const navigateHelp = (e) => {
  if(!!e)
    e.preventDefault();
  window.location.href = "help.html";
}

// ----------------------- Message ----------------------

const openChat = (el, id) => {
  if (el.getAttribute("src") === "images/messages.svg") {
    el.src = "images/return.svg";
  } else {
    el.src = "images/messages.svg";
  }

  const message = parseInt(id.split("-")[1]) - 1;
  const messages = document.querySelectorAll(".messages");

  for (let i = 0; i < messages.length; i++) {
    if (i !== message) {
      messages[i].classList.toggle("hide");
    }
  }

  document
    .getElementById(id)
    .querySelector(".message")
    .classList.toggle("expand");
  document
    .getElementById(id)
    .querySelector(".chatBox")
    .classList.toggle("hide");
};

const sendMessage = (e) => {
  e.preventDefault();
  alert("Message Sent");
};

const navigateMessage = () => {
  window.location.href = "message.html";
}

// ----------------------- Profile ----------------------

const profile = () => {
  if ("content" in document.createElement("template")) {
    const profileContainer =
      document.getElementById("profileContainer") ||
      document.getElementById("editProfile");
    if (!!profileContainer) {
      const template = document.getElementById("profileTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        profileContainer.replaceWith(clone);
      }
    }
  }
};

const editProfile = () => {
  if ("content" in document.createElement("template")) {
    const profileContainer = document.getElementById("Profile");
    if (!!profileContainer) {
      const template = document.getElementById("editProfileTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        profileContainer.replaceWith(clone);
      }
    }
  }
};

const editFirstName = () => {
  if ("content" in document.createElement("template")) {
    const firstName = document.getElementById("firstName");
    if (!!firstName) {
      const template = document.getElementById("firstNameTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        firstName.replaceWith(clone);
      }
    }
  }
};

const editLastName = () => {
  if ("content" in document.createElement("template")) {
    const lastName = document.getElementById("lastName");
    if (!!lastName) {
      const template = document.getElementById("lastNameTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        lastName.replaceWith(clone);
      }
    }
  }
};

const editEmail = () => {
  if ("content" in document.createElement("template")) {
    const email = document.getElementById("email");
    if (!!email) {
      const template = document.getElementById("emailTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        email.replaceWith(clone);
      }
    }
  }
};

const profileSubmit = (e, formData) => {
  e.preventDefault();
  alert("Profile Editted");
  profile();
};

// ----------------------- Vehicle ----------------------

const vehicle = () => {
  if ("content" in document.createElement("template")) {
    const vehicleContainer =
      document.getElementById("vehicleContainer") ||
      document.getElementById("editVehicle");
    if (!!vehicleContainer) {
      const template = document.getElementById("vehicleTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        vehicleContainer.replaceWith(clone);
      }
    }
  }
};

const editVehicle = (id) => {
  console.log("Edit Vehicle:", id);
  const data = document.getElementById(id).querySelectorAll("td");
  console.log(data[0].innerText);
  console.log(data[1].innerText);
  console.log(data[2].innerText);

  alert("Vehicle Editted");
};

const deleleVehicle = (id) => {
  console.log("Delete Vehicle", id);
  const data = document.getElementById(id).querySelectorAll("td");
  console.log(data[0].innerText);
  console.log(data[1].innerText);
  console.log(data[2].innerText);

  alert("Vehicle Deleted");
};

const addVehicle = () => {
  if ("content" in document.createElement("template")) {
    const vehicleContainer = document.getElementById("vehicle");
    if (!!vehicleContainer) {
      const template = document.getElementById("editVehicleTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        vehicleContainer.replaceWith(clone);
      }
    }
  }
};

const vehicleSubmit = (e, formData) => {
  e.preventDefault();
  alert("Vehicle Added");
  vehicle();
};

// -----------------------------------

window.addEventListener("click", function (e) {
  if (
    (!!document.querySelector(".profileIcon") &&
      !document.querySelector(".profileIcon").contains(e.target)) ||
    (!!document.querySelector(".profileMenu") &&
      document.querySelector(".profileMenu").contains(e.target))
  ) {
    document.getElementById("profileMenu").classList.add("hide");
  } else if (
    (!!document.querySelector(".helpIcon") &&
      !document.querySelector(".helpIcon").contains(e.target)) ||
    (!!document.querySelector(".helpMenu") &&
      document.querySelector(".helpMenu").contains(e.target))
  ) {
    document.getElementById("helpMenu").classList.add("hide");
  }
});

// ------------------- Setting -------------------

const setting = () => {
  if ("content" in document.createElement("template")) {
    const settingContainer =
      document.getElementById("settingContainer") ||
      document.getElementById("phoneMain") ||
      document.getElementById("paymentMain");
    if (!!settingContainer) {
      const template = document.getElementById("settingMainTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        settingContainer.replaceWith(clone);
      }
    }
  }
};

const phoneSetting = () => {
  if ("content" in document.createElement("template")) {
    const phoneContainer = document.getElementById("settingMain");
    if (!!phoneContainer) {
      const template = document.getElementById("phoneTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        phoneContainer.replaceWith(clone);
      }
    }
  }
};

const managePayment = () => {
  if ("content" in document.createElement("template")) {
    const paymentContainer =
      document.getElementById("settingMain") ||
      document.getElementById("addPaymentMain");

    if (!!paymentContainer) {
      const template = document.getElementById("paymentTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        paymentContainer.replaceWith(clone);
      }
    }
  }
};

const addPayment = () => {
  if ("content" in document.createElement("template")) {
    const paymentContainer = document.getElementById("paymentMain");
    if (!!paymentContainer) {
      const template = document.getElementById("addPaymentTemplate");
      if (!!template) {
        const clone = template.content.cloneNode(true);
        paymentContainer.replaceWith(clone);
      }
    }
  }
};

const deletePayment = () => {
  alert("Payment Method Deleted");
};

const addCard = (e) => {
  e.preventDefault();
  alert("Card Added");
};

// -------------------- User ---------------------

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
      url: "http://localhost:8082/RoadRescue/otp",
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
              url: "http://localhost:8082/RoadRescue/customer",
              method: "POST",
              contentType: "application/json",
              data: JSON.stringify(loginData),
              success: function (res) {
                if (res.status == 200) {
                  alert(res.message);
                  localStorage.setItem("token", res?.data?.token);
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

const logout = (e, formData) => {
  window.location.href = "signin.html";
};

const messageAlert = () => {
  let icon = document.querySelector(".messageAlert");

  if (icon.getAttribute("src") === "images/messages.svg") {
    icon.src = "images/messageAlert.svg";
  } else {
    icon.src = "images/messages.svg";
  }
};

const notificationAlert = () => {
  let icon = document.querySelector(".notificationAlert");

  if (icon.getAttribute("src") === "images/notifications.svg") {
    icon.src = "images/notificationAlert.svg";
  } else {
    icon.src = "images/notifications.svg";
  }
};

// -------------------- OTP ----------------------
const sendOTP = () => {
  const mobileNo = $('#phone').val();
  if (!mobileNo) {
    alert("Please enter your phone number!");
    return;
  }
  var data = {
    mobileNo: mobileNo,
    option: "SEND_OTP"
  }

  $.ajax({
    url: "http://localhost:8082/RoadRescue/otp",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    success: function (res) {
      if (res.status == 200) {
        alert(res.message);
      } else {
        alert(res.data);
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
}

const onlyNumberKey = (evt) => {
  // Only ASCII character in that range allowed
  var ASCIICode = (evt.which) ? evt.which : evt.keyCode
  if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
    return false;
  return true;
}