// ----------------------- Vehicle ----------------------
const vehicle = () => {
  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle?option=GETALL",
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
        if ("content" in document.createElement("template")) {
          const vehicleContainer =
            document.getElementById("vehicleContainer") ||
            document.getElementById("addVehicle") ||
            document.getElementById("editVehicle");
          if (!!vehicleContainer) {
            const template = document.getElementById("vehicleTemplate");
            if (!!template) {
              const clone = template.content.cloneNode(true);
              vehicleContainer.replaceWith(clone);
            }
          }
        }

        const vehicleTable = document.getElementById("viewVehicleTable");
        const trVehicleTable = vehicleTable.getElementsByTagName("tr");
        removeHtmlElement(trVehicleTable);

        const vehicles = res.data;
        if (vehicles) {
          var html = '<tr>';
          html += "<th>Vehicle</th>"
          html += "<th>Number Plate</th>"
          html += "<th>Type</th>"
          html += "<th></th>"
          html += "</tr>"

          for (const vehicle of vehicles) {
            html += '<tr>';
            html += '<td>' + vehicle.makeName + " " + vehicle.modelName + " " + vehicle.year +'</td>'
            html += '<td>' + vehicle.plateNum +'</td>'
            html += '<td>' + vehicle.type +'</td>'
            html += '<td style="padding: 0 0 0 5px"><img src="images/edit2.svg" data-plateNum="' + vehicle.plateNum + '" onclick="showEditVehicle(event)"/>' +
                '<img src="images/delete.svg" data-plateNum="' + vehicle.plateNum + '" onclick="deleleVehicle(event)"/></td>'
            html += '</tr>';
          }

          vehicleTable.innerHTML = html;
        }
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};

const showEditVehicle = (event) => {
  const plateNum = event.target.getAttribute("data-plateNum");
  if (plateNum) {
    $.ajax({
      url: "http://localhost:8082/RoadRescue/vehicle?option=VIEW&plateNum=" + plateNum,
      method: "GET",
      contentType: "application/json",
      beforeSend: function(request) {
        request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
      },
      success: function (res) {
        if (res.status == 200) {
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

          const vehicle = res.data;
          $.ajax({
            url: "http://localhost:8082/RoadRescue/vehicle/make",
            method: "GET",
            contentType: "application/json",
            beforeSend: function(request) {
              request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
            },
            success: function (resMake) {
              if (resMake.status == 200) {
                const makeSelect = document.getElementById("make");
                const optionMakes = makeSelect.getElementsByTagName("option");
                removeHtmlElement(optionMakes);

                const allMakes = resMake.data;
                if (allMakes) {
                  var html = '';
                  for (const make of allMakes) {
                    html += '<option ' + (vehicle.makeId == make.id ? 'Selected' : "") + ' value="' + make.id + '">' + make.makeName + '</option>';
                  }
                  makeSelect.innerHTML = html;
                }
              }
            }, error: function (ob, textStatus, error) {
              alert(textStatus);
            }
          });

          $.ajax({
            url: "http://localhost:8082/RoadRescue/vehicle/model?makeId=" + vehicle.makeId,
            method: "GET",
            contentType: "application/json",
            beforeSend: function(request) {
              request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
            },
            success: function (res) {
              if (res.status == 200) {
                const modelSelect = document.getElementById("model");
                const optionModels = modelSelect.getElementsByTagName("option");
                removeHtmlElement(optionModels);

                const allModels = res.data;
                if (allModels) {
                  var html = '';
                  for (const model of allModels) {
                    html += '<option ' + (vehicle.modelId == model.id ? 'Selected' : "") + ' value="' + model.id + '">' + model.modelName + '</option>';
                  }
                  modelSelect.innerHTML = html;
                }
              }
            }, error: function (ob, textStatus, error) {
              alert(textStatus);
            }
          });

          const selectYear = document.getElementById("year");
          const optionYears = selectYear.getElementsByTagName("option");
          for (const option of optionYears) {
            if (option.value == vehicle.year) {
              option.selected = true;
              break;
            }
          }

          $('#numberPlate').val(vehicle.plateNum);

          const selectType = document.getElementById("type");
          const optionTypes = selectType.getElementsByTagName("option");
          for (const option of optionTypes) {
            if (option.value == vehicle.type) {
              option.selected = true;
              break;
            }
          }

        }
      }, error: function (ob, textStatus, error) {
        alert(textStatus);
      }
    });
  }
};

const deleleVehicle = (e) => {
  const plateNum = e.target.getAttribute("data-plateNum");
  if (plateNum) {
    const res = confirm("Do you want to delete this vehicle?");
    if (res) {
      $.ajax({
        url: "http://localhost:8082/RoadRescue/vehicle?plateNum=" + plateNum,
        method: "DELETE",
        contentType: "application/json",
        beforeSend: function(request) {
          request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
        },
        success: function (res) {
          alert(res.message);
          if (res.status == 200) {
            vehicle();
          }
        }, error: function (ob, textStatus, error) {
          alert(textStatus);
        }
      });
    }
  }
};

const addVehicle = () => {
  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle/make",
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
        if ("content" in document.createElement("template")) {
          const vehicleContainer = document.getElementById("vehicle");
          if (!!vehicleContainer) {
            const template = document.getElementById("addVehicleTemplate");
            if (!!template) {
              const clone = template.content.cloneNode(true);
              vehicleContainer.replaceWith(clone);
            }
          }
        }

        const makeSelect = document.getElementById("make");
        const optionMakes = makeSelect.getElementsByTagName("option");
        removeHtmlElement(optionMakes);

        const allMakes = res.data;
        if (allMakes) {
          var html = '<option selected disabled hidden value="">Select make</option>';

          for (const make of allMakes) {
            html += '<option value="' + make.id + '">' + make.makeName + '</option>';
          }
          makeSelect.innerHTML = html;
        }
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};

const selectMake = () => {
  const makeId = document.getElementById("make").value;
  if (!makeId) {
    return;
  }

  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle/model?makeId=" + makeId,
    method: "GET",
    contentType: "application/json",
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      if (res.status == 200) {
        const modelSelect = document.getElementById("model");
        const optionModels = modelSelect.getElementsByTagName("option");
        removeHtmlElement(optionModels);

        const allModels = res.data;
        if (allModels) {
          var html = '<option selected disabled hidden value="">Select the model</option>';

          for (const model of allModels) {
            html += '<option value="' + model.id + '">' + model.modelName + '</option>';
          }
          modelSelect.innerHTML = html;
        }
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
}

const vehicleSubmit = (e, formData) => {
  e.preventDefault();
  const makeId = formData?.make?.value;
  const modelId = formData?.model?.value;
  const year = formData?.year?.value;
  const numberPlate = formData?.numberPlate?.value;
  const type = formData?.type?.value;
  if (!makeId) {
    alert("Please choose make");
    return;
  } else if (!modelId) {
    alert("Please choose model");
    return;
  } else if (!year) {
    alert("Please choose year");
    return;
  } else if (!numberPlate) {
    alert("Please enter the number plate");
    return;
  } else if (!type) {
    alert("Please choose type");
    return;
  }
  const data = {
    makeId: makeId,
    modelId: modelId,
    year: year,
    plateNum: numberPlate,
    type: type
  }

  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle",
    method: "POST",
    contentType: "application/json",
    data: JSON.stringify(data),
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      alert(res.message);
      if (res.status == 200) {
        vehicle();
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};

const editVehicleSubmit = (e, formData) => {
  e.preventDefault();

  const makeId = formData?.make?.value;
  const modelId = formData?.model?.value;
  const year = formData?.year?.value;
  const numberPlate = formData?.numberPlate?.value;
  const type = formData?.type?.value;
  if (!makeId) {
    alert("Please choose make");
    return;
  } else if (!modelId) {
    alert("Please choose model");
    return;
  } else if (!year) {
    alert("Please choose year");
    return;
  } else if (!numberPlate) {
    alert("Please enter the number plate");
    return;
  } else if (!type) {
    alert("Please choose type");
    return;
  }

  const data = {
    makeId: makeId,
    modelId: modelId,
    year: year,
    plateNum: numberPlate,
    type: type
  }

  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle",
    method: "PUT",
    contentType: "application/json",
    data: JSON.stringify(data),
    beforeSend: function(request) {
      request.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
    },
    success: function (res) {
      alert(res.message);
      if (res.status == 200) {
        vehicle();
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
}

// -----------------------------------