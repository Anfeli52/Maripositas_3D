// =======================================================
// A. UTILIDADES SWEETALERT2 (Avisos) 🔔
// =======================================================

function swSuccess(title, text) {
    Swal.fire({
        icon: 'success',
        title,
        text,
        confirmButtonColor: '#1d6fd6',
        timer: 1500,
        timerProgressBar: true
    });
}

function swError(title, text, footer = '') {
    Swal.fire({
        icon: 'error',
        title,
        text,
        footer: footer,
        confirmButtonColor: '#d33'
    });
}

function swLoading(message = 'Procesando, por favor espere...') {
    return Swal.fire({
        title: message,
        allowOutsideClick: false,
        showConfirmButton: false,
        willOpen: () => {
            Swal.showLoading();
        }
    });
}

// =======================================================
// B. MODALES HTML (Contenedores de Edición)
// =======================================================
function getModalAndForm(modalId, formId) {
    const modal = document.getElementById(modalId);
    const form = document.getElementById(formId);
    if (!modal || !form) {
        console.error(`Modal o formulario no encontrado: ${modalId} / ${formId}`);
        // Usamos SweetAlert2 para errores de UI
        swError('Error de Interfaz', `El modal o formulario ${modalId} no fue encontrado.`);
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
    // Eliminamos la dependencia de 'errorMessage' ya que SweetAlert maneja los errores
    if (modal) {
        modal.style.display = 'none';
        const form = modal.querySelector('form');
        if (form) form.reset();
        window.onclick = null;
    }
}

// =======================================================
// C. LÓGICA ESPECÍFICA DE EDICIÓN DE MARIPOSAS
// =======================================================

const BUTTERFLY_MODAL_ID = 'editModal';
const BUTTERFLY_FORM_ID = 'editForm';
const BUTTERFLY_API_URL = '/api/mariposas';

function loadButterflyDataToForm(data) {
    document.getElementById("edit-id").value = data.id;
    document.getElementById("edit-ubicacion-id").value = data.ubicacionRecoleccionId;
    document.getElementById("edit-nombre-cientifico").value = data.nombreCientifico;
    document.getElementById("edit-nombre-comun").value = data.nombreComun || ''; // Manejo de nulo
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
        nombreComun: formData.get('edit-nombre-comun'),
        familia: formData.get('edit-familia'),
        descripcion: formData.get('edit-descripcion'),
        departamento: formData.get('edit-departamento'),
        municipio: formData.get('edit-municipio'),
        localidad: formData.get('edit-localidad'),
    };
}

function openModalUpdateButterfly(butterflyId) {
    if (!butterflyId) return;
    fetchDataAndOpenModal(`${BUTTERFLY_API_URL}/${butterflyId}`, BUTTERFLY_MODAL_ID, loadButterflyDataToForm);
}

// =======================================================
// D. LÓGICA ESPECÍFICA DE EDICIÓN DE USUARIOS
// =======================================================

const USER_MODAL_ID = 'editUserModal'; 
const USER_FORM_ID = 'editUserForm';   
var USER_API_URL;

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
    USER_API_URL = '/api/usuarios';
    USER_API_URL = `${USER_API_URL}/${userId}`;
    fetchDataAndOpenModal(`${USER_API_URL}`, USER_MODAL_ID, loadUserDataToForm);
}

// =======================================================
// E. FUNCIONES DE UTILIDAD (FETCH Y MANEJO DE ENVÍO)
// =======================================================

async function fetchDataAndOpenModal(url, modalId, dataMapperFunction) {
    const loadingAlert = swLoading('Cargando detalles de la mariposa...');
    
    try {
        const response = await fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } });
        
        if (!response.ok) {
            const errorBody = await response.json().catch(() => ({})); 
            throw new Error(errorBody.message || `HTTP ${response.status} ${response.statusText}`);
        }

        const data = await response.json();
        
        loadingAlert.close();
        
        dataMapperFunction(data);
        openModal(modalId);
        
    } catch(error) {
        loadingAlert.close();
        console.error('Error al cargar detalles:', error);
        swError('Error de Carga', 'No se pudieron cargar los detalles.', error.message);
    }
}

async function handleFormSubmit(event, apiUrl, modalId, dtoMapperFunction) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const data = dtoMapperFunction(formData);

    const loadingAlert = swLoading('Guardando cambios...');

    try {
        const response = await fetch(apiUrl, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify(data)
        });

        loadingAlert.close();

        if (response.ok || response.status === 204) {
            swSuccess('¡Actualización exitosa!', 'Los datos se han guardado correctamente.');
            closeModal(modalId);
            setTimeout(() => window.location.reload(), 700);
        } else {
            let errorDetail = `Error ${response.status}.`;
            try {
                const errorBody = await response.json();
                errorDetail = errorBody.message || errorDetail;
            } catch {}

            swError('Error al Guardar', `No se pudo actualizar la mariposa.`, errorDetail);
        }
    } catch(error) {
        loadingAlert.close();
        console.error('Error de red: ', error);
        swError('Error de Conexión', 'No se pudo contactar al servidor.', `Mensaje: ${error.message}`);
    }
}

async function deleteUser(userId) {
    if (!userId) return;

    // 1. Confirmación elegante con SweetAlert2
    const result = await Swal.fire({
        title: '¿Eliminar este usuario?',
        html: `
            <p>Esta acción eliminará la cuenta del usuario.</p>
            <p style="color: #dc2626; font-weight: 600;">Esta acción es <b>permanente</b>.</p>
        `,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc2626', // Rojo para la acción peligrosa
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        focusCancel: true
    });

    if (!result.isConfirmed) return;

    // 2. Mostrar spinner de carga (Usando la utilidad swLoading)
    // Usamos el patrón de Swal.fire().showLoading() ya que swLoading fue modificado.
    Swal.fire({
        title: 'Eliminando usuario...',
        allowOutsideClick: false,
        showConfirmButton: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    try {
        const response = await fetch(`${USER_API_URL}/${userId}`, {
            method: 'DELETE',
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        Swal.close(); // Cerrar spinner al recibir respuesta

        if (response.ok || response.status === 204) {
            // Éxito: código 204 No Content es común en DELETE
            await swSuccess('¡Eliminado!', 'El usuario ha sido eliminado correctamente.');
            window.location.reload();
        } else if (response.status === 404) {
            swError('No encontrado', 'El usuario que intentas eliminar no existe.');
        } else {
            // Manejo de otros errores (400, 409, 500, etc.)
            let errorDetail = `Error ${response.status}.`;
            try {
                const errorBody = await response.json();
                errorDetail = errorBody.message || errorDetail;
            } catch {}

            swError('Error al Eliminar', `No se pudo eliminar el usuario.`, errorDetail);
        }
    } catch (error) {
        Swal.close();
        console.error('Error de red: ', error);
        swError('Error de Conexión', 'No se pudo conectar con el servidor.', `Mensaje: ${error.message}`);
    }
}

// =======================================================
// F. FUNCIÓN PARA ELIMINAR UNA MARIPOSA
// =======================================================


async function deleteButterflyCascade(id) {
    if (!id) return;
    const result = await Swal.fire({
        title: '¿Eliminar esta especie?',
        html: `
            <p>Se eliminarán <b>todas las observaciones</b> asociadas.</p>
            <p style="color: #dc2626; font-weight: 600;">Esta acción es <b>permanente</b>.</p>
        `,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc2626',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        focusCancel: true
    });

    if (!result.isConfirmed) return;

    const loadingAlert = swLoading('Eliminando especie...');

    try {
        const response = await fetch(`${BUTTERFLY_API_URL}/${id}?cascade=true`, {
            method: 'DELETE',
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        });

        loadingAlert.close();

        if (response.status === 204) {
            await Swal.fire({
                icon: 'success',
                title: '¡Eliminada!',
                text: 'La especie y sus observaciones fueron eliminadas.',
                confirmButtonColor: '#10b981',
                timer: 2000,
                timerProgressBar: true
            });
            window.location.reload();
        } else if (response.status === 404) {
            swError('No encontrada', 'La especie no existe.');
        } else if (response.status === 409) {
            const error = await response.json().catch(() => ({}));
            swError('No se puede eliminar', error.message || 'Tiene observaciones asociadas.');
        } else {
            swError('Error', `Código: ${response.status}`);
        }
    } catch (error) {
        loadingAlert.close();
        console.error('Error al eliminar:', error);
        swError('Error de Red', 'No se pudo conectar con el servidor.', error.message);
    }
}

// =======================================================
// G. INICIALIZACIÓN
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

// ==============================
// Buscador universal (admin + user) - corregido y listo para pegar
// ==============================
(function () {
  'use strict';

  const COL = { NOMBRE_CIENTIFICO: 0, NOMBRE_COMUN: 1, FAMILIA: 2, DEPARTAMENTO: 3, MUNICIPIO: 4, LOCALIDAD: 5 };

  function q(sel, root = document) { return root.querySelector(sel); }
  function qa(sel, root = document) { return Array.from(root.querySelectorAll(sel)); }
  function textCell(row, idx) { const c = row.querySelectorAll('td')[idx]; return c ? c.textContent.trim() : ''; }
  function normalize(s) { return (s || '').toString().trim().toLowerCase(); }

  function collectValues(rows) {
    const species = new Set(), deps = new Set(), muns = new Set();
    rows.forEach(r => {
      const nc = textCell(r, COL.NOMBRE_CIENTIFICO);
      const ncom = textCell(r, COL.NOMBRE_COMUN);
      const dep = textCell(r, COL.DEPARTAMENTO);
      const mun = textCell(r, COL.MUNICIPIO);
      if (nc) species.add(nc);
      if (ncom) species.add(ncom);
      if (dep) deps.add(dep);
      if (mun) muns.add(mun);
    });
    return { species: Array.from(species).sort(), deps: Array.from(deps).sort(), muns: Array.from(muns).sort() };
  }

  function fillSelect(selectEl, values) {
    if (!selectEl) return;
    const placeholder = selectEl.querySelector('option');
    const placeholderText = placeholder ? placeholder.textContent : '';
    selectEl.innerHTML = '';
    if (placeholder) {
      const ph = document.createElement('option');
      ph.value = '';
      ph.textContent = placeholderText;
      selectEl.appendChild(ph);
    }
    values.forEach(v => {
      const opt = document.createElement('option');
      // guardamos value normalizado (minúsculas) para comparaciones fiables
      opt.value = (v || '').toString().trim().toLowerCase();
      opt.textContent = v;
      selectEl.appendChild(opt);
    });
  }

  function applyFilters(state) {
    const qText = normalize(state.searchInput.value);
    const speciesVal = state.selSpecies ? (state.selSpecies.value || '').toString().trim().toLowerCase() : '';
    const deptVal = state.selDept ? (state.selDept.value || '').toString().trim().toLowerCase() : '';
    const munVal = state.selMun ? (state.selMun.value || '').toString().trim().toLowerCase() : '';

    state.tableRows.forEach(row => {
      const nombreC = normalize(textCell(row, COL.NOMBRE_CIENTIFICO));
      const nombreCom = normalize(textCell(row, COL.NOMBRE_COMUN));
      const familia = normalize(textCell(row, COL.FAMILIA));
      const departamento = normalize(textCell(row, COL.DEPARTAMENTO));
      const municipio = normalize(textCell(row, COL.MUNICIPIO));
      const localidad = normalize(textCell(row, COL.LOCALIDAD));

      const matchesText = !qText || nombreC.includes(qText) || nombreCom.includes(qText) || familia.includes(qText) || localidad.includes(qText);

      // comparar con valores normalizados (option.value ya está en minúsculas)
      const matchesSpecies = !speciesVal || nombreC === speciesVal || nombreCom === speciesVal;
      const matchesDept = !deptVal || departamento === deptVal;
      const matchesMun = !munVal || municipio === munVal;

      row.style.display = (matchesText && matchesSpecies && matchesDept && matchesMun) ? '' : 'none';
    });
  }

  function debounce(fn, wait = 160) { let t; return function () { clearTimeout(t); t = setTimeout(fn, wait); }; }

  function initOnce() {
    const searchInput = q('#searchName');
    const selSpecies = q('#filterSpecies');
    const selDept = q('#filterDepartamento');
    const selMun = q('#filterMunicipio');
    const clearBtn = q('#clearFilters');
    const tableEl = q('.buttlerflys-table');

    if (!tableEl || !searchInput || !selSpecies || !selDept || !selMun || !clearBtn) {
      return null;
    }

    const tableRows = qa('tbody tr', tableEl);
    const vals = collectValues(tableRows);
    fillSelect(selSpecies, vals.species);
    fillSelect(selDept, vals.deps);
    fillSelect(selMun, vals.muns);

    const state = { searchInput, selSpecies, selDept, selMun, clearBtn, tableEl, tableRows };

    searchInput.addEventListener('input', debounce(() => applyFilters(state)));
    selSpecies.addEventListener('change', () => applyFilters(state));
    selDept.addEventListener('change', () => {
      const selected = selDept.value;
      const rows = qa('tbody tr', tableEl);
      const munSet = new Set();
      rows.forEach(r => {
        const d = textCell(r, COL.DEPARTAMENTO);
        const m = textCell(r, COL.MUNICIPIO);
        if (!selected || d.toString().trim().toLowerCase() === selected) munSet.add(m);
      });
      // llenar municipios con valores originales pero option.value será normalizado en fillSelect
      fillSelect(selMun, Array.from(munSet).sort());
      applyFilters(state);
    });
    selMun.addEventListener('change', () => applyFilters(state));

    clearBtn.addEventListener('click', () => {
      searchInput.value = '';
      selSpecies.selectedIndex = 0;
      selDept.selectedIndex = 0;
      selMun.selectedIndex = 0;
      const vals2 = collectValues(tableRows);
      fillSelect(selMun, vals2.muns);
      applyFilters(state);
    });

    // initial filter
    applyFilters(state);
    console.info('Buscador inicializado.');
    return state;
  }

  function startInit(retries = 8, interval = 350) {
    let attempts = 0;
    const tryInit = () => {
      const s = initOnce();
      if (s) return;
      attempts++;
      if (attempts < retries) setTimeout(tryInit, interval);
      else console.warn('Buscador: no se pudo inicializar. Verifica IDs y estructura de la tabla en esta vista.');
    };
    tryInit();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => startInit());
  } else {
    startInit();
  }

})();
