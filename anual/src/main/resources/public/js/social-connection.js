const googleBtn = document.querySelector("#google-connect-btn");
const githubBtn = document.querySelector("#github-connect-btn");

const handleProviderConnection = (provider) => {
	const method = {
		Google: window.handleGoogleConnection,
		Github: handleGithubConnection,
	};

	method[provider]();
};

const handleGithubConnection = () => {
	const githubAuthUrl = `https://github.com/login/oauth/authorize?scope=user:email&client_id=Ov23lio6HofL2ZF56U32&redirect_uri=${encodeURIComponent(
		"https://fridgebridge.simplecharity.com/user/provider/github"
	)}`;
	window.location.href = githubAuthUrl;
};

const handleGoogleResponse = async (googleUser) => {
	const token = googleUser.credential;
	const req = await fetch("/user/provider/google", {
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
			callback: handleGoogleResponse,
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
