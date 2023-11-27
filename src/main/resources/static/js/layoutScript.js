const markerList = [];
// Додайте цей код відображення маркерів з сервера
function displayAidMarkersFromServer() {
    const url = '/getAllAidMarkers'; // URL-адреса вашого контролера для отримання маркерів

    fetch(url)
        .then(response => response.json())
        .then(aid => {
        //                        console.log(aid);

        //                         // data - це масив маркерів, які отримані з сервера
        aid.forEach(marker => {
            const {
                latitude,
                longitude,
                categoryAidEntity,
                description
            } = marker;

            const markerObject = {
                categoryAidEntity: categoryAidEntity,
                description: description
            };


            addToSidebar(marker.id, marker.description)
        });
    })
        .catch(error => {
        console.error('Помилка під час отримання маркерів з сервера:', error);
    });
}
// Викликати цю функцію, коли ви хочете відобразити всі маркери з сервера
displayAidMarkersFromServer();


//$.widget.bridge('uibutton', $.ui.button);


// Assuming you have a reference to your button element
var toggleButton = document.getElementById('toggleMarkerMode');

// Toggle the 'active' class on button click
toggleButton.addEventListener('click', function () {
    this.classList.toggle('active');


    // Update the button text based on the presence of the 'active' class
    if (this.classList.contains('active')) {
        toggleButton.textContent = 'Cancel';
    } else {
        toggleButton.textContent = 'Add aid';
    }

});

//            // Функція для додавання маркера до списку на лівій бічній панелі
//            function addMarkerToList(description) {
//                const list = document.getElementById('markerList');
//                const listItem = document.createElement('li');
//                listItem.textContent = description;
//                list.appendChild(listItem);
//            }



function convertIdToName(id) {
    let name = '';

    switch (id) {
        case 1:
            name = 'Basic Necessities Aid';
            break;
        case 3:
            name = 'Education Aid';
            break;
        case 2:
            name = 'Healthcare Aid';
            break;
        case 4:
            name = 'Employment Aid';
            break;
        case 5:
            name = 'Legal Aid';
            break;
        case 6:
            name = 'Community Aid';
            break;
    }
    return  name;
}

// Функція для додавання маркера до списку на бічній панелі

function addToSidebar(id, description) {

    console.log('addToSidebar', convertIdToName(id));
    const sidebarList = document.getElementById('sidebarList');
    const listItem = document.createElement('li');


    listItem.textContent = convertIdToName(id) + ' ' + description;
    sidebarList.appendChild(listItem);
}