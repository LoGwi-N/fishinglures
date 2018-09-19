localObj = local(localization, startFn);

function local(localization, callback) {
    console.log("local");
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                console.log("Localization load...");
                var data = xhr.responseText;
                localObj = JSON.parse(data);
                localization();
                callback();
            }
        }
    }

    xhr.open("POST", "/localization", true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.send();
}

var arrayFunctions = [
    function () {
        infoUser("infoUser")
    },
    function () {
        showProductsServlet('showProducts', "contentmain")
    },
    function () {
        loadOrdersByID("showUserOrders")
    }
];

function startFn() {
    for (i = 0; i < arrayFunctions.length; i++) {
        arrayFunctions[i]();
    }
}


function localization() {
    document.querySelector("h1").innerText = localObj["titleSite"];
    document.title = localObj["titleSite"];
    var menu = document.querySelector(".menu-item");
    if (menu.querySelector(".menu_title") != null) menu.querySelector(".menu_title").innerText = localObj["welcome"];
    if (menu.querySelector(".submit") != null) menu.querySelector(".submit").innerText = localObj["loginsite"];
    if (menu.querySelector(".movetoreg") != null) menu.querySelector(".movetoreg").innerText = localObj["reg"];
    if (menu.querySelector("input[name$='login']") != null) menu.querySelector("input[name$='login']").placeholder = localObj["login"];
    if (menu.querySelector("input[name$='password']") != null) menu.querySelector("input[name$='password']").placeholder = localObj["password"];

    if (menu.querySelector(".menu_login") != null) menu.querySelector(".menu_login").innerText = localObj["login"];
    if (menu.querySelector(".menu_name") != null) menu.querySelector(".menu_name").innerText = localObj["name"];
    if (menu.querySelector(".menu_surname") != null) menu.querySelector(".menu_surname").innerText = localObj["surname"];
    if (menu.querySelector(".menu_email") != null) menu.querySelector(".menu_email").innerText = localObj["email"];
    if (menu.querySelector(".menu_phone") != null) menu.querySelector(".menu_phone").innerText = localObj["phone"];
    if (menu.querySelector(".menu_address") != null) menu.querySelector(".menu_address").innerText = localObj["address"];
    if (menu.querySelector(".menu_date") != null) menu.querySelector(".menu_date").innerText = localObj["date"];
    if (menu.querySelector(".info_btn") != null) menu.querySelector(".info_btn").innerText = localObj["edit"];
    if (menu.querySelector(".logout") != null) menu.querySelector(".logout").innerText = localObj["logout"];
    if (document.querySelector(".cart_orders") != null) document.querySelector(".cart_orders").innerText = localObj["basket_orders"];
    if (document.getElementById("card-title") != null) document.getElementById("card-title").innerText = localObj["order_list"];

    var locale = localObj["locale"];
    document.querySelector('#lang [value="' + locale + '"]').selected = true;
}


document.addEventListener("DOMContentLoaded", function () {

    userId = null;
    userLogin = null;
    userName = null;
    userSurname = null;
    userEmail = null;
    userPhone = null;
    userAddress = null;
    userDate = null;
    userStatus = null;

    uri = document.location.pathname;

    var select = document.getElementById('lang'),
        curr = null;

    select.onchange = function () {
        var lang = this.value;
        var xhr = new XMLHttpRequest();
        var str = "lang=" + lang;
        xhr.addEventListener('load', function (event) {
            var txt = event.target.responseText;
            if (txt = "true") {
                location.reload();
            }
            console.log(txt);
        });
        xhr.open('POST', "changeLangServlet");
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        xhr.send(str);
    };

//Клик на кнопку оформить заказ
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'order')) {
            order("order");
        }
    }, false);

//Клик на кнопку отменить заказ
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'cancel')) {
            var idOrder = Number(e.target.id.replace(/\D+/g, ""));
            cancelOrder("cancelOrder", idOrder);
        }
    }, false);

//Клик на кнопку оплатить
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'pay')) {
            var idOrder = Number(e.target.id.replace(/\D+/g, ""));
            console.log("pay: " + idOrder);
            payOrder("payOrder", idOrder);
        }
    }, false);

//Клик на "В корзину"
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'activebtn')) {
            var id = Number(e.target.id.replace(/\D+/g, ""));
            var p = e.target.parentElement.parentElement.parentElement;
            var title = p.getElementsByClassName("title")[0].textContent;
            var count = parseInt(p.getElementsByClassName("count")[0].value);
            var price = p.getElementsByClassName("price")[0].textContent;
            price = Number(price.replace(/\D+/g, ""));
            addCartToSession("addCard", id, count, title, price);
        }
    }, false);

//Клик на кнопку удалить товар из корзины
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'del')) {
            var id = Number(e.target.id.replace(/\D+/g, ""));

            deleteProductOfCart(id, "/delProductOfCartServlet");
        }
    }, false);

    if (document.getElementById("edit") != null) {
        document.getElementById("edit").addEventListener("click", function (e) {
                document.getElementById("info-name").disabled = false;
                document.getElementById("info-surname").disabled = false;
                document.getElementById("info-address").disabled = false;
                document.getElementById("info-email").disabled = false;
                document.getElementById("info-phone").disabled = false;
                e.target.style.display = 'none';
                var confirm = document.createElement("input");
                confirm.className = "info_btn";
                confirm.value = localObj["saveChanges"];
                confirm.type = "submit";
                document.getElementById("info-user").appendChild(confirm);
            }
        );
    }

//Изменение значение в поле количество с клавиатуры
    document.addEventListener('keyup', function (e) {
        if (hasClass(e.target, 'count')) {
            e.target.value = parseInt(e.target.value);
            var c = e.target.value;
            mainparent = getInStock(e.target);
            if (c > mainparent) {
                e.target.value = mainparent;
                alert(localObj["lessProducts"] + " " + mainparent + localObj["pcs"] + ".");
            } else if (c < 1 || c == "NaN") {
                e.target.value = 1;
            }
        }
    }, false);

//Клик на плюс для увеличения количество товара в заказе
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'plus')) {
            var selector = e.target.parentNode.getElementsByClassName("count");
            var count = selector[0].value;
            mainparent = getInStock(e.target);
            if (count < mainparent) {
                count++;
                selector[0].value = count;
            } else {
                alert(localObj["lessProducts"] + " " + mainparent + localObj["pcs"] + ".");
            }
        }
    }, false);

//Клик на минус для уменьшения количество товара в заказе
    document.addEventListener('click', function (e) {
        if (hasClass(e.target, 'minus')) {
            var selector = e.target.parentNode.getElementsByClassName("count");
            var count = selector[0].value;
            if (count > 1) {
                count--;
                selector[0].value = count;
            }
        }
    }, false);

    function hasClass(elem, className) {
        return elem.classList.contains(className);
    }

    if (document.getElementById("info-user") != null) {
        window.addEventListener("load", function () {
            var form = document.getElementById("info-user");

            function sendData() {
                var xhr = new XMLHttpRequest();
                var formData = new FormData(form);

                var outputLog = {}, iterator = formData.entries(), end = false;
                var mydata;
                while (end == false) {
                    var item = iterator.next();
                    if (item.value != undefined) {
                        outputLog[item.value[0]] = item.value[1];
                        if (mydata == undefined) {
                            mydata = item.value[0] + "=" + encodeURIComponent(item.value[1]) + "&";
                        } else {
                            mydata += item.value[0] + "=" + encodeURIComponent(item.value[1]) + "&";
                        }

                    } else if (item.done == true) {
                        end = true;
                    }
                }


                xhr.addEventListener("load", function () {
                    location.reload();
                });
                xhr.addEventListener("error", function () {
                    alert('Oops! Something went wrong.');
                });
                xhr.open("POST", "/editUser", true);
                // xhr.setRequestHeader("Content-Type", "multipart/form-data");
                console.log("EDIT: " + mydata);
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                xhr.send(mydata);
            }


            form.addEventListener("submit", function (event) {
                event.preventDefault();
                sendData();
            });
        });
    }

});


// Custom functions

function getInStock(sel) {
    var par1 = sel.parentElement.parentElement.parentElement;
    var ch3 = par1.getElementsByClassName("instock")[0];
    var instockv = ch3.innerHTML;
    return parseInt(instockv);
}

function showProductsServlet(srvUrl, block) {
    var xhr = new XMLHttpRequest();
    console.log("showProductsServlet working...");

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                productObj = JSON.parse(data);
                for (var key in productObj) {
                    var title = productObj[key]['title'];
                    var desc = productObj[key]['desc'];
                    var amount = productObj[key]['amount'];
                    var price = productObj[key]['price'];
                    var id = productObj[key]['id'];
                    createProduct(id, title, desc, price, amount, block);
                }
            } else {
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.send();
}

function createProduct(id, title, desc, price, amount, block) {
    if (document.getElementById(block) == null) return;

    // Create Elements
    var product = document.createElement('div'); // product
    var product_wrap = document.createElement('div'); // product-wrap
    var ptitle = document.createElement('p'); // title
    var pdesc = document.createElement('p'); // desc
    var pprice = document.createElement('p'); // price
    var pamount = document.createElement('p'); // amount
    var topdiv = document.createElement('div');
    var btmdiv = document.createElement('div');
    var product_info = document.createElement('div');
    var info_instock = document.createElement('div');
    var product_btn = document.createElement('div');
    var count_wrap = document.createElement('div');
    var minus = document.createElement('button');
    var plus = document.createElement('button');
    var input = document.createElement('input');
    var btn = document.createElement('button');

    // Classes for elements
    product.className = 'product';
    product_wrap.className = 'product-wrap';
    product_info.className = 'product-info';
    info_instock.className = 'info-instock';
    product_btn.className = 'product-btn';
    count_wrap.className = 'count-wrap';
    input.className = 'count';
    minus.className = 'minus';
    plus.className = 'plus';
    ptitle.className = 'title';
    pdesc.className = 'desc';
    pprice.className = 'price';
    pamount.className = 'amount';
    btn.className = 'tocard';
    btn.id = 'pr' + id;

    // Add content to selectors
    ptitle.innerText = title;
    pdesc.innerText = desc;
    pprice.innerText = price + localObj["rub"];
    minus.innerText = '-';
    plus.innerText = '+';
    btn.innerText = localObj["to_basket"];
    input.value = 1;

    info_instock.appendChild(pamount);
    if (amount > 0) {
        var pinstock = document.createElement('p'); // amount
        pinstock.className = 'instock';
        pinstock.innerText = amount + ' ' + localObj["pcs"];
        pamount.classList.add("stock");
        pamount.innerText = localObj["in_stock"];
        info_instock.appendChild(pinstock);
        btn.classList.add("activebtn");
    } else {
        pamount.innerText = localObj["out_of_stock"];
        btn.classList.add("unactive");
    }
    if (document.getElementById("card") == null) {
        console.log("CARD = null");
        btn.className = "tocard unreg";
    }

    product_info.appendChild(info_instock);
    product_info.appendChild(pprice);
    count_wrap.appendChild(minus);
    count_wrap.appendChild(input);
    count_wrap.appendChild(plus);
    product_btn.appendChild(count_wrap);
    product_btn.appendChild(btn);
    topdiv.appendChild(ptitle);
    topdiv.appendChild(pdesc);
    btmdiv.appendChild(product_info);
    btmdiv.appendChild(product_btn);
    product_wrap.appendChild(topdiv);
    product_wrap.appendChild(btmdiv);
    product.appendChild(product_wrap);
    document.getElementById(block).appendChild(product);
}

function infoUser(srvUrl) {
    console.log("1) infoUser");
    if (document.getElementById("welcome") == null) return;
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                userObj = JSON.parse(data);
                console.log(userObj);
                userId = userObj['id'];
                userLogin = userObj['login'];
                console.log("userLogin: " + userLogin);
                userName = userObj['name'];
                userSurname = userObj['surname'];
                userEmail = userObj['email'];
                userPhone = userObj['phone'];
                userAddress = userObj['address'];
                userDate = userObj['registered'];
                userStatus = userObj['status'];
                console.log({
                    id: userId,
                    login: userLogin,
                    name: userName,
                    surname: userSurname,
                    email: userEmail,
                    phone: userPhone,
                    address: userAddress,
                    date: userDate,
                    status: userStatus
                });
                loadCardList("cart");
                var welcome = document.getElementById("welcome");
                welcome.append(localObj["hello"] + ", " + userName);
                document.getElementById("info-login").value = userLogin;
                document.getElementById("info-login2").value = userLogin;
                document.getElementById("info-name").value = userName;
                document.getElementById("info-surname").value = userSurname;
                document.getElementById("info-email").value = userEmail;
                document.getElementById("info-phone").value = userPhone;
                document.getElementById("info-address").value = userAddress;
                document.getElementById("info-date").value = userDate;
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.send();
}

// Cart object
function Cart(id, title, count, price) {
    this.id = id;
    this.title = title;
    this.count = count;
    this.price = price;
}

function addCartToSession(srvUrl, id, count, title, price) {
    console.log("addCartToSession working...");
    var xhr = new XMLHttpRequest();
    var cart = new Cart(id, title, count, price);
    json = JSON.stringify(cart);

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                console.log(data);
                loadCardList("cart");
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.send(json);
}

// Parse array of JSON
function parseArray(arr) {
    var newArr = [];
    for (var i = 0; i < arr.length; i++) {
        if (!Array.isArray(arr[i])) {
            newArr.push(arr[i]);
        }
        if (Array.isArray(arr[i])) {
            newArr = newArr.concat(parseArray(arr[i]));
        }
    }
    return newArr;
}

function deleteProductOfCart(id, srvUrl) {
    console.log("deleteProductOfCart: " + id);
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                console.log("data: " + data);
                loadCardList("cart");
            } else {
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.send(id);
}

function createCart(id, cart_title, cart_price, cart_count) {
    cart_item = document.createElement("li");
    cart_item.className = "cart-item";
    cart_left = document.createElement("div");
    cart_left.className = "cart-left";
    cart_right = document.createElement("div");
    cart_right.className = "cart-right";
    ptitle = document.createElement("p");
    ptitle.className = "cart__title";
    pprice = document.createElement("p");
    pprice.className = "cart__price";
    pcount = document.createElement("p");
    pcount.className = "cart__count";
    del = document.createElement("button");
    del.className = "del";
    del.id = "del" + id;
    del.innerHTML = "&times;";
    cart_left.appendChild(ptitle);
    cart_left.appendChild(pprice);
    cart_right.appendChild(pcount);
    cart_right.appendChild(del);
    cart_item.appendChild(cart_left);
    cart_item.appendChild(cart_right);
    ptitle.innerText = cart_title;
    pprice.innerText = cart_price + localObj["rub"];
    pcount.innerText = cart_count + localObj["pcs"];
    document.getElementById("cartlist").appendChild(cart_item);
}

//ORDER
function order(srvUrl) {
    console.log("fn order working...");
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                console.log(data);
                if (data == "success") {
                    location.reload();
                } else {
                    alert(localObj["notEnough"]);
                }
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.send();
}

//Show orders by UserID
function loadOrdersByID(srvUrl) {
    var container = document.getElementById("orders");
    if (container == null) return;
    console.log("loadOrdersByID working...");
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                ordersObj = JSON.parse(data);
                var orders_cancelled = 0;
                var orders_paid = 0;
                var orders_processed = 0;
                for (var key in ordersObj) {
                    var id = ordersObj[key]['id'];
                    var ostatus = ordersObj[key]['status'];
                    var products = ordersObj[key]['products'];
                    var date = ordersObj[key]['date'];
                    if (ostatus == "PAID") {
                        orders_paid++;
                    } else if (ostatus == "PROCESSED") {
                        orders_processed++;
                    } else {
                        orders_cancelled++;
                    }
                    //Create Order
                    e_order_item = document.createElement("li");
                    e_order_item.className = "order-item";
                    container.appendChild(e_order_item);
                    e_order_top = document.createElement("div");
                    e_num = document.createElement("p");
                    e_status = document.createElement("p");
                    e_date = document.createElement("p");

                    e_order_top.className = "order-top";
                    e_num.innerText = localObj["order"] + "№" + id;
                    var buttonsfl = true;
                    if (ostatus == "PROCESSED") {
                        console.log(ostatus);
                        e_order_item.classList.add("processed");
                        e_status.innerText = localObj["not_paided"];
                    } else if (ostatus == "PAID") {
                        e_order_item.classList.add("paid");
                        e_status.innerText = localObj["paided_order"];
                        buttonsfl = false;
                    } else {
                        e_order_item.classList.add("cancelled");
                        e_status.innerText = localObj["cancel_order"];
                        buttonsfl = false;
                    }
                    e_date.innerText = date;
                    e_order_info = document.createElement("div");
                    e_order_info.className = "order-info";
                    e_order_top.appendChild(e_num);
                    e_order_top.appendChild(e_date);
                    e_order_top.appendChild(e_status);
                    e_order_item.appendChild(e_order_top);
                    var total_price = 0;
                    for (var k in products) {
                        var pr = products[k]['product'];
                        var title = pr["title"];
                        var price = pr["price"];
                        var amount = products[k]['amount'];
                        total_price += price * amount;
                        e_order_pr = document.createElement("div");
                        e_order_pr.className = "order-pr";
                        e_title = document.createElement("p");
                        e_count = document.createElement("p");
                        e_price = document.createElement("p");
                        e_total = document.createElement("p");
                        e_title.className = "order-title";
                        e_count.className = "order-count";
                        e_price.className = "order-price";
                        e_total.className = "order-total";
                        e_title.innerText = title;
                        e_count.innerText = amount + localObj["pcs"];
                        e_price.innerText = price + localObj["rub"];
                        e_total.innerText = price * amount + localObj["rub"];
                        e_order_pr.appendChild(e_title);
                        e_order_pr.appendChild(e_count);
                        e_order_pr.appendChild(e_price);
                        e_order_pr.appendChild(e_total);
                        e_order_info.appendChild(e_order_pr);
                        e_order_item.appendChild(e_order_info);
                    }
                    e_total = document.createElement("div");
                    e_total.className = "total";
                    e_total.innerText = total_price + localObj["rub"];
                    e_order_info.appendChild(e_total);
                    if (buttonsfl) {
                        var pay = document.createElement("button");
                        var cancel = document.createElement("button");
                        pay.className = "pay";
                        pay.id = "pay" + id;
                        cancel.id = "cancel" + id;
                        cancel.className = "cancel";
                        pay.innerText = localObj["pay"];
                        cancel.innerText = localObj["cancel"];
                        e_order_info.appendChild(pay);
                        e_order_info.appendChild(cancel);
                    }
                }
                var cart_title = document.getElementById("card-title");
                cart_title.innerHTML = cart_title.innerText + " <span>(" + localObj["paided"] + ": <b>" + orders_paid + "</b> | " + localObj["processed"] + ": <b>" + orders_processed + "</b> | " + localObj["cancelled"] + ": <b>" + orders_cancelled + "</b>)</span>";


            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.send();
}

//Cancel Order
function cancelOrder(srvUrl, id) {
    var currentLocation = window.location.origin;
    console.log(currentLocation);
    console.log("cancelOrder working ...");
    console.log("cancel ID: " + id);
    var formData = new FormData();
    console.log(formData);
    formData.append("id", id);
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                if (data == "true") {
                    location.reload();
                } else {
                    alert(localObj["cancelError"]);
                }
            }
        }
    }

    srvUrl = currentLocation + '/' + srvUrl + '?id=' + id;
    xhr.open("GET", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    // xhr.setRequestHeader("Content-Type", "multipart/form-data; charset=UTF-8");
    xhr.send();
}

function payOrder(srvUrl, id) {
    var currentLocation = window.location.origin;
    console.log(currentLocation);
    console.log("payOrder working ...");
    console.log("payOrder ID: " + id);
    var formData = new FormData();
    console.log(formData);
    formData.append("id", id);
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                if (data == "true") {
                    location.reload();
                } else {
                    alert(localObj["orderError"]);
                }
            }
        }
    }

    srvUrl = currentLocation + '/' + srvUrl + '?id=' + id;
    xhr.open("GET", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    // xhr.setRequestHeader("Content-Type", "multipart/form-data; charset=UTF-8");
    xhr.send();
}

function loadCardList(srvUrl) {
    console.log("loadCardList working...");
    var xhr = new XMLHttpRequest();

    function reqReadyStateChange() {
        if (xhr.readyState == 4) {
            var status = xhr.status;
            if (status == 200) {
                var data = xhr.responseText;
                console.log("data: " + data);
                if (data != "null") {
                    var obj = JSON.parse(data);
                    listcarts = parseArray(obj);
                    price = 0;
                    allcount = 0;
                    for (i = 0; i < listcarts.length; i++) {
                        allcount = allcount + listcarts[i].count;
                        price = price + listcarts[i].price * listcarts[i].count;
                    }
                    document.getElementById("card-count").innerText = localObj["products"] + ": " + allcount;
                    cart_p = document.getElementById("cart_p");
                    cart_p.innerText = "";
                    span = document.createElement("span");
                    span.innerText = " " + localObj["rub"];
                    cart_p.innerText = price;
                    cart_p.appendChild(span);
                    cart_wr = document.getElementById("card");
                    cart_wr.appendChild(cart_p);

                    console.log(listcarts);
                    cartlist = document.getElementById("cartlist");
                    cartlist.innerText = "";
                    if (listcarts.length > 0) {
                        cont = document.getElementById("content");
                        var first = cont.firstChild;
                        h2 = document.createElement("h2");
                        h2.innerText = localObj["doingOrder"];
                        cont.insertBefore(h2, first);
                        for (i = 0; i < listcarts.length; i++) {
                            createCart(listcarts[i].id, listcarts[i].title, listcarts[i].price, listcarts[i].count);
                        }
                        endprice = document.createElement("p");
                        endprice.innerText = localObj["total"] + ": " + price + "р.";
                        wr = document.createElement("div");
                        wr.className = "wr_end";
                        btn = document.createElement("button");
                        console.log("Check status: " + userStatus);
                        btn.className = "order orderbtn";
                        // btn.className = "order";
                        // btn.classList.add("orderbtn");
                        btn.innerText = localObj["doOrder"];
                        wr.appendChild(endprice);
                        if (userStatus == 0) {
                            wr.appendChild(btn);
                        } else {
                            var ban = document.createElement("p");
                            ban.className = "bantext";
                            ban.innerText = localObj["acc_blocked"];
                            wr.appendChild(ban);
                        }

                        cartlist.appendChild(wr);
                    }
                }
            }
        }
    }

    xhr.open("POST", srvUrl, true);
    xhr.onreadystatechange = reqReadyStateChange;
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");
    xhr.send();
}