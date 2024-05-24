import { CLI } from "../cli";
import { Manager } from "./manager";

export class PokemonInterface {
	private manager = new Manager();

	private ACTIONS = {
		GET_POKEMON: "get-pokemon",
		GET_POKEMONS_WITH_MOVE: "get-pokemons-with-move",
		EXIT: "exit",
	} as const;

	// this is just a fancy form of preventing a bunch of ifs when running an action based on the choice of the user (don't really know the pattern name :)
	// you might think it is worthwhile to delegate this to another class
	// though, as much complex this might look or be, it is actually the responsibility of the interface: react based one the user input
	private actions: { [key: string]: () => Promise<void> } = {
		[this.ACTIONS.GET_POKEMON]: async () => {
			const name = await CLI.inputPrompt({ message: "Enter the Pokemon name" });
			await this.manager.getPokemonByName(name);
		},
		[this.ACTIONS.GET_POKEMONS_WITH_MOVE]: async () => {
			const name = await CLI.inputPrompt({ message: "Enter the movement name" });
			await this.manager.getPokemonsByMovement(name);
		},
		[this.ACTIONS.EXIT]: () => process.exit(0),
	};

	public start = async () => {
		while (1) {
			const prompt = await CLI.listPrompt({
				message: "What would you like to do?",
				choices: [
					{ name: "Get Pokemon", value: this.ACTIONS.GET_POKEMON },
					{
						name: "Get Pokemons that have a movement",
						value: this.ACTIONS.GET_POKEMONS_WITH_MOVE,
					},
					{ name: "Exit", value: this.ACTIONS.EXIT },
				],
			});

			try {
				await this.actions[prompt]();
			} catch {
				CLI.print("there has been an error, try again please");
			}
		}
	};
}
