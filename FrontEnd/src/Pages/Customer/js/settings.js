// ------------------- Setting -------------------

const setting = () => {
    $.ajax({
        url: API_URL + "/RoadRescue/customer?option=VIEW",
        method: "GET",
        contentType: "application/json",
        beforeSend: function(request) {
            request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
        },
        success: function (res) {
            if (res.status == 200) {
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
                $('#number').text(res.data.mobileNo);
            }
        }, error: function (ob, textStatus, error) {
            alert(textStatus);
        }
    });
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

const phoneSettingSubmit = (e, formData) => {
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
                        const changePhoneNumberData = {
                            mobileNo: phone,
                            option: "CHANGE_PHONE"
                        };
                        // Login and get token
                        $.ajax({
                            url: API_URL + "/RoadRescue/customer",
                            method: "PUT",
                            contentType: "application/json",
                            beforeSend: function(request) {
                                request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
                            },
                            data: JSON.stringify(changePhoneNumberData),
                            success: function (res) {
                                alert(res.message);
                                if (res.status == 200) {
                                    setting();
                                } else {
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