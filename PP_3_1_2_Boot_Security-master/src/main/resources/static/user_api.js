
const url = "/api/user_api "

fetch(url)
    .then((response) => {
        let res = response.json();
        return res;
    })
    .then((user) => {
        console.log(user);
        let roles = ""
        user.roles.forEach((role)=>{roles = roles + role.name + ' '});
        let tbody = document.getElementById('table_user');
        let tr = document.createElement('tr');
        tr.innerHTML = '<td>' + user.id + '</td>' +
            '<td>' + user.userName + '</td>' +
            '<td>' + roles + '</td>' +
            '<td>' + user.password + '</td>'+
            '<td>' + user.email + '</td>'+
               tbody.appendChild(tr);
    })
