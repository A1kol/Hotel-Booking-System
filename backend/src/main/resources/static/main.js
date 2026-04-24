// Данные из localStorage (должны сохраняться при логине)
const token = localStorage.getItem('token');
const userRole = localStorage.getItem('role'); // Например: ROLE_USER, ROLE_ADMIN, ROLE_MANAGER
const userMail = localStorage.getItem('userMail'); // Мейл юзера для получения его броней

// 1. ЗАЩИТА: Если токена нет — на выход
if (!token) {
    window.location.href = 'auth.html';
}

/**
 * ПРОВЕРКА РОЛЕЙ
 * Настраиваем навигацию под текущую роль
 */
function checkPermissions() {
    if (!userRole) return;

    const managerLink = document.getElementById('manager-link');
    const adminLink = document.getElementById('admin-link');

    // Если Менеджер — видит "Все брони"
    if (userRole.includes('MANAGER')) {
        if (managerLink) managerLink.style.display = 'inline-block';
    }

    // Если Админ — видит "Админ-центр"
    if (userRole.includes('ADMIN')) {
        if (adminLink) adminLink.style.display = 'inline-block';
    }
}

/**
 * ПЕРЕКЛЮЧАТЕЛЬ СЕКЦИЙ
 */
function showSection(sectionId) {
    const sections = ['rooms-section', 'my-bookings-container', 'manager-panel', 'admin-panel'];
    sections.forEach(id => {
        const el = document.getElementById(id);
        if (el) el.style.display = (id === sectionId) ? 'block' : 'none';
    });
}

/**
 * 🏨 ROOM CONTROLLER: ЗАГРУЗКА ВСЕХ НОМЕРОВ
 * GET /api/room/all
 */
async function loadRooms() {
    showSection('rooms-section');
    try {
        const response = await fetch('/api/room/all', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const rooms = await response.json();

        // Обновляем статы в админке, если они есть
        const counter = document.getElementById('total-rooms-count');
        if (counter) counter.innerText = rooms.length;

        const grid = document.getElementById('room-grid');
        grid.innerHTML = rooms.map(room => `
            <div class="room-card">
                <div class="room-info">
                    <div class="badge-type">${room.type}</div>
                    <h3>Номер ${room.roomNumber}</h3>
                    <div class="room-details">
                        <span>👥 Мест: ${room.capacity}</span>
                    </div>
                    <span class="price-tag">$${room.pricePerNight} <small>/ ночь</small></span>
                    <button class="btn-select" onclick="openBooking('${room.roomNumber}')">Забронировать</button>
                </div>
            </div>
        `).join('');
    } catch (e) { console.error("Ошибка RoomController:", e); }
}

/**
 * 📅 BOOKING CONTROLLER: ПОДАЧА ЗАЯВКИ
 * POST /api/booking/
 */
let selectedRoomNum = null;

function openBooking(roomNumber) {
    selectedRoomNum = roomNumber;
    document.getElementById('target-room').innerText = roomNumber;
    document.getElementById('booking-modal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('booking-modal').style.display = 'none';
}

async function confirmBooking() {
    const checkIn = document.getElementById('check-in').value;
    const checkOut = document.getElementById('check-out').value;

    if (!checkIn || !checkOut) return alert("Выберите даты заезда и выезда!");

    const dto = {
        roomNumber: selectedRoomNum,
        checkIn: checkIn,
        checkOut: checkOut
    };

    try {
        const response = await fetch('/api/booking/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            alert("Заявка успешно отправлена!");
            closeModal();
            loadMyBookings();
        } else {
            alert("Ошибка: этот номер может быть занят.");
        }
    } catch (e) { console.error(e); }
}


async function loadMyBookings() {
    // ВМЕСТО почты берем ID, который ты сохранил при логине
    const userId = localStorage.getItem('id');

    if (!userId) return alert("ID пользователя не найден! Перезайдите.");

    showSection('my-bookings-container');

    try {
        // Шлем запрос по ID, как ты делал в Postman
        const response = await fetch(`/api/booking/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            console.error(`Ошибка: ${response.status}`);
            return;
        }

        const bookings = await response.json();
        const tbody = document.getElementById('bookings-tbody');

        tbody.innerHTML = (bookings && bookings.length > 0) ? bookings.map(b => `
            <tr>
                <td>${b.roomNumber}</td>
                <td>${b.checkIn}</td>
                <td>${b.checkOut}</td>
                <td><span class="status-badge status-${b.status.toLowerCase()}">${b.status}</span></td>
            </tr>
        `).join('') : '<tr><td colspan="4">У вас нет активных броней</td></tr>';

    } catch (e) {
        console.error("Ошибка:", e);
    }
}

async function showManagerPanel() {
    showSection('manager-panel');
    try {
        const response = await fetch('/api/booking/all', {
            headers: { 'Authorization': `Bearer ${token}` }
        });
        const bookings = await response.json();
        const tbody = document.getElementById('all-bookings-tbody');

        tbody.innerHTML = bookings.map(b => `
            <tr>
                <td>#${b.id}</td>
                <td><strong>${b.roomNumber}</strong></td>
                <td>${b.checkIn} — ${b.checkOut}</td>
                <td><span class="status-badge status-${b.status.toLowerCase()}">${b.status}</span></td>
                <td>
                    ${b.status === 'PENDING' ?
                    `<button onclick="approveBooking(${b.id})" class="btn-approve">Одобрить</button>` : '—'}
                </td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}

async function approveBooking(id) {
    try {
        const response = await fetch(`/api/booking/approve/${id}`, {
            method: 'PATCH',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        if (response.ok) showManagerPanel();
    } catch (e) { console.error(e); }
}

/**
 * 🛠️ ROOM & USER CONTROLLERS: АДМИН-ЦЕНТР
 */
function showAdminPanel() {
    showSection('admin-panel');
}

// Добавить номер (POST /api/room/add)
async function addNewRoom() {
    const dto = {
        roomNumber: document.getElementById('new-room-number').value,
        type: document.getElementById('new-room-type').value,
        pricePerNight: parseInt(document.getElementById('new-room-price').value),
        capacity: parseInt(document.getElementById('new-room-capacity').value)
    };

    const response = await fetch('/api/room/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(dto)
    });

    if (response.ok) {
        alert("Номер успешно добавлен!");
        loadRooms();
    }
}

// Изменить роль (PATCH /api/user/{id})
async function changeUserRole() {
    const id = document.getElementById('user-id-to-change').value;
    const role = document.getElementById('new-user-role').value;

    if (!id) return alert("Введите ID юзера!");

    const response = await fetch(`/api/user/${id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(role) // Бэкенд ждет Enum Role напрямую
    });

    if (response.ok) alert("Роль обновлена!");
}

/**
 * 🚪 ВЫХОД
 */
function logout() {
    localStorage.clear();
    window.location.href = 'auth.html';
}

// ИНИЦИАЛИЗАЦИЯ
checkPermissions();
loadRooms();