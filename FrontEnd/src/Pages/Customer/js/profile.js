// ----------------------- Profile ----------------------

const profile = () => {
  $.ajax({
    url: "http://localhost:8082/RoadRescue/customer?option=VIEW",
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
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
        $('#profileFirstName').html(res.data.firstName);
        $('#profileLastName').html(res.data.lastName);
        const elementMobileNo = res.data.mobileNo + '<img style="float: right" src="images/check.svg" />';
        $('#profileMobileNo').html(elementMobileNo);
        $('#profileEmail').html(res.data.email);
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};

const editProfile = () => {
  $.ajax({
    url: "http://localhost:8082/RoadRescue/customer?option=VIEW",
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
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
        $('#firstName').html(res.data.firstName);
        $('#lastName').html(res.data.lastName);
        const elementMobileNo = res.data.mobileNo + '<img style="float: right" src="images/check.svg" />';
        $('#profileMobileNo').html(elementMobileNo);
        $('#email').html(res.data.email);
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
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

  const firstName = formData?.firstName?.value;
  const lastName = formData?.lastName?.value;
  const email = formData?.email?.value;

  if (!firstName) {
    alert("Please enter first name");
    return;
  } else if (!lastName) {
    alert("Please enter last name");
    return;
  }
  const data = {
    firstName: firstName,
    lastName: lastName,
    email: email
  }

  $.ajax({
    url: "http://localhost:8082/RoadRescue/customer",
    method: "PUT",
    contentType: "application/json",
    data: JSON.stringify(data),
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
        alert(res.message);
        window.location.href = "profile.html";
        profile();
      } else {
        alert(res.data);
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};