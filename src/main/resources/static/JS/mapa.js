// Cliente para consumir /api/mapa/mariposas y mostrar mapa con Leaflet + OpenStreetMap
(function () {
    let map;

    function initMap() {
        // Validar que el contenedor del mapa existe
        const mapContainer = document.getElementById('map');
        if (!mapContainer) {
            console.error('Contenedor #map no encontrado en el DOM');
            return;
        }

        console.log('Inicializando mapa...');

        try {
            // Crear mapa centrado en un punto (ej. centro de Colombia)
            map = L.map('map').setView([4.5709, -74.2973], 6);

            // Añadir capa de OpenStreetMap
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors',
                maxZoom: 19
            }).addTo(map);

            console.log('Mapa inicializado correctamente');

            // Pequeño delay para asegurar que Leaflet está listo
            setTimeout(() => {
                console.log('Cargando mariposas...');
                fetchMariposas();
            }, 500);

        } catch (e) {
            console.error('Error inicializando Leaflet:', e);
        }
    }

    async function fetchMariposas() {
        try {
            console.log('Fetch iniciado hacia /api/mapa/mariposas');
            const res = await fetch('/api/mapa/mariposas');
            if (!res.ok) throw new Error('Error fetching mariposas: ' + res.status);
            const data = await res.json();
            
            console.log('Respuesta del API recibida. Cantidad:', data.length);
            console.log('Datos completos:', data);
            
            // Mostrar debug
            const debugDiv = document.getElementById('map-debug');
            if (debugDiv) {
                debugDiv.style.display = 'block';
                debugDiv.innerHTML = '<strong>Mariposas recibidas:</strong> ' + data.length + '<br/>' +
                    '<pre>' + JSON.stringify(data, null, 2) + '</pre>';
            }
            
            if (data.length === 0) {
                console.warn('No hay mariposas con coordenadas registradas');
                return;
            }
            
            addMarkersToMap(data);
        } catch (err) {
            console.error('Error en fetchMariposas:', err);
        }
    }

    function addMarkersToMap(list) {
        console.log('Procesando ' + list.length + ' mariposas...');
        let marcadoresAñadidos = 0;
        
        list.forEach((item, index) => {
            console.log('Procesando item ' + index + ':', item.nombreComun, 'lat:', item.lat, 'lon:', item.lon);
            
            if (item.lat && item.lon && item.lat !== null && item.lon !== null) {
                try {
                    const marker = L.marker([item.lat, item.lon]).addTo(map);
                    const popupText = `
                        <div style="font-family: Poppins, sans-serif; font-size: 12px; width: 200px;">
                            <b>${item.nombreComun || 'N/A'}</b><br/>
                            <i>${item.nombreCientifico || 'N/A'}</i><br/>
                            <hr style="margin: 4px 0;"/>
                            <strong>Familia:</strong> ${item.familia || 'N/A'}<br/>
                            <strong>Localidad:</strong> ${item.localidad || 'N/A'}<br/>
                            <strong>Municipio:</strong> ${item.municipio || 'N/A'}<br/>
                            <strong>Departamento:</strong> ${item.departamento || 'N/A'}
                        </div>
                    `;
                    marker.bindPopup(popupText);
                    marcadoresAñadidos++;
                    console.log('✓ Marcador añadido:', item.nombreComun);
                } catch (e) {
                    console.error('Error al crear marcador:', e, item);
                }
            } else {
                console.warn('⚠ Mariposa sin coordenadas válidas:', item.nombreComun, {lat: item.lat, lon: item.lon});
            }
        });
        
        console.log('Total de marcadores añadidos al mapa: ' + marcadoresAñadidos + ' de ' + list.length);
    }

    // Inicializar mapa al cargar el DOM
    document.addEventListener('DOMContentLoaded', initMap);
})();
