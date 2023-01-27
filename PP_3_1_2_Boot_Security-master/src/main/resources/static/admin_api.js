let url = "/api/admin_api"

function getAllRoles() {
    return fetch("/api/admin_api/getroles")
        .then((response) => {
            let res = response.json();
            return res;
        })
        .then((roles) => {
            console.log('all roles:')
            console.log(roles);
            return roles;
        })
}

function showAllUsers() {
    fetch(url)
        .then((response) => {
            let res = response.json();
            return res;
        })
        .then((allusers) => {
            console.log(allusers);

            let tbody = document.getElementById('table_allusers');
            allusers.forEach((user) => {
                let roles = "";
                user.roles.forEach((role) => {
                    roles = roles + role.name + ' '
                })
                let tr = document.createElement('tr');
                tr.innerHTML = '<td>' + user.id + '</td>' +
                    '<td>' + user.userName + '</td>' +
                    '<td>' + user.password + '</td>' +
                    '<td>' + user.email + '</td>' +
                    '<td>' +   roles  + '</td>' +
                    '<td>' + '<button type="button" class="btn btn-success" data-toggle="modal" data-whatever="' + user.id + '" data-target="#modalEdit">Edit</button>' + '</td>' +
                    '<td>' + '<button type="button" class="btn btn-danger" data-toggle="modal" data-whatever="' + user.id + '" data-target="#modalDelete">Delete</button>' + '</td>';
                tbody.appendChild(tr);
            })
        })
}
createOptAllRoles();
async function createOptAllRoles() {
    let r = await getAllRoles();
    $("#roles").empty();
    let select = document.getElementById('roles');
    r.forEach((role) => {
        let option = document.createElement('option');
        option.setAttribute('value', role.name);
        option.setAttribute('id', role.id);
        option.setAttribute('name', role.name);
        option.appendChild(document.createTextNode(role.name));
        select.appendChild(option);
    })
}

function newUser(){
    let newUserForm = document.getElementById("newUserForm") // по айди находим форму
    newUserForm.onsubmit = async (e) => {
        e.preventDefault();
        let formData = new FormData(newUserForm);
        let user = {
            userName : formData.get('username'),
            password : formData.get('password'),
            email : formData.get('email'),
            roles : Array.from(document.getElementById("roles"))
                .filter(option => option.selected)
                .map(option => ({name: option.value, id: option.id}))
        }

        fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(user)
        })
            .then((response) => {
                let res = response.json();
                return res;
            })
            .then((user) => {
                console.log(user);
                let roles = "";
                user.roles.forEach((role) => {
                    roles = roles + role.name});
                roles = roles;
                let tbody = document.getElementById('table_allusers');
                let tr = document.createElement('tr');
                tr.innerHTML = '<td>' + user.id + '</td>' +
                    '<td>' + user.userName + '</td>' +
                    '<td>' + user.password + '</td>' +
                    '<td>' + user.email + '</td>' +
                    '<td>' + roles + '</td>' +
                    '<td>' + '<button type="button" class="btn btn-success" data-toggle="modal" data-whatever="' + user.id + '" data-target="#modalEdit">Edit</button>' + '</td>' +
                    '<td>' + '<button type="button" class="btn btn-danger" data-toggle="modal" data-whatever="' + user.id + '" data-target="#modalDelete">Delete</button>' + '</td>';
                tbody.appendChild(tr);
                document.getElementById('newUserForm').onsubmit;
                $('.nav-tabs a[href="#home"]').tab('show');
                $('.nav-tabs a[href="#profile"]').removeClass('active')
            })
    }
}

async function getUser(id){
    let response = await fetch(url + '/' + id);
    return await response.json();
}

async function showDeleteModal(id){
    let user = await getUser(id);

    document.getElementById("delId").value = user.id;
    document.getElementById("delUsername").value = user.userName;
    document.getElementById("delPassword").value = user.password;
    document.getElementById("delName").value = user.email;

    $("#delRoles").empty();
    let select = document.getElementById('delRoles');
    let r = await getAllRoles();
    r.forEach((role) => {
        let option = document.createElement('option');
        option.setAttribute('value', role.name);
        option.setAttribute('id', role.id);
        option.setAttribute('name', role.name);
        option.appendChild(document.createTextNode(role.name));
        select.appendChild(option);
    })
    let optionsToSelect=[];
    for (i=0, j= user.roles.length; i<j; i++) {
        optionsToSelect[i] = user.roles[i].name;
    }
    let selectR = document.getElementById('delRoles');

    for (i = 0, l = selectR.options.length; i < l; i++) {
        let o = selectR.options[i];
        if (optionsToSelect.indexOf(o.text) != -1) {
            o.selected = true;
            // }else{
            //     o.selected = false;
        }
    }
}
async function deleteUser(){
    let userId = document.getElementById('delId').value;
    fetch(url + '/' + userId, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=utf-8'
        }
    })
        .then(() => {
        })
}

async function showEditModal(id){
    let user = await getUser(id);

    document.getElementById("editId").value = user.id;
    document.getElementById("editUsername").value = user.username;
    document.getElementById("editPassword").value = user.password;
    document.getElementById("editEmail").value = user.email;
     $("#editRoles").empty();
    let select = document.getElementById('editRoles');
    let r = await getAllRoles();
    r.forEach((role) => {
        let option = document.createElement('option');
        option.setAttribute('value', role.name);
        option.setAttribute('id', role.id);
        option.setAttribute('name', role.name);
        option.appendChild(document.createTextNode(role.name));
        select.appendChild(option);
    })
    let optionsToSelect=[];
    for (i=0, j= user.roles.length; i<j; i++) {
        optionsToSelect[i] = user.roles[i].name;
    }
    let selectR = document.getElementById('editRoles');

    for (i = 0, l = selectR.options.length; i < l; i++) {
        let o = selectR.options[i];
        if (optionsToSelect.indexOf(o.text) != -1) {
            o.selected = true;
                }
    }
}

async function editUser() {
    let editForm = document.getElementById("editForm")
    let formData = new FormData(editForm);
    let user = {
        id: formData.get('id'),
        userName: formData.get('username'),
        password: formData.get('password'),
        email: formData.get('email'),
        roles: Array.from(document.getElementById("editRoles"))
            .filter(option => option.selected)
            .map(option => ({name: option.value, id: option.id}))
    }

    fetch(url, {
        method: 'PUT',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    })
        .then((response) => {
            document.getElementById('editForm').onsubmit;
        })
    // }
}
showAllUsers();
