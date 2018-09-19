document.addEventListener("DOMContentLoaded", function () {
    //Слежение за соединениями
    // setInterval(function () {
    //     var xhr = new XMLHttpRequest();
    //     var block = document.getElementById("unclosedcon");
    //
    //     function reqReadyStateChange() {
    //         if (xhr.readyState == 4) {
    //             var status = xhr.status;
    //             if (status == 200) {
    //                 var count = xhr.responseText;
    //                 console.log(count);
    //                 block.innerText = count;
    //             } else {
    //                 alert("error");
    //             }
    //         }
    //     }
    //
    //     xhr.open("POST", "unclosed", true);
    //     xhr.onreadystatechange = reqReadyStateChange;
    //     xhr.send();
    // }, 5000);
    ordersObj = null;
    userObj = null;
    //After load page
    showOrders();


//Change menu item
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'menu_item')) {
            var item = e.target;
            var txt = item.innerText;
            var title = document.getElementsByClassName("header-title");
            title[0].innerHTML = txt;
            var siblings = document.querySelectorAll("div.menu_item_active");
            for (i = 0; i < siblings.length; ++i) {
                siblings[i].classList.remove("menu_item_active");
            }
            item.classList.add("menu_item_active");
        }
    }, false);

//Delete product
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'product_del')) {
            var productRow = e.target.parentNode.parentNode;
            var id = productRow.querySelector(".pr_id").innerText;
            sendID(id, success, error, "command", "product", "delete");

            function success() {
                showProducts();
            }

            function error() {
                alert("Ошибка при удалении, попробуйте еще раз");
            }
        }
    }, false);

//Change status of order
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'change_status')) {
            var productRow = e.target.parentNode.parentNode;
            var id = productRow.querySelector(".pr_id").innerText;
            sendID(id, success, error, "command", "product", "status");

            function success() {
                showProducts();
            }

            function error() {
                alert("Ошибка при изменении статуса, попробуйте еще раз");
            }
        }
    }, false);

//Change status of user
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'user_change')) {
            var userRow = e.target.parentNode.parentNode;
            var id = userRow.querySelector(".user_id").innerText;
            var user_status = userRow.querySelector(".user_status").innerText;
            var tmpStatus = "";
            switch (user_status) {
                case "0": {
                    tmpStatus = "statusActive";
                    break;
                }
                case "1": {
                    tmpStatus = "statusBlackList";
                    break;
                }
                case "2": {
                    tmpStatus = "statusBanned";
                    break;
                }
            }

            sendID(id, success, error, "command", "user", tmpStatus);

            function success() {
                showUsers();
            }

            function error() {
                alert("Ошибка при изменении статуса пользователя, попробуйте еще раз");
            }
        }
    }, false);

//Change status of order
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'order_status')) {
            var orderRow = e.target.parentNode.parentNode;
            var id = orderRow.querySelector(".order_id").innerText;
            sendID(id, success, error, "command", "order", "status");

            function success() {
                showOrders();
            }

            function error() {
                alert("Ошибка при изменении статуса заказа, попробуйте еще раз");
            }
        }
    }, false);

//Edit product
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'product_edit')) {
            var item = e.target;
            var productRow = item.parentNode.parentNode;
            var id = productRow.querySelector(".pr_id").innerText;
            var title = productRow.querySelector(".pr_title").innerText;
            var desc = productRow.querySelector(".pr_desc").innerText;
            var price = productRow.querySelector(".pr_price").innerText;
            var amount = productRow.querySelector(".pr_amount").innerText;
            var status = productRow.querySelector(".pr_status").innerText;
            var p_title = document.createElement("p");
            p_title.innerText = "Наименование";
            var p_desc = document.createElement("p");
            p_desc.innerText = "Описание";
            var p_price = document.createElement("p");
            p_price.innerText = "Цена";
            var p_amount = document.createElement("p");
            p_amount.innerText = "Количество";
            var p_status = document.createElement("p");
            p_status.innerText = "Статус";
            var modalform = document.createElement("form");
            var mymodal = document.querySelector(".mymodal");
            mymodal.innerHTML = "<div class='close'>&times;</div>";
            modalform.id = "modalform";
            modalform.className = "modalform";
            var input_id = document.createElement("input");
            var input_title = document.createElement("input");
            var input_desc = document.createElement("textarea");
            var input_price = document.createElement("input");
            var input_amount = document.createElement("input");
            var input_status = document.createElement("input");
            var input_btn = document.createElement("input");

            input_id.type = "hidden";
            input_title.type = "text";
            input_desc.type = "text";
            input_price.type = "text";
            input_amount.type = "text";
            input_status.type = "text";
            input_id.name = "id";
            input_title.name = "title";
            input_desc.name = "desc";
            input_price.name = "price";
            input_amount.name = "amount";
            input_status.name = "status";
            input_id.required = "true";
            input_title.required = "true";
            input_desc.required = "true";
            input_price.required = "true";
            input_amount.required = "true";
            input_status.required = "true";

            input_btn.type = "submit";
            input_btn.value = "Сохранить";
            var inputs = [input_id, p_title, input_title, p_desc, input_desc, p_price, input_price, p_amount, input_amount, p_status, input_status, input_btn];
            input_id.value = id;
            input_title.value = title;
            input_desc.value = desc;
            input_desc.rows = 5;
            input_price.value = price;
            input_amount.value = amount;
            input_status.value = status;
            for (key in inputs) {
                modalform.appendChild(inputs[key]);
            }
            mymodal.appendChild(modalform);

            modalform.addEventListener("submit", function (event) {
                event.preventDefault();
                sendData(modalform, success, error, "editProduct");
            });

            function success() {
                closeModal();
                showProducts();
            }

            function error() {
                alert("Ошибка при редактировании, попробуйте еще раз");
            }

            closeModal();
            showModal();

        }
    }, false);


// Edit user
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'user_edit')) {
            console.log("User edit");
            var item = e.target;
            var userRow = item.parentNode.parentNode;
            var login = userRow.querySelector(".user_login").innerText;
            var name = userRow.querySelector(".user_name").innerText;
            var surname = userRow.querySelector(".user_surname").innerText;
            var email = userRow.querySelector(".user_email").innerText;
            var phone = userRow.querySelector(".user_phone").innerText;
            var address = userRow.querySelector(".user_address").innerText;
            var role = userRow.querySelector(".user_role").innerText;
            var p_name = document.createElement("p");
            p_name.innerText = "Имя";
            var p_surname = document.createElement("p");
            p_surname.innerText = "Фамилия";
            var p_email = document.createElement("p");
            p_email.innerText = "Почта";
            var p_phone = document.createElement("p");
            p_phone.innerText = "Телефон";
            var p_address = document.createElement("p");
            p_address.innerText = "Адрес";
            var p_role = document.createElement("p");
            p_role.innerText = "Роль";
            var modalform = document.createElement("form");
            var mymodal = document.querySelector(".mymodal");
            mymodal.innerHTML = "<div class='close'>&times;</div>";
            modalform.id = "userform";
            modalform.className = "modalform";
            var input_login = document.createElement("input");
            var input_name = document.createElement("input");
            var input_surname = document.createElement("input");
            var input_email = document.createElement("input");
            var input_phone = document.createElement("input");
            var input_address = document.createElement("textarea");
            var input_role = document.createElement("input");
            var input_btn = document.createElement("input");
            input_login.type = "hidden";
            input_name.type = "text";
            input_surname.type = "text";
            input_email.type = "email";
            input_phone.type = "phone";
            input_role.type = "text";
            input_btn.type = "submit";
            input_login.name = "login";
            input_name.name = "name";
            input_surname.name = "surname";
            input_email.name = "email";
            input_phone.name = "phone";
            input_address.name = "address";
            input_role.name = "role";
            input_login.required = "true";
            input_name.required = "true";
            input_surname.required = "true";
            input_email.required = "true";
            input_phone.required = "true";
            input_address.required = "true";
            input_role.required = "true";
            input_login.value = login;
            input_name.value = name;
            input_surname.value = surname;
            input_email.value = email;
            input_phone.value = phone;
            input_address.value = address;
            input_address.rows = 2;
            input_role.value = role;
            input_btn.value = "Сохранить";
            var array = [input_login, p_name, input_name, p_surname, input_surname, p_email, input_email, p_phone, input_phone, p_address, input_address, input_btn];
            for (key in array) {
                modalform.appendChild(array[key]);
            }
            mymodal.appendChild(modalform);
            showModal();
            modalform.addEventListener("submit", function (event) {
                event.preventDefault();
                sendData(modalform, success, error, "editUser");
            });

            function success() {
                closeModal();
                showUsers();
            }

            function error() {
                alert("Ошибка при редактировании, попробуйте еще раз");
            }
        }
    }, false);

// Menu orders
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'order_look')) {
            var item = e.target;
            var productRow = item.parentNode.parentNode;

            var id = productRow.querySelector(".order_id").innerText;
            for (i in ordersObj) {
                if (ordersObj[i]["id"] == id) {
                    var produrtslist = ordersObj[i]["products"];
                    var name = ordersObj[i]["username"];
                    var table = document.createElement("table");
                    table.style.width = "100%";
                    table.innerHTML = "<tbody id='tbody'><tr>\n" +
                        "<th>Наименование</th>\n" +
                        "<th>Количество</th>\n" +
                        "<th>Цена за ед.</th>\n" +
                        "<th>Итого</th>\n" +
                        "</tr>\n" +
                        "</tbody>";
                    for (p in produrtslist) {
                        var title = produrtslist[p]["product"]["title"];
                        var amount = produrtslist[p]["amount"];
                        var price = produrtslist[p]["product"]["price"];
                        var product_row = document.createElement("tr");
                        var or_title = document.createElement("td");
                        var or_amount = document.createElement("td");
                        var or_price = document.createElement("td");
                        var or_total = document.createElement("td");
                        or_title.innerText = title;
                        or_amount.innerText = amount + "шт";
                        or_price.innerText = price + "р.";
                        or_total.innerText = price * amount + "р.";
                        // or_title.innerText = title;
                        product_row.appendChild(or_title);
                        product_row.appendChild(or_amount);
                        product_row.appendChild(or_price);
                        product_row.appendChild(or_total);
                        table.appendChild(product_row);
                    }

                }
            }
            var divhead = document.createElement("div");
            divhead.className = "head_orders";
            divhead.innerHTML = "<p>Заказ №" + id + "</p>" + "<p>" + name + "</p>";

            var mymodal = document.querySelector(".mymodal");
            mymodal.innerHTML = "<div class='close'>&times;</div>";
            mymodal.appendChild(divhead);

            mymodal.appendChild(table);

            showModal();
        }
    }, false);


// Menu product
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'products')) {
            showProducts();
        } else if (hasClass(e.target, 'orders')) {
            ordersObj = showOrders();
        } else if (hasClass(e.target, 'users')) {
            userObj = showUsers();
        }
    }, false);

}); // DOCUMENT READY


function showProducts() {
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                var productObj = JSON.parse(data);
                console.log(productObj);
                var content_wr = document.getElementById("admin_content");
                var table = document.createElement("table");

                table.style.width = "100%";
                content_wr.innerText = "";
                content_wr.appendChild(table);
                var tbody = document.createElement("tbody");
                table.appendChild(tbody);
                tbody.id = "tbody";
                tbody.innerHTML = "<tr>\n" + "<th>ID</th>\n" +
                    "<th>Наименование</th>\n" +
                    "<th>Описание</th>\n" +
                    "<th>Цена</th>\n" +
                    "<th>Количество</th>\n" +
                    "<th>Статус</th>\n" +
                    "<th colspan='3'>Действие</th>\n" +
                    "</tr>";

                console.log(tbody);
                for (var key in productObj) {
                    var id = productObj[key]['id'];
                    var title = productObj[key]['title'];
                    var desc = productObj[key]['desc'];
                    var price = productObj[key]['price'];
                    var amount = productObj[key]['amount'];
                    var pstatus = productObj[key]['status'];
                    var prwr = document.createElement("tr");
                    tbody.appendChild(prwr);
                    var td = document.createElement("td");
                    var td2 = document.createElement("td");
                    var td3 = document.createElement("td");
                    var td4 = document.createElement("td");
                    var td5 = document.createElement("td");
                    var td6 = document.createElement("td");
                    var tde = document.createElement("td");
                    var tds = document.createElement("td");
                    var bte = document.createElement("button");
                    bte.className = "product_edit";
                    var btd = document.createElement("button");
                    btd.className = "product_del";
                    var bts = document.createElement("button");
                    bts.className = "change_status";
                    (pstatus == 0) ? bts.classList.add("active_st") : bts.classList.add("unactive_st");
                    var tdd = document.createElement("td");
                    tde.appendChild(bte);
                    tds.appendChild(bts);
                    tdd.appendChild(btd);
                    td.innerText = id;
                    td.className = "pr_id";
                    td2.innerText = title;
                    td2.className = "pr_title";
                    td3.innerText = desc;
                    td3.className = "pr_desc";
                    td4.innerText = price;
                    td4.className = "pr_price";
                    td5.innerText = amount;
                    td5.className = "pr_amount";
                    td6.innerText = pstatus;
                    td6.className = "pr_status";
                    prwr.appendChild(td);
                    prwr.appendChild(td2);
                    prwr.appendChild(td3);
                    prwr.appendChild(td4);
                    prwr.appendChild(td5);
                    prwr.appendChild(td6);
                    prwr.appendChild(tde);
                    prwr.appendChild(tds);
                    prwr.appendChild(tdd);
                }
                var myform = document.createElement("form");
                myform.id = "addproduct";
                myform.className = "addproduct";
                var title_input = document.createElement("input");
                title_input.placeholder = "Наименование";
                title_input.name = "title";
                title_input.required = "true";
                var desc_input = document.createElement("textarea");
                desc_input.placeholder = "Описание";
                desc_input.name = "desc";
                desc_input.rows = 3;
                desc_input.required = "true";
                var price_input = document.createElement("input");
                price_input.placeholder = "Цена";
                price_input.name = "price";
                price_input.required = "true";
                var amount_input = document.createElement("input");
                amount_input.placeholder = "Количество";
                amount_input.name = "amount";
                amount_input.required = "true";
                var subm = document.createElement("input");
                subm.type = "submit";
                subm.value = "Добавить";
                myform.appendChild(title_input);
                myform.appendChild(desc_input);
                myform.appendChild(price_input);
                myform.appendChild(amount_input);
                myform.appendChild(subm);
                content_wr.insertBefore(myform, content_wr.firstChild);
                myform.addEventListener("submit", function (event) {
                    event.preventDefault();
                    sendData(this, success, error, "addProduct");
                });

                function success() {
                    showProducts();
                }

                function error() {
                    alert("error");
                }
            } else {
            }
        }
    }

    xhr.open("POST", "allProducts", true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.send();
}

function showOrders() {
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                ordersObj = JSON.parse(data);
                console.log(ordersObj);
                var content_wr = document.getElementById("admin_content");
                var table = document.createElement("table");

                table.style.width = "100%";
                content_wr.innerText = "";
                content_wr.appendChild(table);
                var tbody = document.createElement("tbody");
                tbody.id = "tbody";
                table.innerHTML = "<tr>" + "<th>№ Заказа</th>" +
                    "<th>Имя покупателя</th>" +
                    "<th>Статус</th>" +
                    "<th>Дата добавления</th>" +
                    "<th>Итого</th>" +
                    "<th colspan='2'>Действие</th>" +
                    "</tr>";
                for (var key in ordersObj) {
                    var id = ordersObj[key]['id'];
                    var username = ordersObj[key]['username'];
                    var date = ordersObj[key]['date'];
                    var vstatus = ordersObj[key]['status'];
                    var orderrow = document.createElement("tr");
                    var productslist = ordersObj[key]["products"];
                    var total = 0;
                    for (pr in productslist) {
                        var amount = productslist[pr]["amount"];
                        var price = productslist[pr]["product"]["price"];
                        total = total + price * amount;
                    }
                    tbody.appendChild(orderrow);
                    var td_id = document.createElement("td");
                    var td_username = document.createElement("td");
                    var td_date = document.createElement("td");
                    var td_total = document.createElement("td");
                    var td_vstatus = document.createElement("td");
                    var td_look = document.createElement("td");
                    var td_cancel = document.createElement("td");
                    var btn_look = document.createElement("button");
                    btn_look.className = "order_look";
                    var btn_cancel = document.createElement("button");
                    btn_cancel.className = "order_status";
                    if (vstatus == "PROCESSED") {
                        btn_cancel.classList.add("order_processed");
                    } else if (vstatus == "PAID") {
                        btn_cancel.classList.add("order_paid");
                    } else btn_cancel.classList.add("order_cancelled");
                    td_look.appendChild(btn_look);
                    td_cancel.appendChild(btn_cancel);
                    td_id.innerText = id;
                    td_id.className = "order_id";
                    td_total.innerText = total;
                    td_total.className = "order_total";
                    td_username.innerText = username;
                    td_username.className = "order_username";
                    td_date.innerText = date;
                    td_date.className = "order_date";
                    td_vstatus.innerText = vstatus;
                    td_vstatus.className = "order_vstatus";
                    td_look.className = "textcenter";
                    td_cancel.className = "textcenter";
                    var array = [td_id, td_username, td_vstatus, td_date, td_date, td_total, td_look, td_cancel];
                    for (k in array) {
                        orderrow.appendChild(array[k]);
                    }
                }
                console.log("order tbody");
                console.log(tbody);
                table.appendChild(tbody);
                return ordersObj;
            }
        }
    }

    var send = "model=orders";
    xhr.open("POST", "showModels", true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    xhr.send(send);
}

function showUsers() {
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                var usersObj = JSON.parse(data);
                console.log(usersObj);
                var content_wr = document.getElementById("admin_content");
                var table = document.createElement("table");
                table.style.width = "100%";
                content_wr.innerText = "";
                content_wr.appendChild(table);
                var tbody = document.createElement("tbody");
                tbody.id = "tbody";
                table.innerHTML = "<tr>\n" + "<th>ID</th>\n" +
                    "<th>Логин</th>\n" +
                    "<th>Имя</th>\n" +
                    "<th>Фамилия</th>\n" +
                    "<th>Почта</th>\n" +
                    "<th>Телефон</th>\n" +
                    "<th>Адрес</th>\n" +
                    "<th>Роль</th>\n" +
                    "<th>Статус</th>\n" +
                    "<th>Дата регистрации</th>\n" +
                    "<th colspan='2'>Действие</th>\n" +
                    "</tr>";
                for (var key in usersObj) {
                    var id = usersObj[key]['id'];
                    var login = usersObj[key]['login'];
                    var name = usersObj[key]['name'];
                    var surname = usersObj[key]['surname'];
                    var email = usersObj[key]['email'];
                    var phone = usersObj[key]['phone'];
                    var address = usersObj[key]['address'];
                    var role = usersObj[key]['role'];
                    var us_status = usersObj[key]['status'];
                    var date = usersObj[key]['registered'];
                    var user_row = document.createElement("tr");
                    tbody.appendChild(user_row);
                    var td_id = document.createElement("td");
                    var td_login = document.createElement("td");
                    var td_name = document.createElement("td");
                    var td_surname = document.createElement("td");
                    var td_email = document.createElement("td");
                    var td_phone = document.createElement("td");
                    var td_address = document.createElement("td");
                    var td_role = document.createElement("td");
                    var td_status = document.createElement("td");
                    var td_date = document.createElement("td");
                    var td_edit = document.createElement("td");
                    var td_change = document.createElement("td");
                    var btn_edit = document.createElement("button");
                    btn_edit.className = "user_edit";
                    var btn_change = document.createElement("button");
                    btn_change.className = "user_change";
                    if (us_status == 0) {
                        btn_change.classList.add("user_simple");
                    } else if (us_status == 1) {
                        btn_change.classList.add("user_black_list");
                    } else btn_change.classList.add("user_banned");
                    td_edit.appendChild(btn_edit);
                    td_edit.className = "textcenter";
                    td_change.appendChild(btn_change);
                    td_change.className = "textcenter";
                    td_id.innerText = id;
                    td_id.className = "user_id";
                    td_name.innerText = name;
                    td_name.className = "user_name";
                    td_surname.innerText = surname;
                    td_surname.className = "user_surname";
                    td_login.innerText = login;
                    td_login.className = "user_login";
                    td_email.innerText = email;
                    td_email.className = "user_email";
                    td_phone.innerText = phone;
                    td_phone.className = "user_phone";
                    td_address.innerText = address;
                    td_address.className = "user_address";
                    td_role.innerText = role;
                    td_role.className = "user_role";
                    td_status.innerText = us_status;
                    td_status.className = "user_status";
                    td_date.innerText = date;
                    td_date.className = "user_date";
                    var array = [td_id, td_login, td_name, td_surname, td_email, td_phone, td_address, td_role, td_status, td_date, td_edit, td_change];
                    for (k in array) {
                        user_row.appendChild(array[k]);
                    }
                    tbody.appendChild(user_row);
                }
                table.appendChild(tbody);
                return usersObj;
            }
        }
    }

    var send = "model=users";
    xhr.open("POST", "showModels", true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    xhr.send(send);
}

function sendData(form, fsucc, ferr, servlet) {
    var xhr = new XMLHttpRequest();
    var data = new FormData(form);
    data = data.entries();
    var obj = data.next();
    var retrieved = {};
    while (obj.value !== undefined) {
        retrieved[obj.value[0]] = obj.value[1];
        obj = data.next();
    }
    var urlEncodedData = "";
    var urlEncodedDataPairs = [];
    for (var name in retrieved) {
        urlEncodedDataPairs.push(encodeURIComponent(name) + '=' + encodeURIComponent(retrieved[name]));
    }
    urlEncodedData = urlEncodedDataPairs.join('&').replace(/%20/g, '+');

    xhr.addEventListener('load', function (event) {
        fsucc(event.target.responseText);
    });

    xhr.addEventListener('error', function () {
        ferr();
    });

    xhr.open('POST', servlet);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    xhr.send(urlEncodedData);
}

function sendID(id, fsucc, ferr, servlet, model, command) {
    var xhr = new XMLHttpRequest();
    var str = "id=" + id + "&model=" + model + "&command=" + command;

    xhr.addEventListener('load', function (event) {
        fsucc(event.target.responseText);
    });

    xhr.addEventListener('error', function () {
        ferr();
    });

    xhr.open('POST', servlet);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
    xhr.send(str);
}

function hasClass(elem, className) {
    return elem.classList.contains(className);
}

function closeModal() {
    var modal = document.querySelector(".mymodal");
    modal.style.display = "none";
    var hider = document.querySelector(".hider");
    hider.style.display = "none";
}

function showModal() {
    var modal = document.querySelector(".mymodal");
    var hider = document.querySelector(".hider");
    hider.style.display = "block";
    modal.style.display = "block";
}

//Close modal
document.addEventListener('click', function (e) {
    if (hasClass(e.target, 'close')) {
        closeModal();
    }
}, false);