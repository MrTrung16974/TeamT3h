let perPage =  prompt("Số diễn viên bạn muốn trong một trang là  !!!!", '');
let keyword = "";
console.log(perPage);

$.ajax({
    url: "http://localhost:8083/v1/api/actor/search?page=0&perPage="+ perPage +"&name=" + keyword,
    success: function (result) {
        if(result.code == '00') {
            $("#lst-product").empty();
            rederData(result.data.content);
        }else {
            alert(result.message);
        }
    },
    error: function (result) {
        console.log("error " + result);
    }
});

function loadData() {
    $("#lst-product").empty();
    $.ajax({
        url: "http://localhost:8083/v1/api/actors",
        success: function (result) {
            console.log("data response", result);
            if(result.code == '00') {
                rederData(result.data);
            }else {
                alert(result.message);
            }
        },
        error: function (result) {
            console.log("error " + result);
        }
    });
}

function rederData(data) {
    data.map(item => {
        $('#lst-product').append(
        `<div class="card">
                  <img width="200" height="250" src="https://saobiz.net/wp-content/uploads/2016/11/ngoc-trinh-trang-da-blogtamsuvn5_01.jpg"
                  alt="Denim Jeans" style="width:100%">
                  <h3>${item.name}</h3>
                  <p class="price">${item.age} Tuổi</p>
                  <p>${item.country}</p>                  
                  <p><button>Add to Cart</button></p>
                  <p><button onclick="detailActor(${item.id})" id="detailActor" data-toggle="modal"
         data-target="#modalAddProduct">Detail</button></p>
                  <p><button onclick="deleteProduct(${item.id})" >Delete</button></p>
                </div>`);
});
}


function detailActor(idActor) {
    $.ajax({
        type:"GET",
        url:"http://localhost:8083/v1/api/actor/"+ idActor,
        processData: false,
        contentType: 'application/json',
        success: function(result){
            // server trả về HTTP status code là 200 => Thành công
            //hàm đc thực thi khi request thành công không có lỗi
            let id = result.data.id;
            let name = result.data.name;
            let country = result.data.country;
            $("#editActorId").val(id);
            $("#editActorName").val(name);
            $("#editActorConutry").val(country);
        }
    });
}

function paginationActor(page) {
    keyword = $('#actorName').val().trim().toLocaleLowerCase();
    if (keyword == null) {
        keyword = '';
    }
    $.ajax({
        type: "GET",
        url: "http://localhost:8083/v1/api/actor/search?page=" + page + "&perPage="+ perPage +"&name=" + keyword,
        processData: false,
        contentType: 'application/json',
        success: function (result) {
            $("#lst-product").empty();
            // server trả về HTTP status code là 200 => Thành công
            //hàm đc thực thi khi request thành c ông không có lỗi
            if(result.code == "00") {
                rederData(result.data.content);
                $("#totalPage").val(page);
            }
            else {
                alert(result.message);
            }
        }
    });
}

function searchActor() {
    let keyword = $('#actorName').val().trim().toLocaleLowerCase();
    if (keyword == null) {
        keyword = '';
    }
    $.ajax({
        type: "GET",
        url: "http://localhost:8083/v1/api/actor/search?page=0&perPage="+ perPage +"&name=" + keyword,
        processData: false,
        contentType: 'application/json',
        success: function (result) {
            $("#lst-product").empty();
            // server trả về HTTP status code là 200 => Thành công
            //hàm đc thực thi khi request thành công không có lỗi
            if(result.code =="00") {
                $("#pagination").empty();
                for(let i = 0; i < result.total; i++) {
                    let page = i+1;
                    $("#pagination").append("<a onclick='paginationActor("+ i +")' >"+ page +"</a>");
                }
                rederData(result.data.content);
            }else {
                console.log("Error " + result.message)
            }
        }
    });
}

function addProduct() {
    let dataJson = {
        name: $("#createNameActor").val().trim(),
        age: $("#createAgeActor").val().trim(),
        country: $("#createActorConutry").val().trim()
    };
    $.ajax({
        url: "http://localhost:8083/v1/api/actor",
        type: "POST",
        data: JSON.stringify(dataJson),
        contentType: "application/json",
        success: function (result) {
            if(result.code = "00") {
                $('#modalAddActor').modal('hide');
                $("#createNameActor").val("");
                $("#createAgeActor").val("");
                $("#createActorConutry").val("");
                loadData();
                console.log("response" + result.message);
                toastr.error('Bạn đã thêm sản phẩm mới thành công!', 'HAHA!');
            }else {
                toastr.error('Bạn đã thêm sản phẩm mới không thành công!', 'KEKE!');
            }
        },
        error: function (result) {
            toastr.error('Lỗi hệ thống!', 'HUHU!');
            console.log("error " + result.message);
        }
    });
}

function editProduct() {
    let updateData = {
        name: $("#editActorName").val(),
        country: $("#editActorConutry").val().trim()
    }
    $.ajax({
        url: "http://localhost:8083/v1/api/actor/" + $("#editActorId").val(),
        type: "PUT",
        data: JSON.stringify(updateData),
        contentType: "application/json",
        success: function (result) {
            console.log("response" + result.message);
            toastr.error('Bạn đã sửa thành công!', 'HAHA!');
            loadData();
        },
        error: function (result) {
            toastr.error('Lỗi hệ thống!', 'HUHU!');
            console.log("error " + result.message);
        }
    });
}

function deleteProduct(idActor) {
    let idea =  confirm("Bạn chắc muốn xóa chớ ngáo đó !!!!");
    if(idea) {
        $.ajax({
            url: "http://localhost:8083/v1/api/actor/" + idActor,
            type: "DELETE",
            success: function (result) {
                console.log("response" + result.message);
                toastr.error('Bạn đã xóa thành công', 'HAHA!');
                loadData()
            },
            error: function (result) {
                console.log("error " + result.message);
                toastr.error('Lỗi hệ thống!', 'HUHU!');
            }
        });
    }
}
