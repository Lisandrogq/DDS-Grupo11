import inquirer from "inquirer";

export class CLI {
	public static print = async (message: string) => {
		console.log(message);
	};

	public static inputPrompt = async ({ message }: { message: string }) => {
		const { answer } = await inquirer.prompt<{ answer: string }>([
			{ type: "input", name: "answer", message },
		]);

		return answer;
	};

	public static listPrompt = async ({
		message,
		choices,
	}: {
		message: string;
		choices: { name: string; value: string }[];
	}) => {
		const { answer } = await inquirer.prompt<{ answer: string }>([
			{ type: "list", name: "answer", message, choices },
		]);

		return answer;
	};
}
