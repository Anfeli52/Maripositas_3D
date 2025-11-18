var modal = document.getElementById("editModal");
var editForm = document.getElementById("editForm");

function closeEditModal() {
    modal.style.display = 'none';
    document.getElementById("edit-error-message").textContent = ''; // Limpia errores
    editForm.reset();
}

window.onclick = function(event) {
    if (event.target == modal) {
        closeEditModal();
    }
}

function fetchAndOpenModal(butterflyId) {
    if (!butterflyId) return;

    fetch(`/api/mariposas/${butterflyId}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.message || response.statusText); });
            }
            return response.json();
        })
        .then(data => {
            document.getElementById("edit-id").value = data.id;
            document.getElementById("edit-ubicacion-id").value = data.ubicacionRecoleccionId; 
            document.getElementById("edit-nombre-cientifico").value = data.nombreCientifico;
            document.getElementById("edit-familia").value = data.familia;
            document.getElementById("edit-descripcion").value = data.descripcion;
            document.getElementById("edit-departamento").value = data.departamento;
            document.getElementById("edit-municipio").value = data.municipio;
            document.getElementById("edit-localidad").value = data.localidad;
            document.getElementById("edit-nombre-cientifico-display").textContent = data.nombreCientifico;
            
            document.getElementById("edit-error-message").textContent = '';
            modal.style.display = "block";
        })
        .catch(error => {
            console.error('Error al cargar detalles:', error);
            alert('Error: No se pudieron cargar los detalles para edición. ' + error.message);
        });
}

editForm.addEventListener('submit', function (event){
    event.preventDefault();

    const formData= new FormData(editForm);
    const data = Object.fromEntries(formData.entries());

    fetch('/api/mariposas', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    }).then(response => {
        if(response.ok){
            alert('¡Mariposa actualizada con éxito!');
            closeEditModal();
            window.location.reload();
        } else if (response.status === 404) {
            document.getElementById("edit-error-message").textContent = 'Error 404: La mariposa o su ubicación no fue encontrada.';
        } else if (response.status === 400) {
            document.getElementById("edit-error-message").textContent = 'Error 400: Datos incorrectos o faltantes.';
        } else{
            document.getElementById("edit-error-message").textContent = 'Error al guardar los cambios. Código: ' + response.status;
        }
    }).catch(error => {
        console.error('Error de red: ', error);
        document.getElementById("edit-error-message").textContent = 'Error de conexión con el servidor.';
    });
});