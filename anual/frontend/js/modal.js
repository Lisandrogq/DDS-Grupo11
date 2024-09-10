const modal = document.querySelector("#modal");
const modalContent = document.querySelector("#modal-content");

const setupModalClosers = () => {
	document.querySelectorAll("#modal-close").forEach((el) => (el.onclick = closeModal));
};

const setModalContent = (children) => {
	modalContent.innerHTML = children;
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

/**
 * ===================================== Fridge Modal Logic =====================================
 */
const fridgeModal = (name, meals, temp, reserved, state) => `
<div class="d-flex flex-column" style="gap: 40px;">
	<div>
		<h5 class="accent-100 mb-2">${name} fridge</h5>
		<div class="d-flex w-100 justify-content-between align-items-center">
			<p>Contribute donating a meal</p>
			<button class="btn-primary" style="padding: 10px; font-size: var(--paragraph)">Subscribe</button>
		</div>
	</div>

	<div class="d-flex flex-row justify-content-center align-items-center w-100 flex-wrap"> 
		<h5 class="w-50" style="font-weight: 400;">Meals 🍲: <span class="bold">${meals}</span></h5>
		<h5 class="w-50" style="font-weight: 400;">Reserved ✋: <span class="bold">${reserved}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">Temperature 🌡️:<span class="bold"> ${temp}</span><h5>
		<h5 class="w-50" style="font-weight: 400;">State 🤞: <span class="bold">${state}</span><h5>
	</div>	
	
	<div class="d-flex w-100" style="gap: 10px;">
		<button id="fridge-report-failure" class="btn-primary w-100">Report failure</button>
		<button id="fridge-view-report" class="btn-primary w-100">View report</button>
	</div>
</div>
`;

const setupFridgeListeners = () => {
	const fridges = document.querySelectorAll("#fridge");
	fridges.forEach((fridge) => {
		const name = fridge.getAttribute("data-fridge-name");
		const meals = fridge.getAttribute("data-fridge-meals");
		const temp = fridge.getAttribute("data-fridge-temp");
		const reserved = fridge.getAttribute("data-fridge-reserved");
		const state = fridge.getAttribute("data-fridge-state");
		fridge.onclick = () => {
			openModal(fridgeModal(name, meals, temp, reserved, state), () => {
				// setup listener in report failure to open te modal
				const failureBtn = document.querySelector("#fridge-report-failure");
				failureBtn.onclick = () => setModalContent(failureAlert());
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

const contributeModal = `<div class="d-flex flex-column" style="gap: 40px">
		<div>
			<h5 class="accent-100 mb-2">Contributions</h5>
			<p>How do you want to collaborate?</p>
		</div>
		<div class="d-flex flex-row w-100 flex-wrap justify-content-center align-items-center" style="gap: 20px;">
			${contributionFormBtn("Meal donation", "meal-donation")}
			${contributionFormBtn("Meal distribution", "meal-distribution")}
			${contributionFormBtn("Fridge administration", "fridge-admin")}
			${contributionFormBtn("Person registration", "person-registration")}
			${contributionFormBtn("Money donation", "money-donation")}
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
	openModal(contributeModal);
	setupListenersContributionsListeners();
};

/**
 * ===================================== ACTUAL MODALS =====================================
 */
function mealDonation() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Meal Donation</h5>
				<p>Contribute donating a meal</p>
			</div>
			
			<form action="" class="form">
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

					<select required value="fridge" class="w-100">
						<option selected disabled hidden>Choose a fridge</option>
						<option value="Heladera 1">Heladera 1</option>
						<!-- sujeto a cambios-->
						<option value="Heladera 2">Heladera 2</option>
						<option value="Heladera 3">Heladera 3</option>
						<option value="Heladera 4">Heladera 4</option>
					</select>
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
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Meal distribution</h5>
				<p>Contribute distributing a meal</p>
			</div>
			<form action="" class="form">
				<div>
					<span style="color: #136C91;" class="clickable-text" onclick="agregarInput()">Add meal</span> <b>|</b>
					<span  style="color: #136C91" class="clickable-text" onclick="eliminarInput()">Delete meal</span>
				</div>
				<input type="text" id="meal" name="meal" required placeholder="ID of meal to distribute...">
				
				<input type="text" id="reason" name="reason" required placeholder="Reason for relocation...">
				
				<div class="d-flex justify-content-between w-100 gap">
					<select required value="fridge" class="boton1 inputs" style="width: 100%;">
						<option selected disabled hidden>Choose origin fridge</option>
						<option value='Heladera 1' class="desplegables">Heladera 1</option> <!-- sujeto a cambios-->
						<option value='Heladera 2' class="desplegables">Heladera 2</option>
						<option value='Heladera 3' class="desplegables">Heladera 3</option>
						<option value='Heladera 4' class="desplegables">Heladera 4</option>
					</select>

					<select required value="fridge" class="boton1 inputs" style="width: 100%;">
						<option selected disabled hidden>Choose destiny fridge</option>
						<option value='Heladera 1' class="desplegables">Heladera 1</option> <!-- sujeto a cambios-->
						<option value='Heladera 2' class="desplegables">Heladera 2</option>
						<option value='Heladera 3' class="desplegables">Heladera 3</option>
						<option value='Heladera 4' class="desplegables">Heladera 4</option>
					</select>
					
				</div>

				<p>Date of distribution</p>	
				<div class="d-flex justify-content-between w-100 gap">
					<input type="date" id="dateOfDistribution" name="dateOfDistribution" required placeholder="Date of distribution..."
					class="w-100 inputs">
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
				<h5 class="accent-100 mb-2">Meal distribution</h5>
				<p>Contribute distributing a meal</p>
			</div>
		<div>`;
}

function moneyDonation() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Money Donation</h5>
				<p>Contribute donating money</p>
			</div>

			 <form action="" class="form">
				<input type="text" id="amount" name="amount" required placeholder="Amount to donate..." />
				<input type="text" id="cardID" name="cardID" required placeholder="Your card ID..." />
				<input type="password" id="cvv" name="cvv" required placeholder="Your card verification value..." />

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

			<form action="" class="form">
				<input
					type="text"
					id="username"
					name="username"
					required
					placeholder="Username..."
				/>
				<input
					type="password"
					id="contrasena"
					name="contrasena"
					required
					placeholder="Password..."
				/>
				<input
					type="password"
					id="contrasenaConfirmada"
					name="contrasena"
					required
					placeholder="Password again..."
				/>
				<input
					type="text"
					id="nombreYApellido"
					name="nombreYApellido"
					required
					placeholder="Full name..."
				/>
				<p>Birthdate</p>
				<input
					type="date"
					id="fechaDeNacimiento"
					name="fechaDeNacimiento"
					required
					placeholder="Birthdate..."
				/>
				<input
					type="text"
					id="idtarjeta"
					name="idtarjeta"
					required
					placeholder="Card ID..."
				/>
				<input
					type="number"
					id="childrenInCharge"
					name="childrenInCharge"
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

			<form action="" class="form">
				<input
					type="text"
					id="name"
					name="name"
					required
					placeholder="Reward name..."
				/>
				<input
					type="text"
					id="description"
					name="description"
					required
					placeholder="Reward description..."
				/>
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="text"
						id="points"
						name="points"
						required
						placeholder="Points needed..."
					/>

					<select
						required
						value="category"
					>
						<option selected disabled hidden>
							Choose a category of reward
						</option>
						<option value="Technology">Technology</option>
						<!-- sujeto a cambios-->
						<option value="Cooking">Cooking</option>
						<option value="Home">Home</option>
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

function failureAlert() {
	return `
		<div class="d-flex flex-column" style="gap: 40px;">
			<div>
				<h5 class="accent-100 mb-2">Report Failure</h5>
				<p>Report a fridge's malfunction</p>
			</div>
			<form action="" class="form">
				<select required value="fridge" class="boton1 inputs" style="width: 100%">
					<option selected disabled hidden>What happened?</option>
					<option value="low meal" class="desplegables">Low on meal</option>
					<option value="people" class="desplegables">
						Too many people
					</option>
				</select>
				<textarea
					type="textarea"
					cols="10"
					rows="10"
					id="description"
					name="description"
					required
					placeholder="Description..."
				></textarea>
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
