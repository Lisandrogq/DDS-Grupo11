const googleBtn = document.querySelector("#google-connect-btn");
const githubBtn = document.querySelector("#github-connect-btn");

googleBtn.onclick = () => {};

const startApp = () => {
	window.onGoogleLibraryLoad = () => {
		google.accounts.id.initialize({
			client_id: "933613917422-bb9a9ptfqof8saqbc5ub1gr5mtpoohh2.apps.googleusercontent.com",
			callback: handleResponse,
		});
		google.accounts.id.prompt();
	};
};

const handleResponse = async (googleUser) => {
	console.log("GOOGLE USER", googleUser);
	// const id_token = googleUser.getAuthResponse().id_token;
	// const req = await fetch("/user/provider/", {
	// 	method: "POST",
	// 	headers: {
	// 		Accept: "application/json",
	// 		"Content-Type": "application/json",
	// 	},
	// 	body: JSON.stringify({ id_token, provider: "google" }),
	// });
	// const res = await req.json();

	// if (res?.status != 200) {
	// 	alert(`Could not add login provider: ${res?.message}`);
	// }
};

const attachSignin = (element) => {
	auth2.attachClickHandler(
		element,
		{},
		async (googleUser) => {
			alert("Google account connected!");
		},
		(error) => {
			console.log("error", error);
			alert(`Could not add login provider: ${error}`);
		}
	);
};

startApp();
