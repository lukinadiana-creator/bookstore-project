document.addEventListener('DOMContentLoaded', () => {
    loadHeader();
});

const API_URL = "http://localhost:8080/books";

async function loadBooks(params = {}) {
    let url = new URL(API_URL);

    Object.keys(params).forEach(key => {
        if (params[key]) {
            url.searchParams.append(key, params[key]);
        }
    });

    const response = await fetch(url);
    const books = await response.json();

    console.log("BOOKS:", books);

    renderBooks(books);
}

function renderBooks(books) {
    const container = document.querySelector(".books_grid");
    container.innerHTML = "";

    books.forEach(book => {
        const card = document.createElement("div");
        card.className = "card-books";

        card.innerHTML = `
            <div class="card-books_image">
            <img class="book-cover" src="${book.imageUrl}" alt="${book.title}">
            </div>
            <div class="card-content">
                <h3 class="card-title">${book.title}</h3>
                <div class="card-author">${book.author}</div>
                <div class="card-year">Первая публикация: ${book.year} г.</div>
                <div class="card-price">${book.price} руб</div>
            </div>
            <button class="in-basket_btn" onclick="addToBasket(${book.id})">В корзину</button>
        `;

        container.appendChild(card);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    loadBooks();
});

const searchInput = document.getElementById("book");

if (searchInput) {
    searchInput.addEventListener("input", () => {
        const value = searchInput.value;

        loadBooks({
            title: value,
            author: value
        });
    });
}

const minInput = document.querySelector(".price-input_min");
const maxInput = document.querySelector(".price-input_max");

if (minInput && maxInput) {
    [minInput, maxInput].forEach(input => {
        input.addEventListener("input", () => {
            loadBooks({
                minPrice: minInput.value,
                maxPrice: maxInput.value
            });
        });
    });
}


async function addToBasket(bookId) {
    const res = await fetch(`http://localhost:8080/basket/items?bookId=${bookId}&quantity=1`, {
        method: "POST",
        credentials: "include"
    });

    if (res.status === 401) {
        window.location.href = "sign.html";
        return;
    }

    if (!res.ok) {
        alert("Ошибка при добавлении");
        return;
    }

    alert("Добавлено в корзину");
    loadBasket();
}

async function decreaseItem(bookId) {
    await fetch(`http://localhost:8080/basket/items?bookId=${bookId}`, {
        method: "DELETE",
        credentials: "include"
    });

    loadBasket();
}

async function loadBasket() {
    console.log("loadBasket called");
    const res = await fetch("http://localhost:8080/basket", {
        credentials: "include"
    });

    if (res.status === 401) {
        window.location.href = "sign.html";
        return;
    }

    const data = await res.json();

    renderBasket(data);
}

function renderBasket(response) {

    const items = response.items;
    const total = response.totalPrice;

    const container = document.querySelector(".basket-item_grid");
    const title = document.querySelector(".basket_title");
    const content = document.querySelector(".basket-content");

    if (!container) return;

    container.innerHTML = "";

    // пустая корзина
    if (!items || items.length === 0) {

        title.innerText = "Корзина пуста";
        content.style.display = "none";
        return;
    }

    let count = 0;

    items.forEach(item => {

        const book = item.bookResponseDto;

        count += item.quantity;

        const el = document.createElement("div");
        el.className = "card-basket-item";

        el.innerHTML = `
            <div class="card-basket_image">
                <img class="basket-book-cover" src="${book.imageUrl}" alt="${book.title}">
            </div>
            <div class="card-basket-info">

                <h3 class="card-basket-title">${book.title}</h3>

                <div class="card-basket-author">${book.author}</div>

                <div class="card-basket-price">${book.price} ₽</div>

                <div class="basket-item_quantity">

                    <button class="quantity-btn" onclick="decreaseItem(${book.id})">-</button>

                    <span class="quantity-count">${item.quantity}</span>

                    <button class="quantity-btn" onclick="addToBasket(${book.id})">+</button>

                </div>
            </div>
        `;

        container.appendChild(el);
    });

    updateSummary(count, total);
}

function updateSummary(count, total) {

    const countEl = document.querySelector(".summary-count");
    const priceEl = document.querySelector(".summary-price");
    const totalEl = document.querySelector(".summary-total");

    if (countEl) countEl.innerText = `Товары (${count})`;
    if (priceEl) priceEl.innerText = `${total} ₽`;
    if (totalEl) totalEl.innerText = `${total} ₽`;
}

document.addEventListener("DOMContentLoaded", () => {
    loadHeader();

    const container = document.querySelector(".basket-item_grid");

    if (container) {
        loadBasket();
    }
});

async function loadHeader() {
    const navList = document.getElementById('nav-list');

    if (!navList) return;

    try {
        const response = await fetch('http://localhost:8080/me', {
            method: 'GET',
            credentials: 'include'
        });

        if (response.status === 401) {
            navList.innerHTML = `
                <li><a href="index.html">Каталог</a></li>
                <li><a href="sign.html" class="auth-link">Войти</a></li>
            `;
            return;
        }

        const user = await response.json();

        navList.innerHTML = `
            <li><a href="index.html">Каталог</a></li>
            <li><a href="basket.html">Корзина</a></li>
            <li><a href="account.html">${user.name}</a></li>
        `;

    } catch (error) {
        console.error(error);
    }
}


const checkoutBtn = document.querySelector(".checkout-btn");
const modal = document.getElementById("payment-modal");

if (checkoutBtn) {
    checkoutBtn.addEventListener("click", () => {
        modal.style.display = "flex";
    });
}

const cancelBtn = document.getElementById("cancel-payment");

if (cancelBtn) {
    cancelBtn.addEventListener("click", () => {
        modal.style.display = "none";
    });
}

const confirmBtn = document.getElementById("confirm-payment");

if (confirmBtn) {
    confirmBtn.addEventListener("click", async () => {

        const res = await fetch("http://localhost:8080/order", {
            method: "POST",
            credentials: "include"
        });

        if (!res.ok) {
            alert("Ошибка оформления заказа");
            return;
        }

        modal.style.display = "none";

        alert("Заказ успешно оформлен");

        loadBasket();
    });
}

async function loadOrders() {

    const res = await fetch("http://localhost:8080/order", {
        credentials: "include"
    });

    if (res.status === 401) {
        window.location.href = "sign.html";
        return;
    }

    const orders = await res.json();

    renderOrders(orders);
}

function renderOrders(orders) {

    const container = document.querySelector(".profile-orders");

    if (!container) return;

    container.innerHTML = "";

    if (orders.length === 0) {
        container.innerHTML = `
            <div class="empty-orders" style="font-size: 40px; color: #696969;">
                У вас пока нет заказов
            </div>
        `;
        return;
    }

    orders.forEach(order => {

        const itemsHtml = order.items.map(item => `
            <div class="order-item">
                <span class="item-name">${item.title}</span>
                <span class="item-quantity">× ${item.quantity}</span>
                <span class="item-price">${item.price} ₽</span>
            </div>
        `).join("");

        const el = document.createElement("div");

        el.className = "order-card";

        el.innerHTML = `
            <div class="order-header">

                <div class="order-date">
                    ${order.createdAt}
                </div>

                <div class="order-status delivered">
                    ${order.status}
                </div>

                <div class="order-total">
                    ${order.totalAmount} ₽
                </div>

            </div>

            <div class="order-items">
                ${itemsHtml}
            </div>
        `;

        container.appendChild(el);
    });
}

document.addEventListener("DOMContentLoaded", () => {

    const ordersContainer = document.querySelector(".profile-orders");

    if (ordersContainer) {
        loadOrders();
    }
});