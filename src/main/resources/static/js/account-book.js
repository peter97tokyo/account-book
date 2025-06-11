
function saveReceipt(day) {
    const popup = document.getElementById("popup");
    const style = window.getComputedStyle(popup);

    if (style.display === "none") {
        popup.style.display = "block";
    } else {
        popup.style.display = "none";
    }
    return false;
}
