
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


function openSidebar(year, month, day) {
    document.getElementById("contentSidebar").classList.add("open");
    var choosedDate = year + "-" + month + "-" + day
    $.ajax({
        url: "/wallet/subList",   
        type: "GET",          
        data: { choosedDate: choosedDate },      
        dataType: "html",     
        success: function(response) {
            $("#walletHistory").html(response);
        },
        error: function(xhr, status, error) {
            console.error("error:", error);
        }
    });
    
}

function closeSidebar() {
    document.getElementById("contentSidebar").classList.remove("open");
}

function deleteWallet(id) {
    
    if (!confirm("are you sure? do you delete this history?")) return;

    $.ajax({
        url: "/wallet/delete",
        type: "DELETE",
        contentType: "application/json",
        data: JSON.stringify({ id: id }),
        success: function (result) {
            alert(result); 
            location.reload(); 
        },
        error: function (xhr, status, error) {
            alert("Fail delete: " + error);
        }
    });
}

function popupWalletUpdtFrm(id) {
    $.ajax({
        url: "/wallet/updateForm",
        type: "POST",
        dataType: "html",        
        data: { id: id },        
        success: function (result) {
            $("#popupUpdtForm").html(result);
        },
        error: function (xhr, status, error) {
            alert("Fail update form: " + error);
        }
    });
}

function closePopupUpdtForm() {
    $("#popupUpdtForm").html("");
}

function showToast(e) {
    var mouseX = e.pageX;
    var mouseY = e.pageY;
    console.log('클릭 위치: X=' + mouseX + ', Y=' + mouseY);
}

window.onload = function() {
    $("#walletForm").on("submit", function(e) {
        e.preventDefault(); 

        const moneyInput = $.trim(this.money.value);
        const money = Number(moneyInput);

        if (!moneyInput || isNaN(money) || money < 1) {
            alert("Please input a valid amount (greater than 0).");
            return;
        }

        const formData = $(this).serializeArray();
        const jsonData = {};
        $.each(formData, function(_, field) {
            jsonData[field.name] = field.value;
        });

        $.ajax({
            url: "/wallet/save",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify(jsonData),
            success: function() {
                location.reload();
                alert('Successfully saved.')
            },
            error: function(err) {
                console.error(err);
            }
        });
    });
    
    
    $(".showToast").click(function(e) {
        var mouseX = e.pageX;
        var mouseY = e.pageY;
        var title = $(this).attr('title');
        $('#toastBody').html(title)
        $('#toast').addClass('show')
        $("#toast").offset({ top: mouseY, left: mouseX });
    });
    
    const excelBtn = document.getElementById("excelBtn");
    if (excelBtn) {
        excelBtn.addEventListener("click", () => {
            const form = document.getElementById('searchForm');
            form.action = '/wallet/excel';
            form.submit();
            form.action = '/wallet/list';
        });
    }
    
};


$(document).on("submit", "#walletUpdtFrm", function(e) {
    e.preventDefault(); 

    const moneyInput = $.trim(this.money.value);
    const money = Number(moneyInput);

    if (!moneyInput || isNaN(money) || money < 1) {
        alert("Please input a valid amount (greater than 0).");
        return;
    }

    const formData = $(this).serializeArray();
    const jsonData = {};
    $.each(formData, function(_, field) {
        jsonData[field.name] = field.value;
    });

    $.ajax({
        url: "/wallet/update",
        method: "PUT",
        contentType: "application/json",
        data: JSON.stringify(jsonData),
        success: function() {
            alert('Successfully updated.');
            location.reload();
        },
        error: function(err) {
            console.error(err);
        }
    });
});
