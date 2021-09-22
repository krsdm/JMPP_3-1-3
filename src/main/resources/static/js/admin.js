const url = 'http://localhost:8080/api/users'
const allRoles = $("#addForm option").toArray().map(option => option.text)
let userTable = document.querySelector('#userTable tbody')
let addForm = document.querySelector('#addForm')
let editForm = document.querySelector('#editForm')
let delForm = document.querySelector('#delForm')

renderUserTable()

// отслеживает клики по кнопкам Edit и Delete в таблице
userTable.addEventListener('click', click => {

    // при клике по кнопке Delete, данные о юзере из таблицы помещаются в модальное окно
    if (click.target.classList.contains('del-btn')) {
        let user = getUserByEvent(click)
        $("#delId").val(user.id)
        $("#delName").val(user.name)
        $("#delSurname").val(user.surname)
        $("#delAge").val(user.age)
        $("#delEmail").val(user.email)
        $('#delRoles').html(user.roles.map(role => `<option value='ROLE_${role}'>${role}</option>`))
    }

    // при клике по кнопке Edit, данные о юзере из таблицы помещаются в модальное окно
    if (click.target.classList.contains('edit-btn')) {
        let user = getUserByEvent(click)
        $("#editId").val(user.id)
        $("#editName").val(user.name)
        $("#editSurname").val(user.surname)
        $("#editAge").val(user.age)
        $("#editEmail").val(user.email)
        $("#editPassword").val(user.password)
        $('#editRoles').html(allRoles.map(role =>
            `<option value='ROLE_${role}' ${user.roles.indexOf(role) > -1 ? 'selected' : null}>${role}</option>`))
    }

})

addForm.addEventListener('submit', evt => {
    evt.preventDefault()
    fetch(url, {method: 'POST', body: new FormData(addForm)})
        .then(result => result.json())
        .then(user => renderUserTable())
    document.querySelector('#tableLink').click()
    addForm.reset()
    window.scrollTo(0,0)
})

delForm.addEventListener('submit', evt => {
    evt.preventDefault()
    const id = delForm.querySelector('#delId').value
    fetch(`${url}/${id}`, {method: 'DELETE'})
        .then(result => result.text())
        .then(user => renderUserTable())
    $(".modal").modal("hide")
})

editForm.addEventListener('submit', evt => {
    evt.preventDefault()
    fetch(url, {method: 'PUT', body: new FormData(editForm)})
        .then(result => result.json())
        .then(user => renderUserTable())
    $(".modal").modal("hide")
})

function getUserByEvent(click) {
    let user = {}
    // строка таблицы, в которой была нажата кнопка
    const userTableRow = click.target.parentElement.parentElement
    // из строки берутся данные о пользователе и формируется user
    user.id = userTableRow.querySelector('.id').textContent
    user.name = userTableRow.querySelector('.name').textContent
    user.surname = userTableRow.querySelector('.surname').textContent
    user.age = userTableRow.querySelector('.age').textContent
    user.email = userTableRow.querySelector('.email').textContent
    user.password = userTableRow.querySelector('.password').textContent
    user.roles = userTableRow.querySelector('.roles').textContent.trim().split(' ')
    return user
}

function renderUserTable() {
    fetch(url)
        .then(response => response.json())
        .then(users => {
            userTable.innerHTML = ''
            users.forEach(user => {
                userTable.innerHTML +=
                    `<tr>
                <td class="id">${user.id}</td>
                <td class="name">${user.name}</td>
                <td class="surname">${user.surname}</td>
                <td class="age">${user.age}</td>
                <td class="email">${user.email}</td>
                <td class="roles">${user.roles.map(role => role.name.split('_')[1]).toString().replace(',', ' ')}
                <td class="password d-none">${user.password}</td>
                <td><button type="button" class="edit-btn btn btn-info" data-toggle="modal" data-target="#editModal">Edit</button></td>
                <td><button type="button" class="del-btn btn btn-danger" data-toggle="modal" data-target="#deleteModal">Delete</button></td>
            </tr>`
            })
        })
}