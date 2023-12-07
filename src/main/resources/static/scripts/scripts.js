function addElement() {
    var tableBody = document.getElementById('table-body');
    var newRow = document.createElement('tr');
    newRow.innerHTML = `
        <td>1</td>
        <td>Первый нах</td>
        <td>Все лабы</td>
        <td><button class="btn btn-danger" onclick="deleteRow(this)">Удалить</button></td>`;
    tableBody.appendChild(newRow);
}

function deleteRow(button) {
    var row = button.parentElement.parentElement;
    row.remove();
}