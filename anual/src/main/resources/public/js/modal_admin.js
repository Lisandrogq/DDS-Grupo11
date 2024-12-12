const modal = document.querySelector("#modal");
const modalContent = document.querySelector("#modal-content");

const setupModalClosers = () => {
	document.querySelectorAll("#modal-close").forEach((el) => (el.onclick = closeModal));
};

const setModalContent = (children) => {
	modalContent.innerHTML = DOMPurify.sanitize(children);;
	setupModalClosers();
};

const openModal = (children, afterMount) => {
	modal.classList.add("modal-active");
	setModalContent(children);
	afterMount && afterMount();
};

const closeModal = () => {
	modal.classList.remove("modal-active");
	setModalContent("");
};

document.addEventListener("keyup", (e) => {
	if (e.key == "Escape") closeModal();
});
var count = 1;
function agregarInput() {
	if (count < 4) {
		let div = document.createElement("div");
		div.classList.add("input");
		div.innerHTML = DOMPurify.sanitize(
			' <input type="text" id="meal" name="meal_' + count + '" required placeholder="Type of meal to distribute..."class="col-12 inputs">');
		document.getElementById("input-placeholder").appendChild(div);
		count++;
	}
}

function eliminarInput() {
	if (count >= 0) {
		var inputs = document.getElementById("input-placeholder").querySelectorAll(".input");
		inputs[inputs.length - 1].remove();
		count--;
	}
}

/**
 * ===================================== Reports Info Modal =====================================
 */

const reports = document.querySelectorAll("#report");
reports.forEach((report) => {
    const id = report.getAttribute("data-report-id");

    const fromDate = report.getAttribute("data-from-date");
    const toDate = report.getAttribute("data-to-date");

    const reportDescription = "From " + formatDate(fromDate) + " to " + formatDate(toDate);
    const intervalDataElement = report.querySelector("#report-description");
    if (intervalDataElement) {
        intervalDataElement.textContent = reportDescription;
    }

    report.onclick = () => {
        showReportModal(id);
    };
});

function showReportModal(id) {
    if (!id) {
        console.error("No ID provided");
        alert("No ID provided");
        return;
    }

    const url = `/admin/report?id=${encodeURIComponent(id)}`;
    console.log(url);

    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error:', response);
            alertaError("An error occurred. Please try again.");
        }
        return response.json();
    })
    .then(data => {
        console.log("Fridge info retrieved successfully");
        openModal(showReportInfo(data), () => {
        });
    })
    .catch(error => {
        alertaError("An error occurred. Please try again.");
        console.error('Error:', error);
    });
}

function showReportInfo(data) {
	const { id, lastCreatedAt, createdAt, fridgeReportRows, contributorReportRows } = data;
	console.log("Report info:", data);

	const fridgeRecent = fridgeReportRows
    .sort((a, b) => a.fridgeId - b.fridgeId)
    .map(fridge => `
        <tr>
            <td>${fridge.fridgeId}</td>
            <td>${fridge.addedMeals}</td>
            <td>${fridge.removedMeals}</td>
            <td>${fridge.failures}</td>
        </tr>
    `).join('');
	console.log("Frige recent:", fridgeRecent);

    const fridgeTotal = fridgeReportRows
    .sort((a, b) => a.fridgeId - b.fridgeId)
    .map(fridge => `
        <tr>
            <td>${fridge.fridgeId}</td>
            <td>${fridge.totalAddedMeals}</td>
            <td>${fridge.totalRemovedMeals}</td>
            <td>${fridge.totalFailures}</td>
        </tr>
    `).join('');
    console.log("Frige total:", fridgeTotal);

    const contributorRecent = contributorReportRows
    .sort((a, b) => a.contributorId - b.contributorId)
    .map(contributor => `
        <tr>
            <td>${contributor.contributorId}</td>
            <td>${contributor.contributorName}</td>
            <td>${contributor.donatedMeals}</td>
        </tr>
    `).join('');
    console.log("Contributor recent:", contributorRecent);

    const contributorTotal = contributorReportRows
    .sort((a, b) => a.contributorId - b.contributorId)
    .map(contributor => `
        <tr>
            <td>${contributor.contributorId}</td>
            <td>${contributor.contributorName}</td>
            <td>${contributor.totalDonatedMeals}</td>
        </tr>
    `).join('');
    console.log("Contributor total:", contributorTotal);
	
	return DOMPurify.sanitize( `
        <div class="d-flex flex-column" style="gap: 40px;">

            <div class="d-flex justify-content-between align-items-center">
                <h5 class="accent-100 mb-2">Report ${id}</h5>
                <button
                    class="btn btn-primary"
                    id="download-pdf"
                    onclick="downloadPDF()"
                >
                    Download PDF
                </button>
            </div>

            <div class="columns">
                <div class="column">
                    <p>From</p>
                    <b>${formatDate(lastCreatedAt)}</b>
                </div>
                <div class="column">
                    <p>To</p>
                    <b>${formatDate(createdAt)}</b>
                </div>
            </div>

            <style>
                .columns {
                    display: grid;
                    grid-template-columns: 1fr 1fr; /* Dos columnas iguales */
                    gap: 20px; /* Espaciado entre columnas (opcional) */
                    align-items: center; /* Alineación vertical */
                }

                .column {
                    display: flex;
                    flex-direction: column; /* Alinear texto y bold verticalmente */
                    gap: 5px; /* Espaciado entre elementos dentro de una columna */
                }

                p {
                    margin: 0;
                    font-weight: bold;
                }

                b {
                    font-size: 1em; /* Ajustar tamaño según tu diseño */
                }
            </style>

            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Frige recent activity</p>
                <table>
                    <tr>
                        <th>Fridge ID</th>
                        <th>Added meals</th>
                        <th>Removed meals</th>
                        <th>Incidents</th>
                    </tr>
                    ${fridgeRecent}
                </table>  
            </div>

            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Frige total activity</p>
                <table>
                    <tr>
                        <th>Fridge ID</th>
                        <th>Added meals</th>
                        <th>Removed meals</th>
                        <th>Incidents</th>
                    </tr>
                    ${fridgeTotal}
                </table>  
            </div>

            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Contributor recent activity</p>
                <table>
                    <tr>
                        <th>Contributor ID</th>
                        <th>Contributor name</th>
                        <th>Donated meals</th>
                    </tr>
                    ${contributorRecent}
                </table>  
            </div>

            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Contributor total activity</p>
                <table>
                    <tr>
                        <th>Contributor ID</th>
                        <th>Contributor name</th>
                        <th>Donated meals</th>
                    </tr>
                    ${contributorTotal}
                </table>  
            </div>

        </div>
    ` ) ;
}

function downloadPDF() {

    const downloadButton = document.querySelector("#download-pdf");
    downloadButton.style.display = "none";
    const closeButton = document.querySelector("#modal-close");
    closeButton.style.display = "none";

    window.print();

    downloadButton.style.display = "";
    closeButton.style.display = "";
}

/**
 * ===================================== Reports Buttons ========================================
 */

const createReportBtn = document.querySelector("#create-report-btn");

createReportBtn.onclick = () => {
    console.log("Create report button clicked");
    const url = "/admin/report/create";

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
    })
    .then(response => {
        if (!response.ok) {
            console.error('Error:', response);
            alertaError("An error occurred. Please try again.");
            throw new Error('Network response was not ok');
        }
        return response.text(); // Leer el mensaje como texto
    })
    .then(message => {
        console.log("Server response:", message);
        alertaSuccess(message || "Report created successfully");
    })
    .catch(error => {
        alertaError("An error occurred. Please try again.");
        console.error('Error:', error);
    });
};

const newFrequencyBtn = document.querySelector("#new-frequency-btn");

newFrequencyBtn.onclick = () => {
    const actualFrequency = newFrequencyBtn.getAttribute("actual-frequency");
    const actualUnit = newFrequencyBtn.getAttribute("actual-unit");
    console.log("Actual frequency:", actualFrequency);
    console.log("Actual unit:", actualUnit);
    openModal( DOMPurify.sanitize( `
            <div class="d-flex flex-column" style="gap: 20px;">
                <h5 class="accent-100 mb-2">New Frequency</h5>
                <form action="/admin/report/updateFrequency" method="POST">
                    <div class="form-group">
                        <label for="frequency">Report frequency</label>
                        <input type="number" id="frequency" name="frequency" class="form-control" value="${actualFrequency}" required>
                    </div>
                    <div class="form-group">
                        <label for="unit">Unit</label>
                        <select id="unit" name="unit" class="form-control" required>
                            <option value="MINUTES" ${actualUnit === "MINUTES" ? "selected" : ""}>Minutes</option>
                            <option value="HOURS" ${actualUnit === "HOURS" ? "selected" : ""}>Hours</option>
                            <option value="DAYS" ${actualUnit === "DAYS" ? "selected" : ""}>Days</option>
                            <option value="WEEKS" ${actualUnit === "WEEKS" ? "selected" : ""}>Weeks</option>
                        </select>
                    </div>
                    <div align="right">
                        <button type="submit" class="btn btn-primary">Save</button>
                    </div>
                </form>
            </div>
    ` ) , () => { });
}

/**
 * ===================================== Extra ===================================================
 */

function formatDate (milliseconds) {
    const date = new Date(Number(milliseconds)).toLocaleString("es-ES", { day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit", second: "2-digit" });
    return date;
}

function alertaSuccess(mensaje) {
	const contenedorAlertas = document.getElementById("contenedorDeAlertas");
	const alertaSuccess = document.createElement("div");
	alertaSuccess.classList.add("alert", "alert-success", "alert-dismissible", "fade", "show");
	alertaSuccess.setAttribute("role", "alert");
	alertaSuccess.setAttribute("id", "success-alert");
	alertaSuccess.setAttribute("style", "position: absolute; top: 0; left: 0; right: 0; z-index: 1000; margin: auto; margin-top: 10px; width: 50%;");
	alertaSuccess.innerHTML = DOMPurify.sanitize( `
	${mensaje}
	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
	  <span aria-hidden="true">&times;</span>
	</button>
  	` ) ;
	contenedorAlertas.appendChild(alertaSuccess);
}

function alertaError(mensaje) {
	const contenedorAlertas = document.getElementById("contenedorDeAlertas");
	const alertaError = document.createElement("div");
	alertaError.classList.add("alert", "alert-danger", "alert-dismissible", "fade", "show");
	alertaError.setAttribute("role", "alert");
	alertaError.setAttribute("id", "error-alert");
	alertaError.setAttribute("style", "position: absolute; top: 0; left: 0; right: 0; z-index: 1000; margin: auto; margin-top: 10px; width: 50%;");
	alertaError.innerHTML = DOMPurify.sanitize( `
	<strong>Oh snap!</strong> ${mensaje}
	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
	  <span aria-hidden="true">&times;</span>
	</button>
  	` ) ;
	contenedorAlertas.appendChild(alertaError);
}

async function deleteCookieAndRefresh() {
    // Delete the 'access-token' cookie by setting its expiration date to a past date
	document.cookie = "access-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
	await fetch("/user/logout", {
		method: "GET",

	})
	window.location.reload();
}
