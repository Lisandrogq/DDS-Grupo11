const modal = document.querySelector("#modal");
const modalContent = document.querySelector("#modal-content");

const setupModalClosers = () => {
	document.querySelectorAll("#modal-close").forEach((el) => (el.onclick = closeModal));
};

const setModalContent = (children) => {
	modalContent.innerHTML = DOMPurify.sanitize(children);
	if (document.getElementById("has_map") != null) {
		const lat = document.getElementById("has_map").getAttribute("data-lat");
		const lon = document.getElementById("has_map").getAttribute("data-lon");
		setup_map(lat, lon);
	}
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
			' <input type="text" id="meal" name="meal_' +
				count +
				'" required placeholder="ID of meal to distribute..."class="col-12 inputs">'
		);
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

function setup_map(lat, lon) {
	var map = L.map("map").setView([lat, lon], 13);
	L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	}).addTo(map);
	L.marker([lat, lon]).addTo(map);
}

/**
 * ===================================== Fridge Modal Logic =====================================
 */
const fridgeModal = (name, meals, temp, capacity, state, lat, lon) =>
	DOMPurify.sanitize(`
<div
	id="has_map"
	class="d-flex flex-column"
	style="gap: 40px;"
	data-lat=${lat}
	data-lon=${lon}
>
	<div class="d-flex w-100 justify-content-between align-items-center">
		<h5 class="accent-100 mb-2">${name} fridge</h5>
	</div>

	<div class="d-flex flex-row justify-content-center align-items-center w-100 flex-wrap"> 
		<h5 class="w-50" style="font-weight: 400;">Meals 🍲: <span class="bold">${meals}</span></h5>
		<h5 class="w-50" style="font-weight: 400;">Capacity ✋: <span class="bold">${capacity}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">Temperature 🌡️:<span class="bold"> ${temp}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">State 🤞: <span class="bold">${state}</span><h5>
	</div>
	
	<div id="map" style="height: 300px;"></div>

	<div class="d-flex w-100" style="gap: 10px;">
		<button id="fridge-report-failure" class="btn-primary w-100">Add visit</button>
		<button id="fridge-view-info" class="btn-primary w-100">View info</button>
	</div>
</div>
`);

const setupFridgeListeners = () => {
	const fridges = document.querySelectorAll("#fridge");
	fridges.forEach((fridge) => {
		const name = fridge.getAttribute("data-fridge-name");
		const id = fridge.getAttribute("data-fridge-id");
		const lat = fridge.getAttribute("data-fridge-lat").replace(",", ".");
		const lon = fridge.getAttribute("data-fridge-lon").replace(",", ".");
		const meals = fridge.getAttribute("data-fridge-meals");
		const temp = fridge.getAttribute("data-fridge-temp");
		const capacity = fridge.getAttribute("data-fridge-capacity");
		const state = fridge.getAttribute("data-fridge-state");
		fridge.onclick = () => {
			openModal(fridgeModal(name, meals, temp, capacity, state, lat, lon), () => {
				// setup listener in report failure to open te modal
				const failureBtn = document.querySelector("#fridge-report-failure");
				failureBtn.onclick = () => setModalContent(addVisit(id));

				// setup listener in report failure to open te modal
				const infoBtn = document.querySelector("#fridge-view-info");
				infoBtn.onclick = () => showFridgeInfo(id);
			});
		};
	});
};

setupFridgeListeners();

/**
 * ===================================== CONTRIBUTION MODALS =====================================
 */

function addVisit(fridge_id) {
	return DOMPurify.sanitize(`
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Add Visit</h5>
				<p>Add your visit to the fridge</p>
			</div>
			<form method="POST" action="/fridge/add_visit" class="form">
			<div class="d-flex justify-content-between w-100 gap">
				<input
						type="number"
						id="incident_id"
						name="incident_id"
						required
						placeholder="Incident Id..."
					/>
				<select required name="fixed"  class="boton1 inputs" style="width: 100%">
					<option value="" selected disabled hidden>Have you fixed the problem?</option>
					<option value="true" class="desplegables">Yes</option>
					<option value="false" class="desplegables">No</option>
				</select>
			</div>
				<textarea
					type="textarea"
					cols="10"
					rows="10"
					id="description"
					name="description"
					required
					placeholder="Description..."
				></textarea>
				<input type="hidden" name="fridge" value="${fridge_id}">
				<div class="form-btns-container">
					<button
						type="reset"
						class="btn-text w-100"
						id="modal-close"
					>
						Cancel
					</button>
					<button
						type="submit"
						class="btn-primary w-100"
					>
						Submit
					</button>
				</div>
			</form>
		</div>
	`);
}

/**
 * ===================================== FRIDGE MODALS =====================================
 */

function showFridgeInfo(id) {
	if (!id) {
		console.error("Invalid fridge ID:", id);
		alert("Invalid fridge ID. Please try again.");
		return;
	}

	const url = `/fridge/info?id=${encodeURIComponent(id)}`;

	fetch(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	})
		.then((response) => {
			if (!response.ok) {
				console.error("Error:", response);
				throw new Error("Failed to retrieve fridge info. Please try again.");
			}
			return response.json();
		})
		.then((data) => {
			setModalContent(updateFridgeModal(data));
			setAddVisitShortcut(id);
		})
		.catch((error) => {
			console.error("Error:", error);
			alert("An error occurred. Please try again.");
		});
}

function updateFridgeModal(data) {
	const { fridgeId, meals, failures } = data;

	const mealRows = meals
		.map(
			(meal) => `
        <tr>
            <td>${meal.id}</td>
            <td>${meal.type}</td>
            <td>${new Date(meal.expirationDate).toLocaleDateString()}</td>
            <td>${meal.weight} g</td>
            <td>${meal.calories} cal</td>
        </tr>
    `
		)
		.join("");

	const failureRows = failures
		.map(
			(failure) => `
        <tr>
			<td
				id="failureRow"
				data-failure-id=${failure.id}
				style="cursor: pointer; color: #136c91;"
			>${failure.id}</td>
            <td>${failure.description}</td>
            <td>${new Date(failure.detectedAt).toLocaleDateString()}</td>
            <td>${failure.hasBeenFixed}</td>
        </tr>
    `
		)
		.join("");

	return DOMPurify.sanitize(`
        <div class="d-flex flex-column" style="gap: 40px;">
            <div>
                <h5 class="accent-100 mb-2">Fridge reports</h5>
                <p>Report history for this fridge</p>
            </div>
            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Meal history</p>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>Expiration date</th>
                        <th>Weight</th>
                        <th>Calories</th>
                    </tr>
                    ${mealRows}
                </table>  
            </div>
            <div>
                <p class="bold text-200" style="margin-bottom: 10px">Incidents history</p>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Creation date</th>
						<th>Fixed</th>
                    </tr>
                    ${failureRows}
                </table>
            </div>
        </div>
    `);
}

function setAddVisitShortcut(id) {
	const failuresRow = document.querySelectorAll("#failureRow");
	failuresRow.forEach((failureRow) => {
		failureRow.onclick = () => {
			const failureId = failureRow.getAttribute("data-failure-id");
			setModalContent(addVisit(id));
			const incidentIdInput = document.querySelector("#incident_id");
			incidentIdInput.value = failureId;
			incidentIdInput.textContent = failureId;
		};
	});
}

async function deleteCookieAndRefresh() {
	// Delete the 'access-token' cookie by setting its expiration date to a past date
	document.cookie = "access-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
	await fetch("/user/logout", {
		method: "GET",
	});
	window.location.reload();
}
