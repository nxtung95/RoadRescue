// ----------------------- Vehicle ----------------------

const vehicle = () => {
  $.ajax({
    url: "http://localhost:8082/RoadRescue/vehicle",
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
            document.getElementById("editVehicle");
          if (!!vehicleContainer) {
            const template = document.getElementById("vehicleTemplate");
            if (!!template) {
              const clone = template.content.cloneNode(true);
              vehicleContainer.replaceWith(clone);
            }
          }
        }

        const vehicleTable = $('#viewVehicleTable');
        const trVehicleTable = $('#vehicle');
        const vehicles = res.data.allVehicles;
        if (vehicles) {
          for (const vehicle in vehicles) {
            var html = '<tr id="vehicle-"' + vehicle.plateNum + '>';
            html += '<td>' + vehicle.model +'</td>'
            html += '<td>' + vehicle.plateNum +'</td>'
            html += '<td>' + vehicle.type +'</td>'
            // html += '<td style="padding: 0 0 0 5px"><img src="images/edit2.svg" onclick="editVehicle(' + 'vehicle-' + vehicle.plateNum +  + ')/><img src="images/delete.svg" onclick="deleleVehicle(\'vehicle-\'' + vehicle.plateNum + ')"/></td>'
            html += '</tr>';
          }
        }
      }
    }, error: function (ob, textStatus, error) {
      alert(textStatus);
    }
  });
};

const editVehicle = (id) => {
  console.log("Edit Vehicle:", id);
  const data = document.getElementById(id).querySelectorAll("td");
  console.log(data[0].innerText);
  console.log(data[1].innerText);
  console.log(data[2].innerText);

  // if ("content" in document.createElement("template")) {
  //   const vehicleContainer =
  //     document.getElementById("vehicle")
  //   if (!!vehicleContainer) {
  //     const template = document.getElementById("editVehicleTemplate");
  //     if (!!template) {
  //       const clone = template.content.cloneNode(true);
  //       vehicleContainer.replaceWith(clone);
  //     }
  //   }
  // }
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