
function saveWallet(year, month, day) {

    function pad(num) {
        return num.toString().padStart(2, '0');
    }

    var formattedDate = year + "-" + pad(month) + "-" + pad(day);
    console.log("Formatted date:", formattedDate);

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

