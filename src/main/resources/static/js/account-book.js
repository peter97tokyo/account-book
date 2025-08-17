
function popupWallet(year, month, day) {

    function pad(num) {
        return num.toString().padStart(2, '0');
    }

    var formattedDate = year + "-" + pad(month) + "-" + pad(day);
    var dateInput = document.getElementById("choosedDate");

    if (dateInput) {
        dateInput.value = formattedDate;
    }

    var popupForm = document.getElementById("popupForm");
    var style = window.getComputedStyle(popupForm);
    
    if (style.display === "none") {
        popupForm.style.display = "block";
    } else {
        popupForm.style.display = "none";
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

function openSidebar() {
    document.getElementById("contentSidebar").classList.add("open");
}

function closeSidebar() {
    document.getElementById("contentSidebar").classList.remove("open");
}

window.onload = function() {
    
    document.getElementById("walletForm").onsubmit = function() {
        return validateForm();
    }
};