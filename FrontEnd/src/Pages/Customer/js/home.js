$(document).ready(function () {
  const currentCustomer = localStorage.getItem("customer");
  if (!!currentCustomer) {
    const customer = JSON.parse(currentCustomer);
    $('#welcomeCustomer').text("Welcome, " + customer.firstName + " " + customer.lastName);
  }
});