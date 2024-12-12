const googleBtn = document.querySelector("#google-connect-btn");
const githubBtn = document.querySelector("#github-connect-btn");

const handleResponse = async (googleUser) => {
	console.log("GOOGLE USER", googleUser);
	const token = googleUser.credential;
	const req = await fetch("/user/provider/", {
		method: "POST",
		headers: {
			Accept: "application/json",
			"Content-Type": "application/json",
		},
		body: JSON.stringify({
			provider: "Google",
			token,
		}),
	});
	const res = await req.json();

	if (res?.status == 200) {
		alert("Provider added successfully!");
	} else {
		alert(`Could not add login provider: ${res?.message}`);
	}
};

const startApp = () => {
	window.onGoogleLibraryLoad = () => {
		google.accounts.id.initialize({
			client_id: "933613917422-bb9a9ptfqof8saqbc5ub1gr5mtpoohh2.apps.googleusercontent.com",
			callback: handleResponse,
		});
		const parent = document.querySelector("#google-btn-wrapper");
		google.accounts.id.renderButton(parent, {
			type: "icon",
		});
		const googleLoginWrapperButton = parent.querySelector("div[role=button]");
		window.handleGoogleConnection = () => {
			googleLoginWrapperButton.click();
		};
	};
};

startApp();
