const googleBtn = document.querySelector("#google-connect-btn");
const githubBtn = document.querySelector("#github-connect-btn");

const handleResponse = async (googleUser) => {
	const token = googleUser.credential;
	const response = await fetch("/user/login/provider", {
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
	if (response.redirected) {
		window.location.href = response.url;
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
