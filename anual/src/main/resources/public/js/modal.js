const modal = document.querySelector("#modal");
const modalContent = document.querySelector("#modal-content");

const setupModalClosers = () => {
	document.querySelectorAll("#modal-close").forEach((el) => (el.onclick = closeModal));
};

const setModalContent = (children) => {
	modalContent.innerHTML = children;
	if (document.getElementById("has_map") != null) {
		setup_map();
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
		div.innerHTML =
			' <input type="text" id="meal" name="meal_' + count + '" required placeholder="ID of meal to distribute..."class="col-12 inputs">';
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

function setup_map() {
	var map = L.map("map").setView([-34.5978833, -58.4199385], 13);
	L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
		maxZoom: 19,
		attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
	}).addTo(map);
	L.marker([-34.5978833, -58.4199385]).addTo(map);
}

/**
 * ===================================== Fridge Modal Logic =====================================
 */
const fridgeModal = (name, meals, temp, reserved, state) => `
<div id="has_map"class="d-flex flex-column" style="gap: 40px;">
	<div class="d-flex w-100 justify-content-between align-items-center">
		<h5 class="accent-100 mb-2">${name} fridge</h5>
		<div class="d-flex flex-row" style="gap: 10px;">
			<button id="unsubscribeBtn" class="btn-primary" style="padding: 10px; font-size: var(--paragraph)">Unsubscribe</button>
			<button id="subscribeBtn" class="btn-primary" style="padding: 10px; font-size: var(--paragraph)">Subscribe</button>
		</div>
	</div>

	<div class="d-flex flex-row justify-content-center align-items-center w-100 flex-wrap"> 
		<h5 class="w-50" style="font-weight: 400;">Meals 🍲: <span class="bold">${meals}</span></h5>
		<h5 class="w-50" style="font-weight: 400;">Reserved ✋: <span class="bold">${reserved}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">Temperature 🌡️:<span class="bold"> ${temp}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">State 🤞: <span class="bold">${state}</span><h5>
	</div>	
	
	<div id="map" style="height: 300px;"></div>

	<div class="d-flex w-100" style="gap: 10px;">
		<button id="fridge-report-failure" class="btn-primary w-100">Report failure</button>
		<button id="fridge-view-info" class="btn-primary w-100">View info</button>
	</div>
</div>
`;

function handleSubscribe(id) { 
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Subscribe to fridge</h5>
				<p>Get notified about your favorite fridges!!!!!</p>
			</div>
			<form method="POST" action="/fridge/subscribe" class="form">
				<select
					name="type"
					required
					id="subscription-type"
					onchange="toggleQuantityField(event)"
				>
					<option selected disabled hidden>
						Choose a category of subscription
					</option>
					<option value="LowInventory">Low Inventory</option>
					<option value="NearFullInventory">Near Full Inventory</option>
					<option value="Malfunction">Malfunction</option>
				</select>

				<div id="quantity-field" style="display: none;">
					<input
						type="number"
						id="quantity"
						name="quantity"
						placeholder="A partir de n cantidad de comidas..."
					/>
				</div>

				<input type="hidden" name="fridge" value="${id}">

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
	`;
}

function handleUnsubscribe(id) {

	fetch(`/fridge/unsubscribe?fridgeId=${encodeURIComponent(id)}`, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
	})
		.then(response => {
			if (response.ok) {
				alert("Unsubscribed successfully!");
				window.location.reload();
			} else {
				alert("Failed to unsubscribe. Please try again.");
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert("An error occurred. Please try again.");
		});
}

function toggleQuantityField(event) {
	const selectedValue = event.target.value;
	const quantityField = document.getElementById("quantity-field");

	if (selectedValue === "LowInventory" || selectedValue === "NearFullInventory") {
		quantityField.style.display = "block";
		quantityField.querySelector("input").required = true;
	} else {
		quantityField.style.display = "none";
	}
}

const setupFridgeListeners = () => {
	const fridges = document.querySelectorAll("#fridge");
	fridges.forEach((fridge) => {
		const name = fridge.getAttribute("data-fridge-name");
		const id = fridge.getAttribute("data-fridge-id");
		const meals = fridge.getAttribute("data-fridge-meals");
		const temp = fridge.getAttribute("data-fridge-temp");
		const reserved = fridge.getAttribute("data-fridge-reserved");
		const state = fridge.getAttribute("data-fridge-state");
		const subscribed = fridge.getAttribute("data-fridge-subscribed");
		fridge.onclick = () => {
			openModal(fridgeModal(name, meals, temp, reserved, state), () => {
				// setup listener in subscribe button
				const subscribeBtn = document.querySelector("#subscribeBtn");
				subscribeBtn.onclick = () => setModalContent(handleSubscribe(id));

				const unsubscribeBtn = document.querySelector("#unsubscribeBtn");
				if (subscribed === "true") {
					unsubscribeBtn.style.display = "inline-block";
					unsubscribeBtn.onclick = () => handleUnsubscribe(id);
				} else {
					unsubscribeBtn.style.display = "none";
				}

				// setup listener in report failure to open te modal
				const failureBtn = document.querySelector("#fridge-report-failure");
				failureBtn.onclick = () => setModalContent(failureAlert(id));

				// setup listener in report failure to open te modal
				const infoBtn = document.querySelector("#fridge-view-info");
				infoBtn.onclick = () => showFridgeInfo(id);
			});
		};
	});
};

setupFridgeListeners();

/**
 * ===================================== CONTRIBUTIONS MODAL LOGIC =====================================
 */

// maps donation to their modal
const modalMapper = {
	"meal-donation": mealDonation(),
	"meal-distribution": mealDistribution(),
	"fridge-admin": fridgeAdministration(),
	"money-donation": moneyDonation(),
	"person-registration": personRegistration(),
	"reward-collab": rewardCollab(),
};

// base button component
const contributionFormBtn = (text, modalDataAttr) =>
	`<button class="btn-primary" style="flex: 1;" id="contribution-form-btn" data-attr=${modalDataAttr}>${text}</button>`;

const contributeModalIND = `<div class="d-flex flex-column" style="gap: 40px">
		<div>
			<h5 class="accent-100 mb-2">Contributions</h5>
			<p>How do you want to collaborate?</p>
		</div>
		<div class="d-flex flex-row w-100 flex-wrap justify-content-center align-items-center" style="gap: 20px;">
		${contributionFormBtn("Meal donation", "meal-donation")}
		${contributionFormBtn("Meal distribution", "meal-distribution")}
		${contributionFormBtn("Person registration", "person-registration")}
		${contributionFormBtn("Money donation", "money-donation")}
		</div>
	</div>`;

const contributeModalLE = `<div class="d-flex flex-column" style="gap: 40px">
		<div>
			<h5 class="accent-100 mb-2">Contributions</h5>
			<p>How do you want to collaborate?</p>
		</div>
		<div class="d-flex flex-row w-100 flex-wrap justify-content-center align-items-center" style="gap: 20px;">
			${contributionFormBtn("Money donation", "money-donation")}
			${contributionFormBtn("Fridge administration", "fridge-admin")}
			${contributionFormBtn("Reward collaboration", "reward-collab")}
		</div>
	</div>`;

// sets up the btn on click listener to open its respective modal by taking the data-attr tag.
const setupListenersContributionsListeners = () => {
	const btns = document.querySelectorAll("#contribution-form-btn");
	btns.forEach((btn) => {
		const modalDataAttr = btn.getAttribute("data-attr");
		btn.onclick = () => setModalContent(modalMapper[modalDataAttr]);
	});
};

const contributeBtn = document.querySelector("#contribute-btn");
contributeBtn.onclick = () => {
	if (document.getElementById('contribute-btn').getAttribute("user-type") == "IND")
		openModal(contributeModalIND);
	else
		openModal(contributeModalLE);
	setupListenersContributionsListeners();
};

async function deleteCookieAndRefresh() {
    // Delete the 'access-token' cookie by setting its expiration date to a past date
	document.cookie = "access-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
	await fetch("/user/logout", {
		method: "GET",

	})
	window.location.reload();
}

/**
 * ===================================== CONTRIBUTION MODALS =====================================
 */
function mealDonation() {
	

	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Meal Donation</h5>
				<p>Contribute donating a meal</p>
			</div>
			
			<form method="POST" action="/contribution/meal" class="form">
				<input
					type="text"
					id="type"
					name="type"
					required
					placeholder="Type of food..."
				/>
				<p>Expiration date</p>	
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="date"
						id="expirationDate"
						name="expirationDate"
						required
						placeholder="Expiration date..."
					/>

					<input
						type="text"
						id="fridge_address"
						name="fridge_address"
						required
						placeholder="Fridge Address..."
					/>
				</div>
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="text"
						id="calories"
						name="calories"
						required
						placeholder="Amount of calories..."
						class="w-100"
					/>
					<input
						type="text"
						id="weight"
						name="weight"
						required
						placeholder="Weight..."
						class="w-100"
					/>
				</div>

				<div class="form-btns-container">
					<button
						type="reset"
						class="w-100 btn-text"
						id="modal-close"
					>
						Cancel
					</button>
					<button
						type="submit"
						class="w-100 btn-primary"
					>
						Submit
					</button>
				</div>
			</form>
		<div>`;
}

function mealDistribution() {
	return `
		<div  class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Meal distribution</h5>
				<p>Contribute distributing a meal</p>
			</div>
			<form method="POST" action="/contribution/meal/distribution" class="form">
				<div>
					<span id="btnCrearInput" style="color: #136C91;" class="clickable-text" onclick="agregarInput()">Add meal</span> <b>|</b>
					<span  style="color: #136C91" class="clickable-text" onclick="eliminarInput()">Delete meal</span>
				</div>
				<div id="input-placeholder"> 
				<input type="text" id="meal" name="meal_0" required placeholder="Type of meal to distribute..."class="col-12 inputs">
				</div>
				<input type="text" id="reason" name="reason" required placeholder="Reason for relocation...">
				
				<div class="d-flex justify-content-between w-100 gap">
						<input
						type="text"
						id="origin_address"
						name="origin_address"
						required
						placeholder="Origin fridge Address..."
					/>

						<input
						type="text"
						id="destiny_address"
						name="destiny_address"
						required
						placeholder="Destiny fridge Address..."
					/>
					
				</div>

				<div class="form-btns-container">
					<button type="reset" class="btn-text w-100" id="modal-close">Cancel</button>
					<button type="submit" class="btn-primary w-100">Submit</button>
				</div>
			</form>

		
		<div>`;
}

function fridgeAdministration() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Fridge Administration</h5>
				<p>Contribute administrating a fridge</p>
			</div>

			 <form method="POST" action="/contribution/fridge_admin" class="form">
				<input type="text" id="name" name="name" required placeholder="Name of the fridge..." />
				<input type="text" id="address" name="address" required placeholder="Address of the fridge..." />
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="number"
						id="capacity"
						name="capacity"
						required
						placeholder="Capacity of the fridge..."
						class="w-100"
					/>
					<select required name="isActive" value="true" class="boton1 inputs" style="width: 100%;">
						<option selected disabled hidden>Will the fridge be active?</option>
						<option value='true' class="desplegables">Yes</option>
						<option value='false' class="desplegables">No</option>
					</select>
				</div>
				<div class="form-btns-container">
					<button type="reset" class="btn-text w-100" id="modal-close">Cancel</button>
					<button type="submit" class="btn-primary w-100">Submit</button>
				</div>
			</form>
		<div>
	`;
}

function moneyDonation() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Money Donation</h5>
				<p>Contribute donating money</p>
			</div>

			 <form method="POST" action="/contribution/money" class="form">
				<input type="text" id="amount" name="amount" required placeholder="Amount to donate..." />
				<input type="text" id="message" name="message" required placeholder="Your donation message..." />

				<div class="form-btns-container">
					<button type="reset" class="btn-text w-100" id="modal-close">Cancel</button>
					<button type="submit" class="btn-primary w-100">Submit</button>
				</div>
			</form>
		<div>
	`;
}

function personRegistration() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Register Person in Need</h5>
				<p>Create an account for a person in need</p>
			</div>

			<form method="POST" action="/contribution/registration" class="form">
				
				<input
					type="text"
					id="name"
					name="name"
					required
					placeholder="Full name..."
				/>
				<input
					type="number"
					id="dni"
					name="dni"
					required
					placeholder="DNI..."
				/>
				<p>Birthdate</p>
				<input
					type="date"
					id="birth"
					name="birth"
					required
					placeholder="Birthdate..."
				/>
				<input
					type="number"
					id="children_count"
					name="children_count"
					required
					min=0
					placeholder="Number of children in charge..."
				/>

				<div class="form-btns-container">
					<button type="reset" class="btn-text w-100" id="modal-close">
						Cancel
					</button>
					<button type="submit" class="btn-primary w-100">
						Accept
					</button>
				</div>
			</form>
		<div>
	`;
}

function rewardCollab() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Reward collaboration</h5>
				<p>Contribute offering a new reward</p>
			</div>

			<form method="POST" action="/contribution/reward" class="form">
			<div class="d-flex justify-content-between w-100 gap">	
					<input
					type="text"
					id="name"
					name="name"
					required
					placeholder="Reward name..."
				/>
					<input
					type="number"
					id="stock"
					name="stock"
					required
					placeholder="Available stock..."
				/>
			</div>
				<input
					type="text"
					id="description"
					name="description"
					required
					placeholder="Reward description..."
				/>
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="number"
						id="points"
						name="points"
						required
						placeholder="Points needed..."
					/>

					<select
						name = "category"
						required
						value="category"
					>
						<option selected disabled hidden>
							Choose a category of reward
						</option>
						<option value="TECH">Technology</option>
						<!-- sujeto a cambios-->
						<option value="COOKING">Cooking</option>
						<option value="HOME">Home</option>
					</select>
				</div>

				<p>Add a reward picture</p>
				<input
					type="file"
					id="picture"
					name="picture"
					accept=".png"
				/>
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
		<div>
	`;
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
				throw new Error("Failed to retrieve fridge info. Please try again.");
			}
			return response.json();
		})
		.then(data => {
			console.log("Fridge info retrieved successfully:", data);
			setModalContent(updateFridgeModal(data));
		})
		.catch(error => {
			console.error('Error:', error);
			alert("An error occurred. Please try again.");
		});
}

function updateFridgeModal(data) {
	const { fridgeId, meals, failures } = data;
	console.log("Fridge info:", data);

	const mealRows = meals.map(meal => `
        <tr>
            <td>${meal.id}</td>
            <td>${meal.type}</td>
            <td>${new Date(meal.expirationDate).toLocaleDateString()}</td>
            <td>${meal.weight} g</td>
            <td>${meal.calories} cal</td>
        </tr>
    `).join('');
	console.log(mealRows);

	const failureRows = failures.map(failure => `
        <tr>
			<td>${failure.id}</td>
            <td>${failure.description}</td>
            <td>${new Date(failure.detectedAt).toLocaleDateString()}</td>
            <td>${failure.hasBeenFixed}</td>
        </tr>
    `).join('');
	console.log(failureRows);
	
	return `
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
    `;
}

function failureAlert(fridge_id) {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Report Failure</h5>
				<p>Report a fridge's malfunction</p>
			</div>
			<form method="POST" action="/fridge/failure" class="form">
			<div class="d-flex justify-content-between w-100 gap">
				<select required name="urgency"  class="boton1 inputs" style="width: 100%">
					<option value="" selected disabled hidden>Urgency</option>
					<option value="High" class="desplegables">High Urgency</option>
					<option value="Medium" class="desplegables">
						Medium Urgency
					</option>
					<option value="Low" class="desplegables">
						Low Urgency
					</option>
				</select>
				<select required name="set_inactive"  class="boton1 inputs" style="width: 100%">
					<option value="" selected disabled hidden>Should we disable fridge?</option>
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
	`;
}

/**
 * ===================================== REWARDS MODAL LOGIC =====================================
 */

// Puntos de usuario
const userPoints = document.querySelector("#user-points");
let originalPoints = parseInt(userPoints.getAttribute("data-user-points"));
let dataUserPoints = parseInt(userPoints.getAttribute("data-user-points"));

// Botones de cancelar y confirmar
const cancelBtn = document.getElementById("cancel-reward-btn");
cancelBtn.style.display = "none";
const confirmBtn = document.getElementById("confirm-reward-btn");
confirmBtn.style.display = "none";

// Botones de reclamar recompensas
const redeemRewardBtns = document.querySelectorAll("#redeem-reward-btn");
let originalQuantities = {};
let quantities = {};

redeemRewardBtns.forEach((button) => {
	const rewardId = button.getAttribute("data-reward-id");
	const descriptionElement = button.closest(".d-flex").querySelector("p");
	const originalQuantity = parseInt(button.getAttribute("data-reward-quantity"));
	const neededPoints = parseInt(button.getAttribute("data-reward-neededpoints"));

	originalQuantities[rewardId] = originalQuantity;
	quantities[rewardId] = originalQuantity;

	button.onclick = () => {
		if (dataUserPoints >= neededPoints && quantities[rewardId] > 0) {
			dataUserPoints -= neededPoints;
			userPoints.textContent = dataUserPoints;
			userPoints.setAttribute("data-user-points", dataUserPoints);

			quantities[rewardId] -= 1;
			button.setAttribute("data-reward-quantity", quantities[rewardId]);

			if (descriptionElement) {
				const newDescription = descriptionElement.textContent.replace(
					/\((.*?) remaining\)/,
					`(${quantities[rewardId]} remaining)`
				);
				descriptionElement.textContent = newDescription;
			}

			cancelBtn.style.display = "inline-block";
			confirmBtn.style.display = "inline-block";

		} else if (quantities[rewardId] <= 0) {
			alert("There are no more rewards available");
		} else {
			alert("You don't have enough points");
		}
	};
});

cancelBtn.onclick = () => {
	cancelBtn.style.display = "none";
	confirmBtn.style.display = "none";

	dataUserPoints = originalPoints;
	userPoints.textContent = originalPoints;
	userPoints.setAttribute("data-user-points", originalPoints);

	redeemRewardBtns.forEach((button) => {
		const rewardId = button.getAttribute("data-reward-id");
		const descriptionElement = button.closest(".d-flex").querySelector("p");
		const originalQuantity = originalQuantities[rewardId];
		
		button.setAttribute("data-reward-quantity", originalQuantity);
		quantities[rewardId] = originalQuantity;

		if (descriptionElement) {
			const newDescription = descriptionElement.textContent.replace(/\d+ remaining/, `${originalQuantity} remaining`);
			descriptionElement.textContent = newDescription;
		}
	});
};

confirmBtn.onclick = () => {

	// Actualizar BD
	const data = {
		userPoints: dataUserPoints,
		rewards: []
	};

	redeemRewardBtns.forEach((button) => {
		const rewardId = button.getAttribute("data-reward-id");
		if (quantities[rewardId] < originalQuantities[rewardId]) {
			const quantity = quantities[rewardId];
			data.rewards.push({ rewardId, quantity });
		}
	});

	console.log(data);

	fetch("/rewards", {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},
		body: JSON.stringify(data),
	})
		.then(response => {
			if (response.ok) {
				alert("Reward redeemed successfully!");
				console.log("Antes del cambio:", originalPoints);
				console.log("Antes del cambio:", originalQuantities);
				originalPoints = dataUserPoints;
				originalQuantities = { ...quantities };
				console.log("Tras el cambio:", originalPoints);
				console.log("Tras el cambio:", originalQuantities);
				cancelBtn.style.display = "none";
				confirmBtn.style.display = "none";
			} else {
				alert("Failed to redeem the reward. Please try again.");
			}
		})
		.catch(error => {
			console.error('Error:', error);
			alert("An error occurred. Please try again.");
		});

};
