$.ajax({
    url: "http://localhost:8099/order/products",
    type: "GET",
    success: function (response) {
        if(response.code == "00") {
            rederData(response.data);
        }else {
            toastr.error('Find not data!', response.message);
        }
    },
    error: function (result) {
        toastr.error('có lỗi xảy ra . Xin vui lòng thử lại', response.message);
    }
});


function rederData(data) {
    $("#list-product").empty();
    data.map(item => {
        $('#list-product').append(
        `<div class="card col-6 col-sm-6 col-md-4 col-lg-3 col-xl-3" style="width: 100%;">
            <img src="https://cf.shopee.vn/file/110be78c00d495ae468f1d1bd9f51815" class="card-img-top" alt="...">
            <div class="card-body" >
                <h5 class="card-title">${item.id}</h5>
                <h5 class="card-title">${item.name}</h5>
                <p class="card-text">$${item.price}</p>
                <button class="btn btn-primary" onclick="addToCastDB('${item.id}')">Add to cart</button>
            </div>
        </div>`);
    });
}

function rederDataCast(data) {
    $('#list-item-cast').empty();
    data.map(item => {
        $('#list-item-cast').append(
            `<div class="card col-6 col-sm-6 col-md-4 col-lg-3 col-xl-3" style="width: 100%;">
            <img src="https://cf.shopee.vn/file/110be78c00d495ae468f1d1bd9f51815" class="card-img-top" alt="...">
            <div class="card-body" >
                <h5 class="card-title">${item.id}</h5>
                <h5 class="card-title">${item.name}</h5>
                <p class="card-text">$${item.price}</p>
                <div class="btn-group my-2 d-block" role="group" aria-label="Basic example">
                  <button type="button" onclick="deleteToCastDB(${item.id})" class="btn btn-light">-</button>
                  <input type="number" style="width: 50px; padding: 0px;" value="${item.number}" id="number-product" class="btn"></input>
                  <button type="button" onclick="addToCastDB(${item.id})" class="btn btn-light">+</button>
                </div>
                <button class="btn btn-primary" onclick="deleteToCastDB('${item.id}')">Delete</button>
            </div>
        </div>`);
    });
}

getProductInCast();
function getProductInCast() {
    $.ajax({
        url: "http://localhost:8099/order/products/hiep",
        type: "GET",
        success: function (response) {
            if(response.code = '00') {
                cart = response.data;
                if(cart.listProduct != null) {
                    getTotalProductInCast(cart);
                    if(cart.listProduct[0] != null) {
                        getPriceProductInCast(cart);
                        rederDataCast(cart.listProduct);
                    }
                }
            }
        },
        error: function (error) {
            toastr.error('có lỗi xảy ra . Xin vui lòng thử lại', response.message);
        }
    });
}

function addToCastDB(idProduct) {
    let updateCastRequest = {
        name: "hiep",
        listProductCast: [{
            id: idProduct,
            number: 1,
            type: 1
        }]
    };
    $.ajax({
        url: "http://localhost:8099/order/update/" + cart.id,
        type: "POST",
        data: JSON.stringify(updateCastRequest),
        contentType: "application/json",
        success: function (response) {
            if (response.code = "00") {
                cart = response.data;
                if(cart.listProduct != null) {
                    getTotalProductInCast(cart);
                    if(cart.listProduct[0] != null) {
                        getPriceProductInCast(cart);
                        rederDataCast(cart.listProduct);
                        toastr.error('Add product success!', "HAHA");
                    }
                }
            }
        },
        error: function (error) {
            toastr.error('có lỗi xảy ra . Xin vui lòng thử lại', response.message);
        }
    });
}

function paymentToCastDB(idProduct) {
    let updateCastRequest = {
        name: "hiep",
        listProductCast: [{
            id: idProduct,
            number: 1,
            type: 2
        }]
    };
    $.ajax({
        url: "http://localhost:8099/order/update/" + cart.id,
        type: "POST",
        data: JSON.stringify(updateCastRequest),
        contentType: "application/json",
        success: function (response) {
            if (response.code = "00") {
                cart = response.data;
                if(cart.listProduct != null) {
                    getTotalProductInCast(cart);
                    if(cart.listProduct[0] != null) {
                        getPriceProductInCast(cart);
                        rederDataCast(cart.listProduct);
                        toastr.error('Pay product success!',  "HAHA");
                    }
                }
            }
        },
        error: function (error) {
            toastr.error('có lỗi xảy ra . Xin vui lòng thử lại', response.message);
        }
    });
}

function deleteToCastDB(idProduct) {
    let updateCastRequest = {
        name: "hiep",
        listProductCast: [{
            id: idProduct,
            number: 1,
            type: 2
        }]
    };
    $.ajax({
        url: "http://localhost:8099/order/update/" + cart.id,
        type: "POST",
        data: JSON.stringify(updateCastRequest),
        contentType: "application/json",
        success: function (response) {
            if (response.code = "00") {
                cart = response.data;
                if(cart.listProduct != null) {
                    getTotalProductInCast(cart);
                    if(cart.listProduct[0] != null) {
                        getPriceProductInCast(cart);
                        rederDataCast(cart.listProduct);
                        toastr.error('Delete product success!',  "HAHA");
                    }
                }
            }
        },
        error: function (error) {
            toastr.error('có lỗi xảy ra . Xin vui lòng thử lại', response.message);
        }
    });
}