import { CLI } from "../cli";
import { PokemonAPI } from "./api";
import { Movement, Pokemon } from "./dtos";

export class Manager {
	private api = new PokemonAPI();

	public getPokemonByName = async (name: string) => {
		try {
			const pokemon = await this.api.fetchPokemon(name);
			const pokemons = new Pokemon({
				name: pokemon.name,
				image_url: pokemon.sprites?.front_default || "",
				movements: pokemon.moves.map((item) => new Movement(item.move.name)),
			});
			pokemons.printData();
		} catch (err) {
			return Promise.reject(err);
		}
	};

	public getPokemonsByMovement = async (move: string) => {
		try {
			const movements = await this.api.fetchMovement(move);
			movements.learned_by_pokemon?.map((pokemon) => CLI.print(`- ${pokemon.name}`));
		} catch (err) {
			console.log(err);

			return Promise.reject(err);
		}
	};
}
