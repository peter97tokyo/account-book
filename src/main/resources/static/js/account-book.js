
function popupWallet(year, month, day) {

    function pad(num) {
        return num.toString().padStart(2, '0');
    }

    var formattedDate = year + "-" + pad(month) + "-" + pad(day);
    var dateInput = document.getElementById("choosedDate");

    if (dateInput) {
        dateInput.value = formattedDate;
    }

    var popup = document.getElementById("popup");
    var style = window.getComputedStyle(popup);
    
    if (style.display === "none") {
        popup.style.display = "block";
    } else {
        popup.style.display = "none";
    }

    return false;
}



function validateForm() {
    const form = document.getElementById("walletForm");

    var moneyInput = form.money.value;

    if (moneyInput == "" || moneyInput < 1 || moneyInput == null) {
        alert('input money or needs bigger then 0')
        return false;
    }
    return true;
}

window.onload = function() {
    
    document.getElementById("walletForm").onsubmit = function() {
        return validateForm();
    }
};