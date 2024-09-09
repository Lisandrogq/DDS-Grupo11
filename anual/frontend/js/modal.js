const modal = document.querySelector("#modal");
const modalContent = document.querySelector("#modal-content");

const setupModalClosers = () => {
	document.querySelectorAll("#modal-close").forEach((el) => (el.onclick = closeModal));
};

const setModalContent = (children) => {
	modalContent.innerHTML = children;
	setupModalClosers();
};

const openModal = (children) => {
	modal.classList.add("modal-active");
	setModalContent(children);
};

const closeModal = () => {
	modal.classList.remove("modal-active");
	setModalContent("");
};

/**
 * ===================================== CONTRIBUTIONS MODAL LOGIC =====================================
 */

// maps donation to their modal
const modalMapper = {
	"meal-donation": mealDonation(),
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
const setupListeners = () => {
	const btns = document.querySelectorAll("#contribution-form-btn");
	btns.forEach((btn) => {
		const modalDataAttr = btn.getAttribute("data-attr");
		btn.onclick = () => setModalContent(modalMapper[modalDataAttr]);
	});
};

const contributeBtn = document.querySelector("#contribute-btn");
contributeBtn.onclick = () => {
	openModal(contributeModal);
	setupListeners();
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
				<div class="d-flex justify-content-between w-100 gap">
					<input
						type="text"
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
				<h5 class="accent-100 mb-2">Meal Donation</h5>
				<p>Contribute donating a meal</p>
			</div>
			
			
		<div>`;
}
