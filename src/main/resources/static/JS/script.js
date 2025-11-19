// =======================================================
// A. LÓGICA GENÉRICA DE MODALES (UI)
// =======================================================

function getModalAndForm(modalId, formId) {
    const modal = document.getElementById(modalId);
    const form = document.getElementById(formId);
    if (!modal || !form) {
        console.error(`Modal o formulario no encontrado: ${modalId} / ${formId}`);
        return null;
    }
    return { modal, form };
}

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = 'flex';
        window.onclick = function (event) {
            if (event.target === modal) {
                closeModal(modalId);
            }
        };
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    const errorMessage = document.getElementById(`${modalId.replace('Modal', '')}-error-message`);

    if (modal) {
        modal.style.display = 'none';

        const form = modal.querySelector('form');
        
        if (form) form.reset();
        if (errorMessage) errorMessage.textContent = '';

        window.onclick = null;
    }
}

// =======================================================
// B. LÓGICA ESPECÍFICA DE EDICIÓN DE MARIPOSAS
// =======================================================

const BUTTERFLY_MODAL_ID = 'editModal';
const BUTTERFLY_FORM_ID = 'editForm';
const BUTTERFLY_API_URL = '/api/mariposas';

function loadButterflyDataToForm(data) {
    document.getElementById("edit-id").value = data.id;
    document.getElementById("edit-ubicacion-id").value = data.ubicacionRecoleccionId;
    document.getElementById("edit-nombre-cientifico").value = data.nombreCientifico;
    document.getElementById("edit-familia").value = data.familia;
    document.getElementById("edit-descripcion").value = data.descripcion;
    document.getElementById("edit-departamento").value = data.departamento;
    document.getElementById("edit-municipio").value = data.municipio;
    document.getElementById("edit-localidad").value = data.localidad;
    document.getElementById("edit-nombre-cientifico-display").textContent = data.nombreCientifico;
}

function mapButterflyFormToDTO(formData) {
    return {
        id: formData.get('edit-id'),
        ubicacionRecoleccionId: formData.get('edit-ubicacion-id'),
        nombreCientifico: formData.get('edit-nombre-cientifico'),
        familia: formData.get('edit-familia'),
        descripcion: formData.get('edit-descripcion'),
        departamento: formData.get('edit-departamento'),
        municipio: formData.get('edit-municipio'),
        localidad: formData.get('edit-localidad'),
    };
}

// Función pública para abrir la modal de Mariposa (llamada desde el HTML)
function openModalUpdateButterfly(butterflyId) {
    if (!butterflyId) return;

    // Aquí puedes usar tu lógica de fetchAndOpenModal mejorada con manejo de errores y AJAX
    fetchDataAndOpenModal(`${BUTTERFLY_API_URL}/${butterflyId}`, BUTTERFLY_MODAL_ID, loadButterflyDataToForm);
}

// =======================================================
// C. LÓGICA ESPECÍFICA DE EDICIÓN DE USUARIOS
// =======================================================

const USER_MODAL_ID = 'editUserModal'; // Debes crear un nuevo ID para esta modal
const USER_FORM_ID = 'editUserForm';   // Debes crear un nuevo ID para este formulario
const USER_API_URL = '/api/usuario';  // Corregido a singular

function loadUserDataToForm(data) {
    document.getElementById("user-edit-id").value = data.id;
    document.getElementById("user-edit-nombre").value = data.nombre;
    document.getElementById("user-edit-correo").value = data.correo;
    document.getElementById("user-edit-rol").value = data.rol;
}

function mapUserFormToDTO(formData) {
    return {
        id: formData.get('user-edit-id'),
        nombre: formData.get('user-edit-nombre'),
        correo: formData.get('user-edit-correo'),
        rol: formData.get('user-edit-rol'),
    };
}

function openModalUpdateUser(userId) {
    if (!userId) return;
    fetchDataAndOpenModal(`${USER_API_URL}/${userId}`, USER_MODAL_ID, loadUserDataToForm);
}


// =======================================================
// D. FUNCIONES DE UTILIDAD (FETCH Y MANEJO DE ENVÍO)
// =======================================================

function fetchDataAndOpenModal(url, modalId, dataMapperFunction) {
    fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } })
        .then(response => {
            if (!response.ok) {
                // Aquí puedes reintroducir tu lógica robusta de manejo de 401/302/JSON/HTML
                // Por simplicidad, usando la versión básica
                return response.json().then(err => { throw new Error(err.message || response.statusText); });
            }
            return response.json();
        })
        .then(data => {
            dataMapperFunction(data);
            openModal(modalId);
        })
        .catch(error => {
            console.error('Error al cargar detalles:', error);
            alert('Error: No se pudieron cargar los detalles. ' + error.message);
        });
}

function handleFormSubmit(event, apiUrl, modalId, dtoMapperFunction) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const data = dtoMapperFunction(formData);

    // Enviar PUT a /api/usuario/{id}
    fetch(`${apiUrl}/${data.id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.ok) {
            alert('¡Actualización exitosa!');
            closeModal(modalId);
            window.location.reload();
        } else {
            document.getElementById(`${modalId.replace('Modal', '')}-error-message`).textContent =
                `Error al guardar los cambios. Código: ${response.status}`;
        }
    }).catch(error => {
        console.error('Error de red: ', error);
        document.getElementById(`${modalId.replace('Modal', '')}-error-message`).textContent =
            'Error de conexión con el servidor.';
    });
}

function deleteUser(userId) {
    if (!userId) return;
    if (!confirm('¿Seguro que deseas eliminar este usuario?')) return;
    fetch(`${USER_API_URL}/${userId}`, {
        method: 'DELETE',
        headers: {
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => {
        if (response.ok) {
            alert('Usuario eliminado correctamente.');
            window.location.reload();
        } else {
            alert('Error al eliminar el usuario.');
        }
    })
    .catch(error => {
        alert('Error de conexión con el servidor.');
        console.error(error);
    });
}

// =======================================================
// E. INICIALIZACIÓN
// =======================================================

document.addEventListener('DOMContentLoaded', () => {
    const butterflyForm = document.getElementById(BUTTERFLY_FORM_ID);
    if (butterflyForm) {
        butterflyForm.addEventListener('submit', (event) =>
            handleFormSubmit(event, BUTTERFLY_API_URL, BUTTERFLY_MODAL_ID, mapButterflyFormToDTO)
        );
    }

    const userForm = document.getElementById(USER_FORM_ID);
    if (userForm) {
        userForm.addEventListener('submit', (event) =>
            handleFormSubmit(event, USER_API_URL, USER_MODAL_ID, mapUserFormToDTO)
        );
    }

});
